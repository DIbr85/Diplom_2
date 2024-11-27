package client;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import models.User;

import static io.restassured.RestAssured.given;


public class UserClient {
    static final String REGISTER_USER_API = "/api/auth/register";
    static final String AUTH_USER_API = "/api/auth/user";
    static final String LOGIN_USER_API = "/api/auth/login";

    @Step("Создание пользователя")
    public ValidatableResponse createUserStep(User user) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(REGISTER_USER_API)
                .then().log().all();

    }

    @Step("Получение токена")
    public String getUserAccessTokenStep(User user) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(LOGIN_USER_API)
                .then()
                .extract()
                .body()
                .path("accessToken");


    }

    @Step("Удаление пользователя")
    public void deleteUserStep(String accessToken) {
        given().log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(accessToken.replace("Bearer ", ""))
                .when()
                .delete(AUTH_USER_API)
                .then().log().all();

    }

    @Step("Логин пользователя")
    public Response loginUserStep(User user) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(LOGIN_USER_API);
    }

    @Step("Обновление данных пользователя")
    public ValidatableResponse patchUserStep(String accessToken, User user) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(accessToken.replace("Bearer ", ""))
                .body(user)
                .when()
                .patch(AUTH_USER_API)
                .then().log().all();

    }
}
