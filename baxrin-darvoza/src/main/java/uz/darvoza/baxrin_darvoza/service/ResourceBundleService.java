package uz.darvoza.baxrin_darvoza.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import uz.darvoza.baxrin_darvoza.enums.AppLanguage;

import java.util.Locale;
@Service
public class ResourceBundleService {
    @Autowired
    private ResourceBundleMessageSource messageSource;

    public String getMessage(String code, AppLanguage language) {
        return messageSource.getMessage(code,null,new Locale(language.name()));
    }
}
