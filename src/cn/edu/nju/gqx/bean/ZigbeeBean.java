package cn.edu.nju.gqx.bean;

import java.sql.Timestamp;
import java.util.List;

import cn.edu.nju.gqx.db.po.Zigbee;

public class ZigbeeBean implements java.io.Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -487422899566899373L;
	private Integer id;
	private String name;
	private Integer gid;
	private String mac;
	private Timestamp update_time;
	private Zigbee zigbee;
	private List<?> SwitchList;
	private Integer ztype;
	
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
	public Integer getGid() {
		return gid;
	}
	public void setGid(Integer gid) {
		this.gid = gid;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public Timestamp getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Timestamp update_time) {
		this.update_time = update_time;
	}
	public List getSwitchList() {
		return SwitchList;
	}
	public void setSwitchList(List switchList) {
		SwitchList = switchList;
	}
	public Zigbee getZigbee() {
		return zigbee;
	}
	public Integer getZtype() {
		return ztype;
	}
	public void setZtype(Integer ztype) {
		this.ztype = ztype;
	}
	public void setZigbee(Zigbee zigbee) {
		this.zigbee = zigbee;
		this.gid = zigbee.getGid();
		this.id = zigbee.getId();
		this.mac = zigbee.getMac();
		this.name = zigbee.getName();
		this.update_time = zigbee.getUpdate_time();
		this.ztype = zigbee.getZtype();
	}
	
}
