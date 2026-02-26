package utils;

import io.cucumber.core.eventbus.UuidGenerator;

import java.util.UUID;

public final class UniqueFields implements UuidGenerator {

    private UniqueFields() {}

    public static String generateRandomEmail(String email) {
        String[] part = email.split("@");
        return part[0] + "_"+ UUID.randomUUID() + "@" + part[1];
    }

    public static String generateUniqueUsername(String baseUsername) {
        return baseUsername + "_" + UUID.randomUUID().toString().substring(0,4);
    }

    @Override
    public UUID generateId() {

        return null;
    }

    @Override
    public UUID get() {
        return UuidGenerator.super.get();
    }
}
