package com.sb.ubergodriver.viewModel.login.editProfile;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.sb.ubergodriver.apiConstants.ApiResponse;
import com.sb.ubergodriver.model.forgot.EmailOtpRequest;
import com.sb.ubergodriver.model.forgot.ForgotPasswordRequest;
import com.sb.ubergodriver.model.requests.ChangePasswordRequest;
import com.sb.ubergodriver.model.requests.LoginRequest;
import com.sb.ubergodriver.viewModel.login.Repository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ${Saquib} on 03-05-2018.
 */

public class ChangePasswordViewModel extends ViewModel {

    public Repository repository;
    public final CompositeDisposable disposables = new CompositeDisposable();
    public final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();


    public ChangePasswordViewModel(Repository repository) {
        this.repository = repository;
    }

    public MutableLiveData<ApiResponse> loginResponse() {
        return responseLiveData;
    }

    /*
     * method to call normal login api with $(mobileNumber + password)
     * */
    public void hitChangePasswordApi(ChangePasswordRequest loginRequest) {


        disposables.add(repository.executeChangePasswoed(loginRequest)
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
    public void hitForgotPhoneApi(ForgotPasswordRequest loginRequest) {


        disposables.add(repository.executeForgotChangePasswoed(loginRequest)
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
