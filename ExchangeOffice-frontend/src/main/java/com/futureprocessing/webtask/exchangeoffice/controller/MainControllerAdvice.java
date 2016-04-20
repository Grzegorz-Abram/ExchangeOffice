package com.futureprocessing.webtask.exchangeoffice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class MainControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(MainControllerAdvice.class);

    @ExceptionHandler(Exception.class)
    public ModelAndView allException(Exception e) {
        logger.error(e.getMessage(), e);

        ModelAndView model = new ModelAndView("exception");
        model.addObject("error", e.getClass().getSimpleName());
        model.addObject("message", e.getMessage());

        return model;
    }

}
