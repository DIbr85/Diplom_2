package user;

import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static generators.UserGenerator.randomUser;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;


public class PatchUserTest {

    private User user;
    private UserClient userClient;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        userClient = new UserClient();
        user = randomUser();
        userClient.createUserStep(user);
    }

    @After
    public void tearDown() {
        String accessToken = userClient.getUserAccessTokenStep(user);
        if (accessToken != null) {
            userClient.deleteUserStep(accessToken);
        }
    }


    @Test
    @DisplayName("Изменение имени пользователя")
    @Description("Проверка успешного изменения имени пользователя")
    public void testPatchUserWithSetNewName() {
        String accessToken = userClient.getUserAccessTokenStep(user);
        user.setEmail(user.getEmail());
        user.setPassword(user.getPassword());
        user.setName("Another name");
        userClient.patchUserStep(accessToken, user)
                .assertThat().statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение электронной почты пользователя")
    @Description("Проверка успешного изменения электронной почты пользователя")
    public void testPatchUserWithSetNewEmail() {
        String accessToken = userClient.getUserAccessTokenStep(user);
        user.setEmail("Another_email@email.com");
        user.setPassword(user.getPassword());
        user.setName(user.getName());
        userClient.patchUserStep(accessToken, user)
                .assertThat().statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение пароля пользователя")
    @Description("Проверка успешного изменения пароля пользователя")
    public void testPatchUserWithSetNewPassword() {
        String accessToken = userClient.getUserAccessTokenStep(user);
        user.setEmail(user.getEmail());
        user.setPassword("12345");
        user.setName(user.getName());
        userClient.patchUserStep(accessToken, user)
                .assertThat().statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение имени неавторизованного пользователя")
    @Description("Проверка неуспешного изменения имени неавторизованного пользователя")
    public void testPatchUserWithoutAuthorizationWithSetNewName() {
        user.setEmail(user.getEmail());
        user.setPassword(user.getPassword());
        user.setName("Another name");
        userClient.patchUserStep("", user)
                .assertThat().statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение электронной почты неавторизованного пользователя")
    @Description("Проверка неуспешного изменения электронной почты неавторизованного пользователя")
    public void testPatchUserWithoutAuthorizationWithSetNewEmail() {
        user.setEmail("Another_email@email.com");
        user.setPassword(user.getPassword());
        user.setName(user.getName());
        userClient.patchUserStep("", user)
                .assertThat().statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение пароля неавторизованного пользователя")
    @Description("Проверка неуспешного изменения пароля неавторизованного пользователя")
    public void testPatchUserWithoutAuthorizationWithSetNewPassword() {
        user.setEmail(user.getEmail());
        user.setPassword("12345");
        user.setName(user.getName());
        userClient.patchUserStep("", user)
                .assertThat().statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение электронной почты пользователя на уже зарегистрированную")
    @Description("Проверка неуспешного изменения электронной почты пользователя на принадлежащую другому зарегистрированному пользователю")
    public void testPatchUserWithSetIdenticalEmail() {
        User anotherUser = randomUser();
        userClient.createUserStep(anotherUser);
        String accessToken = userClient.getUserAccessTokenStep(anotherUser);
        anotherUser.setEmail(user.getEmail());
        anotherUser.setPassword(anotherUser.getPassword());
        anotherUser.setName(anotherUser.getName());
        userClient.patchUserStep(accessToken, anotherUser)
                .assertThat().statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User with such email already exists"));
    }

}

