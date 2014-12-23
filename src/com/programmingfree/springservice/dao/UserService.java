package com.programmingfree.springservice.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.programmingfree.springservice.domain.GCM_USER;
import com.programmingfree.springservice.domain.User;
import com.programmingfree.springservice.utility.DBUtility;



public class UserService {
	
	private Connection connection;
	private static final long serialVersionUID = 1L;

	// Put your Google API Server Key here
	private static final String GOOGLE_SERVER_KEY = "AIzaSyDsgSYLEUUUxSjGMVmosWddgRMa054non0";
	static final String MESSAGE_KEY = "message";
	public UserService() {
		connection = DBUtility.getConnection();
	}
	
	public int getUserCount(){
		int count=0;
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select count(*) as count from tblUser");		
			while (rs.next()) {
				count=rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public void addUser(User user) {
		try {
			
			PreparedStatement preparedStatement = connection
					.prepareStatement("insert into tblUser(userid,firstname,lastname,email) values (?,?, ?, ? )");
			// Parameters start with 1
			preparedStatement.setInt(1, user.getUserid());
			preparedStatement.setString(2, user.getFirstName());
			preparedStatement.setString(3, user.getLastName());			
			preparedStatement.setString(4, user.getEmail());
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addGCMUSER(GCM_USER gcm_user) {
		try {
			java.util.Date date = new Date();
			Timestamp timestamp = new Timestamp(date.getTime());
			gcm_user.setCreated_at(timestamp);
			PreparedStatement preparedStatement = connection
					.prepareStatement("insert into gcm_users(gcm_regid,name,email,imei,created_at) values (?,?,?,?,?)");
			// Parameters start with 1
			preparedStatement.setString(1, gcm_user.getGcm_regid());
			preparedStatement.setString(2, gcm_user.getName());
			preparedStatement.setString(3, gcm_user.getEmail());			
			preparedStatement.setString(4, gcm_user.getImei());
			preparedStatement.setTimestamp(5,gcm_user.getCreated_at());
			preparedStatement.executeUpdate();
			

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public GCM_USER getIMEIUser(GCM_USER gcm_user)
	{
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * FROM gcm_users where imei='"+gcm_user.getImei()+"'");
			if (rs.next()) {
				GCM_USER user = new GCM_USER();
				user.setGcm_regid(rs.getString("gcm_regid"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));				
				user.setImei(rs.getString("imei"));
		return user;
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		GCM_USER user = new GCM_USER();
		user.setName(null);
		return user;
	}
	
	public void send_push_notification(String gcm_regid,String gcm_message)
	{
		Result result = null;
		String share = "Bingo";
		// GCM RedgId of Android device to send push notification
		String regId = gcm_regid;
	

			try {
				String userMessage = gcm_message;
				Sender sender = new Sender(GOOGLE_SERVER_KEY);
				Message message = new Message.Builder().timeToLive(30)
						.delayWhileIdle(true).addData(MESSAGE_KEY, userMessage).build();
				System.out.println("regId: " + regId);
				result = sender.send(message, regId, 1);
				System.out.println("Result andy "+result);
			} catch (IOException ioe) {
				ioe.printStackTrace();
				
			} catch (Exception e) {
				e.printStackTrace();
			
			}
			
	
	}
	
	public boolean isUserExisted(GCM_USER gcm_user)
	{
		try {
			PreparedStatement preparedStatement = connection.
					prepareStatement("SELECT email from gcm_users WHERE email = ? and gcm_regid = ?");
			preparedStatement.setString(1, gcm_user.getEmail());
			preparedStatement.setString(2, gcm_user.getGcm_regid());
			ResultSet rs = preparedStatement.executeQuery();
			
			if (rs.next()) {
				return true;
			}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void deleteUser(int userId) {
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("delete from tblUser where userid=?");
			// Parameters start with 1
			preparedStatement.setInt(1, userId);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateUser(User user) throws ParseException {
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("update tblUser set lastname=?,email=?" +
							"where userid=?");
			// Parameters start with 1			
			preparedStatement.setString(1, user.getLastName());
			preparedStatement.setString(2, user.getEmail());			
			preparedStatement.setInt(3, user.getUserid());
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<User> getAllUsers() {
		List<User> users = new ArrayList<User>();
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from tblUser limit 15");
			while (rs.next()) {
				User user = new User();
				user.setUserid(rs.getInt("userid"));
				user.setFirstName(rs.getString("firstname"));
				user.setLastName(rs.getString("lastname"));				
				user.setEmail(rs.getString("email"));
				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users;
	}
	public List<GCM_USER> getUserData() {
		List<GCM_USER> users = new ArrayList<GCM_USER>();
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from gcm_users");
			while (rs.next()) {
				GCM_USER user = new GCM_USER();
				user.setImei(rs.getString("imei"));
				user.setGcm_regid(rs.getString("gcm_regid"));
				user.setName(rs.getString("name"));				
				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users;
	}	
	public User getUserById(int userId) {
		User user = new User();
		try {
			PreparedStatement preparedStatement = connection.
					prepareStatement("select * from tblUser where userid=?");
			preparedStatement.setInt(1, userId);
			ResultSet rs = preparedStatement.executeQuery();
			
			if (rs.next()) {
				user.setUserid(rs.getInt("userid"));
				user.setFirstName(rs.getString("firstname"));
				user.setLastName(rs.getString("lastname"));
				
				user.setEmail(rs.getString("email"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

}

