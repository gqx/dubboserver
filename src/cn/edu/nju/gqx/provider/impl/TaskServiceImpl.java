package cn.edu.nju.gqx.provider.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import cn.edu.nju.gqx.db.dao.SwitchDao;
import cn.edu.nju.gqx.db.dao.TaskDao;
import cn.edu.nju.gqx.db.po.Switch;
import cn.edu.nju.gqx.db.po.Task;
import cn.edu.nju.gqx.provider.TaskService;
import cn.edu.nju.gqx.util.SwitchOffTask;
import cn.edu.nju.gqx.util.SwitchOnTask;
import cn.edu.nju.gqx.util.TimerManager;

public class TaskServiceImpl implements TaskService{

	@Resource(name = "switchDao")
	private SwitchDao switchDao;
	@Resource(name = "taskDao")
	private TaskDao taskDao;
	
	@Override
	public List getAll() {
		// TODO Auto-generated method stub
		return taskDao.getAllTask();
		
	}

	/**
	 * time��ʽhh:mm 24Сʱ��
	 * 
	 */
	@Override
	public int setClock(String[] names, String tname,String onTime, String offTime) {
		// TODO Auto-generated method stub
		
		String[] str = onTime.split(":");
		int onHour = Integer.parseInt(str[0]);
		int onMinute = Integer.parseInt(str[1]);
		
		String[] str1 = offTime.split(":");
		int offHour = Integer.parseInt(str1[0]);
		int offMinute = Integer.parseInt(str1[1]); 
		
		//���ݿ����������
		int tid = taskDao.createTask(tname,onTime, offTime);
		
		
		SwitchOnTask onTask = new SwitchOnTask(tid);
		SwitchOffTask offTask = new SwitchOffTask(tid);
		//�������������
		for(String name: names){
			Switch s = switchDao.getSwitchByName(name);
			switchDao.updateSwitchTidByName(s.getName(), tid);
		}
		
		//����������ʱ��
		TimerManager onTimerManager = new TimerManager(onHour, onMinute, onTask);
		TimerManager offTimerManager = new TimerManager(offHour, offMinute, offTask);
		
		System.out.println(onTime+" "+offTime);
		
		return 1;
	}
	
	@Override
	public int setClock(ArrayList<Switch> switchList,String tname, String onTime, String offTime) {
		// TODO Auto-generated method stub
		
		String[] str = onTime.split(":");
		int onHour = Integer.parseInt(str[0]);
		int onMinute = Integer.parseInt(str[1]);
		
		String[] str1 = offTime.split(":");
		int offHour = Integer.parseInt(str1[0]);
		int offMinute = Integer.parseInt(str1[1]); 
		
		//���ݿ����������
		int tid = taskDao.createTask(tname,onTime, offTime);
		
		
		SwitchOnTask onTask = new SwitchOnTask(tid);
		SwitchOffTask offTask = new SwitchOffTask(tid);
		//�������������
		for(Switch s: switchList){
			switchDao.updateSwitchTidByName(s.getName(), tid);
		}
		
		//����������ʱ��
		TimerManager onTimerManager = new TimerManager(onHour, onMinute, onTask);
		TimerManager offTimerManager = new TimerManager(offHour, offMinute, offTask);
		
		return 1;
	}
	
	/**
	 * ����ϵͳ��ʱ���ʼ������
	 */
	@Override
	public void initTask(){
		//��֮ǰ���������
		TimerManager.cancelAll();
		
		ArrayList<Task> taskList = (ArrayList<Task>) taskDao.getAllTask();
		for(Task t: taskList){
			
			int tid = t.getId();
			//���ÿ��task��Ӧ��switch
			ArrayList<Switch> switchList = (ArrayList<Switch>) switchDao.getSwitchsByTid(tid);
			
			SwitchOnTask onTask = new SwitchOnTask(t.getId());
			SwitchOffTask offTask = new SwitchOffTask(t.getId());
			
			String[] str = t.getStart_time().split(":");
			int onHour = Integer.parseInt(str[0]);
			int onMinute = Integer.parseInt(str[1]);
			
			String[] str1 = t.getStop_time().split(":");
			int offHour = Integer.parseInt(str1[0]);
			int offMinute = Integer.parseInt(str1[1]); 
			
			//����������ʱ��
			TimerManager onTimerManager = new TimerManager(onHour, onMinute,  onTask);
			TimerManager offTimerManager = new TimerManager(offHour, offMinute, offTask);
			
		}
	}

	
	@Override
	public int setClock(int[] sid,String tname, String onTime, String offTime) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Task getTaskById(int id) {
		// TODO Auto-generated method stub
		
		return taskDao.getTaskById(id);
	}

	@Override
	public int updateTask(int tid, String onTime, String offTime) {
		// TODO Auto-generated method stub
		int result = taskDao.updateTask(tid, onTime, offTime);
		initTask();
		
		return result;
	}

	@Override
	public int deleteTaskById(int id) {
		// TODO Auto-generated method stub
		ArrayList<Switch> switchList = (ArrayList<Switch>) switchDao.getSwitchsByTid(id);
		taskDao.deleteTaskById(id);
		for(Switch s: switchList){
			switchDao.updateSwitchTidByName(s.getName(), 0);
		}
		
		initTask();
		
		return 1;
	}
	
}


