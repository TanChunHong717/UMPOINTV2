package my.edu.um.umpoint.common.utils;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

public class MessageUtils {
    private static ResourceBundleMessageSource getMessageSource() {
        ResourceBundleMessageSource bundleMessageSource = new ResourceBundleMessageSource();
        bundleMessageSource.setDefaultEncoding("UTF-8");
        bundleMessageSource.setBasenames("i18n/message");
        return bundleMessageSource;
    }

    public static String getMessage(int code){
        return getMessage(code, new String[0]);
    }

    public static String getMessage(int code, String... params){
        return getMessageSource().getMessage(code+"", params, LocaleContextHolder.getLocale());
    }
}
