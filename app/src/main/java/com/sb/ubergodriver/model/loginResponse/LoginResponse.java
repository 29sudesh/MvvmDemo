package com.sb.ubergodriver.model.loginResponse;

import java.util.ArrayList;

public class LoginResponse {
  private boolean success;
  private String message;
  Driver driver;


 // Getter Methods 

  public boolean getSuccess() {
    return success;
  }

  public String getMessage() {
    return message;
  }

  public Driver getDriver() {
    return driver;
  }

 // Setter Methods 

  public void setSuccess( boolean success ) {
    this.success = success;
  }

  public void setMessage( String message ) {
    this.message = message;
  }

  public void setDriver( Driver driverObject ) {
    this.driver = driverObject;
  }
}
