package ca.gc.tri_agency.granting_data.security.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

@Retention(RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@PreAuthorize("hasRole('MDM ADMIN')")
public @interface AdminOnly {

	// Meta annotation that ensures that only an admin user can access methods and
	// classes

}
