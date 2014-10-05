package cn.edu.nju.gqx.provider.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import javax.annotation.Resource;

import org.dom4j.Element;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.edu.nju.gqx.db.dao.SwitchDao;
import cn.edu.nju.gqx.db.po.Switch;
import cn.edu.nju.gqx.db.po.Turngroup;
import cn.edu.nju.gqx.db.po.Turntask;
import cn.edu.nju.gqx.util.AttributeName;
import cn.edu.nju.gqx.util.XmlUtil;

@Component("turntaskWithExedateStrategy")
@Scope("prototype")
public class TurntaskWithExedateStrategy extends TurntaskStrategy{
	@Resource(name="switchDao")
	SwitchDao switchDao;
	
	@Override
	public void startAutoTaskBySysname(boolean isRestart, String sysname) {
		// TODO Auto-generated method stub

		if (sysname.equals("all")) {
			ArrayList<String> sysnameList = turnDao.getSysname();
			for (String s : sysnameList) {
				taskFlagMap.put(s, true);
				startNewTaskThread(s);
			}
		} else {
			taskFlagMap.put(sysname, true);
			startNewTaskThread(sysname);
		}
	}

	@Override
	public void stopAutoTaskBySysname(String sysname, boolean stopSwitches) {
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

	@Override
	public void createTurntaskByConfig(int daysBeforeStart) {
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
			String execute_date = getExecuteDate(eturn.attributeValue("dayId"),daysBeforeStart);
			int token = Integer.parseInt(eturn.attributeValue("token"));
			int grpid = Integer.parseInt(eturn.attributeValue("grpid"));
			log.info("create task "+"ttid="+ttid);
			turnDao.createTurntask(ttid, ttname, sysname, grpid, start_time, end_time, token, 0, execute_date,duration);
		}
	}
	
	private String getExecuteDate(String dayId,int daysBeforeStart){
		int day = Integer.parseInt(dayId);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, c.get(Calendar.DATE)+day-1+daysBeforeStart);
		String date = format.format(c.getTime());		
		return date;
	}
	
	private void startNewTaskThread(final String sysname) {

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (taskFlagMap.get(sysname)) {
					log.info("auto run task thread started");
					// this list is ordered by ttid asc;
					ArrayList<Turntask> tasklist = turnDao
							.getTurntaskBySysname(sysname);
					for (int i = 0; i < tasklist.size(); i++) {
						Turntask task = tasklist.get(i);
						
						if (compareToToday(task.getExecute_date()) == 0 && betweenTime(task.getStart_time(),task.getEnd_time())) {
							// turn on task
							turnOnTask(task);
							task.setToken(AttributeName.TASK_SHOULD_RUN_TOKEN);
							turnDao.updateTurntask(task);
						} else {
							// get all switches that should be running.
							ArrayList<Switch> switchesOfShouldRunTask = getShouldRunTaskSwitchesBySysname(task
									.getSysname());
							
							
							if(i == tasklist.size()-1 && task.getToken() == AttributeName.TASK_SHOULD_FINISHED_TOKEN){
								log.info("last task "+task.getTtid());
								//the last task in the system should be over
								//first turn off the pump,then turn off the task
								if(turnOffPump(task.getSysname())==true){
									//all pumps have been turned off
									for(int n = 0;n < 3;n++){
										turnOffTask(task);
										try {
											Thread.sleep(1000*10);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									//notify this thread can be closed
									taskFlagMap.put(sysname, false);
									log.info(task.getSysname()+" task stop");
								}	
							}else{
								if(task.getToken() == AttributeName.TASK_SHOULD_RUN_TOKEN){
									task.setToken(AttributeName.TASK_SHOULD_FINISHED_TOKEN);
									turnDao.updateTurntask(task);
								}
								// if most switches of the running task are on, then
								// we can turn off the switches of not running task
								// now we make sure at least one task is on,so the
								// pump will not breakdown.
								// and the last task must be stopped manually
								if (onSwitchesNum(switchesOfShouldRunTask) >= switchesOfShouldRunTask.size() - 2) {
									// turn off task
									turnOffTask(task);
								}
							}	
						}
					}
					try {
						// √ø10√Î÷”ºÏ≤‚“ª¥Œ
						Thread.sleep(1000 * 10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
	}
	
	private boolean turnOffPump(String sysname){
		boolean allOff = true;
		ArrayList<Switch> slist = (ArrayList<Switch>) switchDao.getSwitchByTypeAndSysname(sysname, AttributeName.ZIGBEE_TYPE_WATER_PUMP);
		for(Switch s:slist){
			if(s.getState() != AttributeName.SWITCH_OFF_STATE){
				switchService.switchOff(s.getName());
				allOff = false;
			}
		}
		return allOff;
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
}
