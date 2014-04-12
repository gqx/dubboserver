package cn.edu.nju.gqx.provider;

import java.util.List;

import cn.edu.nju.gqx.db.po.Gprs;

public interface GprsService {
	public boolean start(byte[] b,String ip);
	public boolean update(byte[] b);
	public boolean close();
	public boolean addNewGprsName(String name);
	public Gprs getGprsByMac(byte[] b);
	public List<?> getAllGprs();

}
