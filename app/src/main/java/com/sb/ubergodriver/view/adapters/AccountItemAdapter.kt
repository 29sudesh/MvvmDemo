package com.sb.ubergodriver.view.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.R
import com.sb.ubergodriver.model.MenuName
import com.sb.ubergodriver.view.activity.MainActivity
import com.sb.ubergodriver.view.fragment.DocumentFragment
import com.sb.ubergodriver.view.fragment.SettingFragment
import kotlinx.android.synthetic.main.item_account.view.*

class AccountItemAdapter(var context:Context, var list:ArrayList<MenuName>) : RecyclerView.Adapter<AccountItemAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
       val myView=LayoutInflater.from(context).inflate(R.layout.item_account,p0,false)
        return ViewHolder(myView)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.tv_menu_name.text=list.get(position).name
        viewHolder.iv_menu_icon.setImageResource(list.get(position).icon!!)


        viewHolder.itemView.id=position

        viewHolder.itemView.setOnClickListener(View.OnClickListener {

            val pos:Int=it!!.id

            if (pos==0)
            {
                MainActivity.mainActivity.replaceFragment(DocumentFragment(),AppConstants.DOCUMENTSCREEN)
            }
           else if (pos==1)
            {
                MainActivity.mainActivity.replaceFragment(SettingFragment(),AppConstants.SETTINGFRAGMENT)
            }
        })

    }

    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        val iv_menu_icon=itemView.iv_menu_icon
        val tv_menu_name=itemView.tv_menu_name

    }
}

