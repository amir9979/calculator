
package calculations.dev.multiplyservice;

import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;

import calculations.dev.MultiplyRequest;
import calculations.dev.MultiplyResponse;
import calculations.dev.NegateRequest;
import calculations.dev.SumRequest;
import calculations.dev.negateservice.gen.SharedNegateServiceConsumer;
import calculations.dev.sumservice.gen.SharedSumServiceConsumer;

public class MultiplyServiceImpl
    implements MultiplyService
{


    public MultiplyResponse multiply(MultiplyRequest param0) {
    	
        MultiplyResponse response = new MultiplyResponse();    	
    	boolean negate = param0.getX() < 0 != param0.getY() < 0;
        
    	int total = 0;
    	
    	try{
    		SharedSumServiceConsumer sumConsumer = new SharedSumServiceConsumer("MultiplyService");
    		SharedNegateServiceConsumer negateConsumer = new SharedNegateServiceConsumer("MultiplyService");
    		
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
	        
	    	for(int i = 1; i < a; i++) {
	    		SumRequest request = new SumRequest();
	    		request.setX(total);
	    		request.setY(b);
	    	    total = sumConsumer.sum(request).getZ();	
	    	}
	
	        if(negate) {
	        	NegateRequest request = new NegateRequest();
	        	request.setX(total);
	        	total = negateConsumer.negate(request).getY();
	        }
        
        } catch(ServiceException e) {
        	e.printStackTrace();
        }
        
        response.setY(total);
        return response;
    }

}
