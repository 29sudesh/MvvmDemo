package com.sb.ubergodriver.view.fragment

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import com.sb.ubergodriver.R
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.CommonUtils.Utils
import com.sb.ubergodriver.apiConstants.ApiResponse
import com.sb.ubergodriver.apiConstants.Constant
import com.sb.ubergodriver.apiConstants.Status
import com.sb.ubergodriver.apiConstants.ViewModelFactory
import com.sb.ubergodriver.application.App
import com.sb.ubergodriver.model.document.DriverDocumentResponse
import com.sb.ubergodriver.model.userRating.RatingList
import com.sb.ubergodriver.model.userRating.UserRatingResponse
import com.sb.ubergodriver.view.activity.MainActivity
import com.sb.ubergodriver.view.adapters.DocumentAdapters
import com.sb.ubergodriver.view.fragment.FeedbackFragment.Companion.feedbackFragment
import com.sb.ubergodriver.viewModel.login.GetDocumentViewModel
import com.sb.ubergodriver.viewModel.login.editProfile.UserRatingViewModel
import kotlinx.android.synthetic.main.fragment_document.*
import kotlinx.android.synthetic.main.fragment_rating.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import java.util.ArrayList
import javax.inject.Inject

class RatingFragment : BaseFragment()/*, RatingBar.OnRatingBarChangeListener*/ {
    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: UserRatingViewModel?= null
    var progressDialog: ProgressDialog?=null
    var APPTAG:String=RatingFragment::class.java.name
    var page:Int=0
    val SKIP:String="?skip="
    companion object {
         var ratingList: List<RatingList> = ArrayList<RatingList>()
    }
   /* override fun onRatingChanged(ratingBar: RatingBar?, rating: Float, fromUser: Boolean) {
        tv_rating.text="$rating "+getString(R.string.star)
    }*/
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_rating, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ratingList = ArrayList<RatingList>()
        MainActivity.mainActivity.onChangingfragment(AppConstants.RATINGSCREEN)
        setupViewPager(viewpager!!)
        tabs!!.setupWithViewPager(viewpager)
        viewpager.beginFakeDrag();
        tv_title_common.text=getString(R.string.rating_text)
        iv_back_common.setOnClickListener(this)

        progressDialog = Constant.getProgressDialog(context, "Please wait...")

        (context!!.applicationContext as App).getAppComponent().doInjection(this)
        
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserRatingViewModel::class.java!!)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

        viewModel!!.hitUserRatingApi(MainActivity.mainActivity.getUserId())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        setupViewPager(viewpager!!)
        tabs!!.setupWithViewPager(viewpager)
//        viewpager.setSaveFromParentEnabled(false);

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.iv_back_common ->
            {
                MainActivity.mainActivity.onReplaceFragment(HomeFragment(),AppConstants.HOMESCREEN)

            }

        }
    }





    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(childFragmentManager!!)
        adapter.addFragment(FeedbackFragment(), getString(R.string.feedback))
        adapter.addFragment(ProTipFragment(), getString(R.string.pro_tips))
        viewPager.adapter = adapter
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
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
            val loginResponse = gson1.fromJson(data, UserRatingResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE))
            {
                Log.d("data=", loginResponse.totalRating.toString()+"")
                ratingList=loginResponse.ratingList
                tv_rating.setText(loginResponse.totalRating.toString()+" "+getString(R.string.star))
                tv_rating_value.setText(loginResponse.totalRating.toString())
                tv_accepted_value.setText(loginResponse.accepted.toString()+"%")
                tv_cancelled_value.setText(loginResponse.canceled.toString()+"%")
                ratingBar.rating= loginResponse.totalRating.toFloat()

                feedbackFragment.setFeedBack(ratingList as ArrayList<RatingList>?)

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
