package com.amit.enterprise.rest.javaneturlclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


//Virtual CLient for testing webservice
public class LoginCheck {
	
	private static final String targetURL = "http://192.168.1.13:8067/ServerAndroidWebservice/rest/jsonServices/logincheck";
	
	public static void main(String[] args) {
		 
		try {
			 
			URL targetUrl = new URL(targetURL);
			
			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");
	 
//			String input = "{\"first_name\":\"fcc\",\"last_name\":\"ccc\",\"uname\":\"whatson\",\"pass\":\"y\"}";
	
			String input = "{\"uname\":\"kk\",\"pass\":\"kk\"}";
			System.out.println("thhhhh "+input);
			OutputStream outputStream = httpConnection.getOutputStream();
			outputStream.write(input.getBytes());
			outputStream.flush();
	 
			if (httpConnection.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ httpConnection.getResponseCode());
			}
	 
			BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(
					(httpConnection.getInputStream())));
	 
			String output;
			System.out.println("Output from Server:\n");
			while ((output = responseBuffer.readLine()) != null) {
				System.out.println(output);
			}
	 
			httpConnection.disconnect();
	 
		  } catch (MalformedURLException e) {
	 
			e.printStackTrace();
	 
		  } catch (IOException e) {
	 
			e.printStackTrace();
	 
		 }
	 
		}
	 
}