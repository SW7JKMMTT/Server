package rocks.stalin.sw708e16.server.services.builders;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.core.authentication.Permission;
import rocks.stalin.sw708e16.server.core.authentication.PermissionType;

public class PermissionBuilder {
    private PermissionType permission;

    public PermissionBuilder() {
    }

    @JsonCreator
    public PermissionBuilder(String permission) {
        this.setPermission(permission);
    }

    @JsonValue
    public PermissionType getPermission() {
        return permission;
    }

    public void setPermission(PermissionType permission) {
        this.permission = permission;
    }

    public void setPermission(String permissionType) {
        this.permission = PermissionType.fromString(permissionType);
    }

    public Permission build(User user) {
        return new Permission(user, permission);
    }
}
