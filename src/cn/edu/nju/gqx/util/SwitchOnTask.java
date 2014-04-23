package cn.edu.nju.gqx.util;

import java.util.ArrayList;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;

import cn.edu.nju.gqx.db.po.Switch;
import cn.edu.nju.gqx.provider.SwitchService;
import cn.edu.nju.gqx.provider.impl.Provider;

public class SwitchOnTask extends SwitchTask {

	@Resource(name="switchService")
	SwitchService switchService;
	
	private ArrayList<Switch> switchList;
	
	public SwitchOnTask(int taskId){
		this.taskId = taskId;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		switchService = (SwitchService) Provider.getContext().getBean("switchService");
		switchList = (ArrayList<Switch>) switchService.getSwitchByTid(taskId);
		System.out.println("switchOn task run"+switchList.size());
		for(Switch s : switchList){
			switchService.switchOn(s.getName());
		}
	}

}
