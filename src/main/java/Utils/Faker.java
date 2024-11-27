package Utils;


public class Faker {
    public static String fakerName() {
        com.github.javafaker.Faker faker = new com.github.javafaker.Faker();
        return faker.name().fullName();
    }

    public static String fakerEmail() {
        com.github.javafaker.Faker faker = new com.github.javafaker.Faker();
        return faker.internet().emailAddress();
    }

    public static String fakerPassword() {
        com.github.javafaker.Faker faker = new com.github.javafaker.Faker();
        return faker.internet().password();
    }

}
