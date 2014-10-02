package cn.edu.nju.gqx.db.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.edu.nju.gqx.db.po.Task;
import cn.edu.nju.gqx.db.po.Zigbee;
import cn.edu.nju.gqx.db.util.HibernateUtil;

@Component("taskDao")
@Scope("prototype")
public class TaskDao {
	
	public int createTask(String tname,String onTime,String offTime){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Task t = new Task();
		t.setTname(tname);
		t.setStart_time(onTime);
		t.setStop_time(offTime);
		
		session.save(t);
		session.getTransaction().commit();
		HibernateUtil.closeSession();
		return t.getId();
	}
	
	public void deleteTaskById(int id){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Task where id=?");
		query.setInteger(0, id);
		List<?> list = query.list();
		Task t = null;
		if(list != null && list.size() != 0){
			t = (Task)list.get(0);
		}
		
		session.delete(t);
		
		session.getTransaction().commit();
		HibernateUtil.closeSession();
	}

	public List<?> getAllTask(){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Task");
		List<?> list = query.list();
		HibernateUtil.closeSession();
		return list;
	}
	
	public Task getTaskById(int id){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Task where id=?");
		query.setInteger(0, id);
		List<?> list = query.list();
		Task t = null;
		if(list!=null &&list.size() !=0){
			t = (Task) list.get(0);
		}
		HibernateUtil.closeSession();
		return t;
	}
	
	public int updateTask(int tid,String onTime,String offTime){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Task where id=?");
		query.setInteger(0, tid);
		List<?> list = query.list();
		Task t = null;
		if(list != null && list.size() != 0){
			t = (Task) list.get(0);
		}else{
			HibernateUtil.closeSession();
			return 0;
		}
		t.setStart_time(onTime);
		t.setStop_time(offTime);
		
		session.update(t);
		session.getTransaction().commit();
		HibernateUtil.closeSession();
		return 1;
	}
	
}
