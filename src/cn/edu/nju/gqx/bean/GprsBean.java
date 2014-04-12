package cn.edu.nju.gqx.bean;

import java.sql.Timestamp;
import java.util.List;

import cn.edu.nju.gqx.db.po.Gprs;

public class GprsBean implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1004843137544226418L;
	private Integer id;
	private String name;
	private String mac;
	private String ip;
	private Integer voltage;
	private Integer temperature;
	private Integer humidity;
	private Timestamp update_time;
	private List<ZigbeeBean> zigbeelist;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getVoltage() {
		return voltage;
	}
	public void setVoltage(Integer voltage) {
		this.voltage = voltage;
	}
	public Integer getTemperature() {
		return temperature;
	}
	public void setTemperature(Integer temperature) {
		this.temperature = temperature;
	}
	public Integer getHumidity() {
		return humidity;
	}
	public void setHumidity(Integer humidity) {
		this.humidity = humidity;
	}
	public Timestamp getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Timestamp update_time) {
		this.update_time = update_time;
	}
	public List<ZigbeeBean> getZigbeelist() {
		return zigbeelist;
	}
	public void setZigbeelist(List<ZigbeeBean> zigbeelist) {
		this.zigbeelist = zigbeelist;
	}
	
	public void setGprs(Gprs gprs){
		this.id = gprs.getId();
		this.mac = gprs.getMac();
		this.ip = gprs.getIp();
		this.name = gprs.getName();
		this.humidity = gprs.getHumidity();
		this.temperature = gprs.getTemperature();
		this.update_time = gprs.getUpdate_time();
		this.voltage = gprs.getVoltage();
	}
	
}
