package com.ucla.jam.user.authentication;

public class InMemoryInternalCredentialsRepositoryTest extends CredentialsRepositoryContract {

    private final InMemoryInternalCredentialsRepository rep = new InMemoryInternalCredentialsRepository();

    @Override
    public CredentialsRepository rep() {
        return rep;
    }
}
