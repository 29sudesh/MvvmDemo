package com.sb.ubergodriver.view.activity

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.CommonUtils.Utils
import com.sb.ubergodriver.R
import com.sb.ubergodriver.apiConstants.ApiResponse
import com.sb.ubergodriver.apiConstants.Constant
import com.sb.ubergodriver.apiConstants.Status
import com.sb.ubergodriver.apiConstants.ViewModelFactory
import com.sb.ubergodriver.application.App
import com.sb.ubergodriver.model.loginResponse.LoginResponse
import com.sb.ubergodriver.model.requests.ChangePasswordRequest
import com.sb.ubergodriver.viewModel.login.editProfile.ChangeEmailViewModel
import com.sb.ubergodriver.viewModel.login.editProfile.ChangePasswordViewModel
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*

import javax.inject.Inject

class EditChangePasswordActivity : BaseActivity() {

    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: ChangePasswordViewModel?= null
    var progressDialog: ProgressDialog?=null
    var APPTAG:String=EditChangePasswordActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        btn_save.setOnClickListener(this)
        iv_back_common.setImageResource(R.drawable.ic_back_black)
        et_current_password.setOnClickListener(this)
        et_new_password.setOnClickListener(this)
        et_confirm_password.setOnClickListener(this)

        progressDialog = Constant.getProgressDialog(this, "Please wait...")

        (application as App).getAppComponent().doInjection(this)


        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ChangePasswordViewModel::class.java!!)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

    }


    override fun onClick(v: View?) {
        super.onClick(v)

        when(v!!.id)
        {
            R.id.iv_back_common->
            {
                finish()
            }
            R.id.btn_save->
            {
                changePassword()
            }
            R.id.et_current_password->
            {
                showKeyboard(et_current_password)
            }
            R.id.et_new_password->
            {
                showKeyboard(et_new_password)
            }
            R.id.et_confirm_password->
            {
                showKeyboard(et_confirm_password)
            }
        }
    }

    private fun changePassword() {
        if (et_current_password.text.length<8)
        {
            et_current_password.requestFocus()
            showAlert(getString(R.string.passwordshouldnotempty))
        }
        else if (et_new_password.text.length<8)
        {
            et_new_password.requestFocus()
            showAlert(getString(R.string.passwordshouldnotempty))
        }
        else if (!et_confirm_password.text.toString().equals(et_new_password.text.toString()))
        {
            et_confirm_password.requestFocus()
            showAlert(getString(R.string.confirmpasswordshouldmatch))
        }
        else
        {
            if (!Constant.checkInternetConnection(this)) {
            } else {
                val loginRequest = ChangePasswordRequest()
                loginRequest.driverId=getUserId()
                loginRequest.newPassword = et_new_password.text.toString()
                loginRequest.oldPassword=et_current_password.text.toString()
                viewModel!!.hitChangePasswordApi(loginRequest)
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
            Log.e(APPTAG+"response=", response.toString())
            val data:String=  Utils.toJson(response)
            val gson1 =  Gson();
            val loginResponse = gson1.fromJson(data, LoginResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE))
            {
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
}
