package com.bapcraft.bclotto;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsernameFetcher {

	private static final String MOJANG_API_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s";
	//private static final String STRINGS_REGEX = "/\\\"[A-z|0-9]+\\\"/g";
	
	/**
	 * 
	 * @param uuid The player's UUID.
	 * @return
	 * @throws Exception
	 */
	public static String getUsername(UUID uuid) throws Exception {
		
		String uuidAsString = uuid.toString().toLowerCase();
		String bareUUID =
				uuidAsString.substring(0, 8) 
				+ uuidAsString.substring(9, 13)
				+ uuidAsString.substring(14, 18)
				+ uuidAsString.substring(19, 23)
				+ uuidAsString.substring(24, 36);
		
		System.out.println(bareUUID);
		
		URL url = new URL(String.format(MOJANG_API_URL, bareUUID));
		
		System.out.println(url.getPath());
		
		URLConnection con = url.openConnection();
		con.connect();
		
		InputStream is = con.getInputStream();
		
		StringBuilder response = new StringBuilder();
		int last; // You'll find out later.
		while ((last = is.read()) != -1) response.append((char) last); // Eww.
		
		return doRegexMagic(response.toString());
		
	}
	
	private static String doRegexMagic(String response) {
		
		/*
		 * HERE: http://txt2re.com/index-java.php3
		 * 
		 * This class took me an hour to make, mainly because regexes are hard. 
		 * 
		 */
		
		String re1=".*?";					// Non-greedy match on filler
	    String re2="(?:[a-z][a-z0-9_]*)";	// Uninteresting: var
	    String re3=".*?";					// Non-greedy match on filler
	    String re4="(?:[a-z][a-z0-9_]*)";	// Uninteresting: var
	    String re5=".*?";					// Non-greedy match on filler
	    String re6="(?:[a-z][a-z0-9_]*)";	// Uninteresting: var
	    String re7=".*?";					// Non-greedy match on filler
	    String re8="((?:[a-z][a-z0-9_]*))";	// Variable Name 1

	    Pattern p = Pattern.compile(re1+re2+re3+re4+re5+re6+re7+re8, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    Matcher m = p.matcher(response);
	    if (m.find()) {
	        return m.group(1);
	    } else {
	    	System.out.println("Something happened.  It doesn't matter at this point.");
	    	return "8====D";
	    }
	    
	}
	
}
