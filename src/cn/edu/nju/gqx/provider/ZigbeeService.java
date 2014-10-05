package cn.edu.nju.gqx.provider;

import java.util.List;

import cn.edu.nju.gqx.bean.ZigbeeBean;

public interface ZigbeeService {
	public boolean start(byte[] b);
	public boolean close();
	public void updateData(byte[] b);
	public void informError(byte[] b);
	public List getAll();
	public boolean updateSwitchByMac(String mac, byte[] b);
	public List<ZigbeeBean> getZigbeesByType(int ztype);
	 
}
