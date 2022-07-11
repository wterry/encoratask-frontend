package com.skytouch.task.pagecontrollers;

import com.skytouch.task.commons.dtos.requests.DeleteProductDTO;
import com.skytouch.task.commons.dtos.requests.ProductDTO;
import com.skytouch.task.commons.dtos.requests.UpdateProductDTO;
import com.skytouch.task.commons.dtos.responses.Response;
import com.skytouch.task.commons.dtos.responses.ValidationErrorResponse;
import com.skytouch.task.commons.utils.JsonUtils;
import com.skytouch.task.pagecontrollers.config.PagePropertiesConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.List;

/**
 * Controller set up to handle the model logic for the Product related JSP views. These are programmed as if they were a stand alone web
 * application, entirely divorced from the rest services they call to interact with persistence services.
 *
 * @author Waldo Terry
 */
@Controller
@ControllerAdvice
@RequestMapping("/pages/products")
@Slf4j
public class ProductListPageController {

    private final RestTemplate restTemplate;
    private final PagePropertiesConfiguration properties;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    public ProductListPageController(RestTemplate restTemplate, PagePropertiesConfiguration properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    @GetMapping("/list")
    public String viewListProducts(Model model) {
        try {
            Response searchResult = restTemplate.getForObject(properties.getListProductsUri(), Response.class);

            model.addAttribute("productSearch", searchResult.getContent());

            if (model.asMap().get("operationStatus") != null && model.asMap().get("operationStatus").equals(200)) {
                model.addAttribute("success", true);
            }
        }
        catch (HttpClientErrorException e) {
            model.addAttribute("operationStatus", e.getStatusCode().value());
            model.addAttribute("genericError", true);

            Response response = null;
            try {
                response = JsonUtils.getObjectFromJson(e.getResponseBodyAsString(), Response.class);
                model.addAttribute("errorContent", response.getMessage());
            } catch (IOException ex) {
                model.addAttribute("operationStatus", 500);
                model.addAttribute("errorContent", "Error unmarshalling error details");
            }
        }

        return "products";
    }

    @GetMapping("/add")
    public RedirectView viewAddProduct(RedirectAttributes model) {
        model.addFlashAttribute("adding", true);
        model.addFlashAttribute("newProduct", new ProductDTO());

        return new RedirectView("/pages/products/list", true);
    }

    @GetMapping("/edit/{productId}")
    public RedirectView viewEditProduct(@PathVariable("productId") Integer productId, RedirectAttributes model) {
        try {
            Response searchResult = restTemplate.getForObject(properties.getFindProductByIdUri() + "/" + productId + "/", Response.class);
            UpdateProductDTO formObject = JsonUtils.getObjectFromJson(JsonUtils.getJsonFromObject(searchResult.getContent()), UpdateProductDTO.class);

            model.addFlashAttribute("editing", true);
            model.addFlashAttribute("changingProduct", formObject);

            return new RedirectView("/pages/products/list", true);
        }
        catch (IOException e) {
            handleGenericError(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR), model);
            return new RedirectView("/pages/products/list", true);
        }
        catch (HttpClientErrorException e) {
            handleGenericError(e, model);
            return new RedirectView("/pages/products/list", true);
        }
    }

    @GetMapping("/delete/{productId}")
    public RedirectView doDeleteProduct(@PathVariable("productId") Integer productId, RedirectAttributes model) {
        DeleteProductDTO request = new DeleteProductDTO(productId);

        HttpEntity<DeleteProductDTO> param = new HttpEntity<>(request);

        try {
            Response deletionResult = restTemplate.exchange(properties.getDeleteProductUri(), HttpMethod.DELETE, param, Response.class).getBody();
            handleAsynchronousOperation(model, deletionResult);

            return new RedirectView("/pages/products/list", true);
        }
        catch (HttpClientErrorException e) {

            if (e.getStatusCode().value() == 400) {
                handleValidationErrors(e, model);
            }
            else {
                handleGenericError(e, model);
            }
            return new RedirectView("/pages/products/list", true);
        }
    }

    @PostMapping("/edit")
    public RedirectView doEditProduct(@ModelAttribute("changingProduct") UpdateProductDTO request, RedirectAttributes model) {

        HttpEntity<UpdateProductDTO> param = new HttpEntity<>(request);
        Response updateResult = null;

        try {
            updateResult = restTemplate.exchange(properties.getUpdateProductUri(), HttpMethod.PUT, param, Response.class).getBody();
        }
        catch (HttpClientErrorException e) {

            if (e.getStatusCode().value() == 400) {
                handleValidationErrors(e, model);
                model.addFlashAttribute("editing", true);
            }
            else {
                handleGenericError(e, model);
            }
            return new RedirectView("/pages/products/list", true);
        }

        handleAsynchronousOperation(model, updateResult);

        return new RedirectView("/pages/products/list", true);
    }

    @PostMapping("/add")
    public RedirectView doAddProduct(@ModelAttribute("newProduct") ProductDTO request, RedirectAttributes model) {
        Response addResult = null;

        try {
            addResult = restTemplate.postForObject(properties.getAddProductUrl(), request, Response.class);
        }
        catch (HttpClientErrorException e) {

            if (e.getStatusCode().value() == 400) {
                handleValidationErrors(e, model);
                model.addFlashAttribute("adding", true);
                model.addFlashAttribute("newProduct", request);
            }
            else {
                handleGenericError(e, model);
            }
            return new RedirectView("/pages/products/list", true);
        }

        handleAsynchronousOperation(model, addResult);

        return new RedirectView("/pages/products/list", true);
    }

    /**
     * Utility method that abstracts the steps necessary to handle SUCCESSFUL asynchronous operations called on the web page.
     * This includes saves, updates or deletes (no searches of any kind).
     *
     * <code>operationStatus</code> controls the banner at the top of the page that reports success or failure.
     * <code>correlationalId</code> Its the operation identifier in the message queues. Ideally, response time should be
     * quick enough that by the time the reload completes, the operation will have completed but if it is not the case, this
     * unique String will identify the request for tracing purposes.
     * @param model The accumulated model variables that will be used in this operation.
     * @param response The actual response from the REST service.
     */
    private void handleAsynchronousOperation(RedirectAttributes model, Response response) {
        model.addFlashAttribute("operationStatus", response.getStatusCode());
        model.addFlashAttribute("correlationalId", response.getContent());
    }

    /**
     * Handle the detected validation errors from the REST API.
     *
     * @param model Accumulated variables for the current request.
     * @param e The detected exception.
     */
    private void handleValidationErrors(HttpClientErrorException e, RedirectAttributes model) {
        Response response = null;

        try {
            response = JsonUtils.getObjectFromJson(e.getResponseBodyAsString(), Response.class);
        } catch (IOException ex) {
            handleGenericError(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR), model);
            return;
        }

        model.addFlashAttribute("operationStatus", response.getStatusCode());
        model.addFlashAttribute("validationError", true);

        List<ValidationErrorResponse> validationErrors = (List<ValidationErrorResponse>) response.getContent();

        model.addFlashAttribute("errorContent", validationErrors);
    }

    /**
     * Handle the general erros received from the REST API.
     *
     * @param model Accumulated variables for the current request.
     * @param e The received exception.
     */
    private void handleGenericError(HttpClientErrorException e, RedirectAttributes model) {
        model.addFlashAttribute("operationStatus", e.getStatusCode().value());
        model.addFlashAttribute("genericError", true);

        Response response = null;
        try {
            response = JsonUtils.getObjectFromJson(e.getResponseBodyAsString(), Response.class);
            model.addFlashAttribute("errorContent", response.getMessage());
        } catch (IOException ex) {
            model.addFlashAttribute("operationStatus", 500);
            model.addFlashAttribute("errorContent", "Error unmarshalling error details");
        }
    }
}
