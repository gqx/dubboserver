package cn.edu.nju.gqx.provider.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.edu.nju.gqx.bean.ZigbeeBean;
import cn.edu.nju.gqx.db.dao.GprsDao;
import cn.edu.nju.gqx.db.dao.SwitchDao;
import cn.edu.nju.gqx.db.dao.ZigbeeDao;
import cn.edu.nju.gqx.db.po.Switch;
import cn.edu.nju.gqx.db.po.Zigbee;
import cn.edu.nju.gqx.provider.ZigbeeService;
import cn.edu.nju.gqx.util.AttributeName;
import cn.edu.nju.gqx.util.HexConvert;

@Component("zigbeeService")
@Scope("prototype")
public class ZigbeeServiceImpl implements ZigbeeService {

	@Resource(name = "gprsDao")
	GprsDao gprsDao;
	@Resource(name = "zigbeeDao")
	ZigbeeDao zigbeeDao;
	@Resource(name = "switchDao")
	SwitchDao switchDao;

	@Override
	public boolean start(byte[] b) {
		// TODO Auto-generated method stub

		byte ztypeByte = b[2];
		
		int ztype = 0;
		if(ztypeByte == AttributeName.ZIGBEE_TYPE_SWITCH_FLAG){
			ztype = 0;
		}else if(ztypeByte == AttributeName.ZIGBEE_TYPE_WATER_PUMP_FLAG){
			ztype = 1;
		}else if(ztypeByte == AttributeName.ZIGBEE_TYPE_PRESSURE_SENSOR_FLAG){
			ztype = 2;
		}
		
		byte[] zigbeeMac = new byte[8];
		for (int i = 4; i < 12; i++) {
			zigbeeMac[i - 4] = b[i];
		}

		byte[] state = new byte[4];
		state[0] = b[12];
		state[1] = b[13];
		state[2] = b[14];
		state[3] = b[15];

		byte[] gprsMac = new byte[15];
		for (int i = 16; i < 31; i++) {
			gprsMac[i - 16] = b[i];
		}

		String gprsMacStr = HexConvert.bytesToHexString(gprsMac);
		String zigbeeMacStr = HexConvert.bytesToHexString(zigbeeMac);

		int zid = zigbeeDao.createZigbee(zigbeeMacStr, gprsMacStr,ztype);
		String gname = gprsDao.getByMac(gprsMacStr).getName();
		System.out.println("result: " + zid);
		Zigbee zigbee = zigbeeDao.getById(zid);

		if (zid > 0) {
			if (createSwitch(gname, zid, state,zigbee.getName()) == true) {
				return true;
			}
			return false;
		}

		return false;

	}

	private boolean createSwitch(String gname, int zid, byte[] state,String zname) {
		if (state[0] == 0x01)
			switchDao.createSwitch(zid, gname+"-"+zname + "-A");
		if (state[1] == 0x01)
			switchDao.createSwitch(zid, gname+"-"+zname + "-B");
		if (state[2] == 0x01)
			switchDao.createSwitch(zid, gname+"-"+zname + "-C");
		if (state[3] == 0x01)
			switchDao.createSwitch(zid, gname+"-"+zname + "-D");

		return true;
	}

	@Override
	public boolean close() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateData(byte[] b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void informError(byte[] b) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<?> getAll() {
		// TODO Auto-generated method stub
		List<Zigbee> list = (List<Zigbee>) zigbeeDao.getAll();
		List<ZigbeeBean> beans = new ArrayList<ZigbeeBean>();

		for (Zigbee z : list) {
			ZigbeeBean bean = new ZigbeeBean();
			bean.setZigbee(z);
			bean.setSwitchList(switchDao.getSwitchsByZid(z.getId()));
			beans.add(bean);
		}

//		System.out.println("beans: " + beans.size());

		return beans;
	}

	public boolean updateSwitchByMac(String mac, byte[] b) {
		Zigbee zigbee = zigbeeDao.getByMac(mac);
		if (zigbee == null) {
			return false;
		}

		ArrayList<Switch> switchlist = (ArrayList<Switch>) switchDao.getSwitchsByZid(zigbee.getId());

		for (Switch s : switchlist) {
			String sname = s.getName();
			sname = sname.substring(sname.length() - 1);
//			System.out.println("sname: "+sname);
			if (sname.equals("A")) {
				int state; 
				if(b[12] == 0x01){
					state = AttributeName.SWITCH_ON_CMD;
					switchDao.updateSwitchStateByName(s.getName(), state);
				}else if(b[12] == 0x00){
					state = AttributeName.SWITCH_OFF_CMD;
					switchDao.updateSwitchStateByName(s.getName(), state);
				}
				
			}
			else if (sname.equals("B")) {
				int state; 
				if(b[12] == 0x01){
					state = AttributeName.SWITCH_ON_CMD;
					switchDao.updateSwitchStateByName(s.getName(), state);
				}else if(b[12] == 0x00){
					state = AttributeName.SWITCH_OFF_CMD;
					switchDao.updateSwitchStateByName(s.getName(), state);
				}
			}
			else if (sname.equals("C")) {
				int state; 
				if(b[12] == 0x01){
					state = AttributeName.SWITCH_ON_CMD;
					switchDao.updateSwitchStateByName(s.getName(), state);
				}else if(b[12] == 0x00){
					state = AttributeName.SWITCH_OFF_CMD;
					switchDao.updateSwitchStateByName(s.getName(), state);
				}
			}
			else if (sname.equals("D")) {
				int state; 
				if(b[12] == 0x01){
					state = AttributeName.SWITCH_ON_CMD;
					switchDao.updateSwitchStateByName(s.getName(), state);
				}else if(b[12] == 0x00){
					state = AttributeName.SWITCH_OFF_CMD;
					switchDao.updateSwitchStateByName(s.getName(), state);
				}
			}
		}

		return true;
	}

	@Override
	public List<ZigbeeBean> getZigbeesByType(int ztype) {
		// TODO Auto-generated method stub
		
		ArrayList<ZigbeeBean> beanlist = new ArrayList<ZigbeeBean>();
		ArrayList<Zigbee> zigbeelist = (ArrayList<Zigbee>) zigbeeDao.getByType(ztype);
		if(zigbeelist != null){
			for(Zigbee z : zigbeelist){
				ZigbeeBean bean = new ZigbeeBean();
				bean.setZigbee(z);
				bean.setSwitchList(switchDao.getSwitchsByZid(z.getId()));
				beanlist.add(bean);
			}
		}
		
		return beanlist;
	}

	// public static void main(String[] args){
	// String s = "1A";
	// System.out.println(s.substring(s.length()-1));
	// }

}
