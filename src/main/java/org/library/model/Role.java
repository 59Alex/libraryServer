package org.library.model;

import org.library.builder.RoleBuilder;

import java.util.Objects;

public class Role {

    private Long id;
    private RoleEnum name;
    private String description;

    public Role(Long id, RoleEnum name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Role() {
    }

    public static synchronized RoleBuilder createBuilder() {
        return RoleBuilder.init();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleEnum getName() {
        return name;
    }

    public void setName(RoleEnum name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id.equals(role.id) && name.equals(role.name) && description.equals(role.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }
}
