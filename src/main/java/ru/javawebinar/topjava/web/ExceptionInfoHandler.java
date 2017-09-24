package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.ErrorInfo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.validation.BindException;

@ControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private static Logger LOG = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    @Autowired
    private MessageSource messageSource;

    private static Map<String, String> constraintCodeMap = new HashMap<String, String>() {
        {
            put("users_unique_email_idx", "app.duplicate");
            put("meals_unique_user_datetime_idx", "meal.datetime.duplicate");
        }
    };

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @Order(value = Ordered.HIGHEST_PRECEDENCE + 2)
    @ResponseBody
    public ErrorInfo bindValidationError(HttpServletRequest req, BindingResult result) {
        return logAndGetValidationErrors(req, result);
    }


    //  http://stackoverflow.com/a/22358422/548473
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        String rootMsg = ValidationUtil.getRootCause(e).getMessage();
        if (rootMsg != null) {
            Optional<Map.Entry<String, String>> entry = constraintCodeMap.entrySet().stream()
                    .filter((it) -> rootMsg.contains(it.getKey()))
                    .findAny();
            if (entry.isPresent()) {
                e = new DataIntegrityViolationException(
                        messageSource.getMessage(entry.get().getValue(), null, LocaleContextHolder.getLocale()));
            }
        }
        return new ErrorInfo(req.getRequestURL(), e);
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true);
    }

    private ErrorInfo logAndGetValidationErrors(HttpServletRequest req, BindingResult result) {
        String[] details = result.getFieldErrors().stream()
                .map(f -> f.getField() + ' ' + f.getDefaultMessage())
                .toArray(String[]::new);
        LOG.warn("Validation exception on request " + req.getRequestURL() + " " + Arrays.toString(details));
        return new ErrorInfo(req.getRequestURL(), "ValidationException", details);
    }

    private static ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logException) {
            LOG.error("Exception at request " + req.getRequestURL(), rootCause);
        } else {
            LOG.warn("Exception at request " + req.getRequestURL() + ": " + rootCause.toString());
        }
        return new ErrorInfo(req.getRequestURL(), rootCause);
    }
}