package com.sb.ubergodriver.view.activity

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.hbb20.CountryCodePicker
import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.CommonUtils.AppConstants.*
import com.sb.ubergodriver.CommonUtils.PermissionUtils
import com.sb.ubergodriver.CommonUtils.PermissionUtils.Companion.PHONEREQUESTCODE
import com.sb.ubergodriver.CommonUtils.Utils
import com.sb.ubergodriver.R
import com.sb.ubergodriver.apiConstants.ApiResponse
import com.sb.ubergodriver.apiConstants.Constant
import com.sb.ubergodriver.apiConstants.Status
import com.sb.ubergodriver.apiConstants.ViewModelFactory
import com.sb.ubergodriver.application.App
import com.sb.ubergodriver.model.loginResponse.LoginResponse
import com.sb.ubergodriver.model.phoneStatus.PhoneStatusResponse
import com.sb.ubergodriver.model.requests.EditProfilerequest
import com.sb.ubergodriver.model.requests.PhoneStatusRequest
import com.sb.ubergodriver.viewModel.login.editProfile.ChangePhoneStatusViewModel
import com.sb.ubergodriver.viewModel.login.editProfile.EditProfileViewModel
import kotlinx.android.synthetic.main.activity_edit_number.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*

import java.security.AccessController.getContext
import javax.inject.Inject

class EditPhoneNumberActivity : BaseActivity() {
    var recievedData:String=""
    var recievedCode:String=""
    var countryCode:String=""
    var phoneNumber:String=""
    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: EditProfileViewModel?= null
    var progressDialog: ProgressDialog?=null
    var viewModelPhoneStatus: ChangePhoneStatusViewModel?= null
    var APPTAG:String=EditPhoneNumberActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_number)
        recievedData=intent.getStringExtra(AppConstants.SENDEDDATA)
        recievedCode=intent.getStringExtra(AppConstants.SENDCODE)
        iv_back_common.setImageResource(R.drawable.ic_back_black)
        et_phone_number.setText(recievedData)
        ccp.setOnClickListener(this)
        ccp!!.setCountryForPhoneCode(Integer.parseInt(recievedCode))
        et_phone_number.setOnClickListener(this)
        btn_save.setOnClickListener(this)

        if (MainActivity.mainActivity.getUserDetail().driver!=null)
        {
            val driver=MainActivity.mainActivity.getUserDetail().driver
            countryCode=driver.contryCode
            phoneNumber=driver.phone


        }

        progressDialog = Constant.getProgressDialog(this, "Please wait...")

        (application as App).getAppComponent().doInjection(this)


        viewModel = ViewModelProviders.of(this, viewModelFactory).get(EditProfileViewModel::class.java!!)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

        viewModelPhoneStatus = ViewModelProviders.of(this, viewModelFactory).get(ChangePhoneStatusViewModel::class.java!!)

        viewModelPhoneStatus!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponsePhoneStatus(it!!) })


    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.btn_save->
            {
                if (!et_phone_number.text.toString().isNullOrEmpty() && et_phone_number.text.toString().length>7)
                {

                    val checkPhone:String=ccp.selectedCountryCodeWithPlus.toString()+et_phone_number.text.toString()
                    val oldPHone:String=countryCode+phoneNumber
                    if (checkPhone.equals(oldPHone))
                    {
                        showAlert(getString(R.string.noneedtocahangenumber))
                    }
                    else {
                        if (!Constant.checkInternetConnection(this)) {
                        } else {
                            val loginRequest = PhoneStatusRequest()
                            loginRequest.driverId = getUserId()
                            loginRequest.adminId=getAdminId()
                            loginRequest.countryCode = ccp.selectedCountryCodeWithPlus.toString()
                            loginRequest.phone = et_phone_number.text.toString()
                            viewModelPhoneStatus!!.hitCheckPhoneStatusApi(loginRequest)
                        }
                    }

                }
                else
                {
                    showAlert(getString(R.string.enter_valid_mobile))
                }
            }
            R.id.iv_back_common->
            {
                finish()
            }

            R.id.et_phone_number->
            {
                et_phone_number.isFocusable=true
                et_phone_number.isFocusableInTouchMode=true
                et_phone_number.requestFocus()
                MainActivity.mainActivity.showKeyboard(et_phone_number)
            }
//            R.id.ccp ->
//            {
//                CountryCodeDialog().openCountryCodeDialog(ccp)
//            }
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
                showAlert( resources.getString(R.string.errorString))
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
            Log.e(APPTAG+"response=","renderSuccesResponse  "+ response.toString())
            val data:String=  Utils.toJson(response)
            val gson1 =  Gson();
            val loginResponse = gson1.fromJson(data, LoginResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE))
            {
                Log.d("data=", loginResponse.driver.id.toString()+"")
                getMyPreferences().setStringValue(AppConstants.USER_ID,loginResponse.driver.id.toString())
                getMyPreferences().setStringValue(AppConstants.USERDETAIL,data)

                val myIntent = Intent()
                myIntent.putExtra(SENDEDDATA, et_phone_number.getText().toString())
                myIntent.putExtra(SENDCODE, ccp.selectedCountryCodeWithPlus.toString())

                setResult(RESULT_OK, myIntent)
                finish()
            }
            else
            {
                showAlert(loginResponse.message.toString())
            }
        } else {
            showAlert(resources.getString(R.string.errorString))
        }
    }

    /*
* method to handle response
* */
    private fun consumeResponsePhoneStatus(apiResponse: ApiResponse) {

        when (apiResponse.status) {

            Status.LOADING -> progressDialog!!.show()

            Status.SUCCESS -> {
                progressDialog!!.dismiss()
                renderSuccessResponsePhoneStatus(apiResponse.data!!)
            }

            Status.ERROR -> {
                progressDialog!!.dismiss()
                showAlert( resources.getString(R.string.errorString))
            }
            else -> {
            }
        }
    }
    /*
    * method to handle success response
    * */
    private fun renderSuccessResponsePhoneStatus(response: JsonElement) {
        if (!response.isJsonNull) {
            Log.e(APPTAG+"response=","renderSuccessResponsePhoneStatus   "+ response.toString())
            val data:String=  Utils.toJson(response)
            val gson1 =  Gson();
            val loginResponse = gson1.fromJson(data, PhoneStatusResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE))
            {
                val phonebookIntent = Intent(this@EditPhoneNumberActivity, OtpChangeNumberActivity::class.java)
                phonebookIntent.putExtra(AppConstants.SENDEDDATA,et_phone_number.text.toString().trim())
                phonebookIntent.putExtra(AppConstants.SENDCODE, ccp.selectedCountryCodeWithPlus.toString())
                startActivityForResult(phonebookIntent,PHONEREQUESTCODE)
            }
            else
            {
                showAlert(loginResponse.message.toString())
            }
        } else {
            showAlert(resources.getString(R.string.errorString))
        }
    }

    override fun getRequestCode(requestcode: Int, data: Intent?) {
        super.getRequestCode(requestcode, data)
        when(requestcode)
        {
            PermissionUtils.PHONEREQUESTCODE ->
            {
                if (data!!.getStringExtra(SENDEDDATA).toString().equals(TRUE))
                {
                    val loginRequest = EditProfilerequest()
                    loginRequest.driverId=getUserId()
                    loginRequest.contryCode=ccp.selectedCountryCodeWithPlus.toString()
                    loginRequest.phone = et_phone_number.text.toString()
                    viewModel!!.hitEditProfileApi(loginRequest)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

    }
}
