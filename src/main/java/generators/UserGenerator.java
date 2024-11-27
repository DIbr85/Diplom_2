package generators;

import models.User;


import static Utils.Faker.*;


public class UserGenerator {
    public static User randomUser() {
        return User
                .builder()
                .email(fakerEmail())
                .password(fakerPassword())
                .name(fakerName())
                .build();

    }

    public static User randomUserWithoutName() {
        return User
                .builder()
                .email(fakerEmail())
                .password(fakerPassword())
                .build();
    }

    public static User randomUserWithoutEmail() {
        return User
                .builder()
                .password(fakerPassword())
                .name(fakerName())
                .build();
    }

    public static User randomUserWithoutPassword() {
        return User
                .builder()
                .email(fakerEmail())
                .name(fakerName())
                .build();
    }


}
