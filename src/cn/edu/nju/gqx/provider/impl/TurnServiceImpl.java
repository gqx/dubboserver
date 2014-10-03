package cn.edu.nju.gqx.provider.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import cn.edu.nju.gqx.db.dao.TurnDao;
import cn.edu.nju.gqx.db.po.Switch;
import cn.edu.nju.gqx.db.po.Turngroup;
import cn.edu.nju.gqx.db.po.Turntask;
import cn.edu.nju.gqx.provider.SwitchService;
import cn.edu.nju.gqx.provider.TurnService;
import cn.edu.nju.gqx.util.AttributeName;
import cn.edu.nju.gqx.util.XmlUtil;

public class TurnServiceImpl implements TurnService{
	@Resource(name = "turnDao")
	private TurnDao turnDao;
	
	@Resource(name = "switchService")
	private SwitchService switchService;
	
	private HashMap<String,Boolean> taskFlagMap = new HashMap<String, Boolean>();
	Logger log = Logger.getLogger(getClass());
	/**
	 * first clear Turngroup table
	 * then insert new data
	 */
	@Override
	public void createTurngroupByConfig() {
		// TODO Auto-generated method stub
		log.info("create Turngroup by config");
		turnDao.clearAllTurngroup();
		Iterator i = XmlUtil.readXml(AttributeName.TURNGROUP_FILE_PATH);
		while (i.hasNext()) {
//			System.out.println("======");
			Element egroup = (Element) i.next();
//			System.out.println(egprs.attributeValue("name"));
			int grpid = Integer.parseInt(egroup.attributeValue("id"));
			Iterator iswitch = egroup.elementIterator();
			while(iswitch.hasNext()){
//				System.out.println("********");
				Element eswitch = (Element) iswitch.next();
				String sname = eswitch.attributeValue("name");
//				System.out.println(grpid+" "+sname+" ");
				turnDao.createTurnGroup(grpid, sname);
				
			}
		}
		
	}

	/**
	 * first clear Turntask table
	 * then insert new data
	 */
	@Override
	public void createTurntaskByConfig() {
		// TODO Auto-generated method stub
		log.info("create Turntask by config");
		turnDao.clearAllTurntask();
		Iterator i = XmlUtil.readXml(AttributeName.TURNTASK_FILE_PATH);
		while (i.hasNext()) {
//			System.out.println("======");
			Element eturn = (Element) i.next();
//			System.out.println(egprs.attributeValue("name"));
			int ttid = Integer.parseInt(eturn.attributeValue("id"));
			String ttname = eturn.attributeValue("name");
			String sysname = eturn.attributeValue("sysname");
			String start_time = eturn.attributeValue("start");
			String end_time = eturn.attributeValue("end");
			String duration = eturn.attributeValue("duration");
			int token = Integer.parseInt(eturn.attributeValue("token"));
			int grpid = Integer.parseInt(eturn.attributeValue("grpid"));
			log.info("create task "+"ttid="+ttid);
			turnDao.createTurntask(ttid, ttname, sysname, grpid, start_time, end_time, token, 0, "0000-00-00",duration);
		}
	}



	@Override
	public ArrayList<String> getSysname() {
		// TODO Auto-generated method stub
		
		return turnDao.getSysname();
	}

	@Override
	public ArrayList<Turntask> getTurntaskBySysname(String sysname) {
		// TODO Auto-generated method stub
		
		return turnDao.getTurntaskBySysname(sysname);
	}

	@Override
	public ArrayList<Turngroup> getTurngroupByGrpid(int grpid) {
		// TODO Auto-generated method stub
		return turnDao.getTurngroupByGrpid(grpid);
	}

	@Override
	public void startAutoTaskBySysname(boolean isRestart,String sysname) {
		// TODO Auto-generated method stub
		if(isRestart){
			setRestartTime();
		}

		if(sysname.equals("all")){
			ArrayList<String> sysnameList = turnDao.getSysname();
			for(String s:sysnameList){
				taskFlagMap.put(s, true);
				startNewTaskThread(s);
			}
		}else{
			taskFlagMap.put(sysname, true);
			startNewTaskThread(sysname);
		}
		
		
		
	}
	
	private void startNewTaskThread(final String sysname){
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(taskFlagMap.get(sysname)){
					log.info("auto run task thread started");
					// this list is ordered by ttid asc;
					ArrayList<Turntask> tasklist = turnDao.getTurntaskBySysname(sysname);
					for(int i = 0; i < tasklist.size();i++){
						Turntask task = tasklist.get(i);
						if(task.getToken() == 1){
							if(compareToToday(task.getExecute_date()) == 0 && betweenTime(task.getStart_time(),task.getEnd_time())){
								// turn on task
								turnOnTask(task);
							}else {													
								// pass token
								if(i+1 < tasklist.size()){
									Turntask nextTask = tasklist.get(i+1);
									if(task.getSysname().equals(nextTask.getSysname()) && task.getTtid() < nextTask.getTtid()){
										passToken(task,nextTask);
									}
								}else{
									//to the end, stop the loop
//									taskFlagMap.put(sysname, false);
								}
							}
							
						}else{
							//check if all switches of running task in the same system are on.
							ArrayList<Switch> switchesOfRunningTask = getRunningTaskSwitchesBySysname(task.getSysname());
							//if most switches of the running task are on, then we can turn off the switches of not running task
							//now we make sure at least one task is on,so the pump will not breakdown.
							//and the last task must be stopped manually 
							if(onSwitchesNum(switchesOfRunningTask) >= switchesOfRunningTask.size()-2){
								// turn off task
								turnOffTask(task);
							}
						}
					}
					
					
					try {
						//Ã¿10ÃëÖÓ¼ì²âÒ»´Î
						Thread.sleep(1000*10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
		
	}

	/**
	 * 
	 * @param date yyyy-mm-dd
	 * @return -1 0 1
	 */
	private int compareToToday(String date){
		Calendar c = Calendar.getInstance();
		int currentYear = c.get(Calendar.YEAR);
		int currentMonth = c.get(Calendar.MONTH)+1;
		int currentDay = c.get(Calendar.DAY_OF_MONTH);
		
		String[] s = date.split("-");
		
		int year = Integer.parseInt(s[0]);
		int month = Integer.parseInt(s[1]);
		int day = Integer.parseInt(s[2]);
		
		if(year == currentYear){
			if(month == currentMonth){
				if(day == currentDay){
					return 0;
				}else if(day > currentDay){
					return 1;
				}else{
					return -1;
				}
			}else if(month > currentMonth){
				return 1;
			}else{
				return -1;
			}
		}else if(year > currentYear){
			return 1;
		}else{
			return -1;
		}
		
	}
	
	/**
	 * 
	 * @param start HH:MM
	 * @param end HH:MM
	 * @return
	 */
	private boolean betweenTime(String start, String end){
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		
		String[] s = start.split(":");
		int startHour = Integer.parseInt(s[0]);
		int startMinute = Integer.parseInt(s[1]);
		
		String[] s1 = end.split(":");
		int endHour = Integer.parseInt(s1[0]);
		int endMinute = Integer.parseInt(s1[1]);
		
		if(startHour < hour && hour < endHour){
			return true;
		}else if(hour == startHour && hour < endHour && startMinute <= minute){
			return true;
		}else if(hour == endHour && hour > startHour && minute <= endMinute){
			return true;
		}else if(hour == startHour && hour == endHour && startMinute <= minute && minute <= endMinute){
			return true;
		}
		return false;
	}
	
	
	private void turnOnTask(Turntask task){
		log.info("turn on task "+task.getId());
		ArrayList<Turngroup> grouplist = turnDao.getTurngroupByGrpid(task.getGrpid());
		if(grouplist != null){
			for(Turngroup group:grouplist){
				switchService.switchOn(group.getSname());
			}
		}
		task.setState(1);
		turnDao.updateTurntask(task);
	}
	
	private void turnOffTask(Turntask task){
		log.info("turn off task "+task.getId());
		ArrayList<Turngroup> grouplist = turnDao.getTurngroupByGrpid(task.getGrpid());
		if(grouplist != null){
			for(Turngroup group:grouplist){
				switchService.switchOff(group.getSname());
			}
		}
		task.setState(0);
		turnDao.updateTurntask(task);
	}
	
	private void passToken(Turntask task,Turntask nextTask){
		log.info("pass token from "+task.getId()+" to "+nextTask.getId());
		task.setToken(0);
		nextTask.setToken(1);
		Calendar c = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String execute_date = format.format(c.getTime());
		nextTask.setExecute_date(execute_date);
		turnDao.updateTurntask(task);
		turnDao.updateTurntask(nextTask);
	}
	
	@Override
	public void stopAutoTaskBySysname(String sysname,boolean stopSwitches) {
		// TODO Auto-generated method stub
		if(sysname.equals("all")){
			ArrayList<String> sysnames = turnDao.getSysname();
			
			if(sysnames != null){
				for(String s:sysnames){
					taskFlagMap.put(s, false);
					executeStopAutoTaskBySysname(s,stopSwitches);
				}
			}
		}else{
			taskFlagMap.put(sysname, false);
			executeStopAutoTaskBySysname(sysname,stopSwitches);
		}
		updateTurnTask(sysname,0);
	}
	
	private void executeStopAutoTaskBySysname(String sysname,boolean stopSwitches){
		ArrayList<Turntask> taskList = turnDao.getTurntaskBySysname(sysname);
		ArrayList<String> namelist = new ArrayList<String>();
		if(taskList != null){
			//get turngroup of each turntask
			ArrayList<Turngroup> groupList = new ArrayList<Turngroup>();
			for(Turntask t:taskList){
				ArrayList<Turngroup> list = turnDao.getTurngroupByGrpid(t.getGrpid());
				groupList.addAll(list);
			}
			
			for(Turngroup group:groupList){
				namelist.add(group.getSname());
			}
		}
		if(stopSwitches){
			switchService.switchesOff(namelist);
		}	
	}
	
	private void updateTurnTask(String sysname,int state){
		ArrayList<Turntask> tasklist = turnDao.getTurntaskBySysname(sysname);
		if(tasklist != null){
			for(Turntask task: tasklist){
				task.setState(state);
				turnDao.updateTurntask(task);
			}
		}
	}
	
	/**
	 * make the task start from today
	 * it will start the turn task that the running time is now
	 */
	private void setRestartTime(){
		log.info("set restart time");
		ArrayList<Turntask> tasklist = turnDao.getTurntaskByToken(1);
		if(tasklist != null){
			Calendar c = Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String execute_date = format.format(c.getTime());
			for(Turntask t:tasklist){
				t.setExecute_date(execute_date);
				turnDao.updateTurntask(t);
			}
		}
	}

	@Override
	public ArrayList<Turntask> getRunnableTurntaskBySysname(String sysname) {
		// TODO Auto-generated method stub
		return turnDao.getRunnableTurntaskBySysname(sysname);
	}

	@Override
	public ArrayList<Switch> getRunningTaskSwitchesBySysname(String sysname){
		ArrayList<Turntask> taskList = turnDao.getTurntaskBySysnameAndToken(sysname, 1);
		ArrayList<Switch> switchList = new ArrayList<Switch>();
		if(taskList != null){
			for(Turntask task:taskList){
				ArrayList<Switch> slist = turnDao.getSwitchesByGrpid(task.getGrpid());
				if(slist != null){
					switchList.addAll(slist);
				}
			}
		}
		return switchList;
	}
	
	@Override
	public ArrayList<Switch> getOnSwitchesBySysname(String sysname){
		ArrayList<Turntask> taskList = turnDao.getTurntaskBySysname(sysname);
		ArrayList<Switch> switchList = new ArrayList<Switch>();
		if(taskList != null){
			for(Turntask task:taskList){
				ArrayList<Switch> slist = turnDao.getSwitchesByGrpid(task.getGrpid());
				if(slist != null){
					for(Switch s:slist){ 
					System.out.println(s.getName());
						if(s.getState() == Switch.ON_STATE){
							switchList.add(s);
						}
					}
					
				}
			}
		}
		return switchList;
	}
	
	@Override
	public long getOnSwitchNumBySysname(String sysname) {
		// TODO Auto-generated method stub
		
		return turnDao.getOnSwitchNumBySysname(sysname);
	}

	private int onSwitchesNum(ArrayList<Switch> switchList){
		int num = 0;
		for(Switch s:switchList){
			if(s.getState() == Switch.ON_STATE){
				num++;
			}
		}
		return num;
	}

	@Override
	public String getTurntaskNameBySwitchName(String switchName) {
		// TODO Auto-generated method stub
		return turnDao.getTurntaskNameBySwitchName(switchName);
	}

	
}
