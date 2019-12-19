package com.sb.ubergodriver.view.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.CommonUtils.Utils
import com.sb.ubergodriver.R
import kotlinx.android.synthetic.main.activity_select_language.*

class SelectLanguageActivity : BaseActivity() {
    var mySelectedLanguage:String="en"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_language)
        btn_change_language.setOnClickListener(this)
        rg_language.setOnCheckedChangeListener(object: RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(group:RadioGroup, checkedId:Int) {
                // checkedId is the RadioButton selected
                val rb = findViewById(checkedId) as RadioButton
                val language=rb.text.toString()
                changeLanguage(language)
                //Toast.makeText(getApplicationContext(), rb.getText(), Toast.LENGTH_SHORT).show();
            }
        })
    }

    private fun changeLanguage(language: String) {
        if (language.toString().equals(getString(R.string.english)))
        {
            mySelectedLanguage="en"
        }
        else if (language.toString().equals(getString(R.string.turkish)))
        {
            mySelectedLanguage="tr"
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.btn_change_language->
            {
                Utils(this@SelectLanguageActivity).changeLanguage(mySelectedLanguage)
                getMyPreferences().setStringValue(AppConstants.MYLANGUAGE,mySelectedLanguage)
                navigatewithFinish(LogInActivity::class.java)
            }
        }
    }
}
