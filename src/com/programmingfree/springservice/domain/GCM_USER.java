package com.programmingfree.springservice.domain;

import java.sql.Timestamp;



public class GCM_USER {

	
	String gcm_regid;
	String name;
	String email;
	String imei;
	Timestamp created_at;
	public String getGcm_regid() {
		return gcm_regid;
	}
	public void setGcm_regid(String gcm_regid) {
		this.gcm_regid = gcm_regid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public Timestamp getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}
	
	
}
