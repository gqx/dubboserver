package cn.edu.nju.gqx.db.dao;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import cn.edu.nju.gqx.db.po.Gprs;
import cn.edu.nju.gqx.db.util.HibernateUtil;

@Component("gprsDao")
@Scope("prototype")
public class GprsDao {
	
	public int createGprs(String mac,String name,String ip,Integer voltage,Integer temperature,Integer humidity){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Gprs gprs = new Gprs();
		gprs.setName(name);
		gprs.setMac(mac);
		gprs.setIp(ip);
		gprs.setVoltage(voltage);
		gprs.setTemperature(temperature);
		gprs.setHumidity(humidity);
		
		session.save(gprs);
		session.getTransaction().commit();
		return gprs.getId();
	}
	
	public int updateByMac(String mac,String name,String ip,Integer voltage,Integer temperature,Integer humidity){
		Gprs gprs = getByMac(mac);
		if(gprs == null){
			return -1;
		}
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		gprs.setName(name);
		gprs.setMac(mac);
		gprs.setIp(ip);
		gprs.setVoltage(voltage);
		gprs.setTemperature(temperature);
		gprs.setHumidity(humidity);
		
		session.save(gprs);
		session.getTransaction().commit();
		return gprs.getId();
		
	}
	
	
	public Gprs getByMac(String mac){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Gprs where mac=?");
		query.setString(0, mac);
		List<?> list = query.list();
		Gprs gprs = null;
		if(list != null && list.size() != 0){
			gprs = (Gprs)list.get(0);
		}
		session.getTransaction().commit();
		return gprs;
	}
	
	public Gprs getByName(String name){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Gprs where name=?");
		query.setString(0, name);
		List<?> list = query.list();
		Gprs gprs = null;
		if(list != null && list.size() != 0){
			gprs = (Gprs)list.get(0);
		}
		session.getTransaction().commit();
		return gprs;
	}
	
	public List<Gprs> getAll(){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Gprs");
		List<Gprs> list = query.list();

		return (List<Gprs>) list;
	}
}
