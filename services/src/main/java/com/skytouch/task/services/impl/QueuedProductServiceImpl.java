package com.skytouch.task.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skytouch.task.commons.dtos.requests.DeleteProductDTO;
import com.skytouch.task.commons.dtos.requests.ProductDTO;
import com.skytouch.task.commons.dtos.requests.UpdateProductDTO;
import com.skytouch.task.commons.dtos.responses.ProductResponse;
import com.skytouch.task.commons.event.Event;
import com.skytouch.task.commons.event.EventType;
import com.skytouch.task.commons.event.services.Producer;
import com.skytouch.task.commons.utils.JsonUtils;
import com.skytouch.task.services.ProductService;
import com.skytouch.task.services.exceptions.NoResultsReceivedException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Implementation of the product service interface that uses queues for communicating to and from the backend components.
 *
 * @author Waldo Terry
 */
@Service
@AllArgsConstructor
public class QueuedProductServiceImpl implements ProductService {

    private Producer producer;

    /**
     * Queues up a message that will EVENTUALLY trigger the creation of the provided Product.
     *
     * @param product Product to create.
     * @return The correlationalId of the queued message.
     * @throws JsonProcessingException If the provided input cannot be marshalled into a valid Json.
     */
    @Override
    public String createProduct(ProductDTO product) throws JsonProcessingException {

        String content = JsonUtils.getJsonFromObject(product);
        Event toSend = new Event(EventType.PRODUCT_CREATION, new Date(), content);

        producer.sendEvent(toSend);

        return toSend.getCorrelationalId();
    }

    /**
     * Queues up a message that will EVENTUALLY trigger an update on the provided product.
     *
     * @param product Desired final state of a product. Its ID is mandatory for the update to be successful.
     * @return The correlationalId of the queued message.
     * @throws JsonProcessingException If the provided input cannot be marshalled into a valid JSON.
     */
    @Override
    public String updateProduct(UpdateProductDTO product) throws JsonProcessingException {
        String content = JsonUtils.getJsonFromObject(product);
        Event toSend = new Event(EventType.PRODUCT_UPDATE, new Date(), content);

        producer.sendEvent(toSend);

        return toSend.getCorrelationalId();
    }

    /**
     * Queues up a message that will EVENTUALLY trigger the elimination of a product.
     *
     * @param product The product to delete. Its ID is mandatory for the deletion to be successful.
     * @return The correlationalId of the queued message.
     * @throws JsonProcessingException If the provided input cannot be marshalled into a valid JSON.
     */
    @Override
    public String deleteProduct(DeleteProductDTO product) throws JsonProcessingException {
        String content = JsonUtils.getJsonFromObject(product);
        Event toSend = new Event(EventType.PRODUCT_DELETION, new Date(), content);

        producer.sendEvent(toSend);

        return toSend.getCorrelationalId();
    }

    /**
     * Sends a message requesting the list of all products and then WAITS for the response, effectively making this operation
     * synchronous even as it is handled via a queue.
     *
     * @return The requested product list or an empty list if none can be found.
     */
    @Override
    public List<ProductResponse> findAllProducts() throws IOException, NoResultsReceivedException {
        Event toSend = new Event(EventType.LIST_PRODUCTS, new Date(), null);

        String searchResults = producer.requestReplyEvent(toSend);

        if (searchResults.equals(Producer.NO_RESULTS_RECEIVED)) {
            throw new NoResultsReceivedException("Search timed out. No response received.");
        }
        else if (searchResults.startsWith(Producer.ERROR_IN_SEARCH)){
            throw new NoResultsReceivedException(searchResults);
        }

        return JsonUtils.getObjectListFromJson(searchResults, ProductResponse.class);
    }

    /**
     * Sends a message requesting a specific product from the database and then WAITS for the response, effectively making this operation
     * synchronous even as it is handled via a queue.
     *
     * @param id Id of the requested product, mandatory
     * @return The requested product or <code>null</code> if it does not exist.
     */
    @Override
    public ProductResponse findProduct(Integer id) throws IOException, NoResultsReceivedException {
        Event toSend = new Event(EventType.FIND_PRODUCT, new Date(), id);

        String searchResults = producer.requestReplyEvent(toSend);

        if (searchResults.equals(Producer.NO_RESULTS_RECEIVED)) {
            throw new NoResultsReceivedException("Search timed out. No response received.");
        }
        else if (searchResults.startsWith(Producer.ERROR_IN_SEARCH)){
            throw new NoResultsReceivedException(searchResults);
        }

        return JsonUtils.getObjectFromJson(searchResults, ProductResponse.class);
    }
}
