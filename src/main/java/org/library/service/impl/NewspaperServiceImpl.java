package org.library.service.impl;

import org.library.model.Newspaper;
import org.library.service.abstr.NewspaperService;

import java.util.List;
import java.util.Optional;

public class NewspaperServiceImpl implements NewspaperService {
    @Override
    public Optional<Newspaper> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Newspaper>> findAll() {
        return Optional.empty();
    }

    @Override
    public Optional<Newspaper> findByName() {
        return Optional.empty();
    }

    @Override
    public boolean update(Newspaper o) {
        return false;
    }

    @Override
    public boolean deleteByKey(Long id) {
        return false;
    }

    @Override
    public boolean save(Newspaper o) {
        return false;
    }
}
