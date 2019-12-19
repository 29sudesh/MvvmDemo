package com.sb.ubergodriver.view.fragment

import com.sb.ubergodriver.CommonUtils.Utils
import com.sb.ubergodriver.R
import com.sb.ubergodriver.interfaces.DrawerView
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.view.activity.MainActivity
import com.sb.ubergodriver.view.adapters.AlertAdapters

import com.sb.ubergodriver.view.adapters.feedbackAdapters
import kotlinx.android.synthetic.main.fragment_alert.*
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*

class SettingFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        initViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        MainActivity.mainActivity.onChangingfragment(AppConstants.NOMAINSCREEN)
        tv_title_common.text=getString(R.string.setting)
        iv_back_common.setImageResource(R.drawable.ic_back_black)
        iv_back_common.setOnClickListener(this)

        ll_privacy_policy.setOnClickListener(this)
        ll_about_us.setOnClickListener(this)
        ll_contact_us.setOnClickListener(this)
        ll_faq.setOnClickListener(this)
        ll_terms.setOnClickListener(this)
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

            R.id.ll_faq->
            {
                MainActivity.mainActivity.onReplaceFragment(FAQFragment(),AppConstants.NOMAINSCREEN)
            }

            R.id.ll_about_us->
            {
                MainActivity.mainActivity.onReplaceFragment(AboutUsFragment(),AppConstants.NOMAINSCREEN)
            }

            R.id.ll_contact_us->
            {
                MainActivity.mainActivity.onReplaceFragment(ContactUsFragment(),AppConstants.NOMAINSCREEN)
            }

            R.id.ll_privacy_policy->
            {
                MainActivity.mainActivity.onReplaceFragment(PrivacyPolicyFragment(),AppConstants.NOMAINSCREEN)
            }

            R.id.ll_terms->
            {
                MainActivity.mainActivity.onReplaceFragment(TermsConditionFragment(),AppConstants.NOMAINSCREEN)
            }

        }
    }
}
