package com.sb.ubergodriver.view.activity


import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import com.sb.ubergodriver.R
import android.content.Intent

import android.os.Bundle
import android.util.Log

import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonElement

import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.CommonUtils.Utils
import com.sb.ubergodriver.apiConstants.ApiResponse
import com.sb.ubergodriver.apiConstants.Constant
import com.sb.ubergodriver.apiConstants.Status
import com.sb.ubergodriver.apiConstants.ViewModelFactory
import com.sb.ubergodriver.application.App
import com.sb.ubergodriver.model.loginResponse.LoginResponse
import com.sb.ubergodriver.model.registerstatusresponse.RegisterNumberStatusResponse
import com.sb.ubergodriver.model.requests.RegistrerCheckStatusRequest
import com.sb.ubergodriver.viewModel.login.editProfile.EditProfileViewModel
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import javax.inject.Inject


class RegisterActivity : BaseActivity() {
    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: EditProfileViewModel?= null
    var progressDialog: ProgressDialog?=null

    var APPTAG:String=RegisterActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        progressDialog = Constant.getProgressDialog(this, "Please wait...")

        (application as App).getAppComponent().doInjection(this)


        viewModel = ViewModelProviders.of(this, viewModelFactory).get(EditProfileViewModel::class.java!!)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

        toolBarSetUp()
        initViewS()
    }

    private fun toolBarSetUp() {
        val title:String=getString(R.string.register)
       tv_title_common.setText(title)
    }

    private fun initViewS() {
        btn_reset.setOnClickListener(this)
        iv_back_common.setOnClickListener(this)
        et_phonenumber.setOnClickListener(this)
        ll_login.setOnClickListener(this)

    }

    private fun removeFocous() {
        hideKeyboard(et_phonenumber)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.btn_reset->
            {
                if (et_phonenumber.text.toString().length<=6) {
                    showMessage(getString(R.string.enter_valid_mobile))
                }
                else
                {
                    removeFocous()
                    val registerStatusRequest=RegistrerCheckStatusRequest()
                    registerStatusRequest.adminId=getAdminId()
                    registerStatusRequest.contryCode=ccp.selectedCountryCodeWithPlus.toString()
                    registerStatusRequest.phone=et_phonenumber.text.toString()
                    viewModel!!.hitCheckRegisterStatus(registerStatusRequest)

                }
            }

            R.id.iv_back_common->
            {
                removeFocous()
                finish()
            }

            R.id.et_phonenumber->
            {
                showKeyboard(et_phonenumber)
            }

            R.id.ll_login->
            {
                navigatewithFinish(LogInActivity::class.java)
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
            val loginResponse = gson1.fromJson(data, RegisterNumberStatusResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE))
            {
                val phonebookIntent = Intent(this@RegisterActivity, OtpRegisterActivity::class.java)
                phonebookIntent.putExtra(AppConstants.SENDEDDATA,et_phonenumber.text.toString().trim())
                phonebookIntent.putExtra(AppConstants.SENDCODE, ccp.selectedCountryCodeWithPlus.toString())
                startActivity(phonebookIntent)
            }
            else
            {
                showMessage(loginResponse.message.toString())
            }
        } else {
            showMessage(resources.getString(R.string.errorString))
        }
    }



}
