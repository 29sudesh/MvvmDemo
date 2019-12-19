package com.sb.ubergodriver.view.activity

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import com.sb.ubergodriver.view.activity.LogInActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
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
import com.sb.ubergodriver.viewModel.login.editProfile.ForgotPasswordViewModel
import com.sinch.verification.*
import kotlinx.android.synthetic.main.activity_otp.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class OtpRegisterActivity : BaseActivity() {

    var recievedData:String=""
    var recievedCode:String=""

    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: ForgotPasswordViewModel?= null
    var progressDialog: ProgressDialog?=null
    var APPTAG:String=OtpRegisterActivity::class.java.name
    //sinch verification
    private var mIsSmsVerification = true
    private var mShouldFallback = true
    internal var isVerify: Boolean? = false
    lateinit var resendTimer: CountDownTimer
    public var mVerification: Verification? = null
    private var editText1: EditText? = null
    private var editText2: EditText? = null
    private var editText3: EditText? = null
    private var editText4: EditText? = null
    private var editTexts: Array<EditText>? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        recievedData=intent.getStringExtra(AppConstants.SENDEDDATA)
        recievedCode=intent.getStringExtra(AppConstants.SENDCODE)
        tv_phonenumber.text=recievedCode.toString()+" "+recievedData.toString()

        editText1 = findViewById<View>(R.id.et_otp1) as EditText
        editText2 = findViewById<View>(R.id.et_otp2) as EditText
        editText3 = findViewById<View>(R.id.et_otp3) as EditText
        editText4 = findViewById<View>(R.id.et_otp4) as EditText
        editTexts = arrayOf<EditText>(editText1!!, editText2!!, editText3!!, editText4!!)

        editText1!!.addTextChangedListener(PinTextWatcher(0))
        editText2!!.addTextChangedListener(PinTextWatcher(1))
        editText3!!.addTextChangedListener(PinTextWatcher(2))
        editText4!!.addTextChangedListener(PinTextWatcher(3))

        editText1!!.setOnKeyListener(PinOnKeyListener(0))
        editText2!!.setOnKeyListener(PinOnKeyListener(1))
        editText3!!.setOnKeyListener(PinOnKeyListener(2))
        editText4!!.setOnKeyListener(PinOnKeyListener(3))

        initViews()
        tv_resendotp.isEnabled = false



        progressDialog = Constant.getProgressDialog(this, "Please wait...")

        (application as App).getAppComponent().doInjection(this)


        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ForgotPasswordViewModel::class.java!!)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

        createVerification()
        startTimer("start")
    }

    private fun initViews() {
        iv_close.setOnClickListener(this)
        iv_edit.setOnClickListener(this)
        btn_done.setOnClickListener(this)
        tv_resendotp.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.iv_close ->
            {
                navigate( LogInActivity::class.java)
                finishAffinity()
            }

            R.id.iv_edit ->
            {
                finish()

            }


            R.id.btn_done ->
            {
                if (!et_otp1.text.toString().isEmpty() && !et_otp2.text.toString().isEmpty() && !et_otp3.text.toString().isEmpty() && !et_otp4.text.toString().isEmpty())

                {
                    var otp: String =""
                    otp=et_otp1.text.toString()+et_otp2.text.toString()+et_otp3.text.toString()+et_otp4.text.toString()
                    mVerification!!.verify(otp)
                }
                else
                {
                    showAlert(getString(R.string.otpshpouldnotempty))
                }

            }

            R.id.tv_resendotp ->
            {
                startTimer("start")
                createVerification()
            }
        }
    }


    private fun createVerification() {
        // It is important to pass ApplicationContext to the Verification config builder as the
        // verification process might outlive the activity.
        val config = SinchVerification.config()
            .applicationKey(AppConstants.SINCH_APP_KEY)
            .context(applicationContext)
            .build()

        val listener = MyVerificationListener()

       mVerification = SinchVerification.createSmsVerification(config, recievedCode + recievedData, listener)
      mVerification!!.initiate()


    }


    internal inner  class MyVerificationListener : VerificationListener {

        override fun onInitiated(result: InitiationResult) {
//            showAlert(result.toString())
        }

        override fun onInitiationFailed(exception: Exception) {
            // Log.e("onInitiationFailed", "Verification initialization failed: " + exception.getMessage());
            // hideProgressAndShowMessage(R.string.failed);

            if (exception is InvalidInputException) {
                // Incorrect number provided
            } else if (exception is ServiceErrorException) {
                // Verification initiation aborted due to early reject feature,
                // client callback denial, or some other Sinch service error.
                // Fallback to other verification method here.
                fallbackIfNecessary()
            } else {
                // Other system error, such as UnknownHostException in case of network error
            }
        }

        override fun onVerificationFallback() {
            fallbackIfNecessary()
        }

        private fun fallbackIfNecessary() {
            if (mShouldFallback) {
                mIsSmsVerification = !mIsSmsVerification
                if (mIsSmsVerification) {
                    // Log.i("fallbackIfNecessary", "Falling back to sms verification.");
                }
                mShouldFallback = false

            }
        }

        override fun onVerified() {
            Log.e("VerifiedOtp : ","Verified!")
            val phonebookIntent = Intent(this@OtpRegisterActivity, RegisterDetailActivity::class.java)
            phonebookIntent.putExtra(AppConstants.SENDEDDATA,recievedData)
            phonebookIntent.putExtra(AppConstants.SENDCODE,recievedCode)
            startActivity(phonebookIntent)
            finishAffinity()

        }

        override fun onVerificationFailed(exception: Exception) {
            progressDialog!!.hide()
//            if (exception.message.toString().equals("The verification code is incorrect.", ignoreCase = true)) {
                showAlert(exception.message.toString())
//            }
            if (exception is CodeInterceptionException) {
                // Automatic code interception failed, probably due to missing permissions.
                // Let the user try and enter the code manually.

            } else {

            }


        }
    }


    fun startTimer(mode: String) {
        resendTimer = object : CountDownTimer(60000, 1000) {
            override fun onFinish() {
                tv_resendotp.isEnabled = true
                tv_resendotp.text=getString(R.string.resendotp)
            }

            override fun onTick(millisUntilFinished: Long) {
                tv_resendotp.isEnabled = false
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                var time = "0" + minutes.toString() + ":" + seconds.toString()
                tv_resendotp.text = time

            }
        }

        resendTimer.start()
    }



    inner class PinTextWatcher internal constructor(private val currentIndex: Int) : TextWatcher {
        private var isFirst = false
        private var isLast = false
        private var newTypedString = ""

        private val isAllEditTextsFilled: Boolean
            get() {
                for (editText in editTexts!!)
                    if (editText.text.toString().trim { it <= ' ' }.length == 0)
                        return false
                return true
            }

        init {

            if (currentIndex == 0)
                this.isFirst = true
            else if (currentIndex == editTexts!!.size - 1)
                this.isLast = true
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            newTypedString = s.subSequence(start, start + count).toString().trim { it <= ' ' }
        }

        override fun afterTextChanged(s: Editable) {

            var text = newTypedString

            /* Detect paste event and set first char */
            if (text.length > 1)
                text = text[0].toString() // TODO: We can fill out other EditTexts

            editTexts!![currentIndex].removeTextChangedListener(this)
            editTexts!![currentIndex].setText(text)
            editTexts!![currentIndex].setSelection(text.length)
            editTexts!![currentIndex].addTextChangedListener(this)

            if (text.length == 1)
                moveToNext()
            else if (text.length == 0)
                moveToPrevious()
        }

        private fun moveToNext() {
            if (!isLast)
                editTexts!![currentIndex + 1].requestFocus()

            if (isAllEditTextsFilled && isLast) { // isLast is optional
                editTexts!![currentIndex].clearFocus()
                hideKeyboard()
            }
        }

        private fun moveToPrevious() {
            if (!isFirst)
                editTexts!![currentIndex - 1].requestFocus()
        }

        private fun hideKeyboard() {
            if (currentFocus != null) {
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            }
        }
    }

    inner class PinOnKeyListener internal constructor(private val currentIndex: Int) : View.OnKeyListener {
        override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                if (editTexts!![currentIndex].text.toString().isEmpty() && currentIndex != 0)
                    editTexts!![currentIndex - 1].requestFocus()
            }
            return false
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
                Log.d("data=", loginResponse.driver.id.toString()+"")
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
