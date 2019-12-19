package com.sb.ubergodriver.interfaces

import android.support.v4.app.Fragment

public interface DrawerView {

    fun OpenDrawer()
    fun onReplaceFragment(fragment:Fragment,tag:String)
    fun onChangingfragment(type:String)
}