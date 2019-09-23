package com.neu.run.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.neu.run.pojo.Seller;
import com.neu.run.pojo.User;

public class SellerValidator {
	
public boolean supports(Class aClass) {
	return aClass.equals(User.class);
}

public void validate(Object obj, Errors errors) {
	Seller supplier = (Seller) obj;
	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "error.invalid.user", "Username Required");
	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "company", "error.invalid.user", "Company Name Required");
	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "error.invalid.email","Email Required");
	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "error.invalid.password", "Password Required");
}

}
