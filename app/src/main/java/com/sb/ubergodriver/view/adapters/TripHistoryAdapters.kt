package com.sb.ubergodriver.view.adapters

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.CommonUtils.Utils
import com.sb.ubergodriver.R
import com.sb.ubergodriver.model.tripHistory.BookingHistory
import com.sb.ubergodriver.view.activity.MainActivity
import com.sb.ubergodriver.view.fragment.AcceptFragment
import com.sb.ubergodriver.view.fragment.HistoryDetailFragment
import kotlinx.android.synthetic.main.item_triphistory.view.*

class TripHistoryAdapters(var context:Context,var list:ArrayList<BookingHistory>) : RecyclerView.Adapter<TripHistoryAdapters.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
       val myView=LayoutInflater.from(context).inflate(R.layout.item_triphistory,p0,false)
        return ViewHolder(myView)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, p1: Int) {
        viewHolder.tv_amount.setText(context.getString(R.string.turkish_currency_sign)+" "+list.get(p1).fare)
        viewHolder.tv_source.setText(list.get(p1).source.name)
        viewHolder.tv_destination.setText(list.get(p1).destination.name)
        viewHolder.tv_vehicle_type.setText(list.get(p1).vehicleType.name)
        viewHolder.tv_holder_date.setText(Utils.getDateWithtime(list.get(p1).date))

        if (list.get(p1).status.toString().equals("11"))
        {
            viewHolder.tv_cancelled.visibility=View.VISIBLE
        }
        else
        {
            viewHolder.tv_cancelled.visibility=View.GONE
        }
        viewHolder.itemView.id=p1
        viewHolder.itemView.setOnClickListener(View.OnClickListener {

            val pos:Int=it.id
            val bookingHistory:BookingHistory=list.get(pos) as BookingHistory
            val historyDetail = HistoryDetailFragment()
            val b = Bundle()
            b.putParcelable(AppConstants.SENDEDDATA, bookingHistory)
            historyDetail.setArguments(b)
            MainActivity.mainActivity.replaceFragment(historyDetail, AppConstants.HISTORYDETAILSCREEN);
        })

    }

    fun updateData(updatedlist:ArrayList<BookingHistory>)
    {
        list=updatedlist
        notifyDataSetChanged()
    }

    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

        var tv_holder_date=itemView.tv_date
        var tv_vehicle_type=itemView.tv_vehicle_type
        var tv_source=itemView.tv_source
        var tv_destination=itemView.tv_destination
        var tv_amount=itemView.tv_ammount
        var tv_cancelled=itemView.tv_cancelled

    }
}

