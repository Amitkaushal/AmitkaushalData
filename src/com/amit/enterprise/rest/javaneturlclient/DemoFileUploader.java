package com.amit.enterprise.rest.javaneturlclient;

import java.io.File;

import java.io.File;
 
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
public class DemoFileUploader { public static void main(String args[]) throws Exception
{
    DemoFileUploader fileUpload = new DemoFileUploader () ;
    File file = new File("C:/Users/Android/Desktop/2014-09-18_1503.png") ;
    //Upload the file
    fileUpload.executeMultiPartRequest("http://localhost:8068/SpringServiceJsonSample/service/user/upload", 
            file, file.getName(), "File Uploaded :: Tulips.jpg") ;
}  
 
public void executeMultiPartRequest(String urlString, File file, String fileName, String fileDescription) throws Exception 
{
    HttpClient client = new DefaultHttpClient() ;
    HttpPost postRequest = new HttpPost (urlString) ;
    try
    {
        //Set various attributes 
        MultipartEntity multiPartEntity = new MultipartEntity () ;
        multiPartEntity.addPart("fileDescription", new StringBody(fileDescription != null ? fileDescription : "")) ;
        multiPartEntity.addPart("fileName", new StringBody(fileName != null ? fileName : file.getName())) ;

        FileBody fileBody = new FileBody(file, "application/octect-stream") ;
        //Prepare payload
        multiPartEntity.addPart("attachment", fileBody) ;

        //Set to request body
        postRequest.setEntity(multiPartEntity) ;
         
        //Send request
        HttpResponse response = client.execute(postRequest) ;
         
        //Verify response if any
        if (response != null)
        {
//            System.out.println(response.getStatusLine().getStatusCode());
        	System.out.println(response);
        }
    }
    catch (Exception ex)
    {
        ex.printStackTrace() ;
    }
}}