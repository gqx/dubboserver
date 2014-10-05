package cn.edu.nju.gqx.provider.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import cn.edu.nju.gqx.db.dao.PressureDao;
import cn.edu.nju.gqx.db.dao.SwitchDao;
import cn.edu.nju.gqx.db.dao.ZigbeeDao;
import cn.edu.nju.gqx.db.po.Pressure;
import cn.edu.nju.gqx.db.po.Switch;
import cn.edu.nju.gqx.db.po.Zigbee;
import cn.edu.nju.gqx.provider.PressureService;
import cn.edu.nju.gqx.util.HexConvert;

public class PressureServiceImpl implements PressureService{

	@Resource(name="pressureDao")
	private PressureDao pressureDao;
	@Resource(name = "zigbeeDao")
	private ZigbeeDao zigbeeDao;
	@Resource(name = "switchDao")
	private SwitchDao switchDao;
	
	@Override
	public void createPressure(int sid, int pvalue) {
		// TODO Auto-generated method stub
		pressureDao.createPressure(sid, pvalue);
	}

	@Override
	public List<Pressure> getPressureBySid(int sid,int count) {
		// TODO Auto-generated method stub
		return pressureDao.getPressureBySid(sid, count);
	}

	@Override
	public void addPressure(byte[] b) {
		// TODO Auto-generated method stub
		byte[] zmac = new byte[8];
		for(int i=0;i < 8;i++){
			zmac[i] = b[i+19];
		}
		
		byte[] value = new byte[1];
		value[0] = b[28];
		int ivalue = Integer.parseInt(HexConvert.bytesToHexString(value));
		
		Zigbee zigbee = zigbeeDao.getByMac(HexConvert.bytesToHexString(zmac));
		if(zigbee != null){
			ArrayList<Switch> swcList = (ArrayList<Switch>) switchDao.getSwitchsByZid(zigbee.getId());
			//pressure zigbee has only one switch by default.
			if(swcList != null){
				Switch swc = swcList.get(0);
				pressureDao.createPressure(swc.getId(), ivalue);
			}
		}
		
	}

}
