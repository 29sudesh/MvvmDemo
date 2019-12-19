package com.sb.ubergodriver.view.fragment

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import com.sb.ubergodriver.CommonUtils.Utils
import com.sb.ubergodriver.R
import android.os.Bundle
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
import com.sb.ubergodriver.model.document.DriverDocumentResponse
import com.sb.ubergodriver.view.activity.EditChangePasswordActivity
import com.sb.ubergodriver.view.activity.MainActivity
import com.sb.ubergodriver.view.adapters.DocumentAdapters
import com.sb.ubergodriver.viewModel.login.GetDocumentViewModel
import kotlinx.android.synthetic.main.fragment_document.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import javax.inject.Inject

class DocumentFragment : BaseFragment() {


    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: GetDocumentViewModel?= null
    var progressDialog: ProgressDialog?=null
    var APPTAG:String= DocumentFragment::class.java.name

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_document, container, false)
        initViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        MainActivity.mainActivity.onChangingfragment(AppConstants.NOMAINSCREEN)
        tv_title_common.text=getString(R.string.documents)
        iv_back_common.setImageResource(R.drawable.ic_back_black)
        rv_document.layoutManager=LinearLayoutManager(context)
        tv_no_data_found.visibility=View.VISIBLE
        rv_document.visibility=View.GONE

        iv_back_common.setOnClickListener(this)

        progressDialog = Constant.getProgressDialog(context, "Please wait...")

        (context!!.applicationContext as App).getAppComponent().doInjection(this)



        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GetDocumentViewModel::class.java!!)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

        viewModel!!.hitGetDocumentApi(MainActivity.mainActivity.getUserId());
    }

    open fun initViews(view: View?) {

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.iv_back_common->
            {
                MainActivity.mainActivity.onReplaceFragment(AccountFragment(),AppConstants.ACCOUNTSCREEN)
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
            var loginResponse = gson1.fromJson(data, DriverDocumentResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE))
            {
                Log.d("data=", loginResponse.docList.size.toString()+"")
                rv_document.visibility=View.VISIBLE
                tv_no_data_found.visibility=View.GONE
                rv_document.adapter= DocumentAdapters(context!!,loginResponse.docList)

            }
            else
            {
                showAlert(loginResponse.message.toString())
                rv_document.visibility=View.GONE
                tv_no_data_found.visibility=View.VISIBLE
            }
        } else {
            showAlert(resources.getString(R.string.errorString))
        }
    }
}
