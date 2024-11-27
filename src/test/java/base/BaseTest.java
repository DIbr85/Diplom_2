package base;

import client.UserClient;
import io.restassured.RestAssured;
import models.User;
import org.junit.After;
import org.junit.Before;

import static generators.UserGenerator.randomUser;

public class BaseTest {
    public UserClient userClient;
    public User user;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

    }

    @After
    public void tearDown() {

        String accessToken = userClient.getUserAccessTokenStep(user);
        if (accessToken != null) {
            userClient.deleteUserStep(accessToken);
        }
    }
}
