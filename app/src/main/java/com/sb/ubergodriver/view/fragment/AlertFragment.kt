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
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.apiConstants.ApiResponse
import com.sb.ubergodriver.apiConstants.Constant
import com.sb.ubergodriver.apiConstants.Status
import com.sb.ubergodriver.apiConstants.ViewModelFactory
import com.sb.ubergodriver.application.App
import com.sb.ubergodriver.model.alert.Alert
import com.sb.ubergodriver.model.alert.AlertRequest
import com.sb.ubergodriver.model.alert.AlertResponse
import com.sb.ubergodriver.model.userRating.RatingList
import com.sb.ubergodriver.model.userRating.UserRatingResponse
import com.sb.ubergodriver.view.activity.MainActivity
import com.sb.ubergodriver.view.adapters.AlertAdapters
import com.sb.ubergodriver.view.adapters.PaginationScrollListener

import com.sb.ubergodriver.view.adapters.feedbackAdapters
import com.sb.ubergodriver.viewModel.login.editProfile.AlertViewModel
import com.sb.ubergodriver.viewModel.login.editProfile.UserRatingViewModel
import kotlinx.android.synthetic.main.fragment_alert.*
import kotlinx.android.synthetic.main.fragment_rating.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import java.util.ArrayList
import javax.inject.Inject

class AlertFragment : BaseFragment() {

    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: AlertViewModel?= null
    var progressDialog: ProgressDialog?=null
    var APPTAG:String=AlertFragment::class.java.name

    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var skip:Int=0
    var list: ArrayList<Alert> = ArrayList<Alert>()
    var alertAdapters:AlertAdapters?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_alert, container, false)
        initViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        MainActivity.mainActivity.onChangingfragment(AppConstants.ALERTSCREEN)
        tv_title_common.text=getString(R.string.alert)
        val layoutManager:LinearLayoutManager=LinearLayoutManager(context)
        rv_alert.layoutManager=layoutManager
        alertAdapters= AlertAdapters(context!!,list)
        rv_alert.adapter=alertAdapters
        iv_back_common.setOnClickListener(this)


        progressDialog = Constant.getProgressDialog(context, "Please wait...")

        (context!!.applicationContext as App).getAppComponent().doInjection(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AlertViewModel::class.java!!)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

        rv_alert?.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                if (list.size>15) {
                    isLoading = true
                    //you have to call loadmore items to get more data
                    skip = skip + 1
                    getMoreItems(skip)
                }
            }
        })

        getMoreItems(skip)


    }

    private fun getMoreItems(skip: Int) {
        val request=AlertRequest()
        request.driverId=MainActivity.mainActivity.getUserId()
        request.skip=skip
        viewModel!!.hitalertApi(request)
    }

    open fun initViews(view: View?) {

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.iv_back_common->
            {
                MainActivity.mainActivity.onReplaceFragment(HomeFragment(),AppConstants.HOMESCREEN)
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
            isLoading = false
            val loginResponse = gson1.fromJson(data, AlertResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE))
            {
                if (skip==0) {
                    list = loginResponse.alerts
                    alertAdapters!!.updateList(list)
                }
                else
                {
                    list.addAll(loginResponse.alerts)
                    alertAdapters!!.updateList(list)
                }
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
