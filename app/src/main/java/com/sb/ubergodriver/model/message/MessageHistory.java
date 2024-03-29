package com.sb.ubergodriver.model.message;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class MessageHistory implements Parcelable
{

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("messageList")
    @Expose
    private ArrayList<MessageList> messageList = null;
    public final static Parcelable.Creator<MessageHistory> CREATOR = new Creator<MessageHistory>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MessageHistory createFromParcel(Parcel in) {
            return new MessageHistory(in);
        }

        public MessageHistory[] newArray(int size) {
            return (new MessageHistory[size]);
        }

    }
            ;

    protected MessageHistory(Parcel in) {
        this.success = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        in.readList(this.messageList, (MessageList.class.getClassLoader()));
    }

    public MessageHistory() {
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public ArrayList<MessageList> getMessageList() {
        return messageList;
    }

    public void setMessageList(ArrayList<MessageList> messageList) {
        this.messageList = messageList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("success", success).append("messageList", messageList).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeList(messageList);
    }

    public int describeContents() {
        return 0;
    }

}
