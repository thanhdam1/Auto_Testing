package com.ecommerce.utils;

import net.datafaker.Faker;

import java.util.Locale;

public final class DataFakerUtil {

    private static final Faker faker = new Faker(Locale.ENGLISH);

    private DataFakerUtil() {}

    public static String getFirstName() {
        return faker.name().firstName();
    }

    public static String getLastName() {
        return faker.name().lastName();
    }

    public static String getPostalCode() {
        return faker.address().zipCode();
    }

    public static String getEmail() {
        return faker.internet().emailAddress();
    }

    public static String getStreetAddress() {
        return faker.address().streetAddress();
    }

    public static String getCity() {
        return faker.address().city();
    }

    public static String getState() {
        return faker.address().state();
    }

    public static String getPhoneNumber() {
        return faker.phoneNumber().cellPhone();
    }
}
