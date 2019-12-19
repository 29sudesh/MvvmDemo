package com.sb.ubergodriver.view.activity

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.CommonUtils.AppConstants.SENDEDDATA
import com.sb.ubergodriver.CommonUtils.PermissionUtils
import com.sb.ubergodriver.CommonUtils.Utils
import com.sb.ubergodriver.R
import com.sb.ubergodriver.apiConstants.ApiResponse
import com.sb.ubergodriver.apiConstants.Constant
import com.sb.ubergodriver.apiConstants.Status
import com.sb.ubergodriver.apiConstants.ViewModelFactory
import com.sb.ubergodriver.application.App
import com.sb.ubergodriver.model.registerstatusresponse.RegisterNumberStatusResponse
import com.sb.ubergodriver.model.requests.EditProfilerequest
import com.sb.ubergodriver.model.requests.RegisterRequest
import com.sb.ubergodriver.model.requests.RegistrerCheckStatusRequest
import com.sb.ubergodriver.model.uploadImage.UploadImageResponse
import com.sb.ubergodriver.viewModel.login.LoginViewModel
import com.sb.ubergodriver.viewModel.login.editProfile.EditProfileViewModel
import com.sb.ubergodriver.viewModel.login.editProfile.UploadImageViewModel
import kotlinx.android.synthetic.main.activity_regisetr_detail.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Exception
import javax.inject.Inject

class RegisterDetailActivity : BaseActivity() {
    var proFilePic:String=""
    var countryCode:String=""
    var phoneNumber:String=""

    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModelUploadImage: UploadImageViewModel?= null
    var progressDialog: ProgressDialog?=null

    var APPTAG:String=RegisterDetailActivity::class.java.name

    var viewModel: EditProfileViewModel?= null

    var type:String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regisetr_detail)
        try {
            if (intent!=null) {
                if (intent.getStringExtra(AppConstants.SENDEDDATA)!=null) {
                    phoneNumber = intent.getStringExtra(AppConstants.SENDEDDATA)
                    countryCode = intent.getStringExtra(AppConstants.SENDCODE)
                }
            }
        }catch (ex:Exception)
        {
            ex.printStackTrace()
        }


        progressDialog = Constant.getProgressDialog(this, "Please wait...")

        (application as App).getAppComponent().doInjection(this)


        viewModelUploadImage = ViewModelProviders.of(this, viewModelFactory).get(UploadImageViewModel::class.java)

        viewModelUploadImage!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(EditProfileViewModel::class.java)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

        toolBarSetUp()
        initViewS()
    }

    private fun toolBarSetUp() {
        val title:String=getString(R.string.signup)
        tv_title_common.setText(title)
    }


    private fun initViewS() {
        iv_back_common.setOnClickListener(this)
        btn_reset.setOnClickListener(this)
        iv_userImage.setOnClickListener(this)
        iv_back_common.setImageResource(R.drawable.ic_back_black)
        iv_back_common.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.btn_reset->
            {

                goToNextScreen()

            }
            R.id.iv_userImage->
            {
                type="image"
                showPictureDialog()
            }

            R.id.iv_back_common->
            {
                navigatewithFinish(LogInActivity::class.java)
            }

            R.id.et_first_name->
            {
                showKeyboard(et_first_name)
            }
            R.id.et_last_name->
            {
                showKeyboard(et_last_name)
            }

            R.id.et_email->
            {
                showKeyboard(et_email)
            }
            R.id.et_password->
            {
                showKeyboard(et_password)
            }
            R.id.et_confirm_password->
            {
                showKeyboard(et_confirm_password)
            }
        }
    }

    /*
   * method to validate $(mobile number) and $(password)
   * */
    private fun isValid(): Boolean {

         if (et_first_name.getText().toString().trim().isEmpty()) {
            showAlert(resources.getString(R.string.nameshouldnotempty))
             et_first_name.requestFocus()
            return false
        }
        else if (et_last_name.getText().toString().trim().isEmpty()) {
            showAlert(resources.getString(R.string.nameshouldnotempty))
            et_email.requestFocus()
            return false
        }
         else if (!et_email.getText().toString().trim().isEmpty()) {
              if (!Utils.isValidEmail(et_email.getText().toString().trim())) {
                 showAlert(resources.getString(R.string.emailshouldnotempty))
                 et_email.requestFocus()
                 return false
             }
         }
         else if (et_password.getText().toString().length<6) {
                showAlert(resources.getString(R.string.enter_valid_password))
             et_password.requestFocus()
                return false
            }
         else if (!et_confirm_password.getText().toString().equals(et_password.text.toString().trim())) {
             showAlert(resources.getString(R.string.confirmpasswordshouldmatch))
             et_confirm_password.requestFocus()
             return false
         }

        return true
    }

    private fun goToNextScreen() {
        if (isValid()) {
            if (et_email.text.toString().trim().isEmpty())
            {
                val registerRequest = RegisterRequest()
                registerRequest.profilePic=proFilePic
                registerRequest.firstName=et_first_name.text.toString()
                registerRequest.lastName=et_last_name.text.toString()
                registerRequest.email=et_email.text.toString()
                registerRequest.password=et_password.text.toString()
                registerRequest.contryCode=countryCode
                registerRequest.phone=phoneNumber
                val intent = Intent(this@RegisterDetailActivity, RegisterDetailActivity2::class.java)
                intent.putExtra(SENDEDDATA, registerRequest)
                startActivity(intent)
//                finish()
            }
            else
            {
                type="submit"
                val registerRequest=RegistrerCheckStatusRequest()
                registerRequest.email=et_email.text.toString()
                registerRequest.adminId=getAdminId()
                viewModel!!.hitCheckRegisterStatus(registerRequest)
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
                if (type.toString().equals("image"))
                {
                    renderUploadImageSuccessResponse(apiResponse.data!!)

                }
                else
                {
                    renderSuccessResponse(apiResponse.data!!)
                }
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
    private fun renderUploadImageSuccessResponse(response: JsonElement) {
        if (!response.isJsonNull) {
            Log.e(APPTAG+"response=","rendeUploadImageSuccessResponse "+ response.toString())
            val data:String=  Utils.toJson(response)
            val gson1 =  Gson();
            val loginResponse = gson1.fromJson(data, UploadImageResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE))
            {
                Log.d("data=", loginResponse.pic.toString()+"")
                proFilePic=loginResponse.pic.profilePic.toString()
//                getImageRequest(proFilePic).into(iv_userImage)

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
  * method to handle success response
  * */
    private fun renderSuccessResponse(response: JsonElement) {
        if (!response.isJsonNull) {
            Log.e(APPTAG+"responsecheck=","rendeSuccessresponse   "+ response.toString())
            val data:String=  Utils.toJson(response)
            val gson1 =  Gson();
            val loginResponse = gson1.fromJson(data, RegisterNumberStatusResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE))
            {
                val registerRequest = RegisterRequest()
                registerRequest.profilePic=proFilePic
                registerRequest.firstName=et_first_name.text.toString()
                registerRequest.lastName=et_last_name.text.toString()
                registerRequest.email=et_email.text.toString()
                registerRequest.password=et_password.text.toString()
                registerRequest.contryCode=countryCode
                registerRequest.phone=phoneNumber
                val intent = Intent(this@RegisterDetailActivity, RegisterDetailActivity2::class.java)
                intent.putExtra(SENDEDDATA, registerRequest)
                startActivity(intent)
//                finish()
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
        when(requestcode) {

            PermissionUtils.CAMERAPERMISSIONCODE -> {
                Log.e("CAMERAPERMISSIONCODE", "Here")
                val thumbnail = data!!.extras!!.get("data") as Bitmap
                //iv_profile!!.setImageBitmap(thumbnail)
                val file=  File(saveImage(thumbnail))
                Glide.with(this).load(file).into(iv_userImage)
                val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
                val body = MultipartBody.Part.createFormData("pic", file.getAbsolutePath(), reqFile)
                viewModelUploadImage!!.hitUploadImageApi(body)


            }

            PermissionUtils.GALLERYREQUESTCODE -> {
                Log.e("GALLERYREQUESTCODE", "Here")
                val file = File(getImagePathFromUri(data!!.data))
                Glide.with(this).load(file).into(iv_userImage)
                val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
                val body = MultipartBody.Part.createFormData("pic", file.getAbsolutePath(), reqFile)
                viewModelUploadImage!!.hitUploadImageApi(body)


            }
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        navigatewithFinish(LogInActivity::class.java)
    }

}
