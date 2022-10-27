package italo.validate;

import java.util.ArrayList;
import java.util.List;

public class ValidatorParamImpl implements ValidatorParam, RequestValidatorParam {

	private String name;
	private String label;
	private String value;
	private Object to;
	private List<Validator> validators = new ArrayList<Validator>();
	
	public List<Validator> getValidators() {
		return validators;
	}

	public void setValidators(List<Validator> validators) {
		this.validators = validators;
	}

	public Object getTO() {
		return to;
	}

	public void setTO(Object to) {
		this.to = to;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
