package cn.edu.nju.gqx.provider;

import java.util.List;

import cn.edu.nju.gqx.db.po.Switch;

public interface SwitchService {
	int switchOn(int id);
	int switchOn(String name);
	int switchOff(int id);
	int switchOff(String name);
	List<Switch> getAll();
	Switch getSwitchBySid(String sid);
	int setClock(String sid,long onTime,long offTime);//每天几点开，几点关
	int setClock(String sid,long startTime,int onSpan,int offSpan);//从几点开始，开多久，关多久
	int setClock(String[] sid,long onTime,long offTime);
	int setClock(String[] sid,long startTime,int onSpan,int offSpan);
}
