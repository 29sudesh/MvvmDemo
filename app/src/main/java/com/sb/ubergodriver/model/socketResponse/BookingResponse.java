package com.sb.ubergodriver.model.socketResponse;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.reactivex.annotations.Nullable;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class BookingResponse implements Parcelable
{

    @Nullable
    @SerializedName("source")
    @Expose
    private BookingSource source;
    @Nullable
    @SerializedName("destination")
    @Expose
    private BookingDestination destination;
    @Nullable
    @SerializedName("stopPoints")
    @Expose
    private List<BookingSource> stopPoints = null;
    @Nullable
    @SerializedName("adminId")
    @Expose
    private String adminId;
    @Nullable
    @SerializedName("userId")
    @Expose
    private String userId;
    @Nullable
    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;
    @Nullable
    @SerializedName("triptime")
    @Expose
    private String triptime;
    @Nullable
    @SerializedName("fare")
    @Expose
    private Long fare;
    @Nullable
    @SerializedName("paymentMode")
    @Expose
    private String paymentMode;
    @Nullable
    @SerializedName("driverId")
    @Expose
    private String driverId;
    @Nullable
    @SerializedName("userRating")
    @Expose
    private Long userRating;
    @Nullable
    @SerializedName("date")
    @Expose
    private Long date;
    @Nullable
    @SerializedName("socketId")
    @Expose
    private String socketId;
    @Nullable
    @SerializedName("notiType")
    @Expose
    private Long notiType;
    public final static Parcelable.Creator<BookingResponse> CREATOR = new Creator<BookingResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public BookingResponse createFromParcel(Parcel in) {
            return new BookingResponse(in);
        }

        public BookingResponse[] newArray(int size) {
            return (new BookingResponse[size]);
        }

    }
            ;

    protected BookingResponse(Parcel in) {
        this.source = ((BookingSource) in.readValue((BookingSource.class.getClassLoader())));
        this.destination = ((BookingDestination) in.readValue((BookingDestination.class.getClassLoader())));
        in.readList(this.stopPoints, (java.lang.Object.class.getClassLoader()));
        this.adminId = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.vehicleType = ((String) in.readValue((String.class.getClassLoader())));
        this.fare = ((Long) in.readValue((Long.class.getClassLoader())));
        this.paymentMode = ((String) in.readValue((String.class.getClassLoader())));
        this.driverId = ((String) in.readValue((String.class.getClassLoader())));
        this.userRating = ((Long) in.readValue((Long.class.getClassLoader())));
        this.date = ((Long) in.readValue((Long.class.getClassLoader())));
        this.socketId = ((String) in.readValue((String.class.getClassLoader())));
        this.triptime = ((String) in.readValue((String.class.getClassLoader())));
        this.notiType = ((Long) in.readValue((Long.class.getClassLoader())));
    }

    public BookingResponse() {
    }

    public BookingSource getSource() {
        return source;
    }

    public void setSource(BookingSource source) {
        this.source = source;
    }

    public BookingDestination getDestination() {
        return destination;
    }

    public void setDestination(BookingDestination destination) {
        this.destination = destination;
    }

    public List<BookingSource> getStopPoints() {
        return stopPoints;
    }

    public void setStopPoints(List<BookingSource> stopPoints) {
        this.stopPoints = stopPoints;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Long getFare() {
        return fare;
    }

    public void setFare(Long fare) {
        this.fare = fare;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public Long getUserRating() {
        return userRating;
    }

    public void setUserRating(Long userRating) {
        this.userRating = userRating;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    public Long getNotiType() {
        return notiType;
    }

    public void setNotiType(Long notiType) {
        this.notiType = notiType;
    }

    public void setTriptime(String triptime) {
        this.triptime = triptime;
    }

    public String getTriptime() {
        return triptime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("source", source).append("destination", destination).append("stopPoints", stopPoints).append("adminId", adminId).append("userId", userId).append("vehicleType", vehicleType).append("fare", fare).append("paymentMode", paymentMode).append("driverId", driverId).append("userRating", userRating).append("date", date).append("socketId", socketId).append("notiType", notiType).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(source);
        dest.writeValue(destination);
        dest.writeList(stopPoints);
        dest.writeValue(adminId);
        dest.writeValue(userId);
        dest.writeValue(vehicleType);
        dest.writeValue(fare);
        dest.writeValue(paymentMode);
        dest.writeValue(driverId);
        dest.writeValue(userRating);
        dest.writeValue(date);
        dest.writeValue(socketId);
        dest.writeValue(notiType);
        dest.writeValue(triptime);
    }

    public int describeContents() {
        return 0;
    }

}
