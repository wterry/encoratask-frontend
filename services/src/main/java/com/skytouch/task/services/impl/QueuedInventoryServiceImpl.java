package com.skytouch.task.services.impl;

import com.skytouch.task.commons.dtos.responses.RestockInvoiceResponse;
import com.skytouch.task.commons.event.Event;
import com.skytouch.task.commons.event.EventType;
import com.skytouch.task.commons.event.services.Producer;
import com.skytouch.task.commons.utils.JsonUtils;
import com.skytouch.task.services.InventoryService;
import com.skytouch.task.services.exceptions.NoResultsReceivedException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Inventory service interface implementation that uses queues for requests and responses to the backend.
 *
 * @author Waldo Terry
 */
@Service
@AllArgsConstructor
public class QueuedInventoryServiceImpl implements InventoryService {

    private Producer producer;
    /**
     * Sends a message requesting the generation of any invoices needed to do a full product inventory restock then WAITS
     * for a response, effectively making this method synchronous even as it is handled through a queue.
     *
     * @return The needed invoices or an empty list if no product in the inventory needs restocking yet (or the product
     * inventory is empty).
     */
    @Override
    public List<RestockInvoiceResponse> generateRestockInvoices() throws IOException, NoResultsReceivedException {
        Event toSend = new Event(EventType.GENERATE_INVOICES, new Date(), null);

        String searchResults = producer.requestReplyEvent(toSend);

        if (searchResults.equals(Producer.NO_RESULTS_RECEIVED)) {
            throw new NoResultsReceivedException("Search timed out. No response received.");
        }
        else if (searchResults.startsWith(Producer.ERROR_IN_SEARCH)){
            throw new NoResultsReceivedException(searchResults);
        }

        return JsonUtils.getObjectListFromJson(searchResults, RestockInvoiceResponse.class);
    }
}
