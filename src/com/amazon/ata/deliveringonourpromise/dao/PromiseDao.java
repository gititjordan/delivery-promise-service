package com.amazon.ata.deliveringonourpromise.dao;

import com.amazon.ata.deliveringonourpromise.App;
import com.amazon.ata.deliveringonourpromise.deliverypromiseservice.DeliveryPromiseServiceClient;
import com.amazon.ata.deliveringonourpromise.orderfulfillmentservice.OrderFulfillmentServiceClient;
import com.amazon.ata.deliveringonourpromise.orderfulfillmentservice.ServiceClient;
import com.amazon.ata.deliveringonourpromise.ordermanipulationauthority.OrderManipulationAuthorityClient;
import com.amazon.ata.deliveringonourpromise.types.OrderItem;
import com.amazon.ata.deliveringonourpromise.types.Promise;
import com.amazon.ata.deliveringonourpromise.types.PromiseHistory;
import com.amazon.ata.ordermanipulationauthority.OrderResult;
import com.amazon.ata.ordermanipulationauthority.OrderResultItem;
import com.amazon.ata.ordermanipulationauthority.OrderResultItemDetail;
import com.amazon.ata.ordermanipulationauthority.OrderShipment;
import com.amazonaws.services.dynamodbv2.xspec.NULL;

import java.security.Provider;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO implementation for Promises.
 */
public class PromiseDao implements ReadOnlyDao<String, List<Promise>> {
    private OrderManipulationAuthorityClient omaClient = App.getOrderManipulationAuthorityClient();
    private List<ServiceClient> serviceClients = new ArrayList<>();


    /**
     * PromiseDao constructor, accepting service clients for DPS and OMA.
     *
     * @param dpsClient DeliveryPromiseServiceClient for DAO to access DPS
     * @param omaClient OrderManipulationAuthorityClient for DAO to access OMA
     */
    public PromiseDao(DeliveryPromiseServiceClient dpsClient,OrderManipulationAuthorityClient omaClient) {
        this.serviceClients.add(dpsClient);
        this.omaClient = omaClient;

    }

    public PromiseDao( List<ServiceClient> serviceClients, OrderManipulationAuthorityClient omaClient) {
        this.serviceClients.addAll(serviceClients);
        this.omaClient = omaClient;

    }

    public PromiseDao(DeliveryPromiseServiceClient deliveryPromiseServiceClient,
                      OrderFulfillmentServiceClient orderFulfillmentServiceClient,
                      OrderManipulationAuthorityClient orderManipulationAuthorityClient) {
    }




    /**
     * Returns a list of all Promises associated with the given order item ID.
     *
     * @param customerOrderItemId the order item ID to fetch promise for
     * @return a List of promises for the given order item ID
     */
    @Override
    public List<Promise> get(String customerOrderItemId) {


        // Fetch the delivery date, so we can add to any promises that we find
        ZonedDateTime itemDeliveryDate = getDeliveryDateForOrderItem(customerOrderItemId);

        List<Promise> promises = new ArrayList<>();

        // fetch Promise from Delivery Promise Service. If exists, add to list of Promises to return.
        // Set delivery date
        for (ServiceClient client : serviceClients) {
            Promise promise = client.getOrderPromiseByOrderItemId(customerOrderItemId);

            if (promise != null) {
                promise.setDeliveryDate(itemDeliveryDate);
                promises.add(promise);
            }
        }
        return promises;
    }

    /*
     * Fetches the delivery date of the shipment containing the order item specified by the given order item ID,
     * if there is one.
     *
     * If the order item ID doesn't correspond to a valid order item, or if the shipment hasn't been delivered
     * yet, return null.
     */
    private ZonedDateTime getDeliveryDateForOrderItem(String customerOrderItemId) {
        OrderResultItem orderResultItem = omaClient.getCustomerOrderItemByOrderItemId(customerOrderItemId);

        if (null == orderResultItem) {
            return null;
        }

        OrderResult orderResult = omaClient.getCustomerOrderByOrderId(orderResultItem.getOrderId());

        for (OrderShipment shipment : orderResult.getOrderShipmentList()) {
            for (OrderShipment.ShipmentItem shipmentItem : shipment.getCustomerShipmentItems()) {
                if (shipmentItem.getCustomerOrderItemId().equals(customerOrderItemId)) {
                    return shipment.getDeliveryDate();
                }
            }
        }

        // didn't find a delivery date!
        return null;
    }
}
