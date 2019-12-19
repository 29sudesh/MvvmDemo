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
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.CommonUtils.AppConstants.SENDEDDATA
import com.sb.ubergodriver.CommonUtils.PermissionUtils
import com.sb.ubergodriver.CommonUtils.PermissionUtils.Companion.CAMERAPERMISSIONCODE
import com.sb.ubergodriver.CommonUtils.Utils
import com.sb.ubergodriver.R
import com.sb.ubergodriver.apiConstants.*
import com.sb.ubergodriver.application.App
import com.sb.ubergodriver.model.loginResponse.LoginResponse
import com.sb.ubergodriver.model.requests.RegisterRequest
import com.sb.ubergodriver.viewModel.login.editProfile.RegisterViewModel
import kotlinx.android.synthetic.main.activity_regisetr_detail3.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Exception
import javax.inject.Inject

class RegisterDetailActivity3 : BaseActivity() {
    var registerRequest = RegisterRequest()
    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: RegisterViewModel? = null
    var APPTAG:String=RegisterDetailActivity3::class.java.name
    var progressDialog: ProgressDialog? = null
    val DRIVER_LICENCE="driver_license";
    val TZINI="tzini"
    val CAR_LICENCE="car_licence"
    val NUMBER_PLATE="number_plate"
    var typeOfImage:String=""

    var driver_licence: File? = null
    var tzini: File? = null
    var numberPlate: File? = null
    var rc: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regisetr_detail3)
        try {
            if (intent!=null)
            {
                registerRequest = intent.getParcelableExtra(SENDEDDATA) as RegisterRequest
            }
        }catch (ex:Exception)
        {
            ex.printStackTrace()
        }


        progressDialog = Constant.getProgressDialog(this, "Please wait...")
        (application as App).getAppComponent().doInjection(this)


        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RegisterViewModel::class.java!!)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })
        toolBarSetUp()
        initViewS()
    }

    private fun toolBarSetUp() {
        val title: String = getString(R.string.signup)
        tv_title_common.setText(title)
    }

    private fun initViewS() {
        iv_back_common.setOnClickListener(this)
        btn_reset.setOnClickListener(this)
        iv_back_common.setImageResource(R.drawable.ic_back_black)
        iv_back_common.setOnClickListener(this)
        iv_driver_licence.setOnClickListener(this)
        iv_tzini.setOnClickListener(this)
        iv_numberPlate.setOnClickListener(this)
        iv_car_licence.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v!!.id) {
            R.id.btn_reset -> {

                if (isValid())
                {
//                    showAlert("Success")
                    val reqFileDriverLicence = RequestBody.create(MediaType.parse("image/*"), driver_licence)
                    val bodyreqFileDriverLicence = MultipartBody.Part.createFormData("licence", driver_licence!!.getAbsolutePath(), reqFileDriverLicence)
                    val reqFiletzini = RequestBody.create(MediaType.parse("image/*"), tzini)
                    val bodyreqFiletzini = MultipartBody.Part.createFormData("inzi", tzini!!.getAbsolutePath(), reqFiletzini)
                    val reqFilecarLicence = RequestBody.create(MediaType.parse("image/*"), rc)
                    val bodyreqFilecarLicence = MultipartBody.Part.createFormData("rc", rc!!.getAbsolutePath(), reqFilecarLicence)
                    val reqFilecarnumberPlate = RequestBody.create(MediaType.parse("image/*"), numberPlate)
                    val bodyreqFilenumberPlate = MultipartBody.Part.createFormData("plate", numberPlate!!.getAbsolutePath(), reqFilecarnumberPlate)
                    // finally serialize the above MyAttach object into JSONObject
               val gson =  Gson();
                 val json:String = gson.toJson(registerRequest)
                    val requestBody = RequestBody.create(
                        MediaType.parse("multipart/form-data"), // notice I'm using "multipart/form-data"
                        json
                    )
                    viewModel!!.hitRegisterApi(requestBody,bodyreqFileDriverLicence,bodyreqFiletzini,bodyreqFilenumberPlate,bodyreqFilecarLicence)
                }
//                goToNextScreen()

            }
            R.id.iv_back_common -> {
//                val intent = Intent(this@RegisterDetailActivity3, RegisterDetailActivity2::class.java)
//                intent.putExtra(SENDEDDATA, registerRequest)
//                startActivity(intent)
                finish()
//                navigatewithFinish(RegisterDetailActivity2::class.java)
            }

            R.id.iv_car_licence -> {
                typeOfImage=CAR_LICENCE
                openCamera()
            }
            R.id.iv_tzini -> {
                typeOfImage=TZINI
             openCamera()
            }
            R.id.iv_numberPlate -> {
                typeOfImage=NUMBER_PLATE
                openCamera()
            }
            R.id.iv_driver_licence -> {
                typeOfImage=DRIVER_LICENCE
                openCamera()
            }
        }
    }

//    private fun goToNextScreen() {
//        val registerRequest = RegisterRequest()
//        val intent = Intent(this@RegisterDetailActivity3, RegisterDetailActivity3::class.java)
//        intent.putExtra(SENDEDDATA, registerRequest)
//        startActivity(intent)
//    }

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
                Toast.makeText(
                    this@RegisterDetailActivity3,
                    resources.getString(R.string.errorString),
                    Toast.LENGTH_SHORT
                ).show()
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
            val data: String = Utils.toJson(response)
            val gson1 = Gson();
            val loginResponse = gson1.fromJson(data, LoginResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE))
            {
                Log.d("data=", loginResponse.driver.id.toString() + "")
                val phonebookIntent = Intent(this@RegisterDetailActivity3, RegisterReviewAcivity::class.java)
                phonebookIntent.putExtra(AppConstants.SENDEDDATA,loginResponse.driver.firstName+" "+loginResponse.driver.lastName)
                phonebookIntent.putExtra(AppConstants.SENDCODE,loginResponse.driver.profilePic)
                startActivity(phonebookIntent)
                finishAffinity()
        } else {
            showAlert(loginResponse.message.toString())
        }
    }
         else {
            Toast.makeText(this@RegisterDetailActivity3, resources.getString(R.string.errorString), Toast.LENGTH_SHORT).show()
        }
    }


    /*
  * method to validate $(mobile number) and $(password)
  * */
    private fun isValid(): Boolean {

        if (driver_licence == null) {
            showAlert(resources.getString(R.string.please_upload_driver_licenve))
            return false
        }
        else if (tzini == null) {
            showAlert(resources.getString(R.string.please_upload_tzini_picture))
            return false
        }
        else if (numberPlate == null) {
            showAlert(resources.getString(R.string.please_upload_numberPlate_picture_with_car))
            return false
        }
        else if (rc == null) {
            showAlert(resources.getString(R.string.please_upload_car_licence_picture))
            return false
        }

        return true
    }

    override fun getRequestCode(requestcode: Int, data: Intent?) {
        super.getRequestCode(requestcode, data)
        when (requestcode) {

            CAMERAPERMISSIONCODE -> {
                Log.e("CAMERAPERMISSIONCODE", "Here")

                val thumbnail = data!!.extras!!.get("data") as Bitmap
                //iv_profile!!.setImageBitmap(thumbnail)
                val file=  File(saveImage(thumbnail))
                if (typeOfImage.toString().equals(DRIVER_LICENCE)) {
                    driver_licence =file
                    // Create glide request manager
                     Glide.with(this).load(driver_licence).into(iv_driver_licence)
                } else if (typeOfImage.toString().equals(CAR_LICENCE)) {
                    rc = file
                    Glide.with(this).load(rc).into(iv_car_licence)
                } else if (typeOfImage.toString().equals(TZINI)) {
                    tzini = file
                    Glide.with(this).load(tzini).into(iv_tzini)
                } else if (typeOfImage.toString().equals(NUMBER_PLATE)) {
                    numberPlate = file
                    Glide.with(this).load(numberPlate).into(iv_numberPlate)
                }

            }
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
//        val intent = Intent(this@RegisterDetailActivity3, RegisterDetailActivity2::class.java)
//        intent.putExtra(SENDEDDATA, registerRequest)
//        startActivity(intent)
        finish()
    }
}
