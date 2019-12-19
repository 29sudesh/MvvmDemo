package com.sb.ubergodriver.view.activity

import android.app.Dialog
import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.CommonUtils.AppConstants.SENDEDDATA
import com.sb.ubergodriver.CommonUtils.CustomTextUtils.CustomEditText
import com.sb.ubergodriver.CommonUtils.Utils
import com.sb.ubergodriver.R
import com.sb.ubergodriver.apiConstants.ApiResponse
import com.sb.ubergodriver.apiConstants.Constant
import com.sb.ubergodriver.apiConstants.Status
import com.sb.ubergodriver.apiConstants.ViewModelFactory
import com.sb.ubergodriver.application.App
import com.sb.ubergodriver.model.requests.RegisterRequest
import com.sb.ubergodriver.model.vehicle_type.VehicleTypeResponse
import com.sb.ubergodriver.model.vehicle_type.Vehicletype
import com.sb.ubergodriver.view.adapters.VehicleAdapters
import com.sb.ubergodriver.viewModel.login.editProfile.RegisterViewModel
import kotlinx.android.synthetic.main.activity_regisetr_detail2.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import java.lang.Exception
import javax.inject.Inject

class RegisterDetailActivity2 : BaseActivity() {
    var registerRequest = RegisterRequest()
    var vehicle_id:String=""

    @set:Inject
    var viewModelFactory: ViewModelFactory? = null

    var viewModel: RegisterViewModel? = null

    var progressDialog: ProgressDialog? = null
    var APPTAG:String=RegisterDetailActivity2::class.java.name

    var vehicleTypeResponse:VehicleTypeResponse?=null

    companion object {
        var registerdetail2Activity:RegisterDetailActivity2?=null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regisetr_detail2)
        registerdetail2Activity=this
        try {
            if (intent!=null) {
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

        viewModel!!.hitVehicleType(getAdminId())

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
        iv_back_common.setImageResource(R.drawable.ic_back_black)
        iv_back_common.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.btn_reset->
            {

                if (isValid()) {
                    goToNextScreen()
                }

            }

            R.id.iv_back_common->
            {
//                val intent = Intent(this@RegisterDetailActivity2, RegisterDetailActivity::class.java)
//                intent.putExtra(SENDEDDATA, registerRequest)
//                startActivity(intent)
                finish()
//                navigatewithFinish(RegisterDetailActivity::class.java)
            }

            R.id.et_vehicle_color->
            {
                showKeyboard(et_vehicle_color)
            }

            R.id.et_vehicle_model->
            {
                showKeyboard(et_vehicle_model)
            }
            R.id.et_vehicle_manufacturing_year->
            {
                showKeyboard(et_vehicle_manufacturing_year)
            }
            R.id.et_vehicle_number->
            {
                showKeyboard(et_vehicle_number)
            }
            R.id.et_vehicle_name->
            {
                showKeyboard(et_vehicle_name)
            }
            R.id.et_vehicle_type->
            {
                if (vehicleTypeResponse!=null) {
                    showMyDialog(vehicleTypeResponse!!,et_vehicle_type)
                }
//                showKeyboard(et_vehicle_type)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onBackPressed() {
//        super.onBackPressed()
//        val intent = Intent(this@RegisterDetailActivity2, RegisterDetailActivity::class.java)
//        intent.putExtra(SENDEDDATA, registerRequest)
//        startActivity(intent)
        finish()
    }

    public fun changeValue(string: String)
    {
        vehicle_id=string
    }

    fun showMyDialog(vehicleTypeResponse: VehicleTypeResponse,et_vehicle_type: CustomEditText) {
        val dialog = Dialog(this@RegisterDetailActivity2)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.vehicle_dialog)
        val window = dialog.getWindow()
        val wlp = window.getAttributes()
        wlp.gravity = Gravity.CENTER or Gravity.CENTER
        wlp.width = LinearLayout.LayoutParams.WRAP_CONTENT
        window.setAttributes(wlp)
        val text_day = dialog.findViewById(R.id.rv_vehicleType) as RecyclerView
        text_day.layoutManager=LinearLayoutManager(this@RegisterDetailActivity2)
        text_day.adapter=VehicleAdapters(this@RegisterDetailActivity2,vehicleTypeResponse.vehicletype as ArrayList<Vehicletype>,et_vehicle_type,registerdetail2Activity,dialog  )

        dialog.show()
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
                Toast.makeText(
                    this@RegisterDetailActivity2,
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
            val loginResponse = gson1.fromJson(data, VehicleTypeResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE))
            {
                vehicleTypeResponse=loginResponse
                Log.d("data=", vehicleTypeResponse!!.vehicletype.toString() + "")
            } else {
                showAlert(loginResponse.message.toString())
            }
        }
        else {
            Toast.makeText(this@RegisterDetailActivity2, resources.getString(R.string.errorString), Toast.LENGTH_SHORT).show()
        }
    }

    /*
  * method to validate $(mobile number) and $(password)
  * */
    private fun isValid(): Boolean {

        if (et_vehicle_name.getText().toString().trim().isEmpty()) {
            showAlert(resources.getString(R.string.vehicle_name_houldnotempty))
            et_vehicle_name.requestFocus()
            return false
        }
        else if (et_vehicle_number.getText().toString().trim().isEmpty()) {
            showAlert(resources.getString(R.string.vehicle_number_should_not_empty))
            et_vehicle_number.requestFocus()
            return false
        }
        else if (et_vehicle_type.getText().toString().trim().isEmpty()) {
            showAlert(resources.getString(R.string.vehicle_type_should_not_empty))
            et_vehicle_type.requestFocus()
            return false
        }
        else if (et_vehicle_model.getText().toString().trim().isEmpty()) {
            showAlert(resources.getString(R.string.vehicle_model_should_not_empty))
            et_vehicle_model.requestFocus()
            return false
        }
        else if (et_vehicle_manufacturing_year.getText().toString().trim().isEmpty()) {
            showAlert(resources.getString(R.string.vehicle_manufacturing_name__should_not_empty))
            et_vehicle_manufacturing_year.requestFocus()
            return false
        }
        else if (et_vehicle_color.getText().toString().trim().isEmpty()) {
            showAlert(resources.getString(R.string.vehicle_color_should_not_empty))
            et_vehicle_color.requestFocus()
            return false
        }

        return true
    }

    private fun goToNextScreen() {
        registerRequest.vehicleColor=et_vehicle_color.text.toString()
        registerRequest.vehicleNumber=et_vehicle_number.text.toString()
        registerRequest.vehicleType=vehicle_id
        registerRequest.model=et_vehicle_model.text.toString()
        registerRequest.vehicleName=et_vehicle_name.text.toString()
        registerRequest.manufacturerName=et_vehicle_manufacturing_year.text.toString()
        registerRequest.adminId=getAdminId()
        registerRequest.isVerified=0
        registerRequest.isFullUrl=0
        val intent=Intent(this@RegisterDetailActivity2,RegisterDetailActivity3::class.java)
        intent.putExtra(SENDEDDATA,registerRequest)
        Log.e("vehicle_id ",registerRequest.vehicleType.toString())
        startActivity(intent)
    }
}
