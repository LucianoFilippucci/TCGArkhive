package it.lucianofilippucci.tcgarkhive.API.V1.DTO;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import it.lucianofilippucci.tcgarkhive.entity.RolesEntity;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {

    @JsonProperty("username")
    private String username;
    @JsonGetter
    public String getUsername() { return this.username; }
    @JsonSetter
    public void setUsername(String username) { this.username = username; }

    @JsonProperty("email")
    private String email;
    @JsonGetter
    public String getEmail() { return this.email; }
    @JsonSetter
    public void setEmail(String email) { this.email = email; }

    @JsonProperty("active")
    private boolean isActive;
    @JsonGetter
    public boolean isActive() { return this.isActive; }
    @JsonSetter
    public void setIsActive(boolean isActive) { this.isActive = isActive; }

    @JsonProperty("user-roles")
    private Set<RolesEntity> userRoles;
    @JsonGetter
    public Set<RolesEntity> getUserRoles() { return this.userRoles; }
    @JsonSetter
    public void setUserRoles(Set<RolesEntity> userRoles) { this.userRoles = userRoles; }
}
