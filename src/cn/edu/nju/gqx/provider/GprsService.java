package cn.edu.nju.gqx.provider;

import java.util.List;

import cn.edu.nju.gqx.bean.GprsBean;
import cn.edu.nju.gqx.db.po.Gprs;

public interface GprsService {
	public boolean start(byte[] b,String ip);
	public boolean update(byte[] b);
	public boolean close();
	public boolean addNewGprsName(String name);
	public Gprs getGprsByMac(byte[] b);
	public List<?> getAllGprsBeans();
	public List<?> getAllGprs();
	public List<?> getDataByGid(Integer gid);
	public List<?> getTodayDataByGid(Integer gid);
	public GprsBean getGprsBean(String name);
}
