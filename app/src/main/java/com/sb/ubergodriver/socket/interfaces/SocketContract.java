package com.sb.ubergodriver.socket.interfaces;

import com.sb.ubergodriver.model.chat.ChatMessage;
import com.sb.ubergodriver.socket.interfaces.BasePresenter;
import com.sb.ubergodriver.socket.interfaces.BaseView;
import com.sb.ubergodriver.socket.interfaces.eventservice.EventListener;
import org.json.JSONObject;

public interface SocketContract {

    interface View extends BaseView<Presenter>, EventListener {

        void onMessageDelivered(JSONObject chatMessage);

        void updateUsername(String username);
    }

    interface Presenter extends BasePresenter, EventListener {

        void sendParamtoSocket(JSONObject params,String type);

        void socketDisConnect(String type);

        void changeUsername(String username);

        void onTyping();

        void onStopTyping();
    }

}
