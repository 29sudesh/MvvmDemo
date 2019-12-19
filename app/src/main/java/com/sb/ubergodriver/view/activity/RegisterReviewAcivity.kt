package com.sb.ubergodriver.view.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.R
import kotlinx.android.synthetic.main.activity_register_review_acivity.*


class RegisterReviewAcivity : BaseActivity() {
    var pic:String=""
    var name:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_review_acivity)
        name=intent.getStringExtra(AppConstants.SENDEDDATA)
        pic=intent.getStringExtra(AppConstants.SENDCODE)
        Log.e("RegisterReviewAcivity",pic+"")
        getImageRequest(pic).into(iv_userImage)
        tv_name.setText(name)
        btn_done.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.btn_done->
            {
                navigatewithFinish(LogInActivity::class.java)
            }
        }
    }
}
