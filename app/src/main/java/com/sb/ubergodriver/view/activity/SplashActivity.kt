package com.sb.ubergodriver.view.activity

import android.content.Intent
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.sb.ubergodriver.CommonUtils.AppConstants.STATICUSERID
import com.sb.ubergodriver.R


class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        isLocationPermissions()
        Handler().postDelayed({
            if (isLogin())
            {
                STATICUSERID=getUserId()
                navigatewithFinish( MainActivity::class.java)
            }
            else {
                navigatewithFinish(SelectLanguageActivity::class.java)
            }
        }, 2500)
    }
}
