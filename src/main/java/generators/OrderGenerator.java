package generators;

import client.OrderClient;
import models.Order;

public class OrderGenerator {
    OrderClient orderClient = new OrderClient();

    public Order withIngredients() {
        return new Order(new String[]{orderClient.getIngredients().extract().body().path("data[0]._id")});
    }

    public Order withoutIngredients() {
        return new Order(new String[]{});
    }

    public Order notValidHash() {
        return new Order(new String[]{"1234"});
    }
}

