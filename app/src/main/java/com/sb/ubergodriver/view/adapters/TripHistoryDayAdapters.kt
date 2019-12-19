package com.sb.ubergodriver.view.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.R
import com.sb.ubergodriver.view.activity.MainActivity
import com.sb.ubergodriver.view.fragment.HistoryDetailFragment

class TripHistoryDayAdapters(var context:Context) : RecyclerView.Adapter<TripHistoryDayAdapters.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
       val myView=LayoutInflater.from(context).inflate(R.layout.item_triphistory,p0,false)
        return ViewHolder(myView)
    }

    override fun getItemCount(): Int {
       return 2
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, p1: Int) {

        viewHolder.itemView.setOnClickListener(View.OnClickListener {
            MainActivity.mainActivity.replaceFragment(HistoryDetailFragment(),AppConstants.HISTORYDETAILSCREEN);
        })

    }

    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

    }
}

