package my.edu.um.umpoint.modules.security.service;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface CaptchaService {

    void create(HttpServletResponse response, String uuid) throws IOException;

    boolean validate(String uuid, String code);
}
