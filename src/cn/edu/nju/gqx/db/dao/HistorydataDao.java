package cn.edu.nju.gqx.db.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.edu.nju.gqx.db.po.Historydata;
import cn.edu.nju.gqx.db.util.HibernateUtil;

@Component("historydataDao")
@Scope("prototype")
public class HistorydataDao {
	public int createHistorydata(Integer gid,Integer voltage,Integer temperature,Integer humidity){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Historydata hd = new Historydata();
		hd.setGid(gid);
		hd.setVoltage(voltage);
		hd.setTemperature(temperature);
		hd.setHumidity(humidity);
		
		SimpleDateFormat dateformat1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date=dateformat1.format(new Date());
		hd.setUpdate_time(date);
		
		
		session.save(hd);
		session.getTransaction().commit();
		HibernateUtil.closeSession();
		return hd.getId();
	}
	
	public List<?> getByGid(Integer gid){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Historydata where gid=?");
		query.setInteger(0, gid);
		List<?> list = query.list();
		HibernateUtil.closeSession();
		return list;
	}
	
	public List<?> getTodayByGid(Integer gid){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Historydata where gid=? and DATEDIFF(update_time,NOW()) =0");
		query.setInteger(0, gid);
		List<?> list = query.list();
		HibernateUtil.closeSession();
		return list;
	}
	
	
}
