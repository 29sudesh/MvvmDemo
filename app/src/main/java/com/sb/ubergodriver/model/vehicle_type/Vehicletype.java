package com.sb.ubergodriver.model.vehicle_type;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Vehicletype implements Parcelable
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("fareRate")
    @Expose
    private Long fareRate;
    @SerializedName("baseFare")
    @Expose
    private Long baseFare;
    @SerializedName("persons")
    @Expose
    private Long persons;
    @SerializedName("__v")
    @Expose
    private Long v;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("fareRateAfter")
    @Expose
    private Long fareRateAfter;
    public final static Parcelable.Creator<Vehicletype> CREATOR = new Creator<Vehicletype>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Vehicletype createFromParcel(Parcel in) {
            return new Vehicletype(in);
        }

        public Vehicletype[] newArray(int size) {
            return (new Vehicletype[size]);
        }

    }
            ;

    protected Vehicletype(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.fareRate = ((Long) in.readValue((Long.class.getClassLoader())));
        this.baseFare = ((Long) in.readValue((Long.class.getClassLoader())));
        this.persons = ((Long) in.readValue((Long.class.getClassLoader())));
        this.v = ((Long) in.readValue((Long.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
        this.fareRateAfter = ((Long) in.readValue((Long.class.getClassLoader())));
    }

    public Vehicletype() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFareRate() {
        return fareRate;
    }

    public void setFareRate(Long fareRate) {
        this.fareRate = fareRate;
    }

    public Long getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(Long baseFare) {
        this.baseFare = baseFare;
    }

    public Long getPersons() {
        return persons;
    }

    public void setPersons(Long persons) {
        this.persons = persons;
    }

    public Long getV() {
        return v;
    }

    public void setV(Long v) {
        this.v = v;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getFareRateAfter() {
        return fareRateAfter;
    }

    public void setFareRateAfter(Long fareRateAfter) {
        this.fareRateAfter = fareRateAfter;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("name", name).append("fareRate", fareRate).append("baseFare", baseFare).append("persons", persons).append("v", v).append("image", image).append("fareRateAfter", fareRateAfter).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(fareRate);
        dest.writeValue(baseFare);
        dest.writeValue(persons);
        dest.writeValue(v);
        dest.writeValue(image);
        dest.writeValue(fareRateAfter);
    }

    public int describeContents() {
        return 0;
    }

}
