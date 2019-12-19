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



import com.sb.ubergodriver.model.chat.ChatMessage;
import com.sb.ubergodriver.socket.interfaces.SocketContract;
import io.reactivex.Flowable;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Service layer that connects/disconnects to the server and
 * sends and receives events too.
 */
public interface EventService  {

    void connect(String user_id) throws URISyntaxException;

    void disconnect();

    void setEventListener(EventListener listener);

    Flowable<JSONObject> sendMessage(JSONObject chatMessage,String type);

    void onTyping();

    void onStopTyping();

    void onUpdateDriverLatitudeLongitude();

    void onBookingRequest();

    void onBookingResponse();

    void onTracking();

    void ongetRideStatus();

    void onChangeRideStatus();

}
