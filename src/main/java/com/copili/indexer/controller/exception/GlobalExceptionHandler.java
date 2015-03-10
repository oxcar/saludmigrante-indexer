package com.copili.indexer.controller.exception;

import com.copili.indexer.controller.rest.ApiResponse;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity defaultErrorHandler(HttpServletRequest httpServletRequest, Exception exception) throws Exception {

        // El resto de excepciones
        log.error("");
        log.error("======================================================================");
        log.error("URL: {} : {}", httpServletRequest.getMethod(), httpServletRequest.getRequestURL());
        log.error("");
        log.error("HEADERS");
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = (String) headerNames.nextElement();
            String value = httpServletRequest.getHeader(name);
            log.error("{}: {}", name, value);
        }
        log.error("");
        log.error("PARAMETERS");
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
        if (!parameterNames.hasMoreElements()) {
            log.error("-- No hay Parametros --");
        }
        while (parameterNames.hasMoreElements()) {
            String name = (String) parameterNames.nextElement();
            String value = ArrayUtils.toString(httpServletRequest.getParameterValues(name));
            log.error("{}: {}", name, value);
        }
        log.error("");
        log.error("Excepcion : {}", exception.getClass());
        log.error("Mensaje: {}", exception.getMessage());
        log.error("Causa: {}", (Object[]) exception.getStackTrace());
        log.error("======================================================================");
        log.error("");

        if (exception instanceof HttpMediaTypeNotSupportedException) {
            return new ResponseEntity<>(ApiResponse.withError(String.format("Content-Type %s no esta soportado", ((HttpMediaTypeNotSupportedException) exception).getContentType().toString())), HttpStatus.BAD_REQUEST);
        }
        if (exception instanceof HttpRequestMethodNotSupportedException) {
            String[] errors = new String[]{
                    String.format("El método %s no esta soportado", ((HttpRequestMethodNotSupportedException) exception).getMethod()),
                    String.format("Métodos soportados: %s", (Object[]) ((HttpRequestMethodNotSupportedException) exception).getSupportedMethods())
            };
            return new ResponseEntity<>(ApiResponse.withErrors(errors), HttpStatus.BAD_REQUEST);
        }
        if (exception instanceof HttpMessageNotReadableException) {
            return new ResponseEntity<>(ApiResponse.withError("El formato de la petición no es correcto"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
