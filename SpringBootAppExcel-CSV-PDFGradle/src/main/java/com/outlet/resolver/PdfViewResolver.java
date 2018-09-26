package com.outlet.resolver;

import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import com.outlet.view.PdfView;


public class PdfViewResolver implements ViewResolver {
    @Override
    public View resolveViewName(String s, Locale locale) {
        PdfView view = new PdfView();
        return view;
    }
}