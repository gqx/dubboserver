package cn.edu.nju.gqx.db.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import cn.edu.nju.gqx.db.po.User;
import cn.edu.nju.gqx.db.util.HibernateUtil;

public class UserDao {
	public int createUser(String name,String password){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		User user = new User();
		user.setName(name);
		user.setPassword(password);
		
		session.save(user);
		session.getTransaction().commit();
		return user.getId();
	}
	
	public User getUserById(int id){
		HibernateUtil.openSession();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from User where id=?");
		query.setInteger(0, id);
		List list = query.list();
		if(list != null && list.size() != 0){
			return (User)list.get(0);
		}else{
			return null;
		}
	}
	
	public static void main (String args[]){
		UserDao ud = new UserDao();
//		System.out.println(ud.createUser("gqx2", "111111"));
		System.out.println(ud.getUserById(1).getName());
	}
}
