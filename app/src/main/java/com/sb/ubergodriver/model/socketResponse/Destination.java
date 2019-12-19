package com.sb.ubergodriver.model.socketResponse;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Destination implements Parcelable
{

    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("name")
    @Expose
    private String name;
    public final static Parcelable.Creator<Destination> CREATOR = new Creator<Destination>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Destination createFromParcel(Parcel in) {
            return new Destination(in);
        }

        public Destination[] newArray(int size) {
            return (new Destination[size]);
        }

    }
            ;

    protected Destination(Parcel in) {
        this.longitude = ((String) in.readValue((String.class.getClassLoader())));
        this.latitude = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Destination() {
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("longitude", longitude).append("latitude", latitude).append("name", name).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(longitude);
        dest.writeValue(latitude);
        dest.writeValue(name);
    }

    public int describeContents() {
        return 0;
    }

}
