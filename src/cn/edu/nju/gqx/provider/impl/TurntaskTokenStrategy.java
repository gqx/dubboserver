package cn.edu.nju.gqx.provider.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import org.dom4j.Element;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import cn.edu.nju.gqx.db.po.Switch;
import cn.edu.nju.gqx.db.po.Turngroup;
import cn.edu.nju.gqx.db.po.Turntask;
import cn.edu.nju.gqx.util.AttributeName;
import cn.edu.nju.gqx.util.XmlUtil;

@Component("turntaskTokenStrategy")
@Scope("prototype")
public class TurntaskTokenStrategy extends TurntaskStrategy {

	@Override
	public void startAutoTaskBySysname(boolean isRestart, String sysname) {
		// TODO Auto-generated method stub
		if (isRestart) {
			setRestartTime();
		}

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

	/**
	 * make the task start from today it will start the turn task that the
	 * running time is now
	 */
	private void setRestartTime() {
		log.info("set restart time");
		ArrayList<Turntask> tasklist = turnDao.getTurntaskByToken(1);
		if (tasklist != null) {
			Calendar c = Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String execute_date = format.format(c.getTime());
			for (Turntask t : tasklist) {
				t.setExecute_date(execute_date);
				turnDao.updateTurntask(t);
			}
		}
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
						if (task.getToken() == 1) {
							if (compareToToday(task.getExecute_date()) == 0
									&& betweenTime(task.getStart_time(),
											task.getEnd_time())) {
								// turn on task
								turnOnTask(task);
							} else {
								// pass token
								if (i + 1 < tasklist.size()) {
									Turntask nextTask = tasklist.get(i + 1);
									if (task.getSysname().equals(
											nextTask.getSysname())
											&& task.getTtid() < nextTask
													.getTtid()) {
										passToken(task, nextTask);
									}
								} else {
									// to the end, stop the loop
									// taskFlagMap.put(sysname, false);
								}
							}

						} else {
							// check if all switches of running task in the same
							// system are on.
							ArrayList<Switch> switchesOfRunningTask = getShouldRunTaskSwitchesBySysname(task
									.getSysname());
							// if most switches of the running task are on, then
							// we can turn off the switches of not running task
							// now we make sure at least one task is on,so the
							// pump will not breakdown.
							// and the last task must be stopped manually
							if (onSwitchesNum(switchesOfRunningTask) >= switchesOfRunningTask
									.size() - 2) {
								// turn off task
								turnOffTask(task);
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

	private void passToken(Turntask task, Turntask nextTask) {
		log.info("pass token from " + task.getId() + " to " + nextTask.getId());
		task.setToken(0);
		nextTask.setToken(1);
		Calendar c = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String execute_date = format.format(c.getTime());
		nextTask.setExecute_date(execute_date);
		turnDao.updateTurntask(task);
		turnDao.updateTurntask(nextTask);
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
			int token = Integer.parseInt(eturn.attributeValue("token"));
			int grpid = Integer.parseInt(eturn.attributeValue("grpid"));
			log.info("create task "+"ttid="+ttid);
			turnDao.createTurntask(ttid, ttname, sysname, grpid, start_time, end_time, token, 0, "0000-00-00",duration);
		}
	}
}
