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
import com.sb.ubergodriver.CommonUtils.AppConstants.SENDEDDATA
import com.sb.ubergodriver.CommonUtils.Utils
import com.sb.ubergodriver.R
import com.sb.ubergodriver.apiConstants.ApiResponse
import com.sb.ubergodriver.apiConstants.Constant
import com.sb.ubergodriver.apiConstants.Status
import com.sb.ubergodriver.apiConstants.ViewModelFactory
import com.sb.ubergodriver.application.App
import com.sb.ubergodriver.model.loginResponse.LoginResponse
import com.sb.ubergodriver.model.requests.UpdateEmailRequest
import com.sb.ubergodriver.viewModel.login.editProfile.ChangeEmailViewModel
import com.sb.ubergodriver.viewModel.login.editProfile.EditProfileViewModel
import kotlinx.android.synthetic.main.activity_edit_email.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import javax.inject.Inject

class EditEmailActivity : BaseActivity() {
    var recievedData:String=""


    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: ChangeEmailViewModel?= null
    var progressDialog: ProgressDialog?=null
    var APPTAG:String=EditEmailActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_email)
        recievedData=intent.getStringExtra(AppConstants.SENDEDDATA)
        iv_back_common.setImageResource(R.drawable.ic_back_black)
        et_email.setText(recievedData)
        et_email.setOnClickListener(this)
        btn_save.setOnClickListener(this)


        progressDialog = Constant.getProgressDialog(this, "Please wait...")

        (application as App).getAppComponent().doInjection(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ChangeEmailViewModel::class.java!!)
        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })
    }
    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.btn_save->
            {
                if (Utils.isValidEmail(et_email.getText().toString().trim()))
                {
                    if (!Constant.checkInternetConnection(this)) {
                    } else {
                        hideKeyboard(et_email)
                        val loginRequest = UpdateEmailRequest()
                        loginRequest.driverId=getUserId()
                        loginRequest.email = et_email.text.toString()
                        loginRequest.adminId=getAdminId()
                        viewModel!!.hitEmailApi(loginRequest)
                    }
                }
                else
                {
                    showAlert(getString(R.string.emailshouldnotempty))
                }
            }
            R.id.iv_back_common->
            {
                finish()
            }

            R.id.et_email->
            {
                showKeyboard(et_email)
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
            Log.d(APPTAG+"response=", response.toString())
            val data:String=  Utils.toJson(response)
            val gson1 =  Gson();
            val loginResponse = gson1.fromJson(data, LoginResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE))
            {
                Log.d("data=", loginResponse.driver.id.toString()+"")
                getMyPreferences().setStringValue(AppConstants.USER_ID,loginResponse.driver.id.toString())
                getMyPreferences().setStringValue(AppConstants.USERDETAIL,data)

                val myIntent = Intent()
                myIntent.putExtra(SENDEDDATA, et_email.getText().toString())
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
}
