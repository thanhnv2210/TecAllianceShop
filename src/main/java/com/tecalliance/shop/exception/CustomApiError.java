package com.tecalliance.shop.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@Data
@JsonInclude(Include.NON_NULL)
public class CustomApiError {
	private HttpStatus status;
	private Timestamp timestamp;
	private String path;
	private String message;

	public CustomApiError(HttpStatus status, String message) {
		super();
		HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		this.status = status;
		this.message = message;
		this.path = String.format("%s-%s", servletRequest.getMethod(), getFullURL(servletRequest));
		this.timestamp = new Timestamp(System.currentTimeMillis());
	}

	public static String getFullURL(HttpServletRequest request) {
		StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
		String queryString = request.getQueryString();

		if (queryString == null) {
			return requestURL.toString();
		} else {
			return requestURL.append('?').append(queryString).toString();
		}
	}
}
