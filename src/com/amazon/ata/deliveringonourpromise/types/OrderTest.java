package com.amazon.ata.deliveringonourpromise.types;
import com.amazon.ata.deliveringonourpromise.types.Order;
import com.amazon.ata.deliveringonourpromise.types.OrderItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

 class OrderTest {

    @Test
     void masteryTaskThree_orderClass_customerOrderItemList_hasExternallyModifiedVariables() {

        //GIVEN
        Order order = new Order();

        //WHEN
        order.getOrderId();



        //THEN
        Assertions.assertTrue(order.getOrderId() == null);


    }
}
