package cn.edu.nju.gqx.db.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.edu.nju.gqx.db.po.Pressure;
import cn.edu.nju.gqx.db.util.HibernateUtil;


@Component("pressureDao")
@Scope("prototype")
public class PressureDao {
	public int createPressure(int sid,int pvalue){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Pressure p = new Pressure();
		p.setSid(sid);
		p.setPvalue(pvalue);
		
		session.save(p);
		session.getTransaction().commit();
		HibernateUtil.closeSession();
		return p.getId();
	}
	
	public List<Pressure> getPressureBySid(int sid, int count){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Pressure where sid=? order by update_time desc");
		query.setInteger(0, sid);
		query.setMaxResults(count);
		
		List<?> list = query.list();
		
		session.getTransaction().commit();
		HibernateUtil.closeSession();
		return (List<Pressure>) list;
	}
}
