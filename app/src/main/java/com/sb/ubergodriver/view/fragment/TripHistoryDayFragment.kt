package com.sb.ubergodriver.view.fragment

import com.sb.ubergodriver.R
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.view.activity.MainActivity
import com.sb.ubergodriver.view.adapters.TripHistoryDayAdapters
import kotlinx.android.synthetic.main.fragment_triphistory_day.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*

class TripHistoryDayFragment : BaseFragment() {




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_triphistory_day, container, false)
        initViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        MainActivity.mainActivity.onChangingfragment(AppConstants.TRIPHISTORYDAYSCREEN)
        tv_title_common.text=getString(R.string.triphistory)
        rv_trip_history_day.layoutManager=LinearLayoutManager(context)
        rv_trip_history_day.adapter= TripHistoryDayAdapters(context!!)
        iv_back_common.setOnClickListener(this)
        btn_view_all.setOnClickListener(this)
    }

    open fun initViews(view: View?) {

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.iv_back_common->
            {
                MainActivity.mainActivity.onReplaceFragment(EarningFragment(),AppConstants.EARNINGSCREEN)
            }

            R.id.btn_view_all ->
            {
                MainActivity.mainActivity.onReplaceFragment(TripHistoryViewAllFragment(),AppConstants.TRIPHISTORYVIEWALLSCREEN)
            }

        }
    }
}
