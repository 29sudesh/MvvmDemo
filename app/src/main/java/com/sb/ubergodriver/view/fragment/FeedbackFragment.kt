package com.sb.ubergodriver.view.fragment

import com.sb.ubergodriver.R
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sb.ubergodriver.model.userRating.RatingList
import com.sb.ubergodriver.view.adapters.feedbackAdapters
import kotlinx.android.synthetic.main.fragment_feedback.*
import java.util.ArrayList

class FeedbackFragment : BaseFragment() {
    companion object {
        var feedbackFragment:FeedbackFragment=FeedbackFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_feedback, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        feedbackFragment=this
        tv_no_data_found.visibility=View.GONE
        rv_feedback.visibility=View.VISIBLE
        rv_feedback.layoutManager=LinearLayoutManager(context)

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {

        }
    }

    override fun setFeedBack(ratingListArrayList: ArrayList<RatingList>?) {
        super.setFeedBack(ratingListArrayList)
        Log.e("ratingListArrayList",ratingListArrayList.toString())
        if (ratingListArrayList != null && !ratingListArrayList.isEmpty())
        {
            tv_no_data_found.visibility=View.GONE
            rv_feedback.visibility=View.VISIBLE
            rv_feedback.adapter=feedbackAdapters(context!!,ratingListArrayList)
        }
        else
        {
            tv_no_data_found.visibility=View.VISIBLE
            rv_feedback.visibility=View.GONE
        }
    }
}
