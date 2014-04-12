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
	int setClock(String sid,long onTime,long offTime);//ÿ�켸�㿪�������
	int setClock(String sid,long startTime,int onSpan,int offSpan);//�Ӽ��㿪ʼ������ã��ض��
	int setClock(String[] sid,long onTime,long offTime);
	int setClock(String[] sid,long startTime,int onSpan,int offSpan);
}
