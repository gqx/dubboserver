package cn.edu.nju.gqx.db.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.edu.nju.gqx.db.po.Gprs;
import cn.edu.nju.gqx.db.po.Zigbee;
import cn.edu.nju.gqx.db.util.HibernateUtil;

@Component("zigbeeDao")
@Scope("prototype")
public class ZigbeeDao {

	@Resource(name="gprsDao")
	private GprsDao gprsDao;
	public int createZigbee(String mac,String gmac){
		Gprs gprs = gprsDao.getByMac(gmac);
		if(gprs == null){//没有对应的gprs
			return -1;
		}
		
		if(getByMac(mac) != null){//此mac对应的zigbee已经存在
			return -2;
		}
		
		int name = getCountByGprsId(gprs.getId());
		
		return createZigbee(mac,gmac,String.valueOf(name+1));
	}
	
	public int createZigbee(String mac,String gmac,String name){
		Gprs gprs = gprsDao.getByMac(gmac);
		if(gprs == null){
			return -1;
		}
		
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Zigbee zigbee = new Zigbee();
		zigbee.setMac(mac);
		zigbee.setGid(gprs.getId());
		zigbee.setName(name);
		
		session.save(zigbee);
		session.getTransaction().commit();
		return zigbee.getId();
	}
	
	public Zigbee getById(int id){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Zigbee where id=?");
		query.setInteger(0, id);
		List<?> list = query.list();
		Zigbee zigbee = null;
		if(list != null && list.size() != 0){
			zigbee = (Zigbee)list.get(0);
		}
		session.getTransaction().commit();
		return zigbee;
	}
	
	public Zigbee getByMac(String mac){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Zigbee where mac=?");
		query.setString(0, mac);
		List<?> list = query.list();
		Zigbee zigbee = null;
		if(list != null && list.size() != 0){
			zigbee = (Zigbee)list.get(0);
		}
		session.getTransaction().commit();
		return zigbee;
	}
	
	public int getCountByGprsId(int gid){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("select count(*) from Zigbee where gid=?");
		query.setInteger(0, gid);
		
		int count = ((Long)query.uniqueResult()).intValue();
		
		session.getTransaction().commit();
//		System.out.println("count: "+count);
		
		return count;
	}
	
	public List<?> getAll(){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Zigbee");
		List<?> list = query.list();
		
		return list;
	}
	
	public List<?> getByGid(int gid){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Zigbee where gid = ?");
		query.setInteger(0, gid);
		List<?> list = query.list();
		
		return list;
	}
	
	
}
