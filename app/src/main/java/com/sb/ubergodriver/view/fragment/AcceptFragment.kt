package com.sb.ubergodriver.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.CommonUtils.AppConstants.*
import com.sb.ubergodriver.CommonUtils.locationMethods.MyLocation
import com.sb.ubergodriver.R
import com.sb.ubergodriver.model.socketResponse.SocketBookingResponse
import com.sb.ubergodriver.view.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_accept.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class AcceptFragment :BaseFragment(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private var latitude = -33.852
    private var longitude = 151.211
    private var myMarker: Marker? = null
    var bitmap:Bitmap?=null
    var mCountDownTimer: CountDownTimer?=null
    var i = 0
    var totalValue=15
    val REJECT:Int=2
    val ACCEPT:Int=1
    var driverRideStatus: SocketBookingResponse?=null

    companion object {
        var acceptFragment:AcceptFragment= AcceptFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_accept,container,false)
        MainActivity.mainActivity.onChangingfragment(AppConstants.ACCEPTSCREEN)
        acceptFragment=this
        try {
            if (getArguments() !=null) {
                driverRideStatus =getArguments()!!.getParcelable(AppConstants.SENDEDDATA) as SocketBookingResponse
            }
            bitmap=getBitmap(R.drawable.ic_car_with_circle_gray)
        }catch (ex:Exception)
        {
            ex.printStackTrace()
        }


        return view
    }

    private fun convertIntoJSONOBject(driverRideStatus: SocketBookingResponse,status:Int) {

        try {
            val myJSonObject=JSONObject()
            val source=JSONObject()
            val destination=JSONObject()
            val stopPoint= JSONArray()
            val  stopPointObject=JSONObject()
            //Source
            source.put(LATITUDE,driverRideStatus.booking.source.latitude)
            source.put(LONGITUDE,driverRideStatus.booking.source.longitude)
            source.put(NAME,driverRideStatus.booking.source.name)

            //Destination
            destination.put(LATITUDE,driverRideStatus.booking.destination.latitude)
            destination.put(LONGITUDE,driverRideStatus.booking.destination.longitude)
            destination.put(NAME,driverRideStatus.booking.destination.name)

            //Destination
            if (driverRideStatus.booking.stopPoints !=null && driverRideStatus.booking.stopPoints.size>0) {
                for (i in 0 until driverRideStatus.booking.stopPoints.size) {
                    stopPointObject.put(LATITUDE,driverRideStatus.booking.stopPoints.get(i).latitude)
                    stopPointObject.put(LONGITUDE,driverRideStatus.booking.stopPoints.get(i).longitude)
                    stopPointObject.put(NAME,driverRideStatus.booking.stopPoints.get(i).name)
                    stopPoint.put(stopPointObject)
                }
            }

            myJSonObject.put(SOURCE,source)
            myJSonObject.put(DESTINATION,destination)
            myJSonObject.put(STOP_POINTS,stopPoint)
            myJSonObject.put(DRIVER_ID,MainActivity.mainActivity.getUserId())
            myJSonObject.put(ADMIN_ID,MainActivity.mainActivity.getAdminId())
            myJSonObject.put(USER_ID_SOCKET,driverRideStatus.booking.userId)
            myJSonObject.put(FARE,driverRideStatus.booking.fare)
            myJSonObject.put(STATUS_SOCKET,status)
            myJSonObject.put(PAYMENT_MODE,driverRideStatus.booking.paymentMode)
            myJSonObject.put(VEHICLE_TYPE,driverRideStatus.booking.vehicleType)
            myJSonObject.put(SOCKET_ID,driverRideStatus.booking.socketId)
//            Log.e("myJSonObject",myJSonObject.toString())
            MainActivity.mainActivity.sendObjectToSocket(myJSonObject, BOOKREQUESTRESPONSE)
        }catch (ex:Exception)
        {
            ex.printStackTrace()
        }


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.

        val mapFragment = getChildFragmentManager()
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        tv_accept.setOnClickListener(this)
        tv_reject.setOnClickListener(this)

        try {
            if (driverRideStatus!=null)
            {
                tv_user_address.setText(driverRideStatus!!.booking.destination.name)
                ratingBar_user.rating=driverRideStatus!!.booking.userRating.toFloat()
                latitude=driverRideStatus!!.booking.source.latitude.toDouble()
                longitude=driverRideStatus!!.booking.source.longitude.toDouble()
                tv_remaining_time.setText(driverRideStatus!!.booking.triptime)
                goToMYLocation()
            }
        }catch (ex:Exception)
        {
            ex.printStackTrace()
        }

        try {
            progressBar.setProgress(i)
            mCountDownTimer = object:CountDownTimer(15000, 1000) {
                override fun onTick(millisUntilFinished:Long) {
//                Log.v("Log_tag", "Tick of Progress" + i +"    time :"+ millisUntilFinished)
                    i++
                    try {
                        if (progressBar!=null) {
                            progressBar.setProgress(i)
                            try {
//                                val value = totalValue - i
//                                tv_remaining_time.setText(value.toString() + " " + getString(R.string.sec))
                            }catch (ex:Exception)
                            {
                                ex.printStackTrace()
                            }

                        }
                    }catch (ex:Exception)
                    {
                        ex.printStackTrace()
                    }

                }
                override  fun onFinish() {
                    //Do what you want
//                i++
//                progressBar.setProgress(15)
                    MainActivity.mainActivity.replaceFragment(HomeFragment(),HOMESCREEN)
                    mCountDownTimer=null
                }
            }
            mCountDownTimer!!.start()
        }catch (ex:Exception)
        {
            ex.printStackTrace()
        }


    }




    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.tv_accept->
            {
                if (driverRideStatus!=null)
                {
                    try {
                        convertIntoJSONOBject(driverRideStatus!!,ACCEPT)
                        if (mCountDownTimer!=null)
                        {
                            mCountDownTimer!!.cancel()
                        }
                        MainActivity.mainActivity.replaceFragment(HomeFragment(), HOMESCREEN)
                    }catch (ex:Exception)
                    {
                        ex.printStackTrace()
                    }

                }

            }
            R.id.tv_reject->
            {
                try {
                    if (driverRideStatus!=null) {
                        convertIntoJSONOBject(driverRideStatus!!, REJECT)
                        if (mCountDownTimer!=null)
                        {
                            mCountDownTimer!!.cancel()
                        }
                        MainActivity.mainActivity.replaceFragment(HomeFragment(), HOMESCREEN)
                    }
                }catch (ex:Exception)
                {
                    ex.printStackTrace()
                }

            }
        }
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.uiSettings.setAllGesturesEnabled(false)
        mMap!!.getUiSettings().setMapToolbarEnabled(false)
        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mMap!!.setMyLocationEnabled(false)
        mMap!!.setMinZoomPreference(13f)
        mMap!!.setMaxZoomPreference(18f)
        goToMYLocation()

    }

    private fun goToMYLocation() {

        drawMarker(latitude, longitude)

    }


    fun drawMarker(lat: Double?, lng: Double?) {

        try {
            if (mMap != null) {
//            mMap!!.clear()
                val gps = LatLng(lat!!, lng!!)
                latitude = lat!!
                longitude = lng!!
                if (gps != null) {
                    myMarker = mMap!!.addMarker(
                        MarkerOptions().position(gps)
                            .title("").icon(BitmapDescriptorFactory.fromBitmap(bitmap)).draggable(true)
                    )
                    mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 13f))

                    MainActivity.mainActivity.startAThread(latitude,longitude)

                }
                mMap!!.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                    override fun onMarkerClick(marker: Marker): Boolean {
                        return true
                    }
                })
            }
        }catch (ex:Exception)
        {
            ex.printStackTrace()
        }


    }

    private fun getBitmap(drawableRes:Int): Bitmap {
        val drawable = getResources().getDrawable(drawableRes)
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight())
        drawable.draw(canvas)
        return bitmap
    }


    private fun canAccessLocation(): Boolean {
        return hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun canAccessCoreLocation(): Boolean {
        return hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun hasPermission(perm: String): Boolean {

        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context!!, perm)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("OnDestroy: ","OnDestroy")
        try {
            if (mCountDownTimer !=null)
            {
                mCountDownTimer!!.cancel()
            }
        }catch (ex:Exception)
        {
            ex.printStackTrace()
        }

    }

    fun change(boolean: Boolean)
    {
        try {
            if (!boolean)
            {
                if (mCountDownTimer !=null)
                {
                    mCountDownTimer!!.cancel()
                }
                MainActivity.mainActivity.replaceFragment(HomeFragment(),HOMESCREEN)
            }
        }catch (ex:Exception)
        {
            ex.printStackTrace()
        }

    }

}