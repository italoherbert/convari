package italo.validate;

import java.util.List;

public interface RequestValidatorParam {
	
	public String getValue();

	public String getName();
	
	public void setName(String name);

	public String getLabel();

	public void setLabel(String label);

	public List<Validator> getValidators();

	public void setValidators(List<Validator> validators);
	
}
