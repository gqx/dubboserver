package cn.edu.nju.gqx.provider;

import java.util.List;

public interface ZigbeeService {
	public boolean start(byte[] b);
	public boolean close();
	public void updateData(byte[] b);
	public void informError(byte[] b);
	public List getAll();
	public boolean updateSwitchByMac(String mac, byte[] b);
	
}
