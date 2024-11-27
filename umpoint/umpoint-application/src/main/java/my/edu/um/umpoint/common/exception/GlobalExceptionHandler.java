package my.edu.um.umpoint.common.exception;

import cn.hutool.core.map.MapUtil;
import my.edu.um.umpoint.common.utils.HttpContextUtils;
import my.edu.um.umpoint.common.utils.IpUtils;
import my.edu.um.umpoint.common.utils.JsonUtils;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.modules.log.entity.SysLogErrorEntity;
import my.edu.um.umpoint.modules.log.service.SysLogErrorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@AllArgsConstructor
public class GlobalExceptionHandler {
    private final SysLogErrorService sysLogErrorService;

    @ExceptionHandler(RenException.class)
    public Result handleRenException(RenException ex) {
        Result result = new Result();
        result.error(ex.getCode(), ex.getMsg());

        return result;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException ex) {
        Result result = new Result();
        result.error(ErrorCode.DB_RECORD_EXISTS);

        return result;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadHttpRequestException.class)
    public Result handleBadHttpRequestException(BadHttpRequestException ex) {
        Result result = new Result();
        result.error(ErrorCode.BAD_REQUEST, ex.getMsg());

        return result;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public Result handleUnauthorizedException(UnauthorizedException ex) {
        Result result = new Result();
        result.error(ErrorCode.UNAUTHORIZED);

        return result;
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception ex) {
        log.error(ex.getMessage(), ex);

        saveLog(ex);

        return new Result().error();
    }

    private void saveLog(Exception ex) {
        SysLogErrorEntity log = new SysLogErrorEntity();

        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        log.setIp(IpUtils.getIpAddr(request));
        log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        log.setRequestUri(request.getRequestURI());
        log.setRequestMethod(request.getMethod());
        Map<String, String> params = HttpContextUtils.getParameterMap(request);
        if (MapUtil.isNotEmpty(params)) {
            log.setRequestParams(JsonUtils.toJsonString(params));
        }

        log.setErrorInfo(ExceptionUtils.getErrorStackTrace(ex));
        sysLogErrorService.save(log);
    }
}
