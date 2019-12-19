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
import android.webkit.WebView
import android.webkit.WebViewClient
import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.apiConstants.Urls.ABOUT_US_URL
import com.sb.ubergodriver.apiConstants.Urls.FAQ_URL
import com.sb.ubergodriver.view.activity.MainActivity
import com.sb.ubergodriver.view.adapters.AlertAdapters

import com.sb.ubergodriver.view.adapters.feedbackAdapters
import kotlinx.android.synthetic.main.fragment_alert.*
import kotlinx.android.synthetic.main.fragment_faq.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*

class FAQFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_faq, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        MainActivity.mainActivity.onChangingfragment(AppConstants.NOMAINSCREEN)
        tv_title_common.text=getString(R.string.faq)
        iv_back_common.setImageResource(R.drawable.ic_back_black)
        iv_back_common.setOnClickListener(this)

        webview!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }


            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progress.visibility=View.GONE
            }
        }
        webview!!.loadUrl(FAQ_URL)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.iv_back_common->
            {
                MainActivity.mainActivity.onReplaceFragment(SettingFragment(),AppConstants.NOMAINSCREEN)
            }

        }
    }
}
