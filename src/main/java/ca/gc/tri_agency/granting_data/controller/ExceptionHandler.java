package ca.gc.tri_agency.granting_data.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

	@org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
	public String handleControllerException(Throwable ex) {

		return "/exception/forbiden-by-role";
	}
}