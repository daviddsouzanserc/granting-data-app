package ca.gc.tri_agency.granting_data.app.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DataRetrievalFailureException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public ModelAndView handleDataRetrievalFailureException(HttpServletRequest request,
			DataRetrievalFailureException drfe) {
		ModelAndView mv = new ModelAndView("/error");
		mv.addObject("timestamp",
				LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
		mv.addObject("path", request.getRequestURL()); // could be getRequestURL
		mv.addObject("error", HttpStatus.NOT_FOUND.getReasonPhrase());
		mv.addObject("status", HttpStatus.NOT_FOUND.value());
		mv.addObject("message", drfe.getMessage());
		String exceptionType = drfe.getClass().getName();
		mv.addObject("exception", exceptionType.substring(exceptionType.lastIndexOf('.') + 1));
		return mv;
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	public String handleAccessDeniedException(HttpServletRequest request, AccessDeniedException ade) {
		return "/exception/forbiden-by-role";
	}
}
