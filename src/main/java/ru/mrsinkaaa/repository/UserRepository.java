package ru.mrsinkaaa.repository;

import ru.mrsinkaaa.entity.User;

import java.util.List;
import java.util.Optional;

public class UserRepository implements CrudRepository<Integer, User> {

    private static final UserRepository INSTANCE = new UserRepository();

    private UserRepository() {}


    @Override
    public Optional<User> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public void update(User entity) {

    }

    @Override
    public User save(User entity) {
        return null;
    }

    @Override
    public boolean delete(User entity) {
        return false;
    }

    public static UserRepository getInstance() {
        return INSTANCE;
    }
}
