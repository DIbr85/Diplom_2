package user;

import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static generators.UserGenerator.*;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class LoginUserTest {
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
    @DisplayName("Логин пользователя")
    @Description("Проверка успешного логина пользователя")
    public void testLoginUser() {
        userClient.createUserStep(user);
        Response response = userClient.loginUserStep(user);
        response.then().log().all().assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Логин пользователя без электронной почты и пароля")
    @Description("Проверка неуспешного логина пользователя без электронной почты и пароля")
    public void testCLoginUserWithWrongLoginAndPassword() {
        userClient.createUserStep(user);
        User wrongUser = randomUser();
        Response response = userClient.loginUserStep(wrongUser);
        response.then().log().all().assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
