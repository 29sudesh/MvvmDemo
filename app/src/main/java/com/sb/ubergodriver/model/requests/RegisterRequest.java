package com.sb.ubergodriver.model.requests;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RegisterRequest implements Parcelable
{

    @SerializedName("adminId")
    @Expose
    private String adminId;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("contryCode")
    @Expose
    private String contryCode;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("deviceId")
    @Expose
    private String deviceId;
    @SerializedName("deviceType")
    @Expose
    private String deviceType;
    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;
    @SerializedName("vehicleNumber")
    @Expose
    private String vehicleNumber;
    @SerializedName("vehicleName")
    @Expose
    private String vehicleName;
    @SerializedName("vehicleColor")
    @Expose
    private String vehicleColor;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("manufacturerName")
    @Expose
    private String manufacturerName;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("isFullUrl")
    @Expose
    private Long isFullUrl;
    @SerializedName("isVerified")
    @Expose
    private Long isVerified;
    @SerializedName("inviteCodeUsed")
    @Expose
    private String inviteCodeUsed;
    public final static Parcelable.Creator<RegisterRequest> CREATOR = new Creator<RegisterRequest>() {


        @SuppressWarnings({
                "unchecked"
        })
        public RegisterRequest createFromParcel(Parcel in) {
            return new RegisterRequest(in);
        }

        public RegisterRequest[] newArray(int size) {
            return (new RegisterRequest[size]);
        }

    }
            ;

    protected RegisterRequest(Parcel in) {
        this.adminId = ((String) in.readValue((String.class.getClassLoader())));
        this.firstName = ((String) in.readValue((String.class.getClassLoader())));
        this.lastName = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.password = ((String) in.readValue((String.class.getClassLoader())));
        this.contryCode = ((String) in.readValue((String.class.getClassLoader())));
        this.phone = ((String) in.readValue((String.class.getClassLoader())));
        this.dob = ((String) in.readValue((String.class.getClassLoader())));
        this.gender = ((String) in.readValue((String.class.getClassLoader())));
        this.bio = ((String) in.readValue((String.class.getClassLoader())));
        this.deviceId = ((String) in.readValue((String.class.getClassLoader())));
        this.deviceType = ((String) in.readValue((String.class.getClassLoader())));
        this.vehicleType = ((String) in.readValue((String.class.getClassLoader())));
        this.vehicleNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.vehicleName = ((String) in.readValue((String.class.getClassLoader())));
        this.vehicleColor = ((String) in.readValue((String.class.getClassLoader())));
        this.model = ((String) in.readValue((String.class.getClassLoader())));
        this.manufacturerName = ((String) in.readValue((String.class.getClassLoader())));
        this.profilePic = ((String) in.readValue((String.class.getClassLoader())));
        this.isFullUrl = ((Long) in.readValue((Long.class.getClassLoader())));
        this.isVerified = ((Long) in.readValue((Long.class.getClassLoader())));
        this.inviteCodeUsed = ((String) in.readValue((String.class.getClassLoader())));
    }

    public RegisterRequest() {
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContryCode() {
        return contryCode;
    }

    public void setContryCode(String contryCode) {
        this.contryCode = contryCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public Long getIsFullUrl() {
        return isFullUrl;
    }

    public void setIsFullUrl(Long isFullUrl) {
        this.isFullUrl = isFullUrl;
    }

    public Long getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Long isVerified) {
        this.isVerified = isVerified;
    }

    public String getInviteCodeUsed() {
        return inviteCodeUsed;
    }

    public void setInviteCodeUsed(String inviteCodeUsed) {
        this.inviteCodeUsed = inviteCodeUsed;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("adminId", adminId).append("firstName", firstName).append("lastName", lastName).append("email", email).append("password", password).append("contryCode", contryCode).append("phone", phone).append("dob", dob).append("gender", gender).append("bio", bio).append("deviceId", deviceId).append("deviceType", deviceType).append("vehicleType", vehicleType).append("vehicleNumber", vehicleNumber).append("vehicleName", vehicleName).append("vehicleColor", vehicleColor).append("model", model).append("manufacturerName", manufacturerName).append("profilePic", profilePic).append("isFullUrl", isFullUrl).append("isVerified", isVerified).append("inviteCodeUsed", inviteCodeUsed).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(adminId);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(email);
        dest.writeValue(password);
        dest.writeValue(contryCode);
        dest.writeValue(phone);
        dest.writeValue(dob);
        dest.writeValue(gender);
        dest.writeValue(bio);
        dest.writeValue(deviceId);
        dest.writeValue(deviceType);
        dest.writeValue(vehicleType);
        dest.writeValue(vehicleNumber);
        dest.writeValue(vehicleName);
        dest.writeValue(vehicleColor);
        dest.writeValue(model);
        dest.writeValue(manufacturerName);
        dest.writeValue(profilePic);
        dest.writeValue(isFullUrl);
        dest.writeValue(isVerified);
        dest.writeValue(inviteCodeUsed);
    }

    public int describeContents() {
        return 0;
    }

}