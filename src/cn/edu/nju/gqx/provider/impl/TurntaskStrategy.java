package cn.edu.nju.gqx.provider.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import cn.edu.nju.gqx.db.dao.TurnDao;
import cn.edu.nju.gqx.db.po.Switch;
import cn.edu.nju.gqx.db.po.Turngroup;
import cn.edu.nju.gqx.db.po.Turntask;
import cn.edu.nju.gqx.provider.SwitchService;

public abstract class TurntaskStrategy {
	@Resource(name = "turnDao")
	protected TurnDao turnDao;
	
	@Resource(name = "switchService")
	protected SwitchService switchService;
	
	HashMap<String, Boolean> taskFlagMap = new HashMap<String, Boolean>();
	
	Logger log = Logger.getLogger(getClass());
	
	public abstract void startAutoTaskBySysname(boolean isRestart,String sysname);
	public abstract void stopAutoTaskBySysname(String sysname,boolean stopSwitches);
	public abstract void createTurntaskByConfig(int daysBeforeStart);
	
	/**
	 * 
	 * @param date yyyy-mm-dd
	 * @return -1 0 1
	 */
	public int compareToToday(String date){
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
	public boolean betweenTime(String start, String end){
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
	
	public void turnOnTask(Turntask task){
		log.info("turn on task "+task.getId());
		ArrayList<Turngroup> grouplist = turnDao.getTurngroupByGrpid(task.getGrpid());
		if(grouplist != null){
			for(Turngroup group:grouplist){
				Switch s = switchService.getSwitchByName(group.getSname());
				if(s.getState() == Switch.OFF_STATE)
				switchService.switchOn(group.getSname());
			}
		}
		task.setState(1);
		turnDao.updateTurntask(task);
	}
	
	public void turnOffTask(Turntask task){
		log.info("turn off task "+task.getId());
		ArrayList<Turngroup> grouplist = turnDao.getTurngroupByGrpid(task.getGrpid());
		if(grouplist != null){
			for(Turngroup group:grouplist){
				Switch s = switchService.getSwitchByName(group.getSname());
				if(s.getState() == Switch.ON_STATE)
				switchService.switchOff(group.getSname());
			}
		}
		task.setState(0);
		turnDao.updateTurntask(task);
	}
	
	public ArrayList<Switch> getShouldRunTaskSwitchesBySysname(String sysname){
		ArrayList<Turntask> taskList = turnDao.getTurntaskBySysnameAndToken(sysname, 1);
		ArrayList<Switch> switchList = new ArrayList<Switch>();
		if(taskList != null){
			for(Turntask task:taskList){
				ArrayList<Switch> slist = turnDao.getSwitchesByGrpid(task.getGrpid());
				if(slist != null){
					for(Switch s:slist){
						if(s != null)
						switchList.add(s);
					}
				}
			}
		}
		return switchList;
	}
	
	public int onSwitchesNum(ArrayList<Switch> switchList){
		int num = 0;
		for(Switch s:switchList){
			if(s!= null && s.getState() == Switch.ON_STATE){
				num++;
			}
		}
		return num;
	}
	
	protected void updateTurnTask(String sysname,int state){
		ArrayList<Turntask> tasklist = turnDao.getTurntaskBySysname(sysname);
		if(tasklist != null){
			for(Turntask task: tasklist){
				task.setState(state);
				turnDao.updateTurntask(task);
			}
		}
	}
}
