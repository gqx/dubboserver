package cn.edu.nju.gqx.db.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.edu.nju.gqx.db.po.Gprs;
import cn.edu.nju.gqx.db.po.Switch;
import cn.edu.nju.gqx.db.po.Zigbee;
import cn.edu.nju.gqx.db.util.HibernateUtil;

@Component("switchDao")
@Scope("prototype")
public class SwitchDao {
	public int createSwitch(int zid, String name){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Switch swc = new Switch();
		swc.setName(name);
		swc.setZid(zid);
		swc.setState(Switch.OFF_STATE);
		
		session.save(swc);
		session.getTransaction().commit();
		HibernateUtil.closeSession();
		return swc.getId();
	}
	
	public Switch getSwitchById(int id){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Switch where id=?");
		query.setInteger(0, id);
		List<?> list = query.list();
		Switch swc = null;
		if(list != null && list.size() != 0){
			swc = (Switch)list.get(0);
		}else{
			swc = null;
		}
		session.getTransaction().commit();
		HibernateUtil.closeSession();
		return swc;
	}
	
	public Switch getSwitchByName(String name){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Switch where name=?");
		query.setString(0, name);
		List<?> list = query.list();
		Switch swc = null;
		if(list != null && list.size() != 0){
			swc = (Switch)list.get(0);
		}else{
			swc = null;
		}
		session.getTransaction().commit();
		
		HibernateUtil.closeSession();
		//Çå³ý¶ÔswcµÄ»º´æ
//		session.evict(swc);
		return swc;
	}
	
	public int updateSwitchStateByName(String name, int state){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Switch where name=?");
		query.setString(0, name);
		List<?> list = query.list();
		Switch swc = null;
		if(list != null && list.size() != 0){
			swc = (Switch) list.get(0);
		}else{
			return 0;
		}
		swc.setState(state);
		session.update(swc);
		session.getTransaction().commit();
		HibernateUtil.closeSession();
		return 1;
	}
	
	public int updateSwitchTidByName(String name, int tid){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Switch where name=?");
		query.setString(0, name);
		List<?> list = query.list();
		Switch swc = null;
		if(list != null && list.size() != 0){
			swc = (Switch) list.get(0);
		}else{
			return 0;
		}
		swc.setTid(tid);
		session.update(swc);
		session.getTransaction().commit();
		HibernateUtil.closeSession();
		return 1;
	}
	
	public List<?> getAll(){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Switch");
		List<?> list = query.list();
		session.getTransaction().commit();
		HibernateUtil.closeSession();
		return list;
	}
	
	public List<?> getSwitchsByZid(int zid){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Switch where zid=?");
		query.setInteger(0, zid);
		List<?> list = query.list();
		session.getTransaction().commit();
		HibernateUtil.closeSession();
		return list;
	}
	
	public List<?> getSwitchsByTid(int tid){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Switch where tid=?");
		query.setInteger(0, tid);
		List<?> list = query.list();
		session.getTransaction().commit();
		HibernateUtil.closeSession();
		return list;
	}
	
	public Gprs getGprsBySwitchName(String name){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createSQLQuery("select * from Gprs g where g.id in(select gid from Zigbee z,Switch s where s.name=? and s.zid=z.id )");
		query.setString(0, name);
		List<?> list = query.list();
		if(list == null || list.size() == 0){
			HibernateUtil.closeSession();
			return null;
		}else{
			Object[] o = (Object[]) list.get(0);
			System.out.println("ooo: "+o[0]);
			Gprs g = new Gprs();
			g.setId((Integer)o[0]);
			g.setName((String)o[1]);
			g.setMac((String)o[2]);
			g.setIp((String)o[3]);
			g.setVoltage((Integer)o[4]);
			g.setTemperature((Integer)o[5]);
			g.setHumidity((Integer)o[6]);
			HibernateUtil.closeSession();
			return g;
		}
		
	}
	
	public Zigbee getZigbeeBySwitchName(String name){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createSQLQuery("select * from Zigbee z where exists(select null from Switch s where s.name=? and s.zid=z.id )");
		query.setString(0, name);
		List<?> list = query.list();
		if(list == null || list.size() == 0){
			HibernateUtil.closeSession();
			return null;
		}else{
			Object[] o = (Object[]) list.get(0);
			Zigbee z = new Zigbee();
			z.setId((Integer)o[0]);
			z.setName((String)o[1]);
			z.setGid((Integer)o[2]);
			z.setMac((String)o[3]);
			HibernateUtil.closeSession();
			return z;
		}	
	}
	
	public static void main (String args[]){
		SwitchDao ud = new SwitchDao();
		System.out.println(ud.createSwitch(0,"A2"));
//		System.out.println(ud.getSwitchById(1).getState());
	}
	
}
