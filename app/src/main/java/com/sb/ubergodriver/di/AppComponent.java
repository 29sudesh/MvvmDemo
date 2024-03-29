package com.sb.ubergodriver.di;


import com.sb.ubergodriver.view.activity.*;
import com.sb.ubergodriver.view.fragment.*;
import dagger.Component;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Created by ${Saquib} on 03-05-2018.
 */

@Component(modules = {AppModule.class, UtilsModule.class})
@Singleton
public interface AppComponent {

   public void doInjection(LogInActivity activity);

   public void doInjection(ForgotActivity activity);

   public void doInjection(EditFirstNameActivity activity);

   public void doInjection(RegisterDetailActivity2 activity);

   public void doInjection(RegisterActivity activity);

   public void doInjection(ForgotNewPasswordActivity activity);


   public void doInjection(EditLastNameActivity activity);

   public void doInjection(EditEmailActivity activity);

   public void doInjection(RegisterDetailActivity activity);

   public void doInjection(RegisterDetailActivity3 activity);

   public void doInjection(EditChangePasswordActivity activity);

   public void doInjection(EditPhoneNumberActivity activity);

   public void doInjection(OtpActivity activity);

   public void doInjection(OtpChangeNumberActivity activity);

   public void doInjection(OtpRegisterActivity activity);

   public void doInjection(EditProfileFragment fragment);

   public void doInjection(DocumentFragment fragment);

   public void doInjection(RatingFragment fragment);

   public void doInjection(PayFragment fragment);

   public void doInjection(HomeFragment fragment);

   public void doInjection(ChatFargment fragment);

   public void doInjection(EarningFragment fragment);

   public void doInjection(AlertFragment fragment);

   public void doInjection(ContactUsFragment fragment);

}
