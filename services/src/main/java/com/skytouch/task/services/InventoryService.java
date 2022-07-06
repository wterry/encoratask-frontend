package com.skytouch.task.services;

import com.skytouch.task.commons.dtos.responses.RestockInvoiceResponse;
import com.skytouch.task.services.exceptions.NoResultsReceivedException;

import java.io.IOException;
import java.util.List;

/**
 * Interface that defines the business logic needed to handle inventory related requests.
 *
 * @author Waldo Terry
 */
public interface InventoryService {

    /**
     * Sends a message requesting the generation of any invoices needed to do a full product inventory restock.
     *
     * @return The needed invoices or an empty list if no product in the inventory needs restocking yet (or the product
     * inventory is empty).
     *
     * @throws IOException if the results cannot be unmarshalled into a valid object from Json.
     * @throws NoResultsReceivedException If after a fixed time (as determined by the <code>Producer</code> implementation)
     * no reply message has been received for the original request, the method times out and throws this exception.
     */
    List<RestockInvoiceResponse> generateRestockInvoices() throws IOException, NoResultsReceivedException;

}
