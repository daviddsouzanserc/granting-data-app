package ca.gc.tri_agency.granting_data.model.util;

import java.lang.reflect.InvocationTargetException;

import org.springframework.context.i18n.LocaleContextHolder;

public interface LocalizedParametersModel {
	public default String getLocalizedAttribute(String attributeName) {
		String retval = null;
		String langAddOn = null;
		if (LocaleContextHolder.getLocale().toString().contains("en")) {
			langAddOn = "En";
		} else {
			langAddOn = "Fr";
		}

		try {
			String methodName = "get" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1)
					+ langAddOn;
			retval = (String) this.getClass().getDeclaredMethod(methodName).invoke(this);
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retval;
	}

}
