package ru.skillbox.diploma.mail;

import javax.servlet.http.HttpServletRequest;

public class SiteUrl {
    public static String getSiteURL(HttpServletRequest request){
        return request.getRequestURL().toString().replace(request.getServletPath(), "");
    }
}
