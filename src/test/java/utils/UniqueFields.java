package utils;

import java.util.UUID;

public final class UniqueFields {

    private UniqueFields() {}

    public static String generateRandomEmail(String email){
        String[] part = email.split("@");
        return part[0] + "_"+ UUID.randomUUID() + "@" + part[1];
    }

    public static String generateUniqueUsername(String baseUsername) {
        return baseUsername + "_" + UUID.randomUUID().toString().substring(0,4);
    }

}
