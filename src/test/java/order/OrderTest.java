package order;

import client.OrderClient;
import client.UserClient;
import generators.OrderGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import models.Order;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static generators.UserGenerator.*;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class OrderTest {
    private User user;
    private UserClient userClient;
    private String accessToken;
    private Order order;
    private OrderClient orderClient;
    private OrderGenerator orderGenerator;


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        orderClient = new OrderClient();
        orderGenerator = new OrderGenerator();
        userClient = new UserClient();

    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUserStep(accessToken);
        }
    }

    @Test
    @DisplayName("Создание нового заказа авторизированным пользователем")
    @Description("Проверка успешного создания нового заказа авторизированным пользователем")
    public void testCreateOrderWithAuthorization() {
        order = orderGenerator.withIngredients();
        user = randomUser();
        accessToken = userClient.createUserStep(user).extract().body().path("accessToken");
        orderClient.createOrder(accessToken, order)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));

    }

    @Test
    @DisplayName("Создание нового заказа без авторизации")
    @Description("Проверка успешного создания нового заказа без авторизации")
    public void testCreateOrderWithoutAuthorization() {
        order = orderGenerator.withIngredients();
        orderClient.createOrder("", order)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));

    }

    @Test
    @DisplayName("Создание нового заказа без ингридиентов")
    @Description("Проверка неуспешного создания нового заказа без ингридиентов")
    public void testCreateOrderWithoutIngredients() {
        order = orderGenerator.withoutIngredients();
        orderClient.createOrder("", order)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание нового заказа с неверным хешем ингредиентов")
    @Description("Проверка неуспешного создания нового заказа с неверным хешем ингредиентов")
    public void testCreateOrderWithNotValidHash() {
        order = orderGenerator.notValidHash();
        orderClient.createOrder("", order)
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Получение заказа авторизированного пользователя")
    @Description("Проверка успешного получения заказа авторизированного пользователя")
    public void testGetOrderWithAuthorization() {
        order = orderGenerator.withIngredients();
        user = randomUser();
        accessToken = userClient.createUserStep(user).extract().body().path("accessToken");
        String expectedId = orderClient.createOrder(accessToken, order).extract().path("order._id");
        orderClient.getOrder(accessToken)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("orders[0]._id", equalTo(expectedId));
    }

    @Test
    @DisplayName("Получение заказа без авторизации")
    @Description("Проверка неуспешного получения заказа без авторизации")
    public void testGetOrderWithoutAuthorization() {
        orderClient.getOrder("")
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}
