package com.sb.ubergodriver.view.fragment


import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import com.sb.ubergodriver.R
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.identity.intents.AddressConstants
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.CommonUtils.AppConstants.SENDCODE
import com.sb.ubergodriver.CommonUtils.AppConstants.SENDEDDATA
import com.sb.ubergodriver.CommonUtils.CompressFile
import com.sb.ubergodriver.CommonUtils.PermissionUtils.Companion.CAMERAPERMISSIONCODE
import com.sb.ubergodriver.CommonUtils.PermissionUtils.Companion.EMAILREQUESTCODE
import com.sb.ubergodriver.CommonUtils.PermissionUtils.Companion.FIRSTNAMEREQUESTCODE
import com.sb.ubergodriver.CommonUtils.PermissionUtils.Companion.GALLERYREQUESTCODE
import com.sb.ubergodriver.CommonUtils.PermissionUtils.Companion.LASTNAMEREQUESTCODE
import com.sb.ubergodriver.CommonUtils.PermissionUtils.Companion.PHONENUMBERREQUESTCODE
import com.sb.ubergodriver.CommonUtils.PermissionUtils.Companion.READSTORAGEPERMISSIONCODE
import com.sb.ubergodriver.CommonUtils.Utils
import com.sb.ubergodriver.apiConstants.ApiResponse
import com.sb.ubergodriver.apiConstants.Constant
import com.sb.ubergodriver.apiConstants.Status
import com.sb.ubergodriver.apiConstants.ViewModelFactory
import com.sb.ubergodriver.application.App
import com.sb.ubergodriver.interfaces.RequestCode
import com.sb.ubergodriver.model.loginResponse.LoginResponse
import com.sb.ubergodriver.model.requests.EditProfilerequest
import com.sb.ubergodriver.model.uploadImage.UploadImageResponse
import com.sb.ubergodriver.view.activity.*
import com.sb.ubergodriver.viewModel.login.editProfile.EditProfileViewModel
import com.sb.ubergodriver.viewModel.login.editProfile.UploadImageViewModel
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.layout_common_toolbar_black.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class
EditProfileFragment : BaseFragment() {
    val IMAGE="image"
    val API="api"
    var requestType:String=IMAGE
    var phoneNumber=""
    var countryCode=""

    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModelUploadImage: UploadImageViewModel?= null
    var progressDialog: ProgressDialog?=null
    var viewModel: EditProfileViewModel?= null
    var APPTAG:String=EditProfileFragment::class.java.name

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        MainActivity.mainActivity.onChangingfragment(AppConstants.NOMAINSCREEN)
        tv_title_common_black.text=getString(R.string.profile)
        iv_back_common_black.setOnClickListener(this)
        iv_end_common_black.setOnClickListener(this)
        rl_userImage.setOnClickListener(this)
        et_first_name.setOnClickListener(this)
        et_last_name.setOnClickListener(this)
        et_email.setOnClickListener(this)
        et_phone_number.setOnClickListener(this)
        et_password.setOnClickListener(this)

        if (MainActivity.mainActivity.getUserDetail().driver!=null)
        {
            val driver=MainActivity.mainActivity.getUserDetail().driver
            et_first_name.setText(driver.firstName)
            et_last_name.setText(driver.lastName)
            et_email.setText(driver.email)
            countryCode=driver.contryCode
            phoneNumber=driver.phone
            et_phone_number.setText(countryCode+phoneNumber)
            MainActivity.mainActivity.getImageRequest(driver.profilePic.toString()).into(iv_userImage)

        }


        progressDialog = Constant.getProgressDialog(context, "Please wait...")

        (context!!.applicationContext as App).getAppComponent().doInjection(this)


        viewModelUploadImage = ViewModelProviders.of(this, viewModelFactory).get(UploadImageViewModel::class.java!!)

        viewModelUploadImage!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(EditProfileViewModel::class.java!!)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })


    }


    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.iv_back_common_black->
            {
                    MainActivity.mainActivity.onReplaceFragment(AccountFragment(),AppConstants.ACCOUNTSCREEN)
            }

            R.id.rl_userImage ->
            {
              showPictureDialog()
            }

            R.id.et_first_name ->
            {
                val phonebookIntent = Intent(context, EditFirstNameActivity::class.java)
                phonebookIntent.putExtra(AppConstants.SENDEDDATA,et_first_name.text.toString())
                startActivityForResult(phonebookIntent, FIRSTNAMEREQUESTCODE)
            }

            R.id.et_last_name ->
            {
                val phonebookIntent = Intent(context, EditLastNameActivity::class.java)
                phonebookIntent.putExtra(AppConstants.SENDEDDATA,et_last_name.text.toString())
                startActivityForResult(phonebookIntent, LASTNAMEREQUESTCODE)
            }

            R.id.et_email ->
            {
                val phonebookIntent = Intent(context, EditEmailActivity::class.java)
                phonebookIntent.putExtra(AppConstants.SENDEDDATA,et_email.text.toString())
                startActivityForResult(phonebookIntent, EMAILREQUESTCODE)
            }

            R.id.et_phone_number ->
            {
                val phonebookIntent = Intent(context, EditPhoneNumberActivity::class.java)
                phonebookIntent.putExtra(AppConstants.SENDEDDATA,phoneNumber)
                phonebookIntent.putExtra(AppConstants.SENDCODE,countryCode)
                startActivityForResult(phonebookIntent, PHONENUMBERREQUESTCODE)
            }
            R.id.et_password ->
            {
                navigate(EditChangePasswordActivity::class.java)

            }


        }
    }

    override fun getRequestCode(requestcode: Int, data: Intent?) {
        super.getRequestCode(requestcode, data)

        when(requestcode)
        {

            CAMERAPERMISSIONCODE->
            {
                Log.e("CAMERAPERMISSIONCODE","Here")

                val thumbnail = data!!.extras!!.get("data") as Bitmap
                //iv_profile!!.setImageBitmap(thumbnail)
                 val file=  File(MainActivity.mainActivity.saveImage(thumbnail))
//                val file = CompressFile.getCompressed(context,MainActivity.mainActivity.imageFilePath)
                Log.e("CAMERAFILE : " ,MainActivity.mainActivity.saveImage(thumbnail))
                val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
                requestType=IMAGE
                val body = MultipartBody.Part.createFormData("pic", file.getAbsolutePath(), reqFile)
                viewModelUploadImage!!.hitUploadImageApi(body)


            }

            GALLERYREQUESTCODE->
            {
                Log.e("GALLERYREQUESTCODE","Here")
                val file = File(MainActivity.mainActivity.getImagePathFromUri(data!!.data))
                val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
                requestType=IMAGE
                val body = MultipartBody.Part.createFormData("pic", file.getAbsolutePath(), reqFile)
                viewModelUploadImage!!.hitUploadImageApi(body)



            }
            FIRSTNAMEREQUESTCODE->
            {
                et_first_name.setText(data!!.getStringExtra(SENDEDDATA).toString())

            }
            LASTNAMEREQUESTCODE->
            {
                et_last_name.setText(data!!.getStringExtra(SENDEDDATA).toString())
            }

            EMAILREQUESTCODE->
            {
                et_email.setText(data!!.getStringExtra(SENDEDDATA).toString())
            }

            PHONENUMBERREQUESTCODE->
            {
                phoneNumber=data!!.getStringExtra(SENDEDDATA).toString()
                countryCode=data!!.getStringExtra(SENDCODE).toString()
                et_phone_number.setText(countryCode+phoneNumber)
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
                if (requestType==IMAGE) {
                    renderUploadImageSuccessResponse(apiResponse.data!!)
                }
                else if (requestType==API)
                {
                    renderApiSuccessResponse(apiResponse.data!!)
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
            Log.e(APPTAG+"response=","renderUploadImageSuccessResponse    "+ response.toString())
            val data:String=  Utils.toJson(response)
            val gson1 =  Gson();
            var loginResponse = gson1.fromJson(data, UploadImageResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE))
            {
                Log.d("data=", loginResponse.pic.toString()+"")

                val loginRequest = EditProfilerequest()
                loginRequest.driverId=MainActivity.mainActivity.getUserId()
                loginRequest.profilePic = loginResponse.pic.profilePic.toString()
                Log.e("profilePic : ",loginRequest.profilePic)
                requestType=API
                viewModel!!.hitEditProfileApi(loginRequest)

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
    private fun renderApiSuccessResponse(response: JsonElement) {
        if (!response.isJsonNull) {
            Log.d(APPTAG+"response=", "renderApiSuccessResponse"+response.toString())
            val data:String=  Utils.toJson(response)
            val gson1 =  Gson();
            var loginResponse = gson1.fromJson(data, LoginResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE))
            {
                Log.d("data=", loginResponse.driver.id.toString()+"")
                requestType=IMAGE
                MainActivity.mainActivity.getImageRequest(loginResponse.driver.profilePic.toString()).into(iv_userImage)
                MainActivity.mainActivity.getMyPreferences().setStringValue(AppConstants.USER_ID,loginResponse.driver.id.toString())
                MainActivity.mainActivity.getMyPreferences().setStringValue(AppConstants.USERDETAIL,data)

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
