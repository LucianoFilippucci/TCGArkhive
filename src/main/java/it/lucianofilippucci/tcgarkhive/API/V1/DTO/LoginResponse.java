package it.lucianofilippucci.tcgarkhive.API.V1.DTO;


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class LoginResponse {
    @JsonProperty("token")
    private String token;
    @JsonGetter
    public String getToken() {return token;}
    @JsonSetter
    public void setToken(String token) {this.token = token;}

    @JsonProperty("refreshToken")
    private String refreshToken;
    @JsonGetter
    public String getRefreshToken() {return refreshToken;}
    @JsonSetter
    public void setRefreshToken(String refreshToken) {this.refreshToken = refreshToken;}
}
