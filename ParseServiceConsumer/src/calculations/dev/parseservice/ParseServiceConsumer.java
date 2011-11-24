package calculations.dev.parseservice;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;

import calculations.dev.ParseRequest;
import calculations.dev.ParseResponse;
import calculations.dev.parseservice.gen.SharedParseServiceConsumer;

public class ParseServiceConsumer extends SharedParseServiceConsumer {

	public ParseServiceConsumer(String clientName) throws ServiceException {
		super(clientName);
		// TODO Auto-generated constructor stub
	}
	
	public static int parse(String x) throws ServiceException {
		ParseServiceConsumer consumer = new ParseServiceConsumer("ParseServiceConsumer");
		ParseRequest request = new ParseRequest();
		request.setInput(x);
		ParseResponse response = consumer.parse(request);
		return response.getOutput();
	}
	
	public static void main(String[] args) throws ServiceException, IOException {
		/**
		 * Enter the testcases!
		 */
		PrintWriter writer = new PrintWriter(new FileWriter(new File("error_output.txt"),true));
		writer.println("-- Begin testcase --");
		writer.println(parse("-3^3") == -27);
		writer.println(parse("3+3") == 6);
		writer.println(parse("-3*9") == -27);
		writer.println(parse("-3*-5") == 15);
		writer.println(parse("3*10") == 30);
		writer.println("-- End testcase --");
		writer.flush();
	}

}
