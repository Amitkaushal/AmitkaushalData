package com.programmingfree.springservice.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.annotation.MultipartConfig;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.programmingfree.springservice.dao.UserService;
import com.programmingfree.springservice.domain.GCM_USER;
import com.programmingfree.springservice.domain.User;


@RestController
@MultipartConfig(fileSizeThreshold=1024*1024*10,    // 10 MB
maxFileSize=1024*1024*50,          // 50 MB
maxRequestSize=1024*1024*100)
@RequestMapping("/service/user/")
public class SpringServiceController {
		
	UserService userService=new UserService();
	String message;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET,headers="Accept=application/json")
	public User getUser(@PathVariable int id) {
		User user=userService.getUserById(id);
		return user;
	}
	
	
	
	@RequestMapping(method = RequestMethod.GET,headers="Accept=application/json")
	public List<User> getAllUsers() {
		List<User> users=userService.getAllUsers();
		
		return users;
	}
	@RequestMapping(value="userdata",method = RequestMethod.GET,headers="Accept=application/json")
	public String getUserData() {
		List<GCM_USER> users=userService.getUserData();
		JSONObject mainObj = new JSONObject();
		try{	JSONArray ja = new JSONArray();
		for (int i = 0; i < users.size(); i++) {
			
				JSONObject jo = new JSONObject();
				jo.put("regid", users.get(i).getGcm_regid());
				jo.put("name", users.get(i).getName());
				jo.put("imei", users.get(i).getImei());
			
				ja.put(jo);
			
			
		}
		
		mainObj.put("Android", ja);
		}catch(Exception e){}
		return mainObj+"";
		
	}
	@RequestMapping( value="person",method = RequestMethod.POST,headers="Accept=application/json")
	  public @ResponseBody User post( @RequestBody final  User person) {    
	 
	      System.out.println("yo bingo"+person.getFirstName() + " " + person.getLastName());
	      return person;
	  }
	
	@RequestMapping( value="sendpush",method = RequestMethod.POST,headers="Accept=application/json")
	  public @ResponseBody String sendpush( @RequestBody final  String person) {    
		org.json.simple.JSONObject jsonObject=new  org.json.simple.JSONObject();
		JSONParser jsonParser = new JSONParser();
		  try {
			jsonObject = (org.json.simple.JSONObject) jsonParser.parse(person);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
if(jsonObject!=null)
{
	GCM_USER user_origin = new GCM_USER();
	user_origin.setImei(jsonObject.get("imei")+"");
	GCM_USER user = new GCM_USER();
	user=userService.getIMEIUser(user_origin);
	if(user.getName()!=null)
	{
		message=user.getName()+"^"+jsonObject.get("imei")+"^"+jsonObject.get("message");
		
		userService.send_push_notification(user.getGcm_regid(), message);
		
	}
//	 System.out.println("yo bingo"+jsonObject.get("name")+ " " + jsonObject.get("message"));
}
	     
	      return person;
	  }
	@RequestMapping( value="store_gcm_user",method = RequestMethod.POST,headers="Accept=application/json")
	  public @ResponseBody String store_gcm_user( @RequestBody final  GCM_USER gcm_user) {    
	 
		if(userService.isUserExisted(gcm_user))
		{
			return "Already Registered";
		}
		else{
			try{userService.addGCMUSER(gcm_user);}catch(Exception e){}
		}
		
	      return "Registered Successfuly";
	  }
	@RequestMapping( value="validate_device",method = RequestMethod.POST,headers="Accept=application/json")
	  public @ResponseBody String validate_device( @RequestBody final  GCM_USER gcm_user) {    
	
		GCM_USER user = new GCM_USER();
		user=userService.getIMEIUser(gcm_user);
		if(user.getName()!=null)
		{
			try{
				JSONObject jo = new JSONObject();
				jo.put("regid", user.getGcm_regid());
				jo.put("name", user.getName());
				jo.put("email", user.getEmail());
				jo.put("imei", user.getImei());
				jo.put("status", "update");
				JSONArray ja = new JSONArray();
				ja.put(jo);
				JSONObject mainObj = new JSONObject();
				mainObj.put("Android", ja);
				return mainObj+"";
			}catch(Exception e){}
			
		
		}
		JSONObject mainObj = new JSONObject();
			try{
				JSONObject jo = new JSONObject();
				jo.put("regid", "");
				jo.put("name", "");
				jo.put("email", "");
				jo.put("imei", "");
				jo.put("status", "install");
				JSONArray ja = new JSONArray();
				ja.put(jo);
				mainObj.put("Android", ja);
			}catch(Exception e){}
		return mainObj+"";
	  }
	
	 @RequestMapping(value="/upload", method=RequestMethod.GET)
	    public @ResponseBody String provideUploadInfo() {
	        return "You can upload a file by posting to this same URL.";
	    }

	    @RequestMapping(value="/upload", method=RequestMethod.POST)
	    public @ResponseBody String handleFileUpload(@RequestParam("name") String name, 
	            @RequestParam("file") MultipartFile file){
	        if (!file.isEmpty()) {
	            try {
	                byte[] bytes = file.getBytes();
	                BufferedOutputStream stream = 
	                        new BufferedOutputStream(new FileOutputStream(new File(name + "-uploaded")));
	                stream.write(bytes);
	                stream.close();
	                System.out.println("You successfully uploaded " + name + " into " + name + "-uploaded !");
	                return "You successfully uploaded " + name + " into " + name + "-uploaded !";
	            } catch (Exception e) {
	                return "You failed to upload " + name + " => " + e.getMessage();
	            }
	        } else {
	            return "You failed to upload " + name + " because the file was empty.";
	        }
	    }
	
}