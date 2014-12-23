package com.amit.enterprise.rest.javaneturlclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


//  Virtual CLient for testing webservice
public class CurrentStatus {
	
	private static final String targetURL = "http://192.168.1.13:8067/JerseyJSONExample/rest/jsonServices/GetCurrentStatus";
	
	public static void main(String[] args) {
		 
		try {
			
			URL targetUrl = new URL(targetURL);
			
			URL url = new URL(targetURL);
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setUseCaches(false);
			httpCon.setAllowUserInteraction(false);
			System.out.println(httpCon.getResponseCode());
			System.out.println(httpCon.getResponseMessage());
			
			BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(
					(httpCon.getInputStream())));
	 
			String output;
			System.out.println("Output from Server:\n");
			while ((output = responseBuffer.readLine()) != null) {
				System.out.println(output);
			}
	 
			httpCon.disconnect();
	 
		  } catch (MalformedURLException e) {
	 
			e.printStackTrace();
	 
		  } catch (IOException e) {
	 
			e.printStackTrace();
	 
		 }
	 
		}
	 
}