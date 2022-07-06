package com.skytouch.task.controllers;

import com.skytouch.task.commons.dtos.responses.RequestValidationErrorResponse;
import com.skytouch.task.commons.dtos.responses.Response;
import com.skytouch.task.commons.event.Event;
import com.skytouch.task.commons.event.EventType;
import com.skytouch.task.commons.utils.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Abstract controller that will contain functionality common to all rest controllers set up for the task.
 *
 * @author Waldo Terry
 */
public abstract class AbstractController {

    /**
     * Handler set up to process bad requests received by the controllers that extend this class.
     *
     * @param e Triggering exception.
     * @return a response indicating the fields in the request that contain an error.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response handleBadRequest(MethodArgumentNotValidException e) {
        Response response = new Response();
        List<RequestValidationErrorResponse> errores = new ArrayList<>();

        e.getBindingResult().getAllErrors().forEach(error -> {
            RequestValidationErrorResponse errorMsg = new RequestValidationErrorResponse();

            errorMsg.setField(((FieldError) error).getField());
            errorMsg.setError(error.getDefaultMessage());

            errores.add(errorMsg);
        });

        response.setContent(errores);
        response.setMessage("Invalid Request");
        response.setStatusCode(400);

        return response;
    }


}
