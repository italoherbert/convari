package convari.controller.operation;

import italo.validate.RequestValidatorParam;

import java.util.List;



public interface Operation {
		
	public void initialize( OperationController controller, List<RequestValidatorParam> vparams ) throws OperationException;
		
	public void execute( OperationController controller, OperationParameters parameters ) throws OperationException;

	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException;

}
