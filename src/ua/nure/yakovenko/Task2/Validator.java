package ua.nure.yakovenko.Task2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class Validator {
	
	private ResourceBundle bundle;
	
	public Validator(ResourceBundle bundle) {
		this.bundle = bundle;
	}
	
	public ArrayList<String> validateUserAuth(User u, boolean isLogin) {
		
		String name = u.getName();
		String login = u.getLogin();
		String email = u.getEmail();
		String password = u.getPassword();

		ArrayList<String> messages = new ArrayList<String>();
		
		Map<String, String> fields = new HashMap<String, String>();
		fields.put(bundle.getString("validation.email"), email);
		fields.put(bundle.getString("validation.password"), password);
		
		if(!isLogin) {
			fields.put(bundle.getString("validation.login"), name);
			fields.put(bundle.getString("validation.name"), login);
		}
		
		ArrayList<String> existence = isLogin ? validateMultipleExistence(fields)
											  : validateMultipleAttributeExistence(fields);
		if(existence.size() > 0 && existence.get(0) != "ok") {
			messages.addAll(existence);
		}
		
		if ((messages.size() == 2 && isLogin) || (messages.size() == 4 && !isLogin)) {
			return messages;
		}
		
		String isEmailValid = validateEmail(email);
		if (email != "ok") {
			messages.add(isEmailValid);
		}
		
		String isPasswordValid = validateMinimalLength(password, bundle.getString("validation.password"), 3);
		if (isPasswordValid != "ok") {
			messages.add(isPasswordValid);
		}
		
		String isLoginValid = validateMinimalLength(login, bundle.getString("validation.login"), 5);
		if (isLoginValid != "ok") {
			messages.add(isLoginValid);
		}
		
		java.util.Collections.sort(messages, new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.length() - o2.length();
			}
		});
		return messages;
	}
	
	public boolean validateExistence(String item) {
		return null != item && item.length() > 0;
	}
	
	public ArrayList<String> validateMultipleExistence(Map<String, String> fields) {
		ArrayList<String> errors = new ArrayList<String>();
		
		for(Map.Entry<String, String> item : fields.entrySet()) {
			if (!validateExistence(item.getValue())) {
				errors.add(item.getKey() + " " + bundle.getString("validation.necessary"));
			}
		}
		return errors;
	}
	
	public String validateEmail(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = java.util.regex.Pattern.compile(ePattern);
        Matcher m = p.matcher(email);
        
        if (m.matches()) {
        	return "ok";
        } else {
        	return bundle.getString("validation.email.invalid");
        }
	}
	
	public String validateAttributeExistence(String item, String itemName) {
		return !validateExistence(item) ? (bundle.getString("validation.field") + " '" + itemName + "' " +
				bundle.getString("validation.must_fill")) : "";
	}
	
	public ArrayList<String> validateMultipleAttributeExistence(Map<String, String> fields) {
		ArrayList<String> errors = new ArrayList<String>();
		
		for(Map.Entry<String, String> item : fields.entrySet()) {
			String v  = validateAttributeExistence(item.getValue(), item.getKey());
			if (v.length() > 0) {
				errors.add(v);
			}
		}
		return errors;
	}
	
	public String validateMinimalLength(String item, String itemName, int length) {
		if (null != item && item.length() < length) {
			return itemName + " " + bundle.getString("validation.length.should_be") + " " +
					bundle.getString("validation.of_length") + " " + length + " " +
					(length < 5 ? bundle.getString("validation.symbols")
							    : bundle.getString("validation.symbols2")) + " "  +
					bundle.getString("validation.or") + " " +
					bundle.getString("validation.length.longer");
		} else {
			return "ok";
		}
	}
	
	public String validateMaximumLength(String item, String itemName, int length) {
		if (item.length() > length) {
			return itemName + " should be shorter than " + (length + 1) + " sybmols";
		} else {
			return "ok";
		}
	}
}
