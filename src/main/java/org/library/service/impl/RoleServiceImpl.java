package org.library.service.impl;

import org.library.model.Role;
import org.library.service.abstr.RoleService;

import java.util.List;
import java.util.Optional;

public class RoleServiceImpl implements RoleService {
    @Override
    public Optional<Role> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Role>> findAll() {
        return Optional.empty();
    }

    @Override
    public Optional<Role> findByName() {
        return Optional.empty();
    }

    @Override
    public boolean update(Role o) {
        return false;
    }

    @Override
    public boolean deleteByKey(Long id) {
        return false;
    }

    @Override
    public boolean save(Role o) {
        return false;
    }
}
