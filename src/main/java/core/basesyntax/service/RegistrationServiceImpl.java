package core.basesyntax.service;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.model.User;

public class RegistrationServiceImpl implements RegistrationService {
    private static final int MIN_AGE = 18;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private final StorageDao storageDao = new StorageDaoImpl();

    @Override
    public User register(User user) {
        if (user == null) {
            throw new RuntimeException("Invalid input. User could not be null");
        }
        if (user.getLogin() == null) {
            throw new RuntimeException("Null login");
        }
        if (user.getLogin().trim().isEmpty()) {
            throw new RuntimeException("Empty login");
        }
        if (storageDao.get(user.getLogin()) != null) {
            throw new RuntimeException("Login already exists");
        }
        if (user.getAge() == null) {
            throw new RuntimeException("Invalid age");
        }
        if (user.getAge() < MIN_AGE) {
            throw new RuntimeException("Age should be at least 18 years old");
        }

        if (user.getPassword() == null) {
            throw new RuntimeException("Invalid password");
        }
        String purePassword = user.getPassword().trim();
        user.setPassword(purePassword);
        if (user.getPassword().length() < MIN_PASSWORD_LENGTH) {
            throw new RuntimeException("Password should be at least 6 symbols");
        }
        storageDao.add(user);
        return user;
    }
}

