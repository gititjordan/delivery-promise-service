package com.amazon.ata.deliveringonourpromise.dao;

import com.amazon.ata.deliveringonourpromise.App;
import com.amazon.ata.deliveringonourpromise.types.Order;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

public class OrderDaoTest {

    private OrderDao dao = App.getOrderDao();

    @Test
    public void get_forKnownOrderId_returnsOrder() {

        //GIVEN
        String nonExistentOrderId = "900-3746401-0000002";

        //WHEN
        Order order = dao.get(nonExistentOrderId);

        //THEN
        Assert.assertNotNull(order);


    }

}
