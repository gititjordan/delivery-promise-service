package com.amazon.ata.deliveringonourpromise.orderfulfillmentservice;

import com.amazon.ata.deliveringonourpromise.types.Promise;

public interface ServiceClient {

    Promise getOrderPromiseByOrderItemId(String customerOrderItemId) ;

}
