package cn.edu.nju.gqx.provider.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.edu.nju.gqx.bean.GprsBean;
import cn.edu.nju.gqx.bean.ZigbeeBean;
import cn.edu.nju.gqx.db.dao.GprsDao;
import cn.edu.nju.gqx.db.dao.SwitchDao;
import cn.edu.nju.gqx.db.dao.ZigbeeDao;
import cn.edu.nju.gqx.db.po.Gprs;
import cn.edu.nju.gqx.db.po.Zigbee;
import cn.edu.nju.gqx.provider.GprsService;
import cn.edu.nju.gqx.util.AttributeName;
import cn.edu.nju.gqx.util.HexConvert;

@Component("gprsService")
@Scope("prototype")
public class GprsServiceImpl implements GprsService{

	@Resource(name="gprsDao")
	private GprsDao gprsDao;
	@Resource(name = "zigbeeDao")
	ZigbeeDao zigbeeDao;
	@Resource(name = "switchDao")
	SwitchDao switchDao;
	
	@Override
	public boolean start(byte[] b,String ip) {
		// TODO Auto-generated method stub
		
		byte[] mac = new byte[15];
		for(int i = 4; i < 19;i++){
			mac[i-4] = b[i];
		}
		
		String macStr = HexConvert.bytesToHexString(mac);
		System.out.println(macStr+" "+ip);
		int result = gprsDao.createGprs(macStr, AttributeName.newGprsName,ip, 0, 0, 0);
		if(result < 0 )
			return false;
		
		AttributeName.newGprsName = null;
		return true;
	}

	@Override
	public boolean close() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(byte[] b) {
		// TODO Auto-generated method stub
		
		byte[] mac = new byte[15];
		for(int i = 4; i < 19;i++){
			mac[i] = b[i];
		}
		
		String macStr = HexConvert.bytesToHexString(mac);
		
		int voltage = Integer.parseInt(String.valueOf(b[19]));
		int temperature = Integer.parseInt(String.valueOf(b[20]));
		int humidity = Integer.parseInt(String.valueOf(b[21]));
		
		Gprs gprs = gprsDao.getByMac(macStr);
		
		int result = gprsDao.createGprs(macStr, AttributeName.newGprsName, gprs.getIp(), voltage, temperature, humidity);
		if(result < 0 )
			return false;
		return true;
	}
	
	public static void main(String[] args){
		byte[] b = new byte[3];
		b[0]=0x01;
		b[1]=0x02;
		b[2]=0x0A;
//		String bb = String.valueOf(b);
		System.out.println(HexConvert.bytesToHexString(b));
		System.out.println(b[2]);
	}

	@Override
	public boolean addNewGprsName(String name) {
		// TODO Auto-generated method stub
		Gprs gprs = gprsDao.getByName(name);
		System.out.println("name:"+name);
		if(gprs == null){
			AttributeName.newGprsName = name;
			return true;
		}else{
			return false;
		}	
	}
	
	public Gprs getGprsByMac(byte[] b){
		byte[] mac = new byte[15];
		for(int i = 4; i < 19;i++){
			mac[i-4] = b[i];
		}
		
		String macStr = HexConvert.bytesToHexString(mac);
		Gprs gprs = gprsDao.getByMac(macStr);
		return gprs;
	}

	@Override
	public List<GprsBean> getAllGprs() {
		// TODO Auto-generated method stub
		List<Gprs> list = (List<Gprs>) gprsDao.getAll();
		
		List<GprsBean> gbeans = new ArrayList<GprsBean>();

		for (Gprs g : list) {
			GprsBean bean = new GprsBean();
			bean.setGprs(g);
			List<ZigbeeBean> zbeans = getZigbeeBean(g.getId());
			bean.setZigbeelist(zbeans);
			gbeans.add(bean);
		}
		
//		System.out.println("gbeans size: "+gbeans.size());
		return gbeans; 
	}
	
	private List<ZigbeeBean> getZigbeeBean(int gid){
		List<Zigbee> zigbeeList = (List<Zigbee>) zigbeeDao.getByGid(gid);
		List<ZigbeeBean> zigbeeBeanList =  new ArrayList<ZigbeeBean>();
		for(Zigbee z : zigbeeList){
			ZigbeeBean zbean = new ZigbeeBean();
			zbean.setZigbee(z);
			zbean.setSwitchList(switchDao.getSwitchsByZid(z.getId()));
			zigbeeBeanList.add(zbean);
		}
//		System.out.println(gid + " zigbeeBeanList size: "+zigbeeBeanList.size());
		return zigbeeBeanList;
	}

}
