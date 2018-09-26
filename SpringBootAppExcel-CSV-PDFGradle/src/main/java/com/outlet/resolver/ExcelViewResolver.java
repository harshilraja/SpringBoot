package com.outlet.resolver;

import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import com.outlet.view.ExcelView;




public class ExcelViewResolver implements ViewResolver {
    @Override
    public View resolveViewName(String s, Locale locale) {
        ExcelView view = new ExcelView();
        return view;
    }
}