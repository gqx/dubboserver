package cn.edu.nju.gqx.db.po;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Turntask")
public class Turntask implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8605935334071620277L;
	private Integer id;
	private Integer ttid;
	private String ttname;
	private Integer grpid;
	private String sysname;
	private String start_time;
	private String end_time;
	private Integer token;
	private Integer state;
	private String duration;
	private String execute_date;
	
	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTtid() {
		return ttid;
	}
	public void setTtid(Integer ttid) {
		this.ttid = ttid;
	}
	public String getTtname() {
		return ttname;
	}
	public void setTtname(String ttname) {
		this.ttname = ttname;
	}
	public Integer getGrpid() {
		return grpid;
	}
	public void setGrpid(Integer grpid) {
		this.grpid = grpid;
	}
	public String getSysname() {
		return sysname;
	}
	public void setSysname(String sysname) {
		this.sysname = sysname;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public Integer getToken() {
		return token;
	}
	public void setToken(Integer token) {
		this.token = token;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getExecute_date() {
		return execute_date;
	}
	public void setExecute_date(String execute_date) {
		this.execute_date = execute_date;
	}
	
	

}
