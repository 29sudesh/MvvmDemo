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

package com.sb.ubergodriver.socket.interfaces.local;

import com.sb.ubergodriver.model.chat.ChatMessage;
import com.sb.ubergodriver.socket.interfaces.DataSource;
import com.sb.ubergodriver.socket.interfaces.eventservice.EventListener;
import io.reactivex.Flowable;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Local data source.
 */
public class LocalDataSource implements DataSource {

    private static LocalDataSource INSTANCE;

    public static LocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LocalDataSource();
        }

        return INSTANCE;
    }

    @Override
    public void onConnect(Object... args) {

    }

    @Override
    public void onDisconnect(Object... args) {

    }

    @Override
    public void onConnectError(Object... args) {

    }

    @Override
    public void onConnectTimeout(Object... args) {

    }

    @Override
    public void onNewMessage(Object... args) {

    }

    @Override
    public void onUserJoined(Object... args) {

    }

    @Override
    public void onUserLeft(Object... args) {

    }

    @Override
    public void onTyping(Object... args) {

    }

    @Override
    public void onStopTyping(Object... args) {

    }

    @Override
    public void onBookingRequest(Object... args) {

    }

    @Override
    public void onUpdateDriverLatitudeLongitude(Object... args) {

    }

    @Override
    public void onGetDriverRideStatus(Object... args) {

    }

    @Override
    public void onBookingRequestResponse(Object... args) {

    }

    @Override
    public void onDriverTracking(Object... args) {

    }

    @Override
    public void onDriverChangeRideStatus(Object... args) {

    }

    @Override
    public void onPaidAmount(Object... args) {

    }

    @Override
    public void onChangeStatus(Object... args) {

    }

    @Override
    public void setEventListener(EventListener eventListener) {

    }

    @Override
    public Flowable<JSONObject> sendMessage(JSONObject chatMessage, String type) {
        return null;
    }

    @Override
    public void onTyping() {

    }

    @Override
    public void onStopTyping() {

    }

    @Override
    public void connect(String user_id) throws URISyntaxException {

    }

    @Override
    public void disconnect() {

    }
}
