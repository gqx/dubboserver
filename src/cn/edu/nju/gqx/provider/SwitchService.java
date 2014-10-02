package cn.edu.nju.gqx.provider;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.gqx.db.po.Switch;

public interface SwitchService {
	int switchOn(int id);
	int switchOn(String name);
	int switchOff(int id);
	int switchOff(String name);
	List<Switch> getAll();
	Switch getSwitchBySid(String sid);
	List<Switch> getSwitchByTid(int tid);
	Switch getSwitchByName(String name);
	List<Switch> getSwitchByGprsName(String name);
	void switchesOn(ArrayList<String> nameList);
	void switchesOff(ArrayList<String> nameList);
}
