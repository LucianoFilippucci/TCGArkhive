package it.lucianofilippucci.tcgarkhive.API.V1.DTO;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequest {
    @JsonProperty("username")
    private String username;
    @JsonGetter
    public String getUsername() {return this.username;}
    @JsonSetter
    public void setUsername(String username) {this.username = username;}

    @JsonProperty("password")
    private String password;
    @JsonGetter
    public String getPassword() {return this.password;}
    @JsonSetter
    public void setPassword(String password) {this.password = password;}

    @JsonProperty("email")
    private String email;
    @JsonGetter
    public String getEmail() {return this.email;}
    @JsonSetter
    public void setEmail(String email) {this.email = email;}
}
