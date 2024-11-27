package client;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import models.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {
    static final String GET_INGREDIENTS_API = "/api/ingredients";
    static final String ORDERS_API = "/api/orders";

    @Step("Получение списка ингредиентов")
    public ValidatableResponse getIngredients() {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get(GET_INGREDIENTS_API)
                .then();
    }

    @Step("Создание заказа")
    public ValidatableResponse createOrder(String accessToken, Order order) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(accessToken.replace("Bearer ", ""))
                .body(order)
                .when()
                .post(ORDERS_API)
                .then().log().all();
    }

    @Step("Получение заказа пользователя")
    public ValidatableResponse getOrder(String accessToken) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(accessToken.replace("Bearer ", ""))
                .when()
                .get(ORDERS_API)
                .then().log().all();
    }
}
