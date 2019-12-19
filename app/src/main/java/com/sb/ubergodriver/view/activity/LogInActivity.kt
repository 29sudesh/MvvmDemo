package com.sb.ubergodriver.view.activity

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.sb.ubergodriver.CommonUtils.Utils
import com.sb.ubergodriver.apiConstants.ApiResponse
import com.sb.ubergodriver.apiConstants.Constant
import com.sb.ubergodriver.apiConstants.Status
import com.sb.ubergodriver.apiConstants.ViewModelFactory
import com.sb.ubergodriver.application.App
import com.sb.ubergodriver.model.requests.LoginRequest
import com.sb.ubergodriver.viewModel.login.LoginViewModel
import kotlinx.android.synthetic.main.activity_log_in.*
import javax.inject.Inject
import com.sb.ubergodriver.CommonUtils.AppConstants.*
import com.sb.ubergodriver.R
import com.sb.ubergodriver.model.loginResponse.LoginResponse
import com.sb.ubergodriver.model.requests.LoginWithMobileRequest

public class LogInActivity : BaseActivity() {

    var iswithEmail:Boolean=false

    @set:Inject
     var viewModelFactory: ViewModelFactory? = null

     var viewModel: LoginViewModel?= null

     var progressDialog: ProgressDialog?=null
    var APPTAG:String=LogInActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)


        initViews()
        setLoginWith()
        progressDialog = Constant.getProgressDialog(this, "Please wait...")
        (application as App).getAppComponent().doInjection(this)


        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java!!)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

    }

    private fun initViews() {
        btn_login.setOnClickListener(this)
        tv_forgot.setOnClickListener(this)
        et_email.setOnClickListener(this)
        et_password.setOnClickListener(this)
        et_phone.setOnClickListener(this)
        et_Phone_password.setOnClickListener(this)
        tv_login_with.setOnClickListener(this)
        ll_signup.setOnClickListener(this)

    }


    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.btn_login ->
            {
                removeFocous()

                login()

            }

            R.id.tv_forgot ->
            {
                removeFocous()
                val intent=Intent(this@LogInActivity, ForgotActivity::class.java)
                startActivity(intent)
            }

            R.id.et_email->
            {
                showKeyboard(et_email)
            }

            R.id.et_password->
            {
                showKeyboard(et_password)
            }

            R.id.et_phone->
            {
                showKeyboard(et_phone)
            }

            R.id.et_Phone_password->
            {
                showKeyboard(et_Phone_password)
            }

            R.id.tv_login_with->
            {
                setLoginWith()
            }

            R.id.ll_signup->
            {
                navigatewithFinish(RegisterActivity::class.java)
            }
        }
    }

    private fun setLoginWith() {
        if (iswithEmail)
        {
            iswithEmail=false
            ll_email.visibility=View.VISIBLE
            ll_phone.visibility=View.GONE
            tv_login_with.text=getString(R.string.loginwithphone)
        }
        else
        {
            iswithEmail=true
            ll_email.visibility=View.GONE
            ll_phone.visibility=View.VISIBLE
            tv_login_with.text=getString(R.string.loginwithemail)
        }
    }

    private fun login() {
        Log.e("isValid : ",iswithEmail.toString()+"")
        if (isValid()) {
            if (!Constant.checkInternetConnection(this)) {
            } else {
                if (!iswithEmail) {
                    val loginRequest = LoginRequest()
                    loginRequest.adminId = getAdminId()
                    loginRequest.deviceId = getDeviceId()
                    loginRequest.deviceType = getDeviceType()
                    loginRequest.email = et_email.text.toString().trim()
                    loginRequest.password = et_password.text.toString()
                    viewModel!!.hitLoginApi(loginRequest)
                }
                else
                {
                    val loginRequest = LoginWithMobileRequest()
                    loginRequest.adminId = getAdminId()
                    loginRequest.deviceId = getDeviceId()
                    loginRequest.deviceType = getDeviceType()
                    loginRequest.contryCode=ccp.selectedCountryCodeWithPlus.toString()
                    loginRequest.phone = et_phone.text.toString().trim()
                    loginRequest.password = et_Phone_password.text.toString()
                    viewModel!!.hitMobileLoginApi(loginRequest)
                }
            }

        }

    }

    private fun removeFocous() {
        hideKeyboard(et_email)
        hideKeyboard(et_password)
    }


    /*
     * method to validate $(mobile number) and $(password)
     * */
    private fun isValid(): Boolean {
        if (!iswithEmail) {
            if (!Utils.isValidEmail(et_email.getText().toString().trim())) {
                showAlert(resources.getString(R.string.emailshouldnotempty))
                return false
            } else if (et_password.getText().toString().trim().isEmpty()) {
                showAlert(resources.getString(R.string.enter_your_password))
                return false
            }
        }
        else
        {
            if (et_phone.getText().toString().trim().length<6) {
                showAlert(resources.getString(R.string.enter_valid_mobile))
                return false
            } else if (et_Phone_password.getText().toString().trim().isEmpty()) {
                showAlert(resources.getString(R.string.enter_your_password))
                return false
            }
        }

        return true
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
                Toast.makeText(this@LogInActivity, resources.getString(R.string.errorString), Toast.LENGTH_SHORT).show()
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
            var loginResponse = gson1.fromJson(data, LoginResponse::class.java)
            if (loginResponse.success.toString().equals(TRUE))
            {
                Log.d("data=", loginResponse.driver.id.toString()+"")
                getMyPreferences().setStringValue(USER_ID,loginResponse.driver.id.toString())
                getMyPreferences().setStringValue(USERDETAIL,data)
                navigatewithFinish(MainActivity::class.java)
            }
            else
            {
                showAlert(loginResponse.message.toString())
            }
        } else {
            Toast.makeText(this@LogInActivity, resources.getString(R.string.errorString), Toast.LENGTH_SHORT).show()
        }
    }
}

