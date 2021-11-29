package com.ucla.jam.user;

public class InMemoryUserRepositoryUnitTest extends UserRepositoryContract {

    private final InMemoryUserRepository rep = new InMemoryUserRepository();

    @Override
    public UserRepository rep() {
        return rep;
    }
}
