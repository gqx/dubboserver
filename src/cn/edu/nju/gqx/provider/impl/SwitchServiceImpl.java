package cn.edu.nju.gqx.provider.impl;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.edu.nju.gqx.db.dao.SwitchDao;
import cn.edu.nju.gqx.db.dao.TaskDao;
import cn.edu.nju.gqx.db.po.Gprs;
import cn.edu.nju.gqx.db.po.Switch;
import cn.edu.nju.gqx.db.po.Task;
import cn.edu.nju.gqx.db.po.Zigbee;
import cn.edu.nju.gqx.gprs.SocketHolder;
import cn.edu.nju.gqx.provider.SwitchService;
import cn.edu.nju.gqx.util.AttributeName;
import cn.edu.nju.gqx.util.HexConvert;
import cn.edu.nju.gqx.util.SwitchOffTask;
import cn.edu.nju.gqx.util.SwitchOnTask;
import cn.edu.nju.gqx.util.TimerManager;

@Component("switchService")
@Scope("prototype")
public class SwitchServiceImpl implements SwitchService {
	@Resource(name = "switchDao")
	private SwitchDao switchDao;
	@Resource(name = "taskDao")
	private TaskDao taskDao;

	@Override
	public int switchOn(int id) {
		// TODO Auto-generated method stub

		System.out.println("switch:" + id + "on");
		return 1;
	}

	@Override
	public int switchOn(String name) {
		// TODO Auto-generated method stub
		Gprs gprs = switchDao.getGprsBySwitchName(name);
		Zigbee zigbee = switchDao.getZigbeeBySwitchName(name);

		if (gprs != null && zigbee != null) {
			String gmac = gprs.getMac();
			char switchOrder = name.charAt(name.length() - 1);// switch的号A，B，C，D
			SocketHolder.getInstance().sendMessage(gmac,getSwitchOnBytes(zigbee.getMac(), switchOrder));
			System.out.println("sendMessage: "+gmac+" switchOrder:"+switchOrder);
			
			return 1;
			
		} else {
			return -1;
		}

	}

	@Override
	public int switchOff(int id) {
		// TODO Auto-generated method stub
		System.out.println("switch:" + id + "off");
		return 1;
	}

	@Override
	public int switchOff(String name) {
		// TODO Auto-generated method stub
		Gprs gprs = switchDao.getGprsBySwitchName(name);
		Zigbee zigbee = switchDao.getZigbeeBySwitchName(name);

		if (gprs != null && zigbee != null) {
			String gmac = gprs.getMac();
			char switchOrder = name.charAt(name.length() - 1);// switch的号A，B，C，D
			SocketHolder.getInstance().sendMessage(gmac,getSwitchOffBytes(zigbee.getMac(), switchOrder));
			System.out.println("sendMessage: "+gmac+" switchOrder:"+switchOrder);
			
//			byte[] b = SocketHolder.getInstance().getMessage(gmac);
//			byte cmd = b[3];
//			if (cmd == AttributeName.ZIGBEE_RESPONSE) {// 来自zigbee（子端）反馈
//				if (HexConvert.FCS(b, 1, 12) == b[13]) {
//					int result = switchDao.updateSwitchByName(name,Switch.OFF_STATE);
//					System.out.println("switch:" + name + "off");
//					return result;
//				}
//			}
			return 1;
		} else {
			return -1;
		}
	}

	@Override
	public Switch getSwitchBySid(String sid) {
		// TODO Auto-generated method stub

		SwitchDao switchDao = new SwitchDao();

		return switchDao.getSwitchByName(sid);
	}


	public static void main(String[] args) {
		Date date = new Date();
		long d = date.getTime();
		System.out.println(date);
		Time t = new Time(d);
		System.out.println(t);
	}

	@Override
	public List<Switch> getAll() {
		// TODO Auto-generated method stub

		return (List<Switch>) switchDao.getAll();
	}

	private byte[] getSwitchOnBytes(String zmac, char switchOrder) {
		byte[] zigbeeMac = HexConvert.hexStringToBytes(zmac);
		byte[] bytes = new byte[17];
		bytes[0] = AttributeName.STAR_TFLAG;
		bytes[1] = AttributeName.TOGPRS_LENGTH;
		bytes[2] = 0x00;
		bytes[3] = AttributeName.TO_GPRS_CMD;
		for (int i = 0; i < 8; i++) {
			bytes[i + 4] = zigbeeMac[i];
		}
		bytes[12] = AttributeName.SWITCH_DO_NOTHING;
		bytes[13] = AttributeName.SWITCH_DO_NOTHING;
		bytes[14] = AttributeName.SWITCH_DO_NOTHING;
		bytes[15] = AttributeName.SWITCH_DO_NOTHING;

//		switch (switchOrder) {
//		case 'A':
//			bytes[12] = AttributeName.SWITCH_ON_CMD;
//		case 'B':
//			bytes[13] = AttributeName.SWITCH_ON_CMD;
//		case 'C':
//			bytes[14] = AttributeName.SWITCH_ON_CMD;
//		case 'D':
//			bytes[15] = AttributeName.SWITCH_ON_CMD;
//		}
		
		if(switchOrder == 'A'){
			bytes[12] = AttributeName.SWITCH_ON_CMD;
		}
		if(switchOrder == 'B'){
			bytes[13] = AttributeName.SWITCH_ON_CMD;
		}
		if(switchOrder == 'C'){
			bytes[14] = AttributeName.SWITCH_ON_CMD;
		}
		if(switchOrder == 'D'){
			bytes[15] = AttributeName.SWITCH_ON_CMD;
		}
		
		

		bytes[16] = HexConvert.FCS(bytes, 1, 15);
		//fe0c00d582bf5e02004b1200 00010000e0
		//fe0c00d5f5be5e02004b12000000000168
		System.out.println("sendMessage: "+HexConvert.bytesToHexString(bytes));
		return bytes;
	}

	private byte[] getSwitchOffBytes(String zmac, char switchOrder) {
		byte[] zigbeeMac = HexConvert.hexStringToBytes(zmac);
		byte[] bytes = new byte[17];
		bytes[0] = AttributeName.STAR_TFLAG;
		bytes[1] = AttributeName.TOGPRS_LENGTH;
		bytes[2] = 0x00;
		bytes[3] = AttributeName.TO_GPRS_CMD;
		for (int i = 0; i < 8; i++) {
			bytes[i + 4] = zigbeeMac[i];
		}
		bytes[12] = AttributeName.SWITCH_DO_NOTHING;
		bytes[13] = AttributeName.SWITCH_DO_NOTHING;
		bytes[14] = AttributeName.SWITCH_DO_NOTHING;
		bytes[15] = AttributeName.SWITCH_DO_NOTHING;

//		switch (switchOrder) {
//		case 'A':
//			bytes[12] = AttributeName.SWITCH_OFF_CMD;
//		case 'B':
//			bytes[13] = AttributeName.SWITCH_OFF_CMD;
//		case 'C':
//			bytes[14] = AttributeName.SWITCH_OFF_CMD;
//		case 'D':
//			bytes[15] = AttributeName.SWITCH_OFF_CMD;
//		}
		
		if(switchOrder == 'A'){
			bytes[12] = AttributeName.SWITCH_OFF_CMD;
		}
		if(switchOrder == 'B'){
			bytes[13] = AttributeName.SWITCH_OFF_CMD;
		}
		if(switchOrder == 'C'){
			bytes[14] = AttributeName.SWITCH_OFF_CMD;
		}
		if(switchOrder == 'D'){
			bytes[15] = AttributeName.SWITCH_OFF_CMD;
		}

		bytes[16] = HexConvert.FCS(bytes, 1, 15);
		System.out.println("sendMessage: "+HexConvert.bytesToHexString(bytes));
		return bytes;
	}

	@Override
	public List<Switch> getSwitchByTid(int tid) {
		// TODO Auto-generated method stub
		return (List<Switch>) switchDao.getSwitchsByTid(tid);
	}

	
}
