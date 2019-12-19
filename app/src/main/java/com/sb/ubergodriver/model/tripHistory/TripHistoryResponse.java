package com.sb.ubergodriver.model.tripHistory;
import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class TripHistoryResponse implements Parcelable
{

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("bookingHistory")
    @Expose
    private ArrayList<BookingHistory> bookingHistory = null;
    @SerializedName("totalEarning")
    @Expose
    private Long totalEarning;
    public final static Parcelable.Creator<TripHistoryResponse> CREATOR = new Creator<TripHistoryResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public TripHistoryResponse createFromParcel(Parcel in) {
            return new TripHistoryResponse(in);
        }

        public TripHistoryResponse[] newArray(int size) {
            return (new TripHistoryResponse[size]);
        }

    }
            ;

    protected TripHistoryResponse(Parcel in) {
        this.success = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        in.readList(this.bookingHistory, (BookingHistory.class.getClassLoader()));
        this.totalEarning = ((Long) in.readValue((Long.class.getClassLoader())));
    }

    public TripHistoryResponse() {
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public ArrayList<BookingHistory> getBookingHistory() {
        return bookingHistory;
    }

    public void setBookingHistory(ArrayList<BookingHistory> bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    public Long getTotalEarning() {
        return totalEarning;
    }

    public void setTotalEarning(Long totalEarning) {
        this.totalEarning = totalEarning;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("success", success).append("bookingHistory", bookingHistory).append("totalEarning", totalEarning).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeList(bookingHistory);
        dest.writeValue(totalEarning);
    }

    public int describeContents() {
        return 0;
    }

}
