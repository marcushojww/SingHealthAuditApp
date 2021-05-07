package com.c2g4.SingHealthWebApp.SystemTests;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Random;

public final class TestResources {
	
	final static Random rand = new Random();
	
	final static String TENANT = "TENANT";
	final static String AUDITOR = "AUDITOR";
	final static String MANAGER = "MANAGER";
	final static String RANDOM = "RANDOM";
	
	public static Map.Entry<String, String> getCredentials(String type) {
		
		String username = null;
		String password = null;
		
		switch(type.toUpperCase()) {
			case "TENANT":
				username = "ZHZHZH";
				password = "test123";
				break;
			case "AUDITOR":
				username = "hannz";
				password = "test123";
				break;
			case "MANAGER":
				username = "mcMarcus";
				password = "test123";
				break;
			default:
				byte[] random_bytes = new byte[rand.nextInt(64)];
				rand.nextBytes(random_bytes);
				username = new String(random_bytes);
				random_bytes = new byte[rand.nextInt(64)];
				rand.nextBytes(random_bytes);
				password = new String(random_bytes);
		}
		return new AbstractMap.SimpleEntry<String, String>(username, password);
	}
}
