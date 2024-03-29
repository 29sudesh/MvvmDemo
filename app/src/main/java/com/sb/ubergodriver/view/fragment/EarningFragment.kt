package com.sb.ubergodriver.view.fragment

import android.app.Dialog
import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
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
import com.sb.ubergodriver.model.tripHistory.BookingHistory
import com.sb.ubergodriver.model.tripHistory.TripHistoryResponse
import com.sb.ubergodriver.model.tripHistory.TriphistoryRequest
import com.sb.ubergodriver.view.activity.MainActivity
import com.sb.ubergodriver.view.adapters.PaginationScrollListener
import com.sb.ubergodriver.view.adapters.TripHistoryAdapters
import com.sb.ubergodriver.viewModel.login.tripHistory.TripHistoryViewModel
import kotlinx.android.synthetic.main.fragment_earning.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import javax.inject.Inject

class EarningFragment:BaseFragment() {

    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: TripHistoryViewModel?= null
    var progressDialog: ProgressDialog?=null
    var APPTAG:String= EarningFragment::class.java.name
    private var type:Int=0
    private var page:Int=0
    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var updatedlist:ArrayList<BookingHistory> = ArrayList<BookingHistory>()
    var adapterEarning:TripHistoryAdapters?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_earning,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.mainActivity.onChangingfragment(AppConstants.EARNINGSCREEN)
        tv_title_common.setText(getString(R.string.earnings))
        iv_back_common.setOnClickListener(this)
        iv_end_common.visibility=View.VISIBLE
        iv_end_common.setImageResource(R.drawable.ic_filter)
        iv_end_common.setOnClickListener(this)
        ll_trip_history.setOnClickListener(this)
        ll_invite.setOnClickListener(this)
        tv_no_data_found.visibility=View.VISIBLE
        rv_trip_history.visibility=View.GONE
        val linearlayoutManager:LinearLayoutManager= LinearLayoutManager(context)
        rv_trip_history.layoutManager= linearlayoutManager
        adapterEarning=TripHistoryAdapters(context!!,updatedlist)
        rv_trip_history.adapter=adapterEarning
        type=0
        page=0
        //ViewModelInitialize
        progressDialog = Constant.getProgressDialog(context, "Please wait...")

        (context!!.applicationContext as App).getAppComponent().doInjection(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TripHistoryViewModel::class.java)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

        rv_trip_history?.addOnScrollListener(object : PaginationScrollListener(linearlayoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                if (updatedlist.size>12) {
                    isLoading = true
                    //you have to call loadmore items to get more data
                    page = page + 1
                    getResponse(page)
                }
            }
        })
        getResponse(page)
    }

    private fun getResponse(page: Int) {
        isLoading = false
        val triprequest=TriphistoryRequest()
        triprequest.driverId=MainActivity.mainActivity.getUserId()
        triprequest.skip= page
        triprequest.type=type
        viewModel!!.hittoGetHistory(triprequest)
    }

    override fun onPause() {
        super.onPause()
        if (progressDialog!=null) {
            progressDialog!!.dismiss()
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.iv_back_common ->
            {
                MainActivity.mainActivity.replaceFragment(HomeFragment(),AppConstants.HOMESCREEN)
            }

            R.id.ll_trip_history ->
            {
                MainActivity.mainActivity.replaceFragment(TripHistoryFragment(),AppConstants.TRIPHISTORY)
            }

            R.id.ll_invite ->
            {

            }

            R.id.iv_end_common ->
            {
                showDialog()
            }
        }
    }


    fun showDialog() {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.filter_dialog)
        val window = dialog.getWindow()
        val wlp = window.getAttributes()
        wlp.gravity = Gravity.TOP or Gravity.RIGHT
        wlp.width = LinearLayout.LayoutParams.WRAP_CONTENT
        window.setAttributes(wlp)
        val text_day = dialog.findViewById(R.id.text_day) as TextView
        val text_weekly = dialog.findViewById(R.id.text_weekly) as TextView
        val text_monthly = dialog.findViewById(R.id.text_monthly) as TextView
        val text_total = dialog.findViewById(R.id.text_total) as TextView
        text_day.setOnClickListener(object:View.OnClickListener {
           override fun onClick(v:View) {
               page=0
               type=0
               getResponse(0)
                dialog.dismiss()
            }
        })
        text_weekly.setOnClickListener(object:View.OnClickListener {
            override fun onClick(v:View) {
                page=0
                type=1
                getResponse(0)
                dialog.dismiss()
            }
        })
        text_monthly.setOnClickListener(object:View.OnClickListener {
            override fun onClick(v:View) {
                page=0
                type=2
                getResponse(0)
                dialog.dismiss()
            }
        })
        text_total.setOnClickListener(object:View.OnClickListener {
            override fun onClick(v:View) {
                page=0
                type=3
                getResponse(0)
                dialog.dismiss()
            }
        })
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
            isLoading = false
            Log.e(APPTAG+"response=", response.toString())
            val data:String=  Utils.toJson(response)
            val gson1 =  Gson();
            val loginResponse = gson1.fromJson(data, TripHistoryResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE))
            {
                if (loginResponse.bookingHistory != null && !loginResponse.bookingHistory.isEmpty()) {
                    tv_no_data_found.visibility = View.GONE
                    rv_trip_history.visibility = View.VISIBLE
                    Log.d("data=", loginResponse.bookingHistory.size.toString() + "")
                    tv_total_earning.setText(loginResponse.totalEarning.toString())
                    if (page == 0) {
                        updatedlist = loginResponse.bookingHistory
                        adapterEarning!!.updateData(updatedlist)
                    }
                    else
                    {
                        updatedlist.addAll(loginResponse.bookingHistory)
                        adapterEarning!!.updateData(updatedlist)
                    }
                }
                else
                {
                    tv_no_data_found.visibility=View.VISIBLE
                    rv_trip_history.visibility=View.GONE
                }

            }
            else
            {
//                showAlert(loginResponse.message.toString())
            }
        } else {
            showAlert(resources.getString(R.string.errorString))
        }
    }
}