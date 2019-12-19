package com.sb.ubergodriver.view.fragment

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import com.sb.ubergodriver.CommonUtils.Utils
import com.sb.ubergodriver.R
import com.sb.ubergodriver.interfaces.DrawerView
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.CommonUtils.AppConstants.*
import com.sb.ubergodriver.apiConstants.ApiResponse
import com.sb.ubergodriver.apiConstants.Constant
import com.sb.ubergodriver.apiConstants.Status
import com.sb.ubergodriver.apiConstants.ViewModelFactory
import com.sb.ubergodriver.application.App
import com.sb.ubergodriver.model.requests.UserRatingRequest
import com.sb.ubergodriver.model.socketResponse.DriverRideStatusResponse
import com.sb.ubergodriver.model.socketResponse.SocketBookingResponse
import com.sb.ubergodriver.model.userRating.UserRatingResponse
import com.sb.ubergodriver.socket.interfaces.SocketContract
import com.sb.ubergodriver.view.activity.EditChangePasswordActivity
import com.sb.ubergodriver.view.activity.MainActivity
import com.sb.ubergodriver.viewModel.login.editProfile.UserRatingViewModel
import kotlinx.android.synthetic.main.fragment_amount_paid.*
import org.json.JSONObject
import java.lang.Exception
import javax.inject.Inject

class PayFragment : BaseFragment() {

    val amount:String="30.0"
    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: UserRatingViewModel?= null
    var APPTAG:String= PayFragment::class.java.name
    //    var progressDialog: ProgressDialog?=null
    var driverRideStatus: DriverRideStatusResponse?=null
    private var animShow: Animation?=null
    private var animHide: Animation?=null
    private var bookingId:String="5c76bafa4226914d99be32d3"
    private var user_id:String="5c74eba3854c9c62e95a2fd0"

    companion object {
        var paymentFragment:PayFragment= PayFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_amount_paid, container, false)
        MainActivity.mainActivity.onChangingfragment(PAYSCREEN)
        paymentFragment=this

//        progressDialog = Constant.getProgressDialog(context, "Please wait...")

        (context!!.applicationContext as App).getAppComponent().doInjection(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserRatingViewModel::class.java)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

        if (getArguments() !=null) {
            driverRideStatus =getArguments()!!.getParcelable(AppConstants.SENDEDDATA) as DriverRideStatusResponse
//            HomeFragment.homeFragment.checkDriverStatus(driverRideStatus!!)

        }
//
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animShow = AnimationUtils.loadAnimation( context, R.anim.view_show)
        animHide = AnimationUtils.loadAnimation( context, R.anim.view_hide)
        btn_paid.setOnClickListener(this)
        btn_rating.setOnClickListener(this)
        btn_skip.setOnClickListener(this)

        if (driverRideStatus!=null) {
            bookingId=driverRideStatus!!.booking.id
            user_id=driverRideStatus!!.booking.userId.id
            tv_paid_amount.setText(getString(R.string.turkish_currency_sign) + " " + driverRideStatus!!.booking.fare)
            Log.e("FARE : ",driverRideStatus!!.booking.fare.toString())
        }

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.btn_rating->
            {
                if (ratingBar_amount_paid.rating<1)
                {
                    showAlert(getString(R.string.minimumrating))
                }
                else
                {
                    try {
                        if (driverRideStatus!=null) {
                            val userrequest = UserRatingRequest()
                            userrequest.bookingId = bookingId
                            userrequest.driverId = MainActivity.mainActivity.getUserId()
                            userrequest.rating = ratingBar_amount_paid.rating.toString()
                            userrequest.review = et_comment.text.toString() + ""
                            userrequest.userId = user_id
                            viewModel!!.hittoUserRating(userrequest)
                            MainActivity.mainActivity.forceHideKeyboard(context!!)
                        }

                    }catch (ex:Exception)
                    {
                        ex.printStackTrace()
                    }
                }
            }

            R.id.btn_paid->
            {
                try {
                    btn_paid.visibility=View.GONE
                    val jsonObject= JSONObject()
                    jsonObject.put(AppConstants.STATUS_SOCKET,"4")
                    jsonObject.put(BOOKING_ID,bookingId)
                    MainActivity.mainActivity.sendObjectToSocket(jsonObject, AppConstants.CHANGEDRIVERSTATUS)
                }catch (ex:Exception)
                {
                    ex.printStackTrace()
                }

            }

            R.id.btn_skip->
            {
                try {
                    MainActivity.mainActivity.replaceFragment(HomeFragment(),HOMESCREEN)
                    MainActivity.mainActivity.forceHideKeyboard(context!!)
                }catch (ex:Exception)
                {
                    ex.printStackTrace()
                }


            }

        }
    }

    override fun onResume() {
        super.onResume()


    }

    /*
 * method to handle response
 * */
    private fun consumeResponse(apiResponse: ApiResponse) {

        when (apiResponse.status) {

            Status.LOADING ->
            {

            }
//                progressDialog!!.show()

            Status.SUCCESS -> {
//                progressDialog!!.dismiss()
                renderSuccessResponse(apiResponse.data!!)
            }

            Status.ERROR -> {
//                progressDialog!!.dismiss()
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
            val loginResponse = gson1.fromJson(data, UserRatingResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE))
            {
                MainActivity.mainActivity.replaceFragment(HomeFragment(),AppConstants.HOMESCREEN)
            }
            else
            {
                showAlert(loginResponse.message.toString())
            }
        } else {
            showAlert(resources.getString(R.string.errorString))
        }
    }

    fun onPaidAmmount(status: Int) {
        try {
            activity!!.runOnUiThread(object:Runnable {
                public override fun run() {
                    // change UI elements here
                    if (status.toString().equals("4"))
                    {
                        btn_paid.visibility=View.GONE
                        btn_paid.startAnimation(animHide)
                        tv_has_been_paid.visibility=View.VISIBLE
                        tv_has_been_paid.startAnimation(animShow)
                        ll_rating.visibility=View.VISIBLE
                        ll_rating.startAnimation(animShow)
                    }
                }
            })
        }catch (ex:Exception)
        {
            ex.printStackTrace()
        }


    }

}
