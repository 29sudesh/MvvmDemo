package com.sb.ubergodriver.view.fragment

import com.beust.klaxon.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson

import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread

import android.Manifest
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import com.sb.ubergodriver.R
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.CompoundButton
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.gson.JsonElement
import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.CommonUtils.AppConstants.*
import com.sb.ubergodriver.CommonUtils.Utils
import com.sb.ubergodriver.CommonUtils.locationMethods.LocationResult
import com.sb.ubergodriver.CommonUtils.locationMethods.MyLocation
import com.sb.ubergodriver.apiConstants.ApiResponse
import com.sb.ubergodriver.apiConstants.Constant
import com.sb.ubergodriver.apiConstants.Status
import com.sb.ubergodriver.apiConstants.ViewModelFactory
import com.sb.ubergodriver.application.App
import com.sb.ubergodriver.model.googleMapApiresponse.GoogleApiResponse
import com.sb.ubergodriver.model.loginResponse.LoginResponse
import com.sb.ubergodriver.model.requests.ChangeDriverStatusRequest
import com.sb.ubergodriver.model.socketResponse.DriverRideStatusResponse
import com.sb.ubergodriver.socket.interfaces.eventservice.EventServiceImpl
import com.sb.ubergodriver.socket.interfaces.eventservice.EventServiceImpl.EVENT_NEW_MESSAGE_LISTENER
import com.sb.ubergodriver.view.activity.EditChangePasswordActivity
import com.sb.ubergodriver.view.activity.MainActivity
import com.sb.ubergodriver.view.activity.SelectLanguageActivity
import com.sb.ubergodriver.view.mapUtils.*
import com.sb.ubergodriver.viewModel.login.editProfile.ChangeDriverStatusViewModel
import com.sb.ubergodriver.viewModel.login.editProfile.EditProfileViewModel
import com.sb.ubergodriver.viewModel.login.editProfile.ForgotPasswordViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_bottom_common_for_driver.*
import kotlinx.android.synthetic.main.layout_topnavigation.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URISyntaxException
import java.net.URL
import java.util.ArrayList
import javax.inject.Inject

class HomeFragment : BaseFragment(), OnMapReadyCallback, LocationResult, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {


    private var SOURCE_LATLNG = LatLng(40.722543,
        -73.998585)
    private var DESTINATION_LATLNG = LatLng(40.7057, -73.9964)

    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: ChangeDriverStatusViewModel?= null
    var APPTAG:String= HomeFragment::class.java.name

//    var progressDialog: ProgressDialog?=null

    var markerPoints = ArrayList<Any>()
    private var currentLocation: Location? = null
    private var firstTimeFlag = true

    public var latitude = -33.852
    public var longitude = 151.211
    private var mapLatlng=LatLng(latitude,longitude)
    private var myLocation: MyLocation? = null
    private var myMarker: Marker? = null
    var firstTime:Boolean=true
    var bitmap:Bitmap?=null
    var sourcebitmap:Bitmap?=null
    private var googleApiClient: GoogleApiClient? = null
    internal var locationManager: LocationManager?=null
    internal var GpsStatus: Boolean = false
    private var driverStatus:Int=9
    private var driverRideSocketResponse:DriverRideStatusResponse?=null
    private var animShow:Animation?=null
    private var animHide: Animation?=null
    private var isTracking:Boolean=false
    private var bearingValue:Double=0.56


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        firstTime=true
        homeFragment=this
        bitmap=getBitmap(R.drawable.ic_car_with_circle_gray)
        sourcebitmap=getBitmap(R.drawable.ic_user_icon_with_black_circle)
        animShow = AnimationUtils.loadAnimation( context, R.anim.view_show);
        animHide = AnimationUtils.loadAnimation( context, R.anim.view_hide);

        //ViewModelInitialize
//        progressDialog = Constant.getProgressDialog(context, "Please wait...")

        (context!!.applicationContext as App).getAppComponent().doInjection(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ChangeDriverStatusViewModel::class.java)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

        myLocation = MyLocation()

        //Initializing googleApiClient
        googleApiClient = GoogleApiClient.Builder(context!!)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        if (!canAccessLocation() || !canAccessCoreLocation()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(INITIAL_PERMS, INITIAL_REQUEST)
            }

        } else {
            val networkPresent = myLocation!!.getLocation(context, this)
            if (!networkPresent) {
                showSettingsAlert()
            }
        }
        return view
    }

    private fun isGooglePlayServicesAvailable(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val status = googleApiAvailability.isGooglePlayServicesAvailable(context)
        if (ConnectionResult.SUCCESS == status)
            return true
        else {
            if (googleApiAvailability.isUserResolvableError(status))
                showAlert("Please Install google play services to use this application")
        }
        return false
    }

    override fun onPause() {
        super.onPause()
        firstTime=false
    }



    private fun animateCamera(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(latLng)))
    }

    private fun getCameraPositionWithBearing(latLng: LatLng): CameraPosition {
        return CameraPosition.Builder().target(latLng).build()
    }

    private fun showMarker(currentLocation: Location) {
        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        if (myMarker == null)
            myMarker =
                mMap!!.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap)).position(latLng))
        else
            MarkerAnimation.animateMarkerToGB(myMarker, latLng, LatLngInterpolator.Spherical())
    }

    //Getting current location
    private fun getCurrentLocation() {
//        if (mMap != null) {
//            mMap!!.clear()
//        }

        if (ActivityCompat.checkSelfPermission(
                context!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context!!,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
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
        goToMYLocation()
//        val location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
//        if (location != null) {
//            currentLocation=location
//            //Getting longitude and latitude
//            longitude = location!!.getLongitude()
//            latitude = location!!.getLatitude()
//            firstTime=true
//
//            //moving the map to location
//            goToMYLocation()
//        }
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_starttrip.setOnClickListener(this)
        ll_navigate.setOnClickListener(this)
        iv_current_location.setOnClickListener(this)
        tv_chat.setOnClickListener(this)
        MainActivity.mainActivity.onChangingfragment(AppConstants.HOMESCREEN)

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.

        val mapFragment = getChildFragmentManager()
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (MainActivity.mainActivity.getUserDetail().driver!=null)
        {
            if (switch_online!=null) {
                if (MainActivity.mainActivity.getUserDetail().driver.isAvailable.toString().equals("1")) {
                    switch_online.isChecked = true
                    changeSwithColor(true)
                    Utils.switchColor(true, switch_online)
                } else {
                    switch_online.isChecked = false
                    changeSwithColor(false)
                    Utils.switchColor(false, switch_online)
                }
            }


        }

        switch_online.setOnCheckedChangeListener(
            object:CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(buttonView:CompoundButton, isChecked:Boolean) {
                    if (isChecked)
                    {
                            val changeStausRequest = ChangeDriverStatusRequest()
                            changeStausRequest.driverId = MainActivity.mainActivity.getUserId()
                            changeStausRequest.status = "1"
                            viewModel!!.hitDriverStatusApi(changeStausRequest)
//                       showAlert("Switch On")
                    }
                    else
                    {
                            val changeStausRequest = ChangeDriverStatusRequest()
                            changeStausRequest.driverId = MainActivity.mainActivity.getUserId()
                            changeStausRequest.status = "0"
                            viewModel!!.hitDriverStatusApi(changeStausRequest)
                    }
                }
            })
    }

    private fun changeSwithColor(checked: Boolean) {
        if (checked) {
            switch_online.setTextColor(Color.GREEN)
            switch_online.setText(getString(R.string.onlile))
        }
        else
        {
            switch_online.setTextColor(Color.RED)
            switch_online.setText(getString(R.string.offline))
        }
    }




    /*
    * method to handle response
    * */
    private fun consumeResponse(apiResponse: ApiResponse) {

        when (apiResponse.status) {

            Status.LOADING ->
            {

            }
//                progressDialog!!.show()

            Status.SUCCESS -> {
//                progressDialog!!.dismiss()
                renderSuccessResponse(apiResponse.data!!)
            }

            Status.ERROR -> {
//                progressDialog!!.dismiss()
                showAlert( resources.getString(R.string.errorString))
            }
            else -> {
            }
        }
    }
    /*
    * method to handle success response
    * */
    private fun renderSuccessResponse(response: JsonElement) {
        if (!response.isJsonNull) {
            Log.e(APPTAG+"response=", response.toString())
            val data:String=  Utils.toJson(response)
            val gson1 =  Gson();
            val loginResponse = gson1.fromJson(data, LoginResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE))
            {
                Log.d("data=", loginResponse.driver.id.toString()+"")
                MainActivity.mainActivity.getMyPreferences().setStringValue(AppConstants.USER_ID,loginResponse.driver.id.toString())
                MainActivity.mainActivity.getMyPreferences().setStringValue(AppConstants.USERDETAIL,data)
                if (loginResponse.driver.isAvailable.toString().equals("1"))
                {
                    switch_online.isChecked=true
                    changeSwithColor(true)
                    Utils.switchColor(true,switch_online)
                }
                else
                {
                    switch_online.isChecked=false
                    changeSwithColor(false)
                    Utils.switchColor(false,switch_online)
                }
            }
            else
            {
                showAlert(loginResponse.message.toString())
            }
        } else {
            showAlert(resources.getString(R.string.errorString))
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.btn_starttrip->
            {

                if (driverRideSocketResponse != null) {
                    Log.e("driverStatus", driverStatus.toString() + "")
                    btn_starttrip.visibility=View.GONE
                    if (driverStatus == 0) {
                        hitSocket("1")
                    } else if (driverStatus == 1) {
                       hitSocket("2")
                    } else if (driverStatus == 2) {
                        hitSocket("3")
                    }
                }
            }
            R.id.tv_chat->
            {
                if (driverRideSocketResponse!=null) {
                    val intent = Intent(context!!, ChatFargment::class.java)
                    intent.putExtra(KEYBOOKINGID, driverRideSocketResponse!!.booking.id)
                    intent.putExtra(KEYPROFILEPIC, driverRideSocketResponse!!.booking.userId.profilePic)
                    intent.putExtra(KEYUSERID, driverRideSocketResponse!!.booking.userId.id)
//                    intent.putExtra(KEYRATING, driverRideSocketResponse!!.booking.userId.ratings.toString()+"")
                    intent.putExtra(KEYNAME, driverRideSocketResponse!!.booking.userId.firstName+ " "+driverRideSocketResponse!!.booking.userId.lastName)
                     startActivity(intent)
//                    val fragment = ChatFargment()
//                    val b = Bundle()
//                    b.putParcelable(SENDEDDATA, driverRideSocketResponse)
//                    fragment.setArguments(b)
//                    MainActivity.mainActivity.replaceFragment(fragment, AppConstants.CHATSCREEN)
                }
            }

            R.id.ll_navigate->
            {
                openMap(mapLatlng)
            }

            R.id.iv_current_location->
            {
                getCurrentLocation()
            }
        }

    }

    private fun hitSocket(s: String) {
        val jsonObject = JSONObject()
        jsonObject.put(STATUS_SOCKET, s)
        jsonObject.put(BOOKING_ID,driverRideSocketResponse!!.booking.id)
        MainActivity.mainActivity.sendObjectToSocket(jsonObject, CHANGEDRIVERSTATUS)

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
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
        mMap!!.setMinZoomPreference(9f)
        mMap!!.setMaxZoomPreference(25f)

        goToMYLocation()

    }

    private fun goToMYLocation() {

        drawMarker(latitude, longitude)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            INITIAL_REQUEST -> if (canAccessLocation() && canAccessCoreLocation()) {
                val networkPresent = myLocation!!.getLocation(context!!, this)
                if (!networkPresent) {
                    showSettingsAlert()
                }
                else
                {
                    goToMYLocation()
                }

                goToMYLocation()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        firstTime=true
        driverRideStatus()
//        driverBookingResponse(null)
    }


    fun drawMarker(lat: Double?, lng: Double?) {
        if (mMap != null) {
//            mMap!!.clear()
            val gps = LatLng(lat!!, lng!!)
            latitude = lat
            longitude = lng
            if (gps != null) {

                MainActivity.mainActivity.runOnUiThread(Runnable {

                    if (currentLocation!=null) {
                        animateCamera(currentLocation!!)
                        showMarker(currentLocation!!)
                    }
                    MainActivity.mainActivity.startAThread(latitude,longitude)
                })

            }

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

    override fun gotLocation(location: Location) {
        currentLocation=location
        latitude = location.getLatitude()
        longitude = location.getLongitude()
        if (isTracking) {
            val mycurrentLatlng:LatLng= LatLng(latitude,longitude)
            bearingValue=bearingBetweenLocations(mycurrentLatlng,mycurrentLatlng)
            tracking()
        }
//       Log.e("LOCATION : ",latitude.toString()+"   "+longitude)
        val params=JSONObject()
        params.put(DRIVER_ID,MainActivity.mainActivity.getUserId())
        params.put(DRIVERLATITUDE,latitude.toString())
        params.put(DRIVERLONGITUDE,longitude.toString())
        MainActivity.mainActivity.sendObjectToSocket(params, UPDATEDRIVERLOCATION)
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        goToMYLocation()

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


    override fun onConnected(p0: Bundle?) {
        getCurrentLocation()
    }

    override fun onConnectionSuspended(p0: Int) {
//        showAlert(p0.toString())

    }

    override fun onConnectionFailed(p0: ConnectionResult) {
//        showAlert(p0.errorMessage.toString())
    }


    fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(
            context!!
        )
        alertDialog.setTitle("SETTINGS")
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?")
        alertDialog.setPositiveButton("Settings",
            object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    val intent = Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS
                    )
                    startActivity(intent)
                }
            })
        alertDialog.setNegativeButton("Cancel",
            object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.cancel()
                }
            })
        alertDialog.show()
    }

    companion object {
        private val TAG = HomeFragment::class.java.getSimpleName()
        public var homeFragment:HomeFragment=HomeFragment()
        public var mMap: GoogleMap? = null

        private val INITIAL_PERMS =
            arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

        private val INITIAL_REQUEST = 13
    }

    // 0 for on way, 1 = reached source, 2= start, 3 = complete, 4 = paid 5 = rating done, 6 = skip rating, 9=no ride, 11 for cancel
    public fun checkDriverStatus(loginResponse: DriverRideStatusResponse)
    {
        try {
            driverRideSocketResponse = loginResponse
            driverStatus=loginResponse.status.toInt()
            Log.e("checkDriverStatus",driverStatus.toString())
            when(driverStatus)
            {
                0->
                {
                    if (MainActivity.mainActivity.FragmentTag.toString().equals(HOMESCREEN)) {
                        try {
                            if (loginResponse.booking != null) {
                                isTracking = true
                                tv_chat.visibility = View.VISIBLE
                                switch_online.visibility = View.GONE
                                ll_top_navigation.visibility = View.VISIBLE
//                ll_top_navigation.startAnimation(animHide)
                                ll_user_bottom.visibility = View.VISIBLE
                                ll_user_bottom.startAnimation(animShow)
                                ll_waiting_rider.visibility = View.GONE
//                ll_waiting_rider.startAnimation(animHide)
                                btn_starttrip.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_yellow))
                                btn_starttrip.setText(getString(R.string.arrived))
                                tv_user_name.setText(loginResponse.booking.userId.firstName + " " + loginResponse.booking.userId.lastName)
                                MainActivity.mainActivity.getImageRequest(loginResponse.booking.userId.profilePic)
                                    .into(iv_user_image)
                                tv_user_address.setText(loginResponse.booking.source.name)
                                MainActivity.mainActivity.onChangingfragment(AppConstants.ACCEPTSCREEN)
                                EVENT_NEW_MESSAGE_LISTENER = loginResponse.booking.userId.id + MainActivity.mainActivity.getUserId()
                                EventServiceImpl.EVENTSOCKETOFF = loginResponse.booking.userId.id +  MainActivity.mainActivity.getUserId()
//                EVENT_NEW_MESSAGE_LISTENER=loginResponse.booking.driverId+loginResponse.booking.userId.id

                                val origin: LatLng = LatLng(latitude, longitude)
                                val source: LatLng = LatLng(
                                    loginResponse.booking.source.latitude.toDouble(),
                                    loginResponse.booking.source.longitude.toDouble()
                                )
                                mapLatlng=source
                                val list: ArrayList<LatLng> = ArrayList<LatLng>()
                                list.add(origin)
                                list.add(source)
                                drawPolyLineOnMap(list)
                            }
                        }catch (ex : Exception)
                        {
                            ex.printStackTrace()
                        }

                    }

                }
                1->
                {
                    if (MainActivity.mainActivity.FragmentTag.toString().equals(HOMESCREEN)) {
                        try {
                            if (loginResponse.booking != null) {
                                isTracking = true
                                tv_chat.visibility = View.VISIBLE
                                switch_online.visibility = View.GONE
                                ll_top_navigation.visibility = View.VISIBLE
//                ll_top_navigation.startAnimation(animShow)
                                ll_user_bottom.visibility = View.VISIBLE
                                ll_user_bottom.startAnimation(animShow)
                                ll_waiting_rider.visibility = View.VISIBLE
                                ll_waiting_rider.startAnimation(animHide)
                                btn_starttrip.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_green))
                                btn_starttrip.setText(getString(R.string.starttrip))
                                tv_user_name.setText(loginResponse.booking.userId.firstName + " " + loginResponse.booking.userId.lastName)
                                MainActivity.mainActivity.getImageRequest(loginResponse.booking.userId.profilePic)
                                    .into(iv_user_image)
                                tv_user_address.setText(loginResponse.booking.source.name)
                                MainActivity.mainActivity.onChangingfragment(AppConstants.ACCEPTSCREEN)
                                EVENT_NEW_MESSAGE_LISTENER = loginResponse.booking.userId.id + MainActivity.mainActivity.getUserId()
                                EventServiceImpl.EVENTSOCKETOFF = loginResponse.booking.userId.id +  MainActivity.mainActivity.getUserId()
//                EVENT_NEW_MESSAGE_LISTENER=loginResponse.booking.driverId+loginResponse.booking.userId.id
                                val origin: LatLng = LatLng(latitude, longitude)
                                val source: LatLng = LatLng(loginResponse.booking.destination.latitude.toDouble(),
                                    loginResponse.booking.destination.longitude.toDouble()
                                )
                                mapLatlng=source
                                val list: ArrayList<LatLng> = ArrayList<LatLng>()
                                list.add(origin)
                                list.add(source)
                                drawPolyLineOnMap(list)
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }

//                showLocations(origin,source)
//                mapLatlng=source
                }
                2->
                {
                    if (MainActivity.mainActivity.FragmentTag.toString().equals(HOMESCREEN)) {
                        try {
                            if (loginResponse.booking != null) {
                                isTracking = true
                                tv_chat.visibility = View.GONE
                                switch_online.visibility = View.GONE
                                ll_top_navigation.visibility = View.VISIBLE
//                ll_top_navigation.startAnimation(animShow)
                                ll_user_bottom.visibility = View.VISIBLE
                                ll_user_bottom.startAnimation(animShow)
                                ll_waiting_rider.visibility = View.GONE
//                ll_waiting_rider.startAnimation(animShow)
                                btn_starttrip.setBackgroundColor(ContextCompat.getColor(context!!, R.color.button_red))
                                btn_starttrip.setText(getString(R.string.complete_trip))
                                tv_user_name.setText(loginResponse.booking.userId.firstName + " " + loginResponse.booking.userId.lastName)
                                MainActivity.mainActivity.getImageRequest(loginResponse.booking.userId.profilePic)
                                    .into(iv_user_image)
                                tv_user_address.setText(loginResponse.booking.destination.name)
                                MainActivity.mainActivity.onChangingfragment(AppConstants.ACCEPTSCREEN)
//                EVENT_NEW_MESSAGE_LISTENER="gdyughk"

                                val origin: LatLng = LatLng(latitude, longitude)
                                val source: LatLng = LatLng(
                                    loginResponse!!.booking.destination.latitude.toDouble(),
                                    loginResponse!!.booking.destination.longitude.toDouble()
                                )
                                mapLatlng=source
                                val list: ArrayList<LatLng> = ArrayList<LatLng>()
                                list.add(origin)
                                list.add(source)
                                drawPolyLineOnMap(list)
                            }

                        }catch (ex:Exception)
                        {
                            ex.printStackTrace()
                        }
                    }
//                showLocations(origin,source)
//                mapLatlng=source
                }
                3->
                {
                    if (MainActivity.mainActivity.FragmentTag.toString().equals(HOMESCREEN)) {
                        try {
                            if (loginResponse.booking != null) {
                                isTracking = false
                                tv_chat.visibility = View.GONE
                                EVENT_NEW_MESSAGE_LISTENER = "gdyughk"
                                val fragment = PayFragment()
                                val b = Bundle()
                                b.putParcelable(SENDEDDATA, loginResponse)
                                fragment.setArguments(b)
                                MainActivity.mainActivity.replaceFragment(fragment, AppConstants.ACCEPTSCREEN)
                            }

                        }catch (ex:Exception)
                        {
                            ex.printStackTrace()
                        }
                    }
                }
                4->
                {
                    isTracking=false
                    EVENT_NEW_MESSAGE_LISTENER="gdyughk"
                    try {
                        PayFragment.paymentFragment.onPaidAmmount(driverStatus)
                    }catch (ex:Exception)
                    {
                        ex.printStackTrace()
                    }

                }
                5->
                {

                }
                6->
                {

                }
                7->
                {

                }
                8->
                {

                }
                9->
                {
                    try {
                        isTracking=false
                        EVENT_NEW_MESSAGE_LISTENER="gdyughk"
                        MainActivity.mainActivity.onChangingfragment(AppConstants.HOMESCREEN)
                    }catch (ex:Exception)
                    {
                        ex.printStackTrace()
                    }

                }
                11->
                {
                    isTracking=false
                    try {
                        MainActivity.mainActivity.replaceFragment(HomeFragment(), AppConstants.HOMESCREEN)
                        showAlert(getString(R.string.canceled_by_user))
                    }catch (ex:Exception)
                    {
                        ex.printStackTrace()
                    }

                }
            }
        }catch (ex:Exception)
        {
            ex.printStackTrace()
        }

    }

    private fun driverRideStatus()
    {
        val params=JSONObject()
        params.put(DRIVER_ID,MainActivity.mainActivity.getUserId())
        params.put(BOOKING_ID,"")
        MainActivity.mainActivity.sendObjectToSocket(params, RIDESTATUS)
    }

    private fun tracking()
    {
        try {
            if (driverRideSocketResponse!=null) {
                val params = JSONObject()
//            params.put(DRIVER_ID, MainActivity.mainActivity.getUserId())
                params.put(BOOKING_ID, driverRideSocketResponse!!.booking.id)
                params.put(LATITUDE, latitude)
                params.put(LONGITUDE, longitude)
                params.put(STATUS_SOCKET,driverStatus)
                params.put(DRIVER_ID,MainActivity.mainActivity.getUserId())
                params.put(BEARING,bearingValue.toString())
//            Log.e("TRACKING",params.toString())
                MainActivity.mainActivity.sendObjectToSocket(params, TRACKING)
            }
        }catch (ex:Exception)
        {
            ex.printStackTrace()
        }

    }

    fun onChangeStatus(loginResponse: DriverRideStatusResponse) {
        checkDriverStatus(loginResponse)
    }


    fun openMap(latLng: LatLng)
    {

        val i = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?q=loc:" + DESTINATION_LATLNG.latitude + "," + DESTINATION_LATLNG.longitude))
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Only if initiating from a Broadcast Receiver
       i.setPackage("com.google.android.apps.maps")
        if (i.resolveActivity(context!!.getPackageManager()) != null)
        {
            i.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
            i.setPackage("com.google.android.apps.maps")
        }
        startActivity(i)
    }


    private fun bearingBetweenLocations(latLng1:LatLng, latLng2:LatLng):Double {
        val PI = 3.14159
        val lat1 = latLng1.latitude * PI / 180
        val long1 = latLng1.longitude * PI / 180
        val lat2 = latLng2.latitude * PI / 180
        val long2 = latLng2.longitude * PI / 180
        val dLon = (long2 - long1)
        val y = Math.sin(dLon) * Math.cos(lat2)
        val x = (Math.cos(lat1) * Math.sin(lat2) - (Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon)))
        var brng = Math.atan2(y, x)
        brng = Math.toDegrees(brng)
        brng = (brng + 360) % 360
        return brng
    }


    // Draw polyline on map
    fun drawPolyLineOnMap(list:List<LatLng>) {
        try {
            if (mMap!=null) {
                mMap!!.clear()
            }
            SOURCE_LATLNG=list.get(0)
            DESTINATION_LATLNG=list.get(1)
            val options = MarkerOptions()
            options.position(SOURCE_LATLNG)
            options.position(DESTINATION_LATLNG)
            mMap!!.addMarker(options)
            val url = getMapsApiDirectionsUrl()
            val downloadTask = ReadTask()
            downloadTask.execute(url)
            addMarkers()
        }catch (ex:java.lang.Exception)
        {
            ex.printStackTrace()
        }


    }

    private fun getMapsApiDirectionsUrl():String {
        val origin = "origin=" + SOURCE_LATLNG.latitude + "," + SOURCE_LATLNG.longitude
        val dest = "destination=" + DESTINATION_LATLNG.latitude + "," + DESTINATION_LATLNG.longitude
        val sensor = "sensor=false"
        val params = "$origin&$dest&$sensor&mode=driving&key="+getString(R.string.google_map_key)
//        val params = "$origin&$dest&$sensor&mode=driving&key=AIzaSyB_EHv3Hz6o0E2pmX-WovIgZDVmU26Y6p0"
//        Log.e("797","https://maps.googleapis.com/maps/api/directions/json?$params")
        return "https://maps.googleapis.com/maps/api/directions/json?$params"
    }

    private fun addMarkers() {
        if (mMap != null)
        {
            mMap!!.clear()
            mMap!!.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap)).position(SOURCE_LATLNG))
            mMap!!.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(sourcebitmap)).position(DESTINATION_LATLNG))

        }
    }

    private class ReadTask:AsyncTask<String, Void, String>() {
       override protected fun doInBackground(vararg url:String):String {
            var data = ""
            try
            {
                val http = HttpConnection()
                data = http.readUrl(url[0])
            }
            catch (e:Exception) {
                Log.d("Background Task", e.toString())
            }
            return data
        }
      override  protected fun onPostExecute(result:String) {
            super.onPostExecute(result)

          try {
              val gson1 =  Gson();
              val loginResponse = gson1.fromJson(result.toString(), GoogleApiResponse::class.java)
              if (loginResponse!=null)
              {
                  if (loginResponse.routes!=null && loginResponse.routes.size>0)
                  {
                      if (loginResponse.routes.get(0).legs!=null && loginResponse.routes.get(0).legs.size>0)
                      {
                          val text:String=loginResponse.routes.get(0).legs.get(0).duration.text
                          HomeFragment.homeFragment.setValueToText(text)
                      }
                  }


              }

//          Log.e("READ TASK : ",result+"")
              ParserTask().execute(result)
          }catch (ex:java.lang.Exception)
          {
              ex.printStackTrace()
          }


        }
    }

    private fun setValueToText(text: String) {
        try {
            tv_user_wait_time.setText(text+"")
        }catch (ex:Exception)
        {
            ex.printStackTrace()
        }

    }

    public  class ParserTask : AsyncTask<String, Int,  List<List<HashMap<String, String>>>>() {
       override protected fun doInBackground(
            vararg jsonData: String
        ):  List<List<HashMap<String, String>>> {
            val jObject: JSONObject
            var routes:  List<List<HashMap<String, String>>>? = null
            try {
                jObject = JSONObject(jsonData[0])
                val parser = PathJSONParser()
                routes = parser.parse(jObject)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return routes!!
        }

       override protected fun onPostExecute(routes:  List<List<HashMap<String, String>>>) {

           try {
               var points: ArrayList<LatLng>? = null
               var polyLineOptions: PolylineOptions? = null
               // traversing through routes
               for (i in 0 until routes.size) {
                   points = ArrayList()
                   polyLineOptions = PolylineOptions()
                   val path = routes[i]
                   for (j in 0 until path.size) {
                       val point = path.get(j)
                       val lat = java.lang.Double.parseDouble(point.get("lat"))
                       val lng = java.lang.Double.parseDouble(point.get("lng"))
                       val position = LatLng(lat, lng)
                       points.add(position)
                   }
                   polyLineOptions.addAll(points)
                   polyLineOptions.width(4f)
                   polyLineOptions.color(Color.BLACK)

               }
               if (mMap!=null) {
                   if (polyLineOptions!=null) {
                       mMap!!.addPolyline(polyLineOptions)
                   }
               }
           }catch (ex:java.lang.Exception)
           {
               ex.printStackTrace()
           }


        }
    }

}
