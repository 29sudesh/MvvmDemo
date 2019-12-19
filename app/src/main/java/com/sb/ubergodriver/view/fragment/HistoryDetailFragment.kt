package com.sb.ubergodriver.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.os.AsyncTask
import com.sb.ubergodriver.R
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beust.klaxon.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.TravelMode
import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.CommonUtils.Utils
import com.sb.ubergodriver.CommonUtils.locationMethods.LocationResult
import com.sb.ubergodriver.CommonUtils.locationMethods.MyLocation
import com.sb.ubergodriver.model.googleMapApiresponse.GoogleApiResponse
import com.sb.ubergodriver.model.tripHistory.BookingHistory
import com.sb.ubergodriver.view.activity.MainActivity
import com.sb.ubergodriver.view.mapUtils.HttpConnection
import com.sb.ubergodriver.view.mapUtils.PathJSONParser
import kotlinx.android.synthetic.main.fragment_history_detail.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.net.URL
import java.util.ArrayList


class HistoryDetailFragment : BaseFragment(), OnMapReadyCallback, LocationResult {

    private var mMap: GoogleMap? = null
    var bitmap: Bitmap?=null
    var sourcebitmap: Bitmap?=null
    var bookingData: BookingHistory?=null

    private var latitude = -33.852
    private var longitude = 151.211

    private var myLocation: MyLocation? = null

    private var SOURCE_LATLNG = LatLng(40.722543,
        -73.998585)
    private var DESTINATION_LATLNG = LatLng(40.7057, -73.9964)

    companion object {
        var historyDetailFragment:HistoryDetailFragment= HistoryDetailFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_history_detail, container, false)
        historyDetailFragment=this
        bitmap=getBitmap(R.drawable.ic_car_with_circle_gray)
        sourcebitmap=getBitmap(R.drawable.ic_user_icon_with_black_circle)
        myLocation = MyLocation()

        val mapFragment = getChildFragmentManager()
            .findFragmentById(R.id.maphistory) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (getArguments() !=null) {
            bookingData =getArguments()!!.getParcelable(AppConstants.SENDEDDATA) as BookingHistory
        }

        return view
    }

    override fun gotLocation(location: Location?) {
        goToMYLocation()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        MainActivity.mainActivity.onChangingfragment(AppConstants.NOMAINSCREEN)
        tv_title_common.text=getString(R.string.triphistory)
        iv_back_common.setOnClickListener(this)
        iv_back_common.setImageResource(R.drawable.ic_back_black)


        if (bookingData!=null)
        {
            Log.e("Booking data :",bookingData!!.fare.toString())
            tv_estimated_payment.setText(bookingData!!.fare.toString())
            tv_source.setText(bookingData!!.source.name.toString())
            tv_destination.setText(bookingData!!.destination.name.toString())
            tv_ride_fare.setText(getString(R.string.turkish_currency_sign)+" "+bookingData!!.fare.toString())
            tv_taxes.setText(getString(R.string.turkish_currency_sign)+" "+bookingData!!.tax.toString())
            tv_rounded.setText(getString(R.string.turkish_currency_sign)+" "+"0")
            tv_total_bill.setText(getString(R.string.turkish_currency_sign)+" "+bookingData!!.fare.toString())
            tv_source_time.setText(Utils.getTime(bookingData!!.tripStartTime))
            tv_destination_time.setText(Utils.getTime(bookingData!!.tripEndTime))
            tv_vehicle_type.setText(bookingData!!.vehicleType.name)
            latitude=bookingData!!.source.latitude.toDouble()
            longitude=bookingData!!.source.longitude.toDouble()
            SOURCE_LATLNG =
                LatLng(bookingData!!.source.latitude.toDouble(), bookingData!!.source.longitude.toDouble())
            DESTINATION_LATLNG =
                LatLng(bookingData!!.destination.latitude.toDouble(), bookingData!!.destination.longitude.toDouble())

            goToMYLocation()


        }
    }

    private fun goToMYLocation() {

        drawMarker(latitude, longitude)

    }


    fun drawMarker(lat: Double?, lng: Double?) {
        if (mMap != null) {
//            mMap!!.clear()
            val gps = LatLng(lat!!, lng!!)
            latitude = lat!!
            longitude = lng!!
            if (gps != null) {
                 mMap!!.addMarker(
                    MarkerOptions().position(gps)
                        .title("").icon(BitmapDescriptorFactory.fromBitmap(bitmap)).draggable(true)
                )
                mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 10f))

                val list: ArrayList<LatLng> = ArrayList<LatLng>()
                list.add(SOURCE_LATLNG)
                list.add(DESTINATION_LATLNG)
                drawPolyLineOnMap(list)


            }
            mMap!!.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                override fun onMarkerClick(marker: Marker): Boolean {
                    return true
                }
            })
        }

    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
//        mMap!!.uiSettings.setAllGesturesEnabled(false)
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
        mMap!!.setMinZoomPreference(10f)

        goToMYLocation()

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


    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.iv_back_common->
            {
                MainActivity.mainActivity.onReplaceFragment(EarningFragment(),AppConstants.EARNINGSCREEN)
            }

        }
    }



    // Draw polyline on map
    fun drawPolyLineOnMap(list:List<LatLng>) {
        SOURCE_LATLNG=list.get(0)
        DESTINATION_LATLNG=list.get(1)
        val options = MarkerOptions()
        options.position(SOURCE_LATLNG)
        options.position(DESTINATION_LATLNG)
//        if (mMap!=null) {
//            mMap!!.addMarker(options)
//        }
        val url = getMapsApiDirectionsUrl()
        val downloadTask = ReadTask()
        downloadTask.execute(url)
        addMarkers()
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
            mMap!!.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap)).position(SOURCE_LATLNG))
            mMap!!.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(sourcebitmap)).position(DESTINATION_LATLNG))
            val latLng:LatLng= LatLng(SOURCE_LATLNG.latitude,SOURCE_LATLNG.longitude)
            mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(latLng)))
        }
    }

    private fun getCameraPositionWithBearing(latLng: LatLng): CameraPosition {
        return CameraPosition.Builder().target(latLng).zoom(11f).build()
    }

    private class ReadTask: AsyncTask<String, Void, String>() {
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

            val gson1 =  Gson();
            val loginResponse = gson1.fromJson(result.toString(), GoogleApiResponse::class.java)
            if (loginResponse!=null)
            {
                try {
                    if (loginResponse.routes!=null && loginResponse.routes.size>0)
                    {
                        if (loginResponse.routes.get(0).legs!=null && loginResponse.routes.get(0).legs.size>0)
                        {
                            val text:String=loginResponse.routes.get(0).legs.get(0).duration.text
                            val text2= loginResponse.routes.get(0).legs.get(0).distance.text
                            HistoryDetailFragment.historyDetailFragment.setValuetoText(text,text2)

                        }
                    }

                }
                catch (ex:Exception)
                {
                    ex.printStackTrace()
                }


            }

            Log.e("READ TASK : ",result+"")
            ParserTask().execute(result)
        }
    }

    private fun setValuetoText(text: String, text2: String?) {
        try {
            if (tv_travel_time!=null) {
                tv_travel_time.setText(text)
                tv_distance.setText(text2)
            }
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
            if (HistoryDetailFragment.historyDetailFragment.mMap!=null) {
                if (polyLineOptions!=null) {
                    HistoryDetailFragment.historyDetailFragment.mMap!!.addPolyline(polyLineOptions)
                }
            }
        }
    }
}
