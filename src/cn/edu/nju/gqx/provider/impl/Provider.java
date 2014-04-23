package cn.edu.nju.gqx.provider.impl;

import javax.swing.SwingUtilities;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.edu.nju.gqx.gprs.GprsListener;
import cn.edu.nju.gqx.ui.MainFrame;

public class Provider {
	static ClassPathXmlApplicationContext context;
	public static void main(String[] args) throws Exception {
		context = new ClassPathXmlApplicationContext(
				new String[] { "applicationContext.xml" });
		context.start();
		
		
		GprsListener listener = (GprsListener) context.getBean("gprsListener");
		Thread gprsThread = new Thread(listener);
		gprsThread.start();
		
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				MainFrame inst = new MainFrame();
//				inst.setLocationRelativeTo(null);
//				inst.setVisible(true);
//			}
//		});
		
		
		System.in.read(); 
	}
	
	public static final String hello(String a){
		return a+"aaaa";
	}
	public static ClassPathXmlApplicationContext getContext() {
		if(context == null){
			context = new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml" });
		}
		return context;
	}
	public static void setContext(ClassPathXmlApplicationContext context) {
		Provider.context = context;
	}
	
	
}