package cn.edu.nju.gqx.util;

import java.util.ArrayList;
import java.util.TimerTask;

import javax.annotation.Resource;

import cn.edu.nju.gqx.db.po.Switch;
import cn.edu.nju.gqx.provider.SwitchService;
import cn.edu.nju.gqx.provider.impl.Provider;

public class SwitchOffTask extends SwitchTask {

	@Resource(name = "switchService")
	SwitchService switchService;

	private ArrayList<Switch> switchList;
	
	public SwitchOffTask(int taskId) {
		this.taskId = taskId;
	}


	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("switchOff task run");
		switchService = (SwitchService) Provider.getContext().getBean("switchService");
		switchList = (ArrayList<Switch>) switchService.getSwitchByTid(taskId);
		for (Switch s : switchList){
			switchService.switchOff(s.getName());
		}
	}

}
