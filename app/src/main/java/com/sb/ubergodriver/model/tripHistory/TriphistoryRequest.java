package com.sb.ubergodriver.model.tripHistory;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class TriphistoryRequest implements Parcelable
{

    @SerializedName("driverId")
    @Expose
    private String driverId;
    @SerializedName("skip")
    @Expose
    private int skip;
    @SerializedName("type")
    @Expose
    private int type;
    public final static Parcelable.Creator<TriphistoryRequest> CREATOR = new Creator<TriphistoryRequest>() {


        @SuppressWarnings({
                "unchecked"
        })
        public TriphistoryRequest createFromParcel(Parcel in) {
            return new TriphistoryRequest(in);
        }

        public TriphistoryRequest[] newArray(int size) {
            return (new TriphistoryRequest[size]);
        }

    }
            ;

    protected TriphistoryRequest(Parcel in) {
        this.driverId = ((String) in.readValue((String.class.getClassLoader())));
        this.skip = ((int) in.readValue((int.class.getClassLoader())));
        this.type = ((int) in.readValue((int.class.getClassLoader())));
    }

    public TriphistoryRequest() {
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("driverId", driverId).append("skip", skip).append("type", type).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(driverId);
        dest.writeValue(skip);
        dest.writeValue(type);
    }

    public int describeContents() {
        return 0;
    }

}