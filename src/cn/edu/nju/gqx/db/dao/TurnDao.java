package cn.edu.nju.gqx.db.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.edu.nju.gqx.db.po.Switch;
import cn.edu.nju.gqx.db.po.Turngroup;
import cn.edu.nju.gqx.db.po.Turntask;
import cn.edu.nju.gqx.db.util.HibernateUtil;

@Component("turnDao")
@Scope("prototype")
public class TurnDao {
	@Resource(name="switchDao")
	SwitchDao switchDao;
	public int createTurnGroup(int grpid,String sname){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Turngroup tg = new  Turngroup();
		tg.setGrpid(grpid);
		tg.setSname(sname);
		
		session.save(tg);
		session.getTransaction().commit();
		HibernateUtil.closeSession();
		return tg.getId();
	}
	
	public int createTurntask(int ttid,String ttname, String sysname, int grpid, String start_time,String end_time,int token,int state,String execute_date,String duration){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Turntask tt = new Turntask();
		tt.setTtid(ttid);
		tt.setTtname(ttname);
		tt.setSysname(sysname);
		tt.setGrpid(grpid);
		tt.setStart_time(start_time);
		tt.setEnd_time(end_time);
		tt.setToken(token);
		tt.setState(state);
		tt.setExecute_date(execute_date);
		tt.setDuration(duration);
		
		session.save(tt);
		session.getTransaction().commit();
		HibernateUtil.closeSession();
		return tt.getId();
	}
	
	public void clearAllTurngroup(){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("delete from Turngroup");
		query.executeUpdate();
		
		session.getTransaction().commit();
		HibernateUtil.closeSession();
	}

	public void clearAllTurntask(){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("delete from Turntask");
		query.executeUpdate();
		
		session.getTransaction().commit();
		HibernateUtil.closeSession();
	}
	
	public ArrayList<String> getSysname(){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createSQLQuery("select distinct sysname from Turntask");
		ArrayList<String> list = (ArrayList<String>) query.list();
		
		session.getTransaction().commit();
		HibernateUtil.closeSession();
		return list;
	}
	
	public ArrayList<Turntask> getTurntaskBySysname(String sysname){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		String hql = null;
		Query query = null;
		if(sysname.equals("all")){
			hql = "from Turntask order by ttid asc";
			query = session.createQuery(hql);
		}else{
			hql = "from Turntask where sysname=? order by ttid asc";
			query = session.createQuery(hql);
			query.setString(0, sysname);
		}
		
		
		List<?> list = query.list();
		session.getTransaction().commit();
		HibernateUtil.closeSession();
		
		return (ArrayList<Turntask>) list;
	}
	
	public ArrayList<Turntask> getTurntaskBySysnameAndToken(String sysname,int token){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		String hql = null;
		Query query = null;
		if(sysname.equals("all")){
			hql = "from Turntask where token = ? order by ttid asc";
			query = session.createQuery(hql);
			query.setInteger(0, token);
		}else{
			hql = "from Turntask where sysname=? and token = ? order by ttid asc";
			query = session.createQuery(hql);
			query.setString(0, sysname);
			query.setInteger(1, token);	
		}
		
		
		List<?> list = query.list();
		session.getTransaction().commit();
		HibernateUtil.closeSession();
		
		return (ArrayList<Turntask>) list;
	}
	
	/**
	 * get turn tasks except whose grpid = -1;
	 * @param sysname
	 * @return
	 */
	public ArrayList<Turntask> getRunnableTurntaskBySysname(String sysname){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		String hql = null;
		Query query = null;
		if(sysname.equals("all")){
			hql = "from Turntask where grpid <> -1 order by ttid asc";
			query = session.createQuery(hql);
		}else{
			hql = "from Turntask where grpid <> -1 and sysname=? order by ttid asc";
			query = session.createQuery(hql);
			query.setString(0, sysname);
		}
		
		
		List<?> list = query.list();
		session.getTransaction().commit();
		HibernateUtil.closeSession();
		
		return (ArrayList<Turntask>) list;
	}
	
	
	public ArrayList<Turngroup> getTurngroupByGrpid(int grpid){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Turngroup where grpid=?");
		query.setInteger(0, grpid);
		List<?> list = query.list();
		
		session.getTransaction().commit();
		HibernateUtil.closeSession();
		
		return (ArrayList<Turngroup>) list;
	}
	
	public void updateTurntask(Turntask task){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		session.update(task);
		
		session.getTransaction().commit();
		HibernateUtil.closeSession();
		
	}
	
	public ArrayList<Turntask> getTurntaskByToken(int token){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Turntask where token=?");
		query.setInteger(0, token);
		List<?> list = query.list();
		
		session.getTransaction().commit();
		HibernateUtil.closeSession();
		
		return (ArrayList<Turntask>) list;
	}
	
	public ArrayList<Switch> getSwitchesByGrpid(int grpid){
		ArrayList<Turngroup> groupList = getTurngroupByGrpid(grpid);
		if(groupList != null){
			ArrayList<Switch> switchList = new ArrayList<Switch>();
			for(Turngroup group:groupList){
				Switch s = switchDao.getSwitchByName(group.getSname());
				switchList.add(s);
			}
			return switchList;
		}else{
			return null;
		}
		
	}
	
	public long getOnSwitchNumBySysname(String sysname){
		
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("select count(*) from Switch s where s.state=1 and s.name in (select distinct tg.sname from Turngroup tg, Turntask tt where sysname='");
		sb.append(sysname);
		sb.append("' and tt.grpid=tg.grpid)");
		Query query = session.createQuery(sb.toString());
     		
		return (long)query.uniqueResult();
	}
	
	public String getTurntaskNameBySwitchName(String switchName){
		
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct tt.ttname from Turntask tt, Turngroup tg where tt.grpid=tg.grpid and tg.sname='");
		sb.append(switchName);
		sb.append("'");
		Query query = session.createQuery(sb.toString());
		
		List<?> list = query.list();
		
		String turntaskName = null;
		if(list != null && list.size() != 0){
			turntaskName =  (String) list.get(0);
		}
		
		session.getTransaction().commit();
		HibernateUtil.closeSession();
		
		return turntaskName;
	}
	
}
