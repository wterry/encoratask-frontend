package com.skytouch.task.pagecontrollers;

import com.skytouch.task.commons.dtos.requests.ProductDTO;
import com.skytouch.task.commons.dtos.responses.Response;
import com.skytouch.task.pagecontrollers.config.PagePropertiesConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/pages/inventory")
@Slf4j
public class InventoryPageController {

    private RestTemplate restTemplate;
    private PagePropertiesConfiguration properties;

    public InventoryPageController (RestTemplate restTemplate, PagePropertiesConfiguration properties) {
        this.properties = properties;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/invoices")
    public String viewAddProduct(Model model) {
        Response searchResult = restTemplate.getForObject(properties.getRestockInvoicesUri(), Response.class);

        log.info("Search complete: " + searchResult.getMessage());
        log.info("Content: " + searchResult.getContent());

        model.addAttribute("invoiceSearch", searchResult.getContent());

        return "invoices";
    }
}
