package com.ucla.jam.user;

public class InMemoryUserRepositoryTest extends UserRepositoryContract {

    private final InMemoryUserRepository rep = new InMemoryUserRepository();

    @Override
    public UserRepository rep() {
        return rep;
    }
}
