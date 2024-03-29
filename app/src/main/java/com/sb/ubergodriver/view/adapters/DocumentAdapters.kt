package com.sb.ubergodriver.view.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sb.ubergodriver.R
import com.sb.ubergodriver.model.document.DocList
import com.sb.ubergodriver.view.activity.MainActivity
import kotlinx.android.synthetic.main.item_document.view.*

class DocumentAdapters(
    var context: Context,var
    docList: MutableList<DocList>
) : RecyclerView.Adapter<DocumentAdapters.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
       val myView=LayoutInflater.from(context).inflate(R.layout.item_document,p0,false)
        return ViewHolder(myView)
    }

    override fun getItemCount(): Int {
       return docList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, p1: Int) {
        MainActivity.mainActivity.getImageRequest(docList.get(p1).image.toString()).into(viewHolder.documentImage)
        if (docList.size ==4) {
            if (p1 == 0) {
                viewHolder.documentName.setText(context.getString(R.string.driver_licence))
            } else if (p1 == 1) {
                viewHolder.documentName.setText(context.getString(R.string.number_plate))
            } else if (p1 == 2) {
                viewHolder.documentName.setText(context.getString(R.string.t_zini))
            } else if (p1 == 3) {
                viewHolder.documentName.setText(context.getString(R.string.car_licence))
            }
        }

    }

    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

        var documentImage=itemView.iv_document_image
        var documentName=itemView.tv_document_name
        var documentExpiry=itemView.tv_document_expiry

    }
}

