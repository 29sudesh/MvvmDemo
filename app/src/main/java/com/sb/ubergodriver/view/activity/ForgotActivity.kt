package com.sb.ubergodriver.view.activity


import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import com.sb.ubergodriver.R
import android.content.Intent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log

import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonElement

import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.CommonUtils.AppConstants.*
import com.sb.ubergodriver.CommonUtils.Utils
import com.sb.ubergodriver.apiConstants.ApiResponse
import com.sb.ubergodriver.apiConstants.Constant
import com.sb.ubergodriver.apiConstants.Status
import com.sb.ubergodriver.apiConstants.ViewModelFactory
import com.sb.ubergodriver.application.App
import com.sb.ubergodriver.model.forgot.EmailOtpRequest
import com.sb.ubergodriver.model.forgot.ForgotPasswordRequest
import com.sb.ubergodriver.model.registerstatusresponse.RegisterNumberStatusResponse
import com.sb.ubergodriver.model.requests.RegistrerCheckStatusRequest
import com.sb.ubergodriver.viewModel.login.editProfile.EditProfileViewModel


import kotlinx.android.synthetic.main.activity_forgot.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import org.jetbrains.anko.email
import org.json.JSONObject
import javax.inject.Inject

class ForgotActivity : BaseActivity() {

    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: EditProfileViewModel?= null
    var progressDialog: ProgressDialog?=null

    var APPTAG:String=ForgotActivity::class.java.name

    var type:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        progressDialog = Constant.getProgressDialog(this, "Please wait...")

        (application as App).getAppComponent().doInjection(this)


        viewModel = ViewModelProviders.of(this, viewModelFactory).get(EditProfileViewModel::class.java!!)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

        toolBarSetUp()
        initViewS()




    }

    private fun toolBarSetUp() {
        val title:String=getString(R.string.forgot_password)
       tv_title_common.setText(title)
    }

    private fun initViewS() {
        btn_reset.setOnClickListener(this)
        iv_back_common.setOnClickListener(this)
        et_email.setOnClickListener(this)
        et_phonenumber.setOnClickListener(this)

        et_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.length>0)
                {
                    et_phonenumber.setText("")
                }
            }


            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        et_phonenumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.length>0)
                {
                    et_email.setText("")
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

    }

    private fun removeFocous() {
        hideKeyboard(et_email)
        hideKeyboard(et_phonenumber)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.btn_reset->
            {

                if (et_phonenumber.text.toString().trim().isEmpty())
                {
                    if (!Utils.isValidEmail(et_email.text.toString().trim()))
                    {
                        showAlert(getString(R.string.emailshouldnotempty))
                        return
                    }
                    else
                    {
                        removeFocous()
                        type=KEY_EMAIL
                        val request=EmailOtpRequest()
                        request.adminId=getAdminId()
                        request.email=et_email.text.toString().trim()
                        viewModel!!.hitForgotEmailApi(request)
                        return
                    }
                }
                if (et_email.text.toString().isEmpty())
                {
                    if (et_phonenumber.text.toString().length<=6) {
                        showAlert(getString(R.string.enter_valid_mobile))
                        return
                    }
                    else
                    {
                        type= KEY_PHONE
                        removeFocous()
                        val checkRequest=RegistrerCheckStatusRequest()
                        checkRequest.adminId=getAdminId()
                        checkRequest.contryCode=ccp.selectedCountryCodeWithPlus.toString()
                        checkRequest.phone=et_phonenumber.text.toString().trim()
                        viewModel!!.hitCheckRegisterStatus(checkRequest)

                        return

                    }
                }

//                navigate(OtpActivity::class.java)
//                finish()
            }

            R.id.iv_back_common->
            {
                removeFocous()
                finish()
            }

            R.id.et_email->
            {
                showKeyboard(et_email)
            }

            R.id.et_phonenumber->
            {
                showKeyboard(et_phonenumber)
            }
        }
    }

    /*
  * method to handle response
  * */
    private fun consumeResponse(apiResponse: ApiResponse) {

        when (apiResponse.status) {

            Status.LOADING -> progressDialog!!.show()

            Status.SUCCESS -> {
                progressDialog!!.dismiss()
                renderSuccessResponse(apiResponse.data!!)
            }

            Status.ERROR -> {
                progressDialog!!.dismiss()
                showMessage( resources.getString(R.string.errorString))
            }
            else -> {
            }
        }
    }
    /*
    * method to handle success response
    * */
    private fun renderSuccessResponse(response: JsonElement) {
        if (!response.isJsonNull) {
            Log.e(APPTAG+"response=", response.toString())
            val data:String=  Utils.toJson(response)
            val gson1 =  Gson();
            if (type.toString().equals(KEY_PHONE)) {
                val loginResponse = gson1.fromJson(data, RegisterNumberStatusResponse::class.java)
                if (loginResponse.success.toString().equals(AppConstants.TRUE)) {
                    showAlert(getString(R.string.this_number_is_not_registered))
                } else {
                    val phonebookIntent = Intent(this@ForgotActivity, OtpActivity::class.java)
                    phonebookIntent.putExtra(AppConstants.KEY_TYPE, KEY_PHONE)
                    phonebookIntent.putExtra(AppConstants.SENDEDDATA, et_phonenumber.text.toString().trim())
                    phonebookIntent.putExtra(AppConstants.SENDCODE, ccp.selectedCountryCodeWithPlus.toString())
                    startActivity(phonebookIntent)

                }
            }
            else if (type.toString().equals(KEY_EMAIL))
            {
                val jsonObject = JSONObject(response.toString())
                if (jsonObject.get(SUCCESS).toString().equals(AppConstants.TRUE)) {
                    val phonebookIntent = Intent(this@ForgotActivity, OtpActivity::class.java)
                    phonebookIntent.putExtra(AppConstants.KEY_TYPE,KEY_EMAIL)
                    phonebookIntent.putExtra(AppConstants.SENDEDDATA, et_email.text.toString().trim())
//                    phonebookIntent.putExtra(AppConstants.SENDCODE, ccp.selectedCountryCodeWithPlus.toString())
                    startActivity(phonebookIntent)
                } else {
                    showAlert(jsonObject.get(MESSAGE).toString())
                }
            }
        } else {
            showMessage(resources.getString(R.string.errorString))
        }
    }



}
