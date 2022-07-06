package com.skytouch.task.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skytouch.task.commons.dtos.requests.DeleteProductDTO;
import com.skytouch.task.commons.dtos.requests.ProductDTO;
import com.skytouch.task.commons.dtos.requests.UpdateProductDTO;
import com.skytouch.task.commons.dtos.responses.ProductResponse;
import com.skytouch.task.services.exceptions.NoResultsReceivedException;

import java.io.IOException;
import java.util.List;

/**
 * Interface that defines the business logic needed to handle product related requests.
 *
 * @author Waldo Terry
 */
public interface ProductService {

    /**
     * Queues up a message that will EVENTUALLY trigger the creation of the provided Product.
     *
     * @param product Product to create.
     * @return The correlationalId of the queued message.
     * @throws JsonProcessingException If the provided input cannot be marshalled into a valid Json.
     */
    String createProduct(ProductDTO product) throws JsonProcessingException;

    /**
     * Queues up a message that will EVENTUALLY trigger an update on the provided product.
     * @param product Desired final state of a product. Its ID is mandatory for the update to be successful.
     * @return The correlationalId of the queued message.
     * @throws JsonProcessingException If the provided input cannot be marshalled into a valid JSON.
     */
    String updateProduct(UpdateProductDTO product) throws JsonProcessingException;

    /**
     * Queues up a message that will EVENTUALLY trigger the elimination of a product.
     * @param product The product to delete. Its ID is mandatory for the deletion to be successful.
     * @return The correlationalId of the queued message.
     * @throws JsonProcessingException If the provided input cannot be marshalled into a valid JSON.
     */
    String deleteProduct(DeleteProductDTO product) throws JsonProcessingException;

    /**
     * Sends a message requesting the list of all products and then WAITS for the response, effectively making this operation
     * synchronous even as it is handled via a queue.
     *
     * @return The requested product list or an empty list if none can be found.
     * @throws IOException if the results cannot be unmarshalled into a valid object from Json.
     * @throws NoResultsReceivedException If after a fixed time (as determined by the <code>Producer</code> implementation)
     * no reply message has been received for the original request, the method times out and throws this exception.
     */
    List<ProductResponse> findAllProducts() throws IOException, NoResultsReceivedException;

    /**
     * Sends a message requesting a specific product from the database and then WAITS for the response, effectively making this operation
     * synchronous even as it is handled via a queue.
     * @param id Id of the requested product, mandatory
     * @return The requested product or <code>null</code> if it does not exist.
     * @throws IOException if the results cannot be unmarshalled into a valid object from Json.
     * @throws NoResultsReceivedException If after a fixed time (as determined by the <code>Producer</code> implementation)
     * no reply message has been received for the original request, the method times out and throws this exception.
     */
    ProductResponse findProduct(Integer id) throws IOException, NoResultsReceivedException;
}
