package my.edu.um.umpoint.common.exception;

import lombok.Getter;
import lombok.Setter;
import my.edu.um.umpoint.common.utils.MessageUtils;

import java.io.Serial;

@Setter
@Getter
public class RenException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 7375107047117423124L;

    private int code;
	private String msg;

	public RenException(int code) {
		this.code = code;
		this.msg = MessageUtils.getMessage(code);
	}

	public RenException(int code, String... params) {
		this.code = code;
		this.msg = MessageUtils.getMessage(code, params);
	}

	public RenException(int code, Throwable e) {
		super(e);
		this.code = code;
		this.msg = MessageUtils.getMessage(code);
	}

	public RenException(int code, Throwable e, String... params) {
		super(e);
		this.code = code;
		this.msg = MessageUtils.getMessage(code, params);
	}

	public RenException(String msg) {
		super(msg);
		this.code = ErrorCode.INTERNAL_SERVER_ERROR;
		this.msg = msg;
	}

	public RenException(String msg, Throwable e) {
		super(msg, e);
		this.code = ErrorCode.INTERNAL_SERVER_ERROR;
		this.msg = msg;
	}

}