package com.sb.ubergodriver.view.fragment

import android.content.Intent
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
import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.model.MenuName
import com.sb.ubergodriver.view.activity.LogInActivity
import com.sb.ubergodriver.view.activity.MainActivity
import com.sb.ubergodriver.view.adapters.AccountItemAdapter

import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.layout_common_toolbar_black.*


class AccountFragment : BaseFragment() {
    var list:ArrayList<MenuName>?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        initViews(view)
        return view

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        list= ArrayList<MenuName>()
//        list!!.add(MenuName(R.drawable.ic_support_gray,getString(R.string.supportandhelp)))
        list!!.add(MenuName(R.drawable.ic_doc_gray,getString(R.string.documents)))
//        list!!.add(MenuName(R.drawable.ic_privacy_gray,getString(R.string.privacypolicy)))
//        list!!.add(MenuName(R.drawable.ic_payment_gray,getString(R.string.bankandpayments)))
        list!!.add(MenuName(R.drawable.ic_setting_gray,getString(R.string.settings)))
        MainActivity.mainActivity.onChangingfragment(AppConstants.ACCOUNTSCREEN)
        tv_title_common_black.text=getString(R.string.profile)
        rv_profile_item.layoutManager=LinearLayoutManager(context)
        rv_profile_item.adapter= AccountItemAdapter(context!!,list!!)
        iv_back_common_black.setImageResource(R.drawable.ic_home_white)
        iv_back_common_black.setOnClickListener(this)
        tv_edit.setOnClickListener(this)
        tv_logout.setOnClickListener(this)

        if (MainActivity.mainActivity.getUserDetail().driver!=null)
        {
            val myRespose=MainActivity.mainActivity.getUserDetail().driver
            tv_name.setText(myRespose.firstName+" "+myRespose.lastName)
            MainActivity.mainActivity.getImageRequest(myRespose.profilePic.toString()).into(iv_userImage)
        }

    }

    open fun initViews(view: View?) {

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.iv_back_common_black->
            {
                MainActivity.mainActivity.onReplaceFragment(HomeFragment(),AppConstants.HOMESCREEN)
            }

            R.id.tv_edit ->
            {
                MainActivity.mainActivity.onReplaceFragment(EditProfileFragment(),AppConstants.EDITPROFILESCREEN)
            }

            R.id.tv_logout ->
            {
                MainActivity.mainActivity.getMyPreferences().clearSharedPrefrences()
                var intent=Intent(context,LogInActivity::class.java)
                startActivity(intent)
                activity!!.finishAffinity()
            }

        }
    }

}
