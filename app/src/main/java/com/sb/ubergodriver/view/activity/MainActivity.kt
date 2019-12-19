package com.sb.ubergodriver.view.activity

import android.arch.lifecycle.ProcessLifecycleOwner
import android.content.Context
import android.content.DialogInterface
import android.os.*
import com.sb.ubergodriver.R
import com.sb.ubergodriver.interfaces.DrawerView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.FrameLayout
import java.util.*
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.CommonUtils.AppConstants.*
import com.sb.ubergodriver.CommonUtils.locationMethods.LocationAddress
import com.sb.ubergodriver.model.message.MessageList
import com.sb.ubergodriver.model.socketResponse.DriverRideStatusResponse
import com.sb.ubergodriver.model.socketResponse.SocketBookingResponse
import com.sb.ubergodriver.socket.interfaces.ChatPresenter
import com.sb.ubergodriver.socket.interfaces.SocketContract
import com.sb.ubergodriver.socket.interfaces.eventservice.EventService
import com.sb.ubergodriver.socket.interfaces.eventservice.EventServiceImpl
import com.sb.ubergodriver.socket.interfaces.util.AppLifeCycleObserver
import com.sb.ubergodriver.socket.interfaces.util.Injection
import com.sb.ubergodriver.view.fragment.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Exception


class MainActivity : BaseActivity(), DrawerView,SocketContract.View {



    override fun showAlert(message: String?, isTrue: Boolean?) {
        Log.i(TAG, "showAlert: $message")
    }

    var FragmentTag:String=""
    public var mPresenter: SocketContract.Presenter? = null
    override fun onChangingfragment(type: String) {
        val grayColor:Int=ContextCompat.getColor(this@MainActivity,R.color.edittextline)
        val blackColor:Int=ContextCompat.getColor(this@MainActivity,R.color.black)
        if (type.toString().equals(RATINGSCREEN))
        {
            iv_rating.setImageResource(R.drawable.ic_star_black_filled)
            tv_rating.setTextColor(blackColor)
            iv_earning.setImageResource(R.drawable.ic_earning_gray)
            tv_earning.setTextColor(grayColor)
            iv_alerts.setImageResource(R.drawable.ic_notification_gray)
            tv_alert.setTextColor(grayColor)
            iv_account.setImageResource(R.drawable.ic_user_icon_with_gray_circle)
            tv_account.setTextColor(grayColor)
            rl_home.setBackgroundResource(R.drawable.ic_seat_belt_not_connected)
            ll_main_bottom.visibility=View.VISIBLE


        }
        else if (type.toString().equals(EARNINGSCREEN))
        {

            iv_rating.setImageResource(R.drawable.ic_star_gray)
            tv_rating.setTextColor(grayColor)
            iv_earning.setImageResource(R.drawable.ic_earning_black_filled)
            tv_earning.setTextColor(blackColor)
            iv_alerts.setImageResource(R.drawable.ic_notification_gray)
            tv_alert.setTextColor(grayColor)
            iv_account.setImageResource(R.drawable.ic_user_icon_with_gray_circle)
            tv_account.setTextColor(grayColor)
            rl_home.setBackgroundResource(R.drawable.ic_seat_belt_not_connected)
            ll_main_bottom.visibility=View.VISIBLE

        }
        else if (type.toString().equals(ALERTSCREEN))
        {

            iv_rating.setImageResource(R.drawable.ic_star_gray)
            tv_rating.setTextColor(grayColor)
            iv_earning.setImageResource(R.drawable.ic_earning_gray)
            tv_earning.setTextColor(grayColor)
            iv_alerts.setImageResource(R.drawable.ic_notification_black)
            tv_alert.setTextColor(blackColor)
            iv_account.setImageResource(R.drawable.ic_user_icon_with_gray_circle)
            tv_account.setTextColor(grayColor)
            rl_home.setBackgroundResource(R.drawable.ic_seat_belt_not_connected)
            ll_main_bottom.visibility=View.VISIBLE

        }
        else if (type.toString().equals(HOMESCREEN))
        {

            iv_rating.setImageResource(R.drawable.ic_star_gray)
            tv_rating.setTextColor(grayColor)
            iv_earning.setImageResource(R.drawable.ic_earning_gray)
            tv_earning.setTextColor(grayColor)
            iv_alerts.setImageResource(R.drawable.ic_notification_gray)
            tv_alert.setTextColor(grayColor)
            iv_account.setImageResource(R.drawable.ic_user_icon_with_gray_circle)
            tv_account.setTextColor(grayColor)
            rl_home.setBackgroundResource(R.drawable.ic_seat_belt_connected)
            ll_main_bottom.visibility=View.VISIBLE

        }
        else if (type.toString().equals(ACCOUNTSCREEN))
        {
            iv_rating.setImageResource(R.drawable.ic_star_gray)
            tv_rating.setTextColor(grayColor)
            iv_earning.setImageResource(R.drawable.ic_earning_gray)
            tv_earning.setTextColor(grayColor)
            iv_alerts.setImageResource(R.drawable.ic_notification_gray)
            tv_alert.setTextColor(grayColor)
            iv_account.setImageResource(R.drawable.ic_user_icon_with_black_circle)
            tv_account.setTextColor(blackColor)
            rl_home.setBackgroundResource(R.drawable.ic_seat_belt_not_connected)
            ll_main_bottom.visibility=View.VISIBLE

        }
        else if (type.toString().equals(NOMAINSCREEN))
        {
            iv_rating.setImageResource(R.drawable.ic_star_gray)
            tv_rating.setTextColor(grayColor)
            iv_earning.setImageResource(R.drawable.ic_earning_gray)
            tv_earning.setTextColor(grayColor)
            iv_alerts.setImageResource(R.drawable.ic_notification_gray)
            tv_alert.setTextColor(grayColor)
            iv_account.setImageResource(R.drawable.ic_user_icon_with_gray_circle)
            tv_account.setTextColor(grayColor)
            rl_home.setBackgroundResource(R.drawable.ic_seat_belt_not_connected)
            ll_main_bottom.visibility=View.GONE
        }
        else if (type.toString().equals(ACCEPTSCREEN))
        {
            ll_main_bottom.visibility=View.GONE
        }
        else if (type.toString().equals(PAYSCREEN))
        {
            ll_main_bottom.visibility=View.GONE
        }
        else if (type.toString().equals(CHATSCREEN))
        {
            ll_main_bottom.visibility=View.GONE
        }
    }

    private var frame: FrameLayout? = null

    private var mFragmentManager: FragmentManager?=null

    override fun onReplaceFragment(fragment: Fragment, tag: String) {
        replaceFragment(fragment,tag)
    }

    override fun OpenDrawer() {

    }

    fun OnExit()
    {
        AlertDialog.Builder(this)
            .setTitle("Exit")
            .setMessage("Are you sure you want to exit?")
            .setNegativeButton(android.R.string.no, null)
            .setPositiveButton(android.R.string.yes, object: DialogInterface.OnClickListener{
                override fun onClick(arg0:DialogInterface, arg1:Int) {
                    finish()
                }
            }).create().show()
    }

    companion object {
        lateinit var mainActivity:MainActivity
    }

    override fun onPause() {
        super.onPause()
        mPresenter!!.unsubscribe()
    }

    override fun onResume() {
        super.onResume()
        ChatPresenter(
            this, this,
            Injection.provideSchedulerProvider(),
            Injection.providesSocketRepository(this)
        )
    }



    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.ll_rating ->
            {
                if (!FragmentTag.equals(RATINGSCREEN)) {
                    onReplaceFragment(RatingFragment(), AppConstants.RATINGSCREEN)
                }

            }
            R.id.ll_earning ->
            {
                if (!FragmentTag.equals(EARNINGSCREEN)) {
                    onReplaceFragment(EarningFragment(),AppConstants.EARNINGSCREEN)
                }

            }

            R.id.ll_alert ->
            {
                if (!FragmentTag.equals(ALERTSCREEN)) {
                    onReplaceFragment(AlertFragment(),AppConstants.ALERTSCREEN)
                }
            }

            R.id.ll_account ->
            {
                if (!FragmentTag.equals(ACCOUNTSCREEN)) {
                    onReplaceFragment(AccountFragment(),AppConstants.ACCOUNTSCREEN)
                }

            }

            R.id.rl_home ->
            {
                if (!FragmentTag.equals(HOMESCREEN)) {
                    onReplaceFragment(HomeFragment(),AppConstants.HOMESCREEN)
                }

            }
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                .detectCleartextNetwork()
                .penaltyLog()
                .build())
        };
        frame = findViewById<View>(R.id.main_container) as FrameLayout
        mFragmentManager = supportFragmentManager
        mainActivity = this
        addFragment(HomeFragment(),AppConstants.HOMESCREEN)
        EventServiceImpl.getInstance().connect(getUserId())
        try {
            Log.e("intent : " , "null")
            if (intent!=null)
            {
                Log.e("intent : " , "not null")
                if (intent.getStringExtra("fromNotification") != null)
                {
                    Log.e("getStringExtra : " , "not null")
                    Handler().postDelayed({
                        sendRequesttoFragment(intent.getStringExtra("fromNotification"))
                    }, 1500)

                }
            }
        }catch (ex:Exception)
        {
            ex.printStackTrace()
        }


    }

    override fun onBackPressed() {

        if (!FragmentTag.isEmpty())
        {

            if (FragmentTag.equals(AppConstants.HOMESCREEN))
            {
                OnExit()
                return
            }
            else if (FragmentTag.equals(AppConstants.EDITPROFILESCREEN))
            {
                replaceFragment(AccountFragment(),AppConstants.ACCOUNTSCREEN)
                return
            }
            else if (FragmentTag.equals(AppConstants.ACCOUNTSCREEN))
            {
                replaceFragment(HomeFragment(),AppConstants.HOMESCREEN)
                return
            }
            else if (FragmentTag.equals(AppConstants.DOCUMENTSCREEN))
            {
                replaceFragment(AccountFragment(),AppConstants.ACCOUNTSCREEN)
                return
            }

            else if (FragmentTag.equals(AppConstants.SETTINGFRAGMENT))
            {
                replaceFragment(AccountFragment(),AppConstants.ACCOUNTSCREEN)
                return
            }

            else if (FragmentTag.equals(AppConstants.HISTORYDETAILSCREEN))
            {
                replaceFragment(EarningFragment(),AppConstants.EARNINGSCREEN)
                return
            }

            else if (FragmentTag.equals(AppConstants.TRIPHISTORYDAYSCREEN))
            {
                replaceFragment(EarningFragment(),AppConstants.EARNINGSCREEN)
                return
            }

            else if (FragmentTag.equals(AppConstants.TRIPHISTORYVIEWALLSCREEN))
            {
                replaceFragment(TripHistoryDayFragment(),AppConstants.TRIPHISTORYDAYSCREEN)
                return
            }


            else if (FragmentTag.equals(AppConstants.CHATSCREEN))
        {
            replaceFragment(HomeFragment(),AppConstants.HOMESCREEN)
            return
        }
            else if (FragmentTag.equals(AppConstants.ALERTSCREEN))
            {
                replaceFragment(HomeFragment(),AppConstants.HOMESCREEN)
                return
            }

            else if (FragmentTag.equals(AppConstants.EARNINGSCREEN))
            {
                replaceFragment(HomeFragment(),AppConstants.HOMESCREEN)
                return
            }

            else if (FragmentTag.equals(AppConstants.ACCOUNTSCREEN))
            {
                replaceFragment(HomeFragment(),AppConstants.HOMESCREEN)
                return
            }
            else if (FragmentTag.equals(AppConstants.ALERTSCREEN))
            {
                replaceFragment(HomeFragment(),AppConstants.HOMESCREEN)
                return
            }
            else if (FragmentTag.equals(AppConstants.TRIPHISTORY))
            {
                replaceFragment(EarningFragment(),AppConstants.EARNINGSCREEN)
                return
            }

            else if (FragmentTag.equals(AppConstants.NOTBACKPRESS))
            {

                return
            }
            else if (FragmentTag.equals(AppConstants.ACCEPTSCREEN))
            {

                return
            }
            else
            {
                OnExit()
                return
            }

        }
        else {
            OnExit()
        }
    }

    fun forceHideKeyboard(context: Context) {
        try {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = getCurrentFocus()
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null)
            {
                view = View(context)
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }
        catch (ex:Exception)
        {
            ex.printStackTrace()
        }

    }

//    private var mFragmentStack: Stack<Fragment>? = null

    fun replaceFragment(fragment: Fragment,tag: String) {
        FragmentTag=tag
//        mFragmentStack = Stack()
        val transaction = mFragmentManager?.beginTransaction()
        transaction!!.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
        transaction.replace(R.id.main_container, fragment,tag)
        transaction.addToBackStack(null)
//        (mFragmentStack as Stack<Fragment>).push(fragment)
        transaction.commitAllowingStateLoss()
    }


    fun noExitTag()
    {
        FragmentTag= NOTBACKPRESS
    }




    fun  startAThread(lat:Double, lng:Double)
    {
        runOnUiThread(object : Runnable {
            override fun run() {
                val locationAddress = LocationAddress()
                locationAddress.getAddressFromLocation(
                    lat, lng,
                    this@MainActivity, GeocoderHandler()
                )
            }
        })
    }


    private inner class GeocoderHandler() : Handler() {
        override fun handleMessage(message: Message) {
            val locationAddress: String?
            when (message.what) {
                1 -> {
                    val bundle = message.getData()
                    locationAddress = bundle.getString("address")
                }
                else -> locationAddress = null
            }
//            tv_address!!.setText(locationAddress)
        }
    }




    fun addFragment(fragment: Fragment,tag: String) {
        if (mFragmentManager != null) {
            FragmentTag=tag
            val transaction = (mFragmentManager as FragmentManager).beginTransaction()
            transaction.add(R.id.main_container, fragment)
            transaction.commitAllowingStateLoss()
        }
    }


    override fun onConnect(vararg args: Any?) {
//       showAlert(args[0].toString())

    }

    override fun updateUsername(username: String?) {

    }

    override fun onDisconnect(vararg args: Any?) {
//        showAlert(args[0].toString())
    }

    override fun onConnectError(vararg args: Any?) {
//        showAlert(args[0].toString())
    }

    override fun onConnectTimeout(vararg args: Any?) {
//        showAlert(args[0].toString())
    }

    override fun onNewMessage(vararg args: Any?) {

//        Log.e("onNewMessage",args[0].toString())
//        var jsonObject:JSONObject= args[0] as JSONObject
//        if (jsonObject.getString("success").toString().equals(TRUE))
//        {
//            val data:String=jsonObject.getJSONObject("msg").toString()
//            val gson1 =  Gson();
//            val loginResponse = gson1.fromJson(data, MessageList::class.java)
//            if (FragmentTag.toString().equals(CHATSCREEN))
//            {
//                runOnUiThread(object:Runnable {
//                    public override fun run() {
//                        // change UI elements here
//                        ChatFargment.chatFragment.onGetNewMessage(loginResponse)
//
//                    }
//                })
//
//            }
//
//        }
//        else
//        {
//            showAlert(jsonObject.getString("message").toString())
//        }
//
// showAlert(args[0].toString())
    }

    override fun onUserJoined(vararg args: Any?) {
//        showAlert(args[0].toString())
    }

    override fun onUserLeft(vararg args: Any?) {
//        showAlert(args[0].toString())
    }

    override fun onTyping(vararg args: Any?) {
//        showAlert(args[0].toString())
    }

    override fun initView() {

    }
    override fun onStopTyping(vararg args: Any?) {

    }

    override fun onMessageDelivered(chatMessage: JSONObject?) {

    }

    override fun setPresenter(presenter: SocketContract.Presenter?) {
        mPresenter = presenter
    }

    override fun hideAlert() {

    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun onBookingRequest(vararg args: Any?) {
//        showAlert(args[0].toString())
        try {
            Log.e("onBookingRequest ",args[0].toString())
            val gson1 =  Gson();
            val loginResponse = gson1.fromJson(args[0].toString(), SocketBookingResponse::class.java)
            if (loginResponse.success.toString().equals(TRUE))
            {
                runOnUiThread(object:Runnable {
                    public override fun run() {
                        // change UI elements here
                        val moneyTransferFragment = AcceptFragment()
                        val b = Bundle()
                        b.putParcelable(SENDEDDATA, loginResponse)
                        moneyTransferFragment.setArguments(b)
                        replaceFragment(moneyTransferFragment,AppConstants.ACCEPTSCREEN)

                    }
                })

            }
        }
        catch (ex:Exception)
        {
            ex.printStackTrace()
        }

    }

    fun sendRequesttoFragment(string: String)
    {
        try {
            Log.e("string ",string)
            val gson1 =  Gson();
            val loginResponse = gson1.fromJson(string, SocketBookingResponse::class.java)
            val moneyTransferFragment = AcceptFragment()
            val b = Bundle()
            b.putParcelable(SENDEDDATA, loginResponse)
            moneyTransferFragment.setArguments(b)
            Log.e("moneyTransferFragment ",b.toString())
            replaceFragment(moneyTransferFragment,AppConstants.ACCEPTSCREEN)
        }catch (ex:Exception)
        {
            ex.printStackTrace()
        }


    }

    override fun onUpdateDriverLatitudeLongitude(vararg args: Any?) {
//        showAlert(args[0].toString())
    }

    override fun onGetDriverRideStatus(vararg args: Any?) {
//        showAlert(args[0].toString())

        try {
            val gson1 =  Gson();
            Log.e("onGetDriverRideStatus ",args[0].toString())
            val loginResponse = gson1.fromJson(args[0].toString(), DriverRideStatusResponse::class.java)
            if (loginResponse.success.toString().equals(TRUE))
            {
                runOnUiThread(object:Runnable {
                    public override fun run() {
                        // change UI elements here
                        HomeFragment.homeFragment.checkDriverStatus(loginResponse)
                    }
                })

            }
        }catch (ex:Exception)
        {
            ex.printStackTrace()
        }

    }

    override fun onPaidAmount(vararg args: Any?) {

    }

    override fun onChangeStatus(vararg args: Any?) {
try {
    Log.e("onChangeStatus ", args[0].toString())
    val gson1 = Gson();
    val loginResponse = gson1.fromJson(args[0].toString(), DriverRideStatusResponse::class.java)
    if (!loginResponse.status.toString().isEmpty()) {

        runOnUiThread(object : Runnable {
            public override fun run() {
                // change UI elements here
                HomeFragment.homeFragment.onChangeStatus(loginResponse)
            }
        })
    }
}catch (ex:Exception)
{
    ex.printStackTrace()
}

    }

    override fun onBookingRequestResponse(vararg args: Any?) {
//        showAlert(args[0].toString())
        try {
            val  myObject:JSONObject = args[0] as JSONObject
            Log.e("RequestResponse ",args[0].toString())
            if (myObject.getString(SUCCESS).toString().equals(FALSE))
            {
                AcceptFragment.acceptFragment.change(false)
                showAlert(myObject.getString(ERRORMESSAGE).toString())
            }
            else
            {
                val gson1 =  Gson();
                val loginResponse = gson1.fromJson(args[0].toString(), DriverRideStatusResponse::class.java)
                if (loginResponse.success.toString().equals(TRUE))
                {
                    runOnUiThread(object:Runnable {
                        public override fun run() {
                            // change UI elements here
                            HomeFragment.homeFragment.checkDriverStatus(loginResponse)
                        }
                    })

                }
            }
        }catch (ex:Exception)
        {
            ex.printStackTrace()
        }

    }

    override fun onDriverTracking(vararg args: Any?) {
//        Log.e("onDriverTracking",args[0].toString())
//        showAlert(args[0].toString())
    }

    override fun onDriverChangeRideStatus(vararg args: Any?) {

        try {
            Log.e("onChangeRideStatus",args[0].toString())
            val gson1 = Gson();
            val loginResponse = gson1.fromJson(args[0].toString(), DriverRideStatusResponse::class.java)
            if (!loginResponse.status.toString().isEmpty()) {

                runOnUiThread(object : Runnable {
                    public override fun run() {
                        // change UI elements here
                        HomeFragment.homeFragment.onChangeStatus(loginResponse)
                    }
                })
            }
        }catch (ex:Exception)
        {
            ex.printStackTrace()
        }

//        showAlert(args[0].toString())
    }



    fun sendObjectToSocket(jsonObject: JSONObject,type:String)
    {
        try {
            mPresenter!!.sendParamtoSocket(jsonObject,type)
        }catch (ex:Exception)
        {
            ex.printStackTrace()
        }

    }

    fun socketOFF(type: String)
    {

    }




}
