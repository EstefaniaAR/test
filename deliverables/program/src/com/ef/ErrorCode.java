package com.ef;

import java.util.HashMap;
import java.util.Map;

public class ErrorCode 
{
	public static final Map<Integer,String>errorCode;
	static
	{
		errorCode = new HashMap<Integer, String>();
		errorCode.put(402,"Payment_Required");
		errorCode.put(403,"Forbidden");
		errorCode.put(404,"Not_Found");
		errorCode.put(404,"Method_Not_Allowed ");
		errorCode.put(406,"Not_Acceptable");
		errorCode.put(407,"Proxy_Authentication_Required");
		errorCode.put(408,"Request_Timeout");
		errorCode.put(409,"Conflict");
		errorCode.put(410,"Gone");
		errorCode.put(411,"Length_Required");
		errorCode.put(412,"Precondition_Failed");
		errorCode.put(413,"Payload_Too_Large");
		errorCode.put(414,"URI_Too_Long");
		errorCode.put(415,"Unsupported_Media_Type");
		errorCode.put(416,"Range_Not_Satisfiable");
		errorCode.put(417,"Expectation_Failed");
		errorCode.put(418,"Im_a_teapot");
		errorCode.put(419,"Misdirected_Request");
		errorCode.put(422,"Unprocessable_Entity");
		errorCode.put(423,"Locked");
		errorCode.put(424,"Failed_Dependency");
		errorCode.put(426,"Upgrade_Required");
		errorCode.put(428,"Precondition_Required");
		errorCode.put(429,"Too_Many_Requests");
		errorCode.put(431,"Request_Header_Fields_Too_Large");
		errorCode.put(451,"Unavailable_For_Legal_Reasons ");
	}
	
	
}
