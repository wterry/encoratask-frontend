package com.skytouch.task.controllers;

import com.skytouch.task.commons.dtos.responses.Response;
import com.skytouch.task.services.InventoryService;
import com.skytouch.task.services.exceptions.NoResultsReceivedException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Controller that handles all inventory related requests from clients.
 *
 * @author Waldo Terry
 */
@RestController
@RequestMapping("/inventory")
@AllArgsConstructor
public class InventoryController {

    private InventoryService service;

    @GetMapping("/restockInvoices")
    public Response generateRestockInvoices() {
        try {
            return new Response(HttpStatus.OK.value(), "Process completed", service.generateRestockInvoices());
        } catch (IOException e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error queuing message, please try again later.", null);
        }
        catch (NoResultsReceivedException e) {
            return new Response(HttpStatus.REQUEST_TIMEOUT.value(), "No search results received.", null);
        }
    }
}
