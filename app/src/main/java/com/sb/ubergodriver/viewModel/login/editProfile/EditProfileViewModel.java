package com.sb.ubergodriver.viewModel.login.editProfile;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.sb.ubergodriver.apiConstants.ApiResponse;
import com.sb.ubergodriver.model.forgot.EmailOtpRequest;
import com.sb.ubergodriver.model.requests.*;
import com.sb.ubergodriver.viewModel.login.Repository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ${Saquib} on 03-05-2018.
 */

public class EditProfileViewModel extends ViewModel {

    public Repository repository;
    public final CompositeDisposable disposables = new CompositeDisposable();
    public final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();

    public EditProfileViewModel(Repository repository) {
        this.repository = repository;
    }

    public MutableLiveData<ApiResponse> loginResponse() {
        return responseLiveData;
    }
    /*
     * method to call normal login api with $(mobileNumber + password)
     * */
    public void hitEditProfileApi(EditProfilerequest loginRequest) {


        disposables.add(repository.executeEditProfile(loginRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> responseLiveData.setValue(ApiResponse.loading()))
                .subscribe(
                        result -> responseLiveData.setValue(ApiResponse.success(result)),
                        throwable -> responseLiveData.setValue(ApiResponse.error(throwable))
                ));

    }


    /*
     * method to call normal login api with $(mobileNumber + password)
     * */
    public void hitCheckRegisterStatus(RegistrerCheckStatusRequest loginRequest) {


        disposables.add(repository.checkRegisterNumber(loginRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> responseLiveData.setValue(ApiResponse.loading()))
                .subscribe(
                        result -> responseLiveData.setValue(ApiResponse.success(result)),
                        throwable -> responseLiveData.setValue(ApiResponse.error(throwable))
                ));

    }

    /*
     * method to call normal login api with $(mobileNumber + password)
     * */
    public void hitForgotEmailApi(EmailOtpRequest loginRequest) {


        disposables.add(repository.executeEmailOtp(loginRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> responseLiveData.setValue(ApiResponse.loading()))
                .subscribe(
                        result -> responseLiveData.setValue(ApiResponse.success(result)),
                        throwable -> responseLiveData.setValue(ApiResponse.error(throwable))
                ));

    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
