package cn.edu.nju.gqx.db.po;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Zigbee")
public class Zigbee implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7193983493385027889L;
	
	private Integer id;
	private String name;
	private Integer gid;
	private String mac;
	private Integer ztype;
	private Timestamp update_time;
	
	@Id
	@GeneratedValue
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
	public Integer getZtype() {
		return ztype;
	}
	public void setZtype(Integer ztype) {
		this.ztype = ztype;
	}
	public Timestamp getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Timestamp update_time) {
		this.update_time = update_time;
	}
	
	

}
