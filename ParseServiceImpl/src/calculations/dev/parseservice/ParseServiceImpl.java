
package calculations.dev.parseservice;

import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;

import calculations.dev.ParseRequest;
import calculations.dev.ParseResponse;
import calculations.dev.negateservice.NegateServiceConsumer;
import calculations.dev.sumservice.SumServiceConsumer;

public class ParseServiceImpl
    implements ParseService
{


    public ParseResponse parse(ParseRequest param0) {
    	ParseResponse response = new ParseResponse();
    	String calculation = param0.getInput();
		int x, y;
		
		try {
		if(calculation.contains("+")) {
			String[] splitstring = calculation.split("[+]");
			x = Integer.parseInt(splitstring[0]);
			y = Integer.parseInt(splitstring[1]);
			response.setOutput(SumServiceConsumer.sum(x,y));
		} /*else if (calculation.contains("*")) {
			String[] splitstring = calculation.split("[*]");
			x = Integer.parseInt(splitstring[0]);
			y = Integer.parseInt(splitstring[1]);
			response.setOutput(MultiplyServiceConsumer.multiply(x,y));	
		} else if (calculation.contains("^")) {
			String[] splitstring = calculation.split("[\\^]");
			x = Integer.parseInt(splitstring[0]);
			y = Integer.parseInt(splitstring[1]);	
			response.setOutput(PowerServiceConsumer.power(x,y));				
		} */else if (calculation.contains("-")) {
			String[] splitstring = calculation.split("[\\-]");
			x = Integer.parseInt(splitstring[1]);
			response.setOutput(NegateServiceConsumer.negate(x));
		} else {
			x = Integer.parseInt(calculation);
			response.setOutput(x);
		}
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
			
    	return response;
    }

}
