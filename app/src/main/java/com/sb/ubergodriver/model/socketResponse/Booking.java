package com.sb.ubergodriver.model.socketResponse;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.reactivex.annotations.Nullable;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Booking implements Parcelable
{
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("userId")
    @Expose
    private UserId userId;
    @SerializedName("driverId")
    @Expose
    private String driverId;
    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;
    @SerializedName("fare")
    @Expose
    private Long fare;
    @SerializedName("paymentMode")
    @Expose
    private String paymentMode;

    @Nullable
    @SerializedName("userRating")
    @Expose
    private String userRating;
    @SerializedName("__v")
    @Expose
    private Long v;
    @SerializedName("status")
    @Expose
    private Long status;
    @SerializedName("tripEndTime")
    @Expose
    private Long tripEndTime;
    @SerializedName("tripStartTime")
    @Expose
    private Long tripStartTime;
    @SerializedName("isPaid")
    @Expose
    private Long isPaid;
    @SerializedName("tax")
    @Expose
    private Long tax;
    @SerializedName("stopPoints")
    @Expose
    private List<Object> stopPoints = null;
    @SerializedName("destination")
    @Expose
    private Destination destination;
    @SerializedName("source")
    @Expose
    private Source source;
    public final static Parcelable.Creator<Booking> CREATOR = new Creator<Booking>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Booking createFromParcel(Parcel in) {
            return new Booking(in);
        }

        public Booking[] newArray(int size) {
            return (new Booking[size]);
        }

    }
            ;

    protected Booking(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((UserId) in.readValue((UserId.class.getClassLoader())));
        this.driverId = ((String) in.readValue((String.class.getClassLoader())));
        this.vehicleType = ((String) in.readValue((String.class.getClassLoader())));
        this.fare = ((Long) in.readValue((Long.class.getClassLoader())));
        this.paymentMode = ((String) in.readValue((String.class.getClassLoader())));
        this.v = ((Long) in.readValue((Long.class.getClassLoader())));
        this.status = ((Long) in.readValue((Long.class.getClassLoader())));
        this.tripEndTime = ((Long) in.readValue((Long.class.getClassLoader())));
        this.tripStartTime = ((Long) in.readValue((Long.class.getClassLoader())));
        this.isPaid = ((Long) in.readValue((Long.class.getClassLoader())));
        this.tax = ((Long) in.readValue((Long.class.getClassLoader())));
        in.readList(this.stopPoints, (java.lang.Object.class.getClassLoader()));
        this.destination = ((Destination) in.readValue((Destination.class.getClassLoader())));
        this.source = ((Source) in.readValue((Source.class.getClassLoader())));
        this.userRating=((String)in.readValue(String.class.getClassLoader()));
    }

    public Booking() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getUserRating() {
        return userRating;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
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

    public Long getV() {
        return v;
    }

    public void setV(Long v) {
        this.v = v;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getTripEndTime() {
        return tripEndTime;
    }

    public void setTripEndTime(Long tripEndTime) {
        this.tripEndTime = tripEndTime;
    }

    public Long getTripStartTime() {
        return tripStartTime;
    }

    public void setTripStartTime(Long tripStartTime) {
        this.tripStartTime = tripStartTime;
    }

    public Long getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Long isPaid) {
        this.isPaid = isPaid;
    }

    public Long getTax() {
        return tax;
    }

    public void setTax(Long tax) {
        this.tax = tax;
    }

    public List<Object> getStopPoints() {
        return stopPoints;
    }

    public void setStopPoints(List<Object> stopPoints) {
        this.stopPoints = stopPoints;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("userId", userId).append("driverId", driverId).append("vehicleType", vehicleType).append("fare", fare).append("paymentMode", paymentMode).append("v", v).append("status", status).append("tripEndTime", tripEndTime).append("tripStartTime", tripStartTime).append("isPaid", isPaid).append("tax", tax).append("stopPoints", stopPoints).append("destination", destination).append("source", source).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(userId);
        dest.writeValue(driverId);
        dest.writeValue(vehicleType);
        dest.writeValue(fare);
        dest.writeValue(paymentMode);
        dest.writeValue(v);
        dest.writeValue(status);
        dest.writeValue(tripEndTime);
        dest.writeValue(tripStartTime);
        dest.writeValue(isPaid);
        dest.writeValue(tax);
        dest.writeList(stopPoints);
        dest.writeValue(destination);
        dest.writeValue(source);
        dest.writeValue(userRating);
    }

    public int describeContents() {
        return 0;
    }

}