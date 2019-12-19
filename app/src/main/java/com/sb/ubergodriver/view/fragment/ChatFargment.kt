package com.sb.ubergodriver.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Parcelable
import android.os.PersistableBundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.R
import com.sb.ubergodriver.apiConstants.ViewModelFactory
import com.sb.ubergodriver.model.message.MessageList
import com.sb.ubergodriver.model.socketResponse.DriverRideStatusResponse
import com.sb.ubergodriver.socket.interfaces.eventservice.EventServiceImpl
import com.sb.ubergodriver.view.activity.BaseActivity
import com.sb.ubergodriver.view.activity.MainActivity
import com.sb.ubergodriver.viewModel.login.editProfile.ChathistoryViewModel
import com.sb.ubergodriver.viewModel.login.editProfile.UploadImageViewModel
import org.json.JSONException
import org.json.JSONObject

import javax.inject.Inject
import java.net.URISyntaxException

import com.sb.ubergodriver.CommonUtils.AppConstants.*
import com.sb.ubergodriver.CommonUtils.Utils
import com.sb.ubergodriver.apiConstants.ApiResponse
import com.sb.ubergodriver.apiConstants.Status
import com.sb.ubergodriver.application.App
import com.sb.ubergodriver.interfaces.DrawerView
import com.sb.ubergodriver.model.chat.ChatHistoryRequest
import com.sb.ubergodriver.model.loginResponse.LoginResponse
import com.sb.ubergodriver.model.message.MessageHistory
import com.sb.ubergodriver.socket.interfaces.ChatPresenter
import com.sb.ubergodriver.socket.interfaces.SocketContract
import com.sb.ubergodriver.socket.interfaces.eventservice.EventServiceImpl.EVENT_NEW_MESSAGE_LISTENER
import com.sb.ubergodriver.socket.interfaces.util.Injection
import com.sb.ubergodriver.view.activity.EditChangePasswordActivity
import com.sb.ubergodriver.view.adapters.ChatAdapters
import com.sb.ubergodriver.viewModel.login.editProfile.EditProfileViewModel
import kotlinx.android.synthetic.main.fragment_edit_profile.*

class ChatFargment :BaseActivity(),  SocketContract.View {
    private var messageList: ArrayList<MessageList>? = null
    private var iv_close: ImageView? = null
    private var iv_userImage: ImageView? = null
    private var iv_send: ImageView? = null
    private var tv_name: TextView? = null
    private var tv_rating: TextView? = null
    private var et_message: EditText? = null
    private var rv_chat: RecyclerView?=null
    private var driverRideStatus: DriverRideStatusResponse? = null
    public var mPresenter: SocketContract.Presenter? = null

    private var userName:String= ""
    private var userID:String= ""
    private var bookingID:String=""
    private var strRating:String=""
    private var strProfilePic:String=""

    private var chatAdapter: ChatAdapters?=null
    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: ChathistoryViewModel? = null
    var APPTAG:String= ChatFargment::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_chat)
        messageList= ArrayList<MessageList>()
//        EventServiceImpl.YES=1
        try {
            if (intent!=null)
            {
                userName=intent.getStringExtra(AppConstants.KEYNAME)
                userID=intent.getStringExtra(AppConstants.KEYUSERID)
                bookingID=intent.getStringExtra(AppConstants.KEYBOOKINGID)
//                strRating=intent.getStringExtra(AppConstants.KEYRATING)
                strProfilePic=intent.getStringExtra(AppConstants.KEYPROFILEPIC)
            }

        }catch (ex:Exception)
        {
            ex.printStackTrace()
        }

        (application as App).getAppComponent().doInjection(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ChathistoryViewModel::class.java!!)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

        rv_chat=findViewById(R.id.rv_chat)
        rv_chat!!.layoutManager=LinearLayoutManager(this@ChatFargment)
        rv_chat!!.setHasFixedSize(true)
        iv_close = findViewById(R.id.iv_close)
        iv_userImage = findViewById(R.id.iv_userImage)
        iv_send = findViewById(R.id.iv_send)
        tv_name = findViewById(R.id.tv_userName)
        tv_rating = findViewById(R.id.tv_rating)
        et_message = findViewById(R.id.et_message)
        iv_close!!.setOnClickListener(this)
        iv_send!!.setOnClickListener(this)
        chatAdapter= ChatAdapters(this@ChatFargment,messageList!!)
        rv_chat!!.adapter=chatAdapter

        try {
            if (!userName.isEmpty()) {
                val chatHistoryRequest = ChatHistoryRequest()
                chatHistoryRequest.bookingId = bookingID
                chatHistoryRequest.driverId = getUserId()
                chatHistoryRequest.userId = userID
                viewModel!!.hitChatHistoryApi(chatHistoryRequest)
                    tv_name!!.setText(userName)
                getImageRequest(strProfilePic).into(iv_userImage!!)

            }
        }catch (ex:java.lang.Exception)
        {
            ex.printStackTrace()
        }

    }

    fun onGetNewMessage(newMessage :MessageList)
    {
            messageList!!.add(newMessage)
            chatAdapter!!.updateChatList(messageList!!)
            rv_chat!!.scrollToPosition(messageList!!.size-1)
    }

    override fun onBackPressed() {
        finish()
    }


    /*
  * method to handle response
  * */
    private fun consumeResponse(apiResponse: ApiResponse) {

        when (apiResponse.status) {

            Status.LOADING -> {
//                progressDialog!!.show()
            }

            Status.SUCCESS -> {
//                progressDialog!!.dismiss()
                    renderApiSuccessResponse(apiResponse.data!!)
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
    private fun renderApiSuccessResponse(response: JsonElement) {
        if (!response.isJsonNull) {
            Log.d(APPTAG+"response=", response.toString())
            val data:String=  Utils.toJson(response)
            val gson1 =  Gson();
            val loginResponse = gson1.fromJson(data, MessageHistory::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE))
            {
                messageList=loginResponse.messageList
//                chatAdapter= ChatAdapters(context!!,messageList!!)
                chatAdapter!!.updateChatList(messageList!!)
                rv_chat!!.scrollToPosition(messageList!!.size-1)
            }
            else
            {
//                showAlert(loginResponse.message.toString())
            }
        } else {
            showAlert(resources.getString(R.string.errorString))
        }
    }

    override fun onStart() {
        super.onStart()
    }


    override fun onClick(v: View?) {
        super.onClick(v)
        when (v!!.id) {
            R.id.iv_close -> {
//                EventServiceImpl.YES=0
//                EventServiceImpl.getInstance().disconnect()
////                EventServiceImpl.getInstance().onStopTyping()
//                EventServiceImpl.EVENT_NEW_MESSAGE_LISTENER = "dfgdfgdf"
//                MainActivity.mainActivity.replaceFragment(HomeFragment(), AppConstants.HOMESCREEN)
                finish()
            }
            R.id.iv_send -> {

                if (et_message!!.text.toString().trim { it <= ' ' }.isEmpty()) {
                    showAlert(getString(R.string.message_should_not_empty))
                } else {
//                    if (driverRideStatus != null) {
                        val myObject = JSONObject()
                        try {
                            myObject.put(DRIVER_ID, getUserId())
                            myObject.put("userId", userID)
                            myObject.put(BOOKING_ID, bookingID)
                            myObject.put(ADMIN_ID, getAdminId())
                            myObject.put(MESSAGE, et_message!!.text.toString())
                            myObject.put(MESSAGE_BY, "1")
                            myObject.put(MY_CHAT_ID,userID + getUserId()
                            )
                            myObject.put(OPPONENT_ID,  getUserId() + userID
                            )
                            sendObjectToSocket(myObject, SENDMESSAGE)
                            et_message!!.setText("")

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }
//                }
            }
        }
    }


    override fun onResume() {
        super.onResume()

        ChatPresenter(
            this, this,
            Injection.provideSchedulerProvider(),
            Injection.providesSocketRepository(this)
        )
        if (!userName.isEmpty())
        {
            val myObject = JSONObject()
            EventServiceImpl.EVENT_NEW_MESSAGE_LISTENER = userID +  getUserId()

            myObject.put(MY_CHAT_ID,  EventServiceImpl.EVENT_NEW_MESSAGE_LISTENER)

            sendObjectToSocket(myObject, EventServiceImpl.EVENT_NEW_MESSAGE_LISTENER)
        }
//        if (driverRideStatus != null) {
//            EventServiceImpl.EVENT_NEW_MESSAGE_LISTENER = driverRideStatus!!.booking.userId.toString() + driverRideStatus!!.booking.driverId
//            try {
//                EventServiceImpl.getInstance().connect(MainActivity.mainActivity.getUserId())
//            } catch (e: URISyntaxException) {
//                e.printStackTrace()
//            }
//        }
    }

    override fun onPause() {
        super.onPause()
        mPresenter!!.unsubscribe()
//        try {
//            EventServiceImpl.getInstance().onStopTyping()
//        }catch (ex:java.lang.Exception)
//        {
//            ex.printStackTrace()
//        }

    }

    override fun onDestroy() {
        super.onDestroy()
//
//        try {
//            EventServiceImpl.getInstance().onStopTyping()
//        }catch (ex:java.lang.Exception)
//        {
//            ex.printStackTrace()
//        }

    }

    override fun showAlert(message: String?, isTrue: Boolean?) {

    }

    override fun updateUsername(username: String?) {

    }

    override fun onConnect(vararg args: Any?) {

    }

    override fun onDisconnect(vararg args: Any?) {

    }

    override fun onConnectError(vararg args: Any?) {

    }

    override fun onConnectTimeout(vararg args: Any?) {

    }

    override fun onNewMessage(vararg args: Any?) {
        Log.e("onNewMessage",args[0].toString())
        var jsonObject:JSONObject= args[0] as JSONObject
        if (jsonObject.getString("success").toString().equals(TRUE))
        {
            val data:String=jsonObject.getJSONObject("msg").toString()
            val gson1 =  Gson();
            val loginResponse = gson1.fromJson(data, MessageList::class.java)

                runOnUiThread(object:Runnable {
                    public override fun run() {
                        // change UI elements here
                       onGetNewMessage(loginResponse)

                    }
                })
        }
        else
        {
            showAlert(jsonObject.getString("message").toString())
        }
    }

    override fun onUserJoined(vararg args: Any?) {

    }

    override fun onTyping(vararg args: Any?) {

    }

    override fun onStopTyping(vararg args: Any?) {

    }

    override fun onBookingRequest(vararg args: Any?) {

    }

    override fun onMessageDelivered(chatMessage: JSONObject?) {

    }

    override fun hideProgress() {

    }

    override fun onUserLeft(vararg args: Any?) {

    }

    override fun onUpdateDriverLatitudeLongitude(vararg args: Any?) {

    }

    override fun onGetDriverRideStatus(vararg args: Any?) {

    }

    override fun onBookingRequestResponse(vararg args: Any?) {

    }

    override fun onDriverTracking(vararg args: Any?) {

    }

    override fun onDriverChangeRideStatus(vararg args: Any?) {

    }

    override fun onPaidAmount(vararg args: Any?) {

    }

    override fun initView() {

    }

    override fun setPresenter(presenter: SocketContract.Presenter?) {
        mPresenter = presenter
    }

    override fun hideAlert() {

    }

    override fun showProgress() {

    }

    override fun onChangeStatus(vararg args: Any?) {

    }

    fun sendObjectToSocket(jsonObject: JSONObject,type:String)
    {
        mPresenter!!.sendParamtoSocket(jsonObject,type)
    }
}
