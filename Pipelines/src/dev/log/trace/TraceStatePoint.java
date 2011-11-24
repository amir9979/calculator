package dev.log.trace;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TraceStatePoint {

	String process_id;
	String interface_id;
	String invoker_id;
	String service_id;
	String sequence_id;
	
	public TraceStatePoint(String process_id, String interface_id, String invoker_id, String service_id, String sequence_id) {
		this.process_id = process_id;
		this.interface_id = interface_id;
		this.invoker_id = invoker_id;
		this.service_id = service_id;
		this.sequence_id = sequence_id;
	}
	
	public void logToFile() {
		try {
			FileWriter writer = new FileWriter(new File("trace_log.txt"),true);
			PrintWriter out = new PrintWriter(writer);
			out.println(String.format("{\"process_id\":\"%s\",\"interface_id\":\"%s\",\"invoker_id\":\"%s\",\"service_id\":\"%s\",\"sequence_id\":\"%s\"}",
					process_id,interface_id,invoker_id,service_id,sequence_id));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getSequence(String processId) {
		// TODO Auto-generated method stub
		try {
			FileReader reader = new FileReader("trace_sequence_"+processId+".txt");
			char[] counter = new char[10];
			int read = reader.read();
			int i = 0;
			while(read != -1) {
				counter[i] = (char)read;
				i++;
				read = reader.read();
			}
			return String.valueOf(counter,0,i);
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void increaseSequence(String processId) {
		
		try {
			String sequenceId = getSequence(processId);
			FileWriter writer = new FileWriter("trace_sequence_"+processId+".txt");
			PrintWriter out = new PrintWriter(writer);
			out.print(Integer.parseInt(sequenceId) + 1);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void initiateSequence(String processId) {
		try {
			FileWriter writer = new FileWriter("trace_sequence_"+processId+".txt");
			PrintWriter out = new PrintWriter(writer);
			out.print(0);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
