package org.library.builder;

import org.library.model.Role;
import org.library.model.RoleEnum;

public class RoleBuilder {
    private Role role;
    private RoleBuilder roleBuilder;

    {
        role = new Role();
    }

    private RoleBuilder() {}

    public static synchronized RoleBuilder init() {
        RoleBuilder roleBuilder = new RoleBuilder();
        roleBuilder.setRoleBuilder(roleBuilder);
        return roleBuilder;
    }
    public void setRoleBuilder(RoleBuilder roleBuilder) {
        this.roleBuilder = roleBuilder;
    }

    public RoleBuilder setId(Long id) {
        this.role.setId(id);
        return this.roleBuilder;
    }

    public RoleBuilder setName(RoleEnum role) {
        this.role.setName(role);
        return this.roleBuilder;
    }

    public RoleBuilder setDescription(String description) {
        this.role.setDescription(description);
        return this.roleBuilder;
    }

    public Role build() {
        return this.role;
    }
}
