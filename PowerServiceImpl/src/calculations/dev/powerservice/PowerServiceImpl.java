
package calculations.dev.powerservice;

import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;

import calculations.dev.PowerRequest;
import calculations.dev.PowerResponse;
import calculations.dev.multiplyservice.MultiplyServiceConsumer;

public class PowerServiceImpl
    implements PowerService
{


    public PowerResponse power(PowerRequest param0) {
        PowerResponse response = new PowerResponse();
        
    	int total = 0;
    	int x = param0.getX();
        int y = param0.getY();
                
        total = x;
        
        try {
        	
	    	for(int i = 1; i < y; i++)
	    	    total = MultiplyServiceConsumer.multiply(total, x);	
	    	
        } catch(ServiceException e) {
        	e.printStackTrace();
        }

        response.setZ(total);
        return response;
    }

}