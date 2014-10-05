package cn.edu.nju.gqx.db.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateUtil {
	
	private final static SessionFactory sessionFactory = buildSessionFactory();
	private final static ThreadLocal session = new ThreadLocal();
	
	private HibernateUtil(){
	}
	
	private static SessionFactory buildSessionFactory(){
		return new AnnotationConfiguration().configure() .buildSessionFactory();
	}

	public static SessionFactory getSessionfactory() {
		return sessionFactory;
	}
	
	public static Session openSession(){
		Session sn = (Session)session.get();
		if(sn == null){
			sn = sessionFactory.openSession();
			session.set(sn);
		}
		return sn;
	}
	
	public static Session getSession(){
		return (Session)session.get();
	}
	
	public static void closeSession(){
		Session sn = (Session)session.get();
		if(sn != null){
			sn.close();
		}
		session.set(null);
	}
}
