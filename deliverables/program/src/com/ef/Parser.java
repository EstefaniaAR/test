package com.ef;

import java.util.List;
import java.util.Map;

public class Parser 
{
	public static void main (String ...args)
	{
		/*
		args = new String[]{
	            "--startDate=2017-01-01.00:00:00", 
	            "--duration=daily",
	            "--threshold=500",
	            "--accesslog=C:\\Users\\Fanny\\Documents\\WALLETHUB\\access.log"};
	     */
		Options opt = new Options();
		LogFile lf = new LogFile();
		Map<String, String>options = opt.checkOptions(args);
		List<LogFile>res =null;
		if(!options.containsKey("INVALID"))
		{
			
			if(options.containsKey("--startDate") 
					&& options.containsKey("--threshold") 
					&& options.containsKey("--duration")
					&& options.containsKey("--accesslog"))
			{
				res=lf.getFile(options.get("--accesslog"));
				int duration = opt.getDuration(options.get("--duration"));
				int threshold = Integer.parseInt(options.get("--threshold"));
				res = lf.getLogFilesRange(options.get("--startDate"), duration,res);
				Map<String,Long>ips=lf.getMostFrecuentIPs(res,threshold);
				Map<String,List<LogFile>>regs=lf.getMostFrecuentLogFileByIP(ips, res);
				System.out.println("The Most frecuent IP's that made any request:");
				for (Map.Entry<String, Long> entry : ips.entrySet()) 
				{
					System.out.println("IP:"+entry.getKey()+", Request Number:"+entry.getValue());
				}
				if(regs.size()>0)
				{
					System.out.println("Inserting log file...");
					for (Map.Entry<String, List<LogFile>> entry : regs.entrySet()) 
					{
						for(LogFile f:entry.getValue())
						{
							//Code to find only the bad request and insert them
							if(ErrorCode.errorCode.containsKey(Integer.valueOf(f.getStatus())))
							{
								f.setComment(ErrorCode.errorCode.get(Integer.valueOf(f.getStatus())));
								lf.insertLogFile(f);
								System.out.println(f);
							}
							//End of the code to find only the bad request and insert them
							
							/* Code to find all the good and bad request and insert them
							f.setComment(ErrorCode.errorCode.get(Integer.valueOf(f.getStatus())));
							lf.insertLogFile(f);
							System.out.println(f);
							*/
						}
					}
					System.out.println("End of the process");
				}
				else
				{
					System.out.println("No request found with the given criteria");
				}
				
			}
			
		}
		

	}

}

