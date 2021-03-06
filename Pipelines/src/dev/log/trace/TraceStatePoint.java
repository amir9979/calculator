package dev.log.trace;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class TraceStatePoint {

	private static final boolean USE_DATABASE = true;
	private static final String LOG_FILE = "trace_log.txt";
	private static final String DATABASE_NAME = "service_logging";
	private static final String TABLE_NAME = "trace_point";
	
	private String process_id;
	private String interface_id;
	private String invoker_id;
	private String service_id;
	private long sequence_id;
	
	private MySQLConnector connector;
	
	public TraceStatePoint(String process_id, String interface_id, String invoker_id, String service_id) {
		this.process_id = process_id;
		this.interface_id = interface_id;
		this.invoker_id = invoker_id;
		this.service_id = service_id;
		this.sequence_id = this.getSequence();

		connector = new MySQLConnector(DATABASE_NAME);
	}
	
	public static void init() {
		MySQLConnector connector = new MySQLConnector(DATABASE_NAME);
		
		connector.createTable("CREATE TABLE IF NOT EXISTS trace_point ("
	               + "id INT UNSIGNED NOT NULL AUTO_INCREMENT,"
	               + "PRIMARY KEY (id),"
	               + "process_id CHAR(40), interface_id CHAR(40), invoker_id CHAR(40), service_id CHAR(40), sequence_id INT UNSIGNED)");
		
		connector.close();
	}
	
	public static long getNextRequestId() {
		MySQLConnector connector = new MySQLConnector(DATABASE_NAME);

		List results = connector.select(String.format("SELECT COUNT(DISTINCT(process_id)) AS req_id FROM %s;",
				TABLE_NAME));
		Map result = (Map)results.get(0);

		connector.close();
		
		return (Long)result.get("req_id");
	}
	
	public void log() {
		connector.update(String.format("INSERT INTO %s VALUES (0, '%s', '%s', '%s', '%s', '%s');",
				TABLE_NAME,
				process_id,
				interface_id,
				invoker_id,
				service_id,
				sequence_id));
		
		connector.close();
	}

	public long getSequence() {
		MySQLConnector connector = new MySQLConnector(DATABASE_NAME);
		List results = connector.select(String.format("SELECT COUNT(*) AS seq_id FROM %s WHERE process_id = '%s';",
				TABLE_NAME,
				process_id));
		Map result = (Map)results.get(0);
		return (Long)result.get("seq_id");
	}
	
}
