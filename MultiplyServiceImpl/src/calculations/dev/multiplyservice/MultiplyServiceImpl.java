
package calculations.dev.multiplyservice;

import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;

import calculations.dev.MultiplyRequest;
import calculations.dev.MultiplyResponse;
import calculations.dev.negateservice.NegateServiceConsumer;
import calculations.dev.sumservice.SumServiceConsumer;

public class MultiplyServiceImpl
    implements MultiplyService
{


    public MultiplyResponse multiply(MultiplyRequest param0) {
    	
        MultiplyResponse response = new MultiplyResponse();    	
    	boolean negate = param0.getX() < 0 != param0.getY() < 0;
        
    	int total = 0;
    	int x = Math.abs(param0.getX());
        int y = Math.abs(param0.getY());
        int a = x;
        int b = y;
        
        // Minimize calls
        if(x>y) { 
          a = y;
          b = x;
        }
        
        total = b;
        
        try{
        	
	    	for(int i = 1; i < a; i++)
	    	    total = SumServiceConsumer.sum(total, b);	
	
	        if(negate)
	        	total = NegateServiceConsumer.negate(total);
        
        } catch(ServiceException e) {
        	e.printStackTrace();
        }
        
        response.setY(total);
        return response;
    }

}
