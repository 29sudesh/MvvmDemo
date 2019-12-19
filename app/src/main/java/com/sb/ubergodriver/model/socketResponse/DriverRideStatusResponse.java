package com.sb.ubergodriver.model.socketResponse;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class DriverRideStatusResponse implements Parcelable
{

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("booking")
    @Expose
    private Booking booking;
    @SerializedName("status")
    @Expose
    private int status;
    public final static Parcelable.Creator<DriverRideStatusResponse> CREATOR = new Creator<DriverRideStatusResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DriverRideStatusResponse createFromParcel(Parcel in) {
            return new DriverRideStatusResponse(in);
        }

        public DriverRideStatusResponse[] newArray(int size) {
            return (new DriverRideStatusResponse[size]);
        }

    }
            ;

    protected DriverRideStatusResponse(Parcel in) {
        this.success = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.booking = ((Booking) in.readValue((Booking.class.getClassLoader())));
        this.status = ((int) in.readValue((int.class.getClassLoader())));
    }

    public DriverRideStatusResponse() {
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("success", success).append("booking", booking).append("status", status).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(booking);
        dest.writeValue(status);
    }

    public int describeContents() {
        return 0;
    }

}