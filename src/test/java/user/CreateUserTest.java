package user;

import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static generators.UserGenerator.*;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class CreateUserTest {
    private User user;
    private UserClient userClient;


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        userClient = new UserClient();
        user = randomUser();
    }

    @After
    public void tearDown() {
        String accessToken = userClient.getUserAccessTokenStep(user);
        if (accessToken != null) {
            userClient.deleteUserStep(accessToken);
        }
    }

    @Test
    @DisplayName("Создание нового пользователя")
    @Description("Проверка успешного создания нового пользователя")
    public void testCreateUser() {
        userClient.createUserStep(user).assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));


    }

    @Test
    @DisplayName("Повторное создание уже существующего пользователя")
    @Description("Проверка возможности создания дубликата уже существующего пользователя")
    public void testCreateDuplicateUser() {
        userClient.createUserStep(user);
        userClient.createUserStep(user)
                .assertThat().statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));

    }

    @Test
    @DisplayName("Создание пользователя без указания электронной почты")
    @Description("Проверка возможности создания пользователя без указания электронной почты")
    public void testCreateUserWithoutEmail() {
        user = randomUserWithoutEmail();
        userClient.createUserStep(user)
                .assertThat().statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без указания электронной пароля")
    @Description("Проверка возможности создания пользователя без указания электронной пароля")
    public void testCreateUserWithoutPassword() {
        user = randomUserWithoutPassword();
        userClient.createUserStep(user)
                .assertThat().statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без указания имени")
    @Description("Проверка возможности создания пользователя без указания имени")
    public void testCreateUserWithoutName() {
        user = randomUserWithoutName();
        userClient.createUserStep(user)
                .assertThat().statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
