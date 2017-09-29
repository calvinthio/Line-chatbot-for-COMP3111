package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.io.IOException;
import java.net.URI;

@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	@Override
	String search(String text) throws Exception {
		//Write your code here
		
		String result = null;
		Connection connection = getConnection();
		PreparedStatement stmt = connection.prepareStatement("SELECT response FROM responsetable where LOWER(keyword)=LOWER(?)");
		stmt.setString(1, text);
		ResultSet rs = stmt.executeQuery();
		
		
		while(rs.next())
		{
				result =  rs.getString(1);
			
		} 
		
		rs.close();
		stmt.close(); 
		connection.close();
		
	
		if (result != null)
			return result;
		throw new Exception("NOT FOUND");
		
	}
	
	
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}

}
