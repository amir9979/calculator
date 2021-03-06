package dev.log.trace;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

public class MySQLConnector {

	private static String DB_USERNAME = "root";
	private static String DB_PASSWORD = "root";
	private static String DB_URL = "jdbc:mysql://localhost/";
	
	private Connection connection = null;
	private String database = "";
	
	public MySQLConnector(String database) {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
			Statement s = connection.createStatement();
			s.execute(String.format("CREATE DATABASE IF NOT EXISTS %s;",database));
			s.execute(String.format("USE %s;",database));
			System.out.println("DB connected");
		} catch (SQLException e){
			System.err.println("DB connection failed.");
		} catch (IllegalAccessException e) {
			System.err.println("Driver init failed.");
		} catch (InstantiationException e) {
			System.err.println("Driver init failed.");
		} catch (ClassNotFoundException e) {
			System.err.println("Driver class not found.");
		}
	}
	
	public void dropTable(String table) {
		try {
			Statement s = connection.createStatement();
			s.executeUpdate(String.format("DROP TABLE IF EXISTS %s",table));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void createTable(String definition) {
		
		try {
			Statement s = connection.createStatement();
			s.executeUpdate(definition);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void update(String query) {
		
		try {
			Statement s = connection.createStatement();
			s.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public List select(String query) {

		List result = null;
		
		try {
			QueryRunner queryRunner = new QueryRunner();
			result = (List)queryRunner.query(connection, query, new MapListHandler());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public void close() {
		if (connection != null) {
			try {
				connection.close();
				System.out.println("DB closed");
			} catch (SQLException e){
				System.err.println("Error while closing DB");
			}
		}
	}
}
