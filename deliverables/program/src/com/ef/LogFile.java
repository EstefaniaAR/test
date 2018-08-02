package com.ef;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
public class LogFile 
{
	private int id;
	private String date;
	private String ip;
	private String request;
	private String status;
	private String userAgent;
	private String comment;
	
	/**
	 * 
	 * @param path
	 * @return List with the parsed log
	 */
	public List<LogFile>getFile(String path)
	{
		List <LogFile> res = new ArrayList<LogFile>();
		BufferedReader b = null;
		try
		{
			b =  Files.newBufferedReader(Paths.get(path));
			String readLine = "";
			while ((readLine = b.readLine()) != null)
			{
				String [] line = readLine.split("\\|");
				if(line.length == 5)
				{
					LogFile f = new LogFile();
					f.setDate(line[0]);
					f.setIp(line[1]);
					f.setRequest(line[2]);
					f.setStatus(line[3]);
					f.setUserAgent(line[4]);
					res.add(f);
				}
	        }
			b.close();
		}
		catch (IOException e)
		{
            e.printStackTrace();
        }
		finally 
		{
			try 
			{
				if(b!=null)
					b.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		return res;
	}
	
	/**
	 * @param startDate
	 * @param duration
	 * @param file
	 * @return a List with all the request between the first date and that date plus the duration
	 */
	public List<LogFile>getLogFilesRange(String startDate, int duration,List<LogFile>file)
	{
		List<LogFile>res = new ArrayList<LogFile>();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
		DateTimeFormatter dtfLog = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		
		LocalDateTime localStartDate = LocalDateTime.parse(startDate,dtf);
		LocalDateTime localEndDate = localStartDate.plusHours(duration);
		for(LogFile f :file)
		{
			
			LocalDateTime fDate = LocalDateTime.parse(f.getDate(),dtfLog);
			if(fDate.isAfter(localStartDate)&& fDate.isBefore(localEndDate))
			{
				res.add(f);
			}
		}
		return res;
	}
	/**
	 * 
	 * @param res
	 * @param threshold
	 * @return return a list of IP that made a request base on the threshold and their number of request
	 */
	public Map<String,Long>getMostFrecuentIPs(List<LogFile>res,int threshold)
	{
		Map<String,Long>result=new HashMap<String,Long>();
		Map<String, Long> counting = res.stream().collect(
                Collectors.groupingBy(LogFile::getIp, Collectors.counting()));
		for (Map.Entry<String, Long> entry : counting.entrySet()) 
		{
	        if(entry.getValue()>=threshold)
	        {
	        	result.put(entry.getKey(), entry.getValue());
	        }
	    }
		return result;
	}

	/**
	 * 
	 * @param file
	 * @return insert a Log File to mysql database
	 */
	public boolean insertLogFile(LogFile file)
	{
		boolean res=false;
		MyConnection con = new MyConnection();
		Connection conn =null;
		try 
		{
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			Date date=format.parse(file.getDate());
			
			String query = " INSERT INTO log "
					+ "                 ("
					+ "                  	  date"
					+ "                  	, ip"
					+ "					 	, request"
					+ "                  	, status"
					+ "                 	, userAgent"
					+ "						, comment"
					+ "                  )"
			        + " VALUES 			("
			        + "						  ?"
			        + "						, ?"
			        + "						, ?"
			        + "						, ?"
			        + "                     , ?"
			        + "						, ?"
			        + "					)";
			
			conn = con.getConnection();
			PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setTimestamp(1, new java.sql.Timestamp(date.getTime()));
		    preparedStmt.setString (2, file.getIp());
		    preparedStmt.setString (3, file.getRequest());
		    preparedStmt.setString (4, file.getStatus());
		    preparedStmt.setString (5, file.getUserAgent());
		    if(file.getComment()!=null)
		    	preparedStmt.setString (6, file.comment);
		    else
		    	preparedStmt.setString (6, "");
		    res=preparedStmt.execute();
		    conn.close();
		} 
		catch (Exception e) 
		{
			System.out.println("Unable to insert "+file);
			e.printStackTrace();
		}
		finally
		{
			
			try 
			{
				if(conn!=null)
					conn.close();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		
		return res;
	}
	/**
	 * 
	 * @param frecIP
	 * @param file
	 * @return return a map with the IP given and all its occurrences
	 */
	public Map<String, List<LogFile>>getMostFrecuentLogFileByIP(Map<String,Long>frecIP,List<LogFile> file )
	{
		Map<String, List<LogFile>> groups = file.stream().collect(Collectors.groupingBy(LogFile::getIp));
		Map<String, List<LogFile>> res = new HashMap<String,List<LogFile>>();
		for (Map.Entry<String, Long> entry : frecIP.entrySet()) 
		{
	        res.put(entry.getKey(), groups.get(entry.getKey()));
	    }
		return res;
		
	}
	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public String getRequest() {
		return request;
	}


	public void setRequest(String request) {
		this.request = request;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getUserAgent() {
		return userAgent;
	}


	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "LogFile [id=" + id + ", date=" + date + ", ip=" + ip + ", request=" + request + ", status=" + status
				+ ", userAgent=" + userAgent + ", comment=" + comment + "]";
	}


}
