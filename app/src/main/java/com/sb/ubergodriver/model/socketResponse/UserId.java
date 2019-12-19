package com.sb.ubergodriver.model.socketResponse;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class UserId implements Parcelable
{

    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("inviteCode")
    @Expose
    private String inviteCode;
    @SerializedName("inviteCodeUsed")
    @Expose
    private String inviteCodeUsed;
    @SerializedName("__v")
    @Expose
    private Long v;
    @SerializedName("isFullUrl")
    @Expose
    private Long isFullUrl;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("ratings")
    @Expose
    private Object ratings;
    @SerializedName("id")
    @Expose
    private String id;
    public final static Parcelable.Creator<UserId> CREATOR = new Creator<UserId>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UserId createFromParcel(Parcel in) {
            return new UserId(in);
        }

        public UserId[] newArray(int size) {
            return (new UserId[size]);
        }

    }
            ;

    protected UserId(Parcel in) {
        this.updatedAt = ((String) in.readValue((String.class.getClassLoader())));
        this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
        this.firstName = ((String) in.readValue((String.class.getClassLoader())));
        this.lastName = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.inviteCode = ((String) in.readValue((String.class.getClassLoader())));
        this.inviteCodeUsed = ((String) in.readValue((String.class.getClassLoader())));
        this.v = ((Long) in.readValue((Long.class.getClassLoader())));
        this.isFullUrl = ((Long) in.readValue((Long.class.getClassLoader())));
        this.profilePic = ((String) in.readValue((String.class.getClassLoader())));
        this.ratings = ((Object) in.readValue((Object.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
    }

    public UserId() {
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getInviteCodeUsed() {
        return inviteCodeUsed;
    }

    public void setInviteCodeUsed(String inviteCodeUsed) {
        this.inviteCodeUsed = inviteCodeUsed;
    }

    public Long getV() {
        return v;
    }

    public void setV(Long v) {
        this.v = v;
    }

    public Long getIsFullUrl() {
        return isFullUrl;
    }

    public void setIsFullUrl(Long isFullUrl) {
        this.isFullUrl = isFullUrl;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public Object getRatings() {
        return ratings;
    }

    public void setRatings(Object ratings) {
        this.ratings = ratings;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("updatedAt", updatedAt).append("createdAt", createdAt).append("firstName", firstName).append("lastName", lastName).append("email", email).append("inviteCode", inviteCode).append("inviteCodeUsed", inviteCodeUsed).append("v", v).append("isFullUrl", isFullUrl).append("profilePic", profilePic).append("ratings", ratings).append("id", id).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(updatedAt);
        dest.writeValue(createdAt);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(email);
        dest.writeValue(inviteCode);
        dest.writeValue(inviteCodeUsed);
        dest.writeValue(v);
        dest.writeValue(isFullUrl);
        dest.writeValue(profilePic);
        dest.writeValue(ratings);
        dest.writeValue(id);
    }

    public int describeContents() {
        return 0;
    }

}
