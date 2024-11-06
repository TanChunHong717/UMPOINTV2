package my.edu.um.umpoint.common.exception;

import lombok.Getter;
import lombok.Setter;
import my.edu.um.umpoint.common.utils.MessageUtils;

import java.io.Serial;

@Setter
@Getter
public class BadHttpRequestException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = -7780348574464522600L;

    private int code;
	private String msg;

	public BadHttpRequestException(int code) {
		this.code = code;
		this.msg = MessageUtils.getMessage(code);
	}

	public BadHttpRequestException(int code, String message) {
		this.code = code;
		this.msg = message;
	}

	public BadHttpRequestException(int code, String... params) {
		this.code = code;
		this.msg = MessageUtils.getMessage(code, params);
	}

	public BadHttpRequestException(int code, Throwable e) {
		super(e);
		this.code = code;
		this.msg = MessageUtils.getMessage(code);
	}

	public BadHttpRequestException(int code, Throwable e, String... params) {
		super(e);
		this.code = code;
		this.msg = MessageUtils.getMessage(code, params);
	}

	public BadHttpRequestException(String msg) {
		super(msg);
		this.code = ErrorCode.INTERNAL_SERVER_ERROR;
		this.msg = msg;
	}

	public BadHttpRequestException(String msg, Throwable e) {
		super(msg, e);
		this.code = ErrorCode.INTERNAL_SERVER_ERROR;
		this.msg = msg;
	}

}