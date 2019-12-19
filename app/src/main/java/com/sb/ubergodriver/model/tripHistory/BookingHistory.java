package com.sb.ubergodriver.model.tripHistory;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sb.ubergodriver.model.socketResponse.Destination;
import com.sb.ubergodriver.model.socketResponse.Source;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class BookingHistory implements Parcelable
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("driverId")
    @Expose
    private String driverId;
    @SerializedName("vehicleType")
    @Expose
    private VehicleType vehicleType;
    @SerializedName("fare")
    @Expose
    private Long fare;
    @SerializedName("paymentMode")
    @Expose
    private String paymentMode;
    @SerializedName("date")
    @Expose
    private Long date;
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
    public final static Parcelable.Creator<BookingHistory> CREATOR = new Creator<BookingHistory>() {


        @SuppressWarnings({
                "unchecked"
        })
        public BookingHistory createFromParcel(Parcel in) {
            return new BookingHistory(in);
        }

        public BookingHistory[] newArray(int size) {
            return (new BookingHistory[size]);
        }

    }
            ;

    protected BookingHistory(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.driverId = ((String) in.readValue((String.class.getClassLoader())));
        this.vehicleType = ((VehicleType) in.readValue((VehicleType.class.getClassLoader())));
        this.fare = ((Long) in.readValue((Long.class.getClassLoader())));
        this.paymentMode = ((String) in.readValue((String.class.getClassLoader())));
        this.date = ((Long) in.readValue((Long.class.getClassLoader())));
        this.v = ((Long) in.readValue((Long.class.getClassLoader())));
        this.status = ((Long) in.readValue((Long.class.getClassLoader())));
        this.tripEndTime = ((Long) in.readValue((Long.class.getClassLoader())));
        this.tripStartTime = ((Long) in.readValue((Long.class.getClassLoader())));
        this.isPaid = ((Long) in.readValue((Long.class.getClassLoader())));
        this.tax = ((Long) in.readValue((Long.class.getClassLoader())));
        in.readList(this.stopPoints, (java.lang.Object.class.getClassLoader()));
        this.destination = ((Destination) in.readValue((Destination.class.getClassLoader())));
        this.source = ((Source) in.readValue((Source.class.getClassLoader())));
    }

    public BookingHistory() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
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

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
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
        return new ToStringBuilder(this).append("id", id).append("userId", userId).append("driverId", driverId).append("vehicleType", vehicleType).append("fare", fare).append("paymentMode", paymentMode).append("date", date).append("v", v).append("status", status).append("tripEndTime", tripEndTime).append("tripStartTime", tripStartTime).append("isPaid", isPaid).append("tax", tax).append("stopPoints", stopPoints).append("destination", destination).append("source", source).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(userId);
        dest.writeValue(driverId);
        dest.writeValue(vehicleType);
        dest.writeValue(fare);
        dest.writeValue(paymentMode);
        dest.writeValue(date);
        dest.writeValue(v);
        dest.writeValue(status);
        dest.writeValue(tripEndTime);
        dest.writeValue(tripStartTime);
        dest.writeValue(isPaid);
        dest.writeValue(tax);
        dest.writeList(stopPoints);
        dest.writeValue(destination);
        dest.writeValue(source);
    }

    public int describeContents() {
        return 0;
    }

}
