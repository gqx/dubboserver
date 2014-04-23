package cn.edu.nju.gqx.provider;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.gqx.db.po.Switch;
import cn.edu.nju.gqx.db.po.Task;

public interface TaskService {
	public List getAll();
	public int setClock(int[] sid,String tname,String onTime,String offTime);
	public void initTask();
	public int setClock(ArrayList<Switch> switchList,String tname, String onTime, String offTime);
	public int setClock(String[] names, String tname,String onTime, String offTime);
	public Task getTaskById(int id);
	public int updateTask(int tid,String onTime,String offTime);
	public int deleteTaskById(int id);
}
