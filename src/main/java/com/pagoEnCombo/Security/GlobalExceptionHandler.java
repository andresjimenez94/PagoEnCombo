package com.pagoEnCombo.Security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.pagoEnCombo.persistence.entity.Mensajes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Mensajes> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Mensajes errorMensaje = new Mensajes();
        errorMensaje.setMensaje("Formato de solicitud inválido o tipo de dato incorrecto. Verifique el ID o los campos numéricos.");
        return new ResponseEntity<>(errorMensaje, HttpStatus.BAD_REQUEST);
    }

}
