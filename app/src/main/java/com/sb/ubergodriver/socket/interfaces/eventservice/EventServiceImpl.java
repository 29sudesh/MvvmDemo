package com.sb.ubergodriver.socket.interfaces.eventservice;/*
 * Copyright 2018 Mayur Rokade
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */



import android.support.annotation.NonNull;
import android.util.Log;
import com.sb.ubergodriver.CommonUtils.AppConstants;
import com.sb.ubergodriver.model.chat.ChatMessage;
import com.sb.ubergodriver.view.activity.MainActivity;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject;

import java.net.URISyntaxException;

import static com.sb.ubergodriver.CommonUtils.AppConstants.CHATSCREEN;
import static com.sb.ubergodriver.CommonUtils.AppConstants.STATICUSERID;
import static com.sb.ubergodriver.CommonUtils.AppConstants.TRACKING;

/**
 * Implementation of {@link } which connects and disconnects to the server.
 * It also sends and receives events from the server.
 */
public class EventServiceImpl implements EventService {

    private static final String TAG = EventServiceImpl.class.getSimpleName();
    private static final String SOCKET_URL = "http://13.127.113.203:8000";
    private static final String EVENT_CONNECT = Socket.EVENT_CONNECT;
    private static final String EVENT_DISCONNECT = Socket.EVENT_DISCONNECT;
    private static final String EVENT_CONNECT_ERROR = Socket.EVENT_CONNECT_ERROR;
    private static final String EVENT_CONNECT_TIMEOUT = Socket.EVENT_CONNECT_TIMEOUT;
    private static final String EVENT_NEW_MESSAGE = "newmessage";
    private static final String EVENT_USER_JOINED = "user joined";
    private static final String EVENT_USER_LEFT = "user left";
    private static final String EVENT_TYPING = "typing";
    private static final String EVENT_STOP_TYPING = "stop typing";
    private static final String EVENT_UPDATE_DRIVER_LATITUDE_LONGITUDE = "updateLatLong";
    private static final String EVENT_DRIVER_RIDE_STATUS = "driverRideStatus";
    private static final String EVENT_BOOKING_REQUEST = "-booking";
    private static final String EVENT_BOOKING_REQUEST_RESPONSE = "bookingRequestResponse";
    private static final String EVENT_DRIVER_TRACKING ="tracking";
    public static  String EVENT_NEW_MESSAGE_LISTENER = "jhggjhghjj";
    private static final String EVENT_CHANGE_DRIVER_STATUS_MANNUALLY = "driverChangeStatus";
    private static final String EVENT_CHANGE_DRIVER_RIDE_STATUS = "-change";
    //    private static final String EVENT_CHANGE_DRIVER_RIDE_STATUS = "'-change'";
    private static EventService INSTANCE;
    private static EventListener mEventListener;
    private static Socket mSocket;
    public static int YES=0;
    public  static String EVENTSOCKETOFF="";


    // Prevent direct instantiation
    private EventServiceImpl() {}

    /**
     * Returns single instance of this class, creating it if necessary.
     *
     * @return
     */
    public static EventService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EventServiceImpl();
        }

        return INSTANCE;
    }

    /**
     * Connect to the server.
     *
     * @param
     * @throws URISyntaxException
     */
    @Override
    public void connect(String user_id) throws URISyntaxException {
        mSocket = IO.socket(SOCKET_URL);
        mSocket.off(EVENTSOCKETOFF, onNewMessage);
        mSocket.off(EVENTSOCKETOFF);
        try {
            STATICUSERID=user_id;
            // Register the incoming events and their listeners
            // on the socket.
            mSocket.on(EVENT_CONNECT, onConnect);
            mSocket.on(EVENT_DISCONNECT, onDisconnect);
            mSocket.on(EVENT_CONNECT_ERROR, onConnectError);
            mSocket.on(EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.on(EVENT_CHANGE_DRIVER_STATUS_MANNUALLY,onChangeStatus);
            mSocket.on(EVENT_UPDATE_DRIVER_LATITUDE_LONGITUDE, onUpdateDriverLocation);
            if (STATICUSERID!=null && !STATICUSERID.isEmpty()) {
                mSocket.on(STATICUSERID+EVENT_BOOKING_REQUEST, onBookinRequest);
                Log.e("STATICUSERID",STATICUSERID+EVENT_BOOKING_REQUEST);
            }
            mSocket.on(EVENT_BOOKING_REQUEST_RESPONSE, onBookinRequestResponse);
            mSocket.on(EVENT_DRIVER_RIDE_STATUS, onGetRideStatus);
            if (STATICUSERID!=null && !STATICUSERID.isEmpty()) {
                mSocket.off(STATICUSERID+EVENT_CHANGE_DRIVER_RIDE_STATUS, onChangeRideStatus);
                mSocket.on(STATICUSERID+EVENT_CHANGE_DRIVER_RIDE_STATUS, onChangeRideStatus);
                Log.e("onChangeRideStatus",STATICUSERID+EVENT_CHANGE_DRIVER_RIDE_STATUS);
            }

//        mSocket.on(EVENT_DRIVER_TRACKING, onTracking);

            mSocket.connect();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    /**
     * Disconnect from the server.
     *
     */
    @Override
    public void disconnect() {
        if (mSocket != null) mSocket.disconnect();
    }

    /**
     * Send chat message to the server.
     *
     * @param chatMessage
     * @return
     */
    @Override
    public Flowable<JSONObject> sendMessage(@NonNull final JSONObject chatMessage,final String type) {
        return Flowable.create(new FlowableOnSubscribe<JSONObject>() {
            @Override
            public void subscribe(FlowableEmitter<JSONObject> emitter) throws Exception {
                /*
                 * Socket.io supports acking messages.
                 * This feature can be used as
                 * mSocket.emit("EVENT_NEW_MESSAGE", chatMessage.getMessage(), new Ack() {
                 *   @Override
                 *   public void call(Object... args) {
                 *       // Do something with args
                 *
                 *       // On success
                 *       emitter.onNext(chatMessage);
                 *
                 *       // On error
                 *       emitter.onError(new Exception("Sending message failed."));
                 *    }
                 * });
                 *
                 * */
                try {
                    if (type.equals(AppConstants.UPDATEDRIVERLOCATION)) {
                        mSocket.emit(EVENT_UPDATE_DRIVER_LATITUDE_LONGITUDE, chatMessage);
                        emitter.onNext(chatMessage);
                    }
                    else if (type.equals(AppConstants.BOOKREQUESTRESPONSE)) {
                        mSocket.emit(EVENT_BOOKING_REQUEST_RESPONSE, chatMessage);
                        emitter.onNext(chatMessage);
                    }
                    else if (type.equals(AppConstants.RIDESTATUS)) {
                        mSocket.emit(EVENT_DRIVER_RIDE_STATUS, chatMessage);
                        emitter.onNext(chatMessage);
                    }
                    else if (type.equals(AppConstants.CHANGERIDESTATUS)) {
                        mSocket.emit(EVENT_CHANGE_DRIVER_RIDE_STATUS, chatMessage);
                        emitter.onNext(chatMessage);
                    }
                    else if (type.equals(AppConstants.TRACKING)) {
                        mSocket.emit(EVENT_DRIVER_TRACKING, chatMessage);
                        emitter.onNext(chatMessage);
                    }
                    else if (type.equals(AppConstants.SENDMESSAGE)) {
//                    Log.e("SENDMESSAGE :",chatMessage.toString());
                        mSocket.emit(EVENT_NEW_MESSAGE, chatMessage);
                        emitter.onNext(chatMessage);
//
//                        mSocket.off(EVENT_NEW_MESSAGE_LISTENER, onNewMessage);
//                        mSocket.on(EVENT_NEW_MESSAGE_LISTENER, onNewMessage);
                    }
                    else if (type.equals(AppConstants.CHANGEDRIVERSTATUS)) {
                        mSocket.emit(EVENT_CHANGE_DRIVER_STATUS_MANNUALLY, chatMessage);
                        emitter.onNext(chatMessage);
                    }
                    else if (type.equals(EVENT_NEW_MESSAGE_LISTENER))
                    {
                        mSocket.off(EVENT_NEW_MESSAGE_LISTENER, onNewMessage);
                        mSocket.on(EVENT_NEW_MESSAGE_LISTENER, onNewMessage);
                    }
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        }, BackpressureStrategy.BUFFER);
    }

    /**
     * Send typing event to the server.
     *
     */
    @Override
    public void onTyping() {
//        mSocket.on(EVENT_NEW_MESSAGE_LISTENER, onNewMessage);


//        mSocket.emit(EVENT_TYPING);
    }

    /**
     * Send stop typing event to the server.
     *
     */
    @Override
    public void onStopTyping() {
        mSocket.off(EVENT_NEW_MESSAGE_LISTENER, onNewMessage);
        Log.e("EVENT NEW MESSAGE : ", EVENT_NEW_MESSAGE_LISTENER);
//        mSocket.emit(EVENT_STOP_TYPING);
    }

    @Override
    public void onUpdateDriverLatitudeLongitude() {
        mSocket.emit(EVENT_UPDATE_DRIVER_LATITUDE_LONGITUDE);
    }

    @Override
    public void onBookingRequest() {
//        mSocket.emit(EVENT_BOOKING_REQUEST);
    }
    @Override
    public void onBookingResponse() {
        mSocket.emit(EVENT_BOOKING_REQUEST_RESPONSE);
    }

    @Override
    public void onTracking() {
        mSocket.emit(EVENT_DRIVER_TRACKING);
    }

    @Override
    public void ongetRideStatus() {
        mSocket.emit(EVENT_DRIVER_RIDE_STATUS);
    }

    @Override
    public void onChangeRideStatus() {
        mSocket.emit(EVENT_CHANGE_DRIVER_RIDE_STATUS);
    }

    /**
     * Set eventListener.
     *
     * When server sends events to the socket, those events are passed to the
     * RemoteDataSource -> SocketRepository -> Presenter -> View using EventListener.
     *
     * @param eventListener
     */
    @Override
    public void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "call: onConnect");
//            mSocket.emit("add user", ");
            if (mEventListener != null) mEventListener.onConnect(args);
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "call: onDisconnect");
            if (mEventListener != null) mEventListener.onDisconnect(args);
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "call: onConnectError");
            if (mEventListener != null) mEventListener.onConnectError(args);
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.i(TAG, "call: onNewMessage");
            if (mEventListener != null) mEventListener.onNewMessage(args);
        }
    };

    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.i(TAG, "call: onUserJoined");
            if (mEventListener != null) mEventListener.onUserJoined(args);
        }
    };

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.i(TAG, "call: onUserLeft");
            if (mEventListener != null) mEventListener.onUserLeft(args);
        }
    };

    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.i(TAG, "call: onTyping");
            if (mEventListener != null) mEventListener.onTyping(args);
        }
    };

    private Emitter.Listener onStopTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.i(TAG, "call: onStopTyping");
            if (mEventListener != null) mEventListener.onStopTyping(args);
        }
    };

    private Emitter.Listener onChangeStatus = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.i(TAG, "call: onChangeStatus");
            if (mEventListener != null) mEventListener.onChangeStatus(args);
        }
    };
    private Emitter.Listener onUpdateDriverLocation = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.i(TAG, "call: onUpdateDriverLocation");
            if (mEventListener != null) mEventListener.onUpdateDriverLatitudeLongitude(args);
        }
    };

    private Emitter.Listener onBookinRequest = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.i(TAG, "call: onBookinRequest");
            if (mEventListener != null) mEventListener.onBookingRequest(args);
        }
    };

    private Emitter.Listener onBookinRequestResponse = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.i(TAG, "call: onBookinRequestResponse");
            if (mEventListener != null) mEventListener.onBookingRequestResponse(args);
        }
    };


    private Emitter.Listener onTracking = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.i(TAG, "call: onTracking");
            if (mEventListener != null) mEventListener.onDriverTracking(args);
        }
    };

    private Emitter.Listener onGetRideStatus = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.i(TAG, "call: onGetRideStatus");
            if (mEventListener != null) mEventListener.onGetDriverRideStatus(args);
        }
    };

    private Emitter.Listener onChangeRideStatus = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.i(TAG, "call: onChangeRideStatus");
            if (mEventListener != null) mEventListener.onDriverChangeRideStatus(args);
        }
    };

}
