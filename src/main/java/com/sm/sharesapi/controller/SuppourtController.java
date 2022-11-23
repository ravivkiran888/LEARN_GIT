package com.sm.sharesapi.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sm.sharesapi.StocksList;
import com.sm.sharesapi.SuppourtScript;
import com.sm.sharesapi.SuppourtScriptResponse;

@RestController
public class SuppourtController {

	private static final String S1CLASS = "table-primary";
	private static final String S2CLASS = "table-secondary";
	private static final String S3CLASS = "table-success";
	
	@PostMapping(value = "/suppourt" , consumes = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<SuppourtScriptResponse> getSuppourt(@RequestBody StocksList stocksList) throws SQLException {
		
		ArrayList<SuppourtScriptResponse> responseToSendList = null;
		try {
			ArrayList<SuppourtScript> scriptList = 	stocksList.getSuppourtScriptList();
		    responseToSendList = 	buildResponse(scriptList);
			}
		catch (Exception e) {
			e.printStackTrace();
		}
		return responseToSendList;
	}
	
	
	
	
	public ArrayList<SuppourtScriptResponse> buildResponse(ArrayList<SuppourtScript> scriptList) throws SQLException
	{
	
		Connection connection = null;
		PreparedStatement psSelect = null;
		ResultSet rs;

		ArrayList<SuppourtScriptResponse> supportList = new ArrayList<SuppourtScriptResponse>();
		
		String sqlstatement = "select s.stockName , s.pp , s.s1 , s.s2 , s.s3  from stocks s where s.stockName = ?";
		
		try
		{
		
		Class.forName("com.mysql.cj.jdbc.Driver"); 
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sm", "root", "root");
		psSelect = connection.prepareStatement(sqlstatement);
		
		
		for(SuppourtScript suppScript: scriptList)
		{
			String name = suppScript.getScripcode();
			String ltp = suppScript.getLtp(); 
			double ltpDouble = Double.parseDouble(ltp);
			String open = suppScript.getOpen();
			String diff = suppScript.getDiff();
			
			SuppourtScriptResponse scriptRes = new SuppourtScriptResponse();
			
			psSelect.setString(1, name);
			
			rs = psSelect.executeQuery();     
			
			while (rs.next()) {
				
				double pp = rs.getDouble("pp");
				double s1 = rs.getDouble("s1");
				double s2 = rs.getDouble("s2");
				double s3 = rs.getDouble("s3");
							
				String className = generateClassName(s1, s2 , s3 , ltpDouble);
				
				String suppourtLevel = getSuppourtLevel(className);
				
				scriptRes.setName(name);
				scriptRes.setLtp(ltpDouble);
				scriptRes.setPp(pp);
				scriptRes.setS1(s1);
				scriptRes.setS2(s2);
				scriptRes.setS3(s3);
				scriptRes.setOpen(Double.parseDouble(open));
				scriptRes.setDiff(Double.parseDouble(diff));
				scriptRes.setClassName(className);
				scriptRes.setSuppourt(suppourtLevel);
				
			}
			
			
			
			
			if(!scriptRes.getClassName().isEmpty())
			supportList.add(scriptRes);
			
		}
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		finally
		{
			if(connection != null)
					connection.close();
			
			if(psSelect != null)
				psSelect.close();
		
		}
		
		return supportList;
	}
	
	
	
	public String generateClassName(double s1 , double s2 , double s3, double ltp)
	{
		String className = "";
		
		double s1Level = Math.abs(ltp - s1);	
		double s2Level = Math.abs(ltp - s2);
		double s3Level = Math.abs(ltp - s3);
		
		
		if(s1Level <= 2)
		{
			className = S1CLASS;
		}
		
		else if(s2Level <= 2)
		{
			className = S2CLASS;
		}
		
		else if(s3Level <= 2)
		{
			className = S3CLASS;
		}
		
		return className;
	}
	
	
	public String getSuppourtLevel(String className)
	{
		
		String suppourt = "";
		
		if(className.equals(S1CLASS))
		{
			suppourt = "S1" ;
		}
		
		else if(className.equals(S2CLASS))
		{
			suppourt = "S2" ;
		}
		
		else if(className.equals(S3CLASS))
		{
			suppourt = "S3" ;
		}
		
		return suppourt ;
	}
	

}
