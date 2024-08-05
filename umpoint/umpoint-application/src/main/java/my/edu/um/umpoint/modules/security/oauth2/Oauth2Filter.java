package my.edu.um.umpoint.modules.security.oauth2;

import cn.hutool.core.util.StrUtil;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.exception.ErrorCode;
import my.edu.um.umpoint.common.utils.HttpContextUtils;
import my.edu.um.umpoint.common.utils.JsonUtils;
import my.edu.um.umpoint.common.utils.Result;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

public class Oauth2Filter extends AuthenticatingFilter {

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {

        String token = getRequestToken((HttpServletRequest) request);

        if (StrUtil.isBlank(token)) {
            return null;
        }

        return new Oauth2Token(token);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }

        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        String token = getRequestToken((HttpServletRequest) request);
        if (StrUtil.isBlank(token)) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setContentType("application/json;charset=utf-8");
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtils.getOrigin());

            String json = JsonUtils.toJsonString(new Result().error(ErrorCode.UNAUTHORIZED));

            httpResponse.getWriter().print(json);

            return false;
        }

        return executeLogin(request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtils.getOrigin());
        try {
            Throwable throwable = e.getCause() == null ? e : e.getCause();
            Result r = new Result().error(HttpStatus.SC_UNAUTHORIZED, throwable.getMessage());

            String json = JsonUtils.toJsonString(r);
            httpResponse.getWriter().print(json);
        } catch (IOException e1) {

        }

        return false;
    }

    private String getRequestToken(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader(Constant.TOKEN_HEADER);

        if (StrUtil.isBlank(token)) {
            token = httpRequest.getParameter(Constant.TOKEN_HEADER);
        }

        return token;
    }

}
