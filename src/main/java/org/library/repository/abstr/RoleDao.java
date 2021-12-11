package org.library.repository.abstr;

import org.library.model.Role;

public interface RoleDao extends DefaultDao<Role> {
    Role getRoleByUserId(Long id);
    Role findByName(String name);
}
