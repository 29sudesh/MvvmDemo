package com.sb.ubergodriver.interfaces

import android.content.Intent

interface RequestCode {
    fun getRequestCode(requestcode:Int,data: Intent?)
    fun onGetPermissionCode(requestCode:Int)
}