package com.example.dtcbuspass.network.entities;

import com.squareup.moshi.Json;

public class AccessToken {


  @Json(name = "token_type")

    String token_type;

    @Json(name = "expires_in")

    int expiresIn;

    @Json(name = "access_token")

    String accessToken;

    @Json(name = "refresh_token")

    String refreshToken;

  public String getToken_type() {
    return token_type;
  }

  public int getExpiresIn() {
    return expiresIn;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setToken_type(String token_type) {
    this.token_type = token_type;
  }

  public void setExpiresIn(int expiresIn) {
    this.expiresIn = expiresIn;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
