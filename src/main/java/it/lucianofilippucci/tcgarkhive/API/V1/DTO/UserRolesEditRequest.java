package it.lucianofilippucci.tcgarkhive.API.V1.DTO;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import it.lucianofilippucci.tcgarkhive.helpers.enums.Actions;
import lombok.Data;

@Data
public class UserRolesEditRequest {
    @JsonProperty("userId")
    private Long userId;
    @JsonGetter
    public Long getUserId() { return this.userId; }
    @JsonSetter
    public void setUserId(Long userId) { this.userId = userId; }

    @JsonProperty("roleId")
    private Long roleId;
    @JsonGetter
    public Long getRoleId() { return this.roleId; }
    @JsonSetter
    public void setRoleId(Long roleId) { this.roleId = roleId; }

    @JsonProperty("action")
    private Actions action;
    @JsonGetter
    public Actions getAction() { return this.action; }
    @JsonSetter
    public void setAction(Actions action) { this.action = action; }
}
