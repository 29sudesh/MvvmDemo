/*
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

package com.sb.ubergodriver.socket.interfaces.remote;

import com.sb.ubergodriver.model.chat.ChatMessage;
import com.sb.ubergodriver.socket.interfaces.DataSource;
import com.sb.ubergodriver.socket.interfaces.eventservice.EventListener;
import com.sb.ubergodriver.socket.interfaces.eventservice.EventService;
import com.sb.ubergodriver.socket.interfaces.eventservice.EventServiceImpl;
import io.reactivex.Flowable;
import org.json.JSONObject;


import java.net.URISyntaxException;

/**
 * Remote data source.
 *
 */
public class RemoteDataSource implements DataSource {
    private static RemoteDataSource INSTANCE;
    private static EventService mEventService = EventServiceImpl.getInstance();
    private EventListener mRepoEventListener;
    private RemoteDataSource() {
        mEventService.setEventListener(this);
    }
    public static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource();
        }

        return INSTANCE;
    }

    @Override
    public void onConnect(Object... args) {
        if (mRepoEventListener != null)
            mRepoEventListener.onConnect(args);
    }

    @Override
    public void onDisconnect(Object... args) {
        if (mRepoEventListener != null)
            mRepoEventListener.onDisconnect(args);
    }

    @Override
    public void onConnectError(Object... args) {
        if (mRepoEventListener != null)
            mRepoEventListener.onConnectError(args);
    }

    @Override
    public void onConnectTimeout(Object... args) {
        if (mRepoEventListener != null)
            mRepoEventListener.onConnectTimeout(args);
    }

    @Override
    public void onNewMessage(Object... args) {
        if (mRepoEventListener != null)
            mRepoEventListener.onNewMessage(args);
    }

    @Override
    public void onUserJoined(Object... args) {
        if (mRepoEventListener != null)
            mRepoEventListener.onUserJoined(args);
    }

    @Override
    public void onUserLeft(Object... args) {
        if (mRepoEventListener != null)
            mRepoEventListener.onUserLeft(args);
    }

    @Override
    public void onTyping(Object... args) {
        if (mRepoEventListener != null)
            mRepoEventListener.onTyping(args);
    }

    @Override
    public void onStopTyping(Object... args) {
        if (mRepoEventListener != null)
            mRepoEventListener.onStopTyping(args);
    }

    @Override
    public void onBookingRequest(Object... args) {
        if (mRepoEventListener != null)
            mRepoEventListener.onBookingRequest(args);
    }

    @Override
    public void onUpdateDriverLatitudeLongitude(Object... args) {
        if (mRepoEventListener != null)
            mRepoEventListener.onUpdateDriverLatitudeLongitude(args);
    }

    @Override
    public void onGetDriverRideStatus(Object... args) {
        if (mRepoEventListener != null)
            mRepoEventListener.onGetDriverRideStatus(args);
    }

    @Override
    public void onBookingRequestResponse(Object... args) {
        if (mRepoEventListener != null)
            mRepoEventListener.onBookingRequestResponse(args);
    }

    @Override
    public void onDriverTracking(Object... args) {
        if (mRepoEventListener != null)
            mRepoEventListener.onDriverTracking(args);
    }

    @Override
    public void onDriverChangeRideStatus(Object... args) {
        if (mRepoEventListener != null)
            mRepoEventListener.onDriverChangeRideStatus(args);
    }

    @Override
    public void onPaidAmount(Object... args) {
        if (mRepoEventListener != null)
            mRepoEventListener.onPaidAmount(args);
    }

    @Override
    public void onChangeStatus(Object... args) {
        if (mRepoEventListener != null)
            mRepoEventListener.onChangeStatus(args);
    }

    @Override
    public void setEventListener(EventListener eventListener) {
        mRepoEventListener = eventListener;
    }


    @Override
    public void onTyping() {
        mEventService.onTyping();
    }

    @Override
    public void onStopTyping() {
        mEventService.onStopTyping();
    }

    @Override
    public void connect(String user_id) throws URISyntaxException {
        mEventService.connect(user_id);
    }

    @Override
    public void disconnect() {
        mEventService.disconnect();
    }

    @Override
    public Flowable<JSONObject> sendMessage(JSONObject chatMessage,String type) {
        return mEventService.sendMessage(chatMessage,type);
    }
}
