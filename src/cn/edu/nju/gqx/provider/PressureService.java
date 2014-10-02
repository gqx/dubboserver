package cn.edu.nju.gqx.provider;

import java.util.List;

import cn.edu.nju.gqx.db.po.Pressure;

public interface PressureService {
	public void createPressure(int sid,int pvalue);
	public List<Pressure> getPressureBySid(int sid,int count);
	public void addPressure(byte[] b);
}
