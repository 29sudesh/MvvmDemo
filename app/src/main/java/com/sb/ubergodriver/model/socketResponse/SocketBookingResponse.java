package com.sb.ubergodriver.model.socketResponse;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SocketBookingResponse implements Parcelable
{

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("booking")
    @Expose
    private BookingResponse booking;
    public final static Parcelable.Creator<SocketBookingResponse> CREATOR = new Creator<SocketBookingResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public SocketBookingResponse createFromParcel(Parcel in) {
            return new SocketBookingResponse(in);
        }

        public SocketBookingResponse[] newArray(int size) {
            return (new SocketBookingResponse[size]);
        }

    }
            ;

    protected SocketBookingResponse(Parcel in) {
        this.success = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.booking = ((BookingResponse) in.readValue((BookingResponse.class.getClassLoader())));
    }

    public SocketBookingResponse() {
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public BookingResponse getBooking() {
        return booking;
    }

    public void setBooking(BookingResponse booking) {
        this.booking = booking;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("success", success).append("booking", booking).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(booking);
    }

    public int describeContents() {
        return 0;
    }

}