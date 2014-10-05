package cn.edu.nju.gqx.db.po;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Pressure")
public class Pressure implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2982785003736451778L;

	private Integer id;
	private Integer sid;
	private Integer pvalue;
	private Timestamp update_time;
	
	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public Integer getPvalue() {
		return pvalue;
	}
	public void setPvalue(Integer pvalue) {
		this.pvalue = pvalue;
	}
	public Timestamp getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Timestamp update_time) {
		this.update_time = update_time;
	}
	
	
}
