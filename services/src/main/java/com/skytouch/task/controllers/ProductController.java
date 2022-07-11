package com.skytouch.task.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skytouch.task.commons.dtos.requests.DeleteProductDTO;
import com.skytouch.task.commons.dtos.requests.ProductDTO;
import com.skytouch.task.commons.dtos.requests.UpdateProductDTO;
import com.skytouch.task.commons.dtos.responses.Response;
import com.skytouch.task.services.ProductService;
import com.skytouch.task.services.exceptions.NoResultsReceivedException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

/**
 * REST Controller that will provide the services called by the front end JSPs to request changes to the database or fetch
 * information from it.
 *
 * By and large the methods in this class will validate inputs and request the creation of messages to a Kafka topic.
 *
 * @author Waldo Terry
 */
@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController extends AbstractController {

    private ProductService service;

    @PostMapping("/create")
    public ResponseEntity<Response> createProduct(@RequestBody @Valid ProductDTO product) {
        try {
            product.sanitizeTextInputs();
            String correlationalId = service.createProduct(product);

            return new ResponseEntity<Response> (new Response(HttpStatus.OK.value(), "Request queued", correlationalId), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<Response> (new Response (HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error queuing message, please try again later.", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateProduct(@RequestBody @Valid UpdateProductDTO product) {
        try {
            product.sanitizeTextInputs();
            String correlationalId = service.updateProduct(product);

            return new ResponseEntity<Response> (new Response(HttpStatus.OK.value(), "Request queued", correlationalId), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<Response> (new Response (HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error queuing message, please try again later.", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Response> deleteProduct(@RequestBody @Valid DeleteProductDTO product) {
        try {
            String correlationalId = service.deleteProduct(product);

            return new ResponseEntity<Response> (new Response(HttpStatus.OK.value(), "Request queued", correlationalId), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<Response> (new Response (HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error queuing message, please try again later.", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Before JSP shenanigans
    @GetMapping("")
    public ResponseEntity<Response> findAllProducts() {
        try {
            return new ResponseEntity<>(new Response(HttpStatus.OK.value(), "Search completed", service.findAllProducts()), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<Response>( new Response (HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error queuing message, please try again later.", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (NoResultsReceivedException e) {
            return new ResponseEntity<Response> (new Response(HttpStatus.REQUEST_TIMEOUT.value(), "No search results received.", null), HttpStatus.REQUEST_TIMEOUT);
        }
    }

    @GetMapping("/{id}/")
    public ResponseEntity<Response> findProduct(@PathVariable("id") Integer id) {
        try {
            return new ResponseEntity<Response> (new Response (HttpStatus.OK.value(), "Search completed", service.findProduct(id)), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<Response>( new Response (HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error queuing message, please try again later.", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (NoResultsReceivedException e) {
            return new ResponseEntity<Response> (new Response(HttpStatus.REQUEST_TIMEOUT.value(), "No search results received.", null), HttpStatus.REQUEST_TIMEOUT);
        }
    }
}
