package cn.edu.nju.gqx.provider.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.edu.nju.gqx.bean.GprsBean;
import cn.edu.nju.gqx.bean.ZigbeeBean;
import cn.edu.nju.gqx.db.dao.GprsDao;
import cn.edu.nju.gqx.db.dao.HistorydataDao;
import cn.edu.nju.gqx.db.dao.SwitchDao;
import cn.edu.nju.gqx.db.dao.ZigbeeDao;
import cn.edu.nju.gqx.db.po.Gprs;
import cn.edu.nju.gqx.db.po.Historydata;
import cn.edu.nju.gqx.db.po.Zigbee;
import cn.edu.nju.gqx.provider.GprsService;
import cn.edu.nju.gqx.util.AttributeName;
import cn.edu.nju.gqx.util.HexConvert;

@Component("gprsService")
@Scope("prototype")
public class GprsServiceImpl implements GprsService {

	@Resource(name = "gprsDao")
	private GprsDao gprsDao;
	@Resource(name = "zigbeeDao")
	private ZigbeeDao zigbeeDao;
	@Resource(name = "switchDao")
	private SwitchDao switchDao;
	@Resource(name = "historydataDao")
	private HistorydataDao historydataDao;

	@Override
	public boolean start(byte[] b, String ip) {
		// TODO Auto-generated method stub

		byte[] mac = new byte[15];
		for (int i = 4; i < 19; i++) {
			mac[i - 4] = b[i];
		}

		String macStr = HexConvert.bytesToHexString(mac);
		System.out.println(macStr + " " + ip);
		int result = gprsDao.createGprs(macStr, AttributeName.newGprsName, ip,
				0, 0, 0);
		if (result < 0)
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
		for (int i = 4; i < 19; i++) {
			mac[i - 4] = b[i];
		}

		String macStr = HexConvert.bytesToHexString(mac);

		int voltage = Integer.parseInt(String.valueOf(b[19]));
		int temperature = Integer.parseInt(String.valueOf(b[20])) - 40;
		int humidity = Integer.parseInt(String.valueOf(b[21])) - 40;

		Gprs gprs = gprsDao.getByMac(macStr);
		int gid = gprs.getId();
		// int gid = gprsDao.createGprs(macStr,
		// AttributeName.newGprsName,gprs.getIp(), voltage, temperature,
		// humidity);

		gprs.setHumidity(humidity);
		gprs.setTemperature(temperature);
		gprs.setVoltage(voltage);
		gprsDao.update(gprs);

		if (gid < 0) {
			return false;
		} else if (canUpdate(gid)) {

			historydataDao.createHistorydata(gid, voltage, temperature,
					humidity);
			return true;
		}
		return false;
	}

	/**
	 * 判断数据库中最新的数据的小时是否和当前时间的小时一样，如果一样则不需要跟新数据
	 * 
	 * @param gid
	 * @return
	 */
	private boolean canUpdate(int gid) {
		ArrayList<Historydata> data = (ArrayList<Historydata>) historydataDao
				.getTodayByGid(gid);
		if (data == null || data.size() == 0) {
			return true;
		}
		System.out.println("**********" + data.size());
		System.out.println(data.get(data.size() - 1).getGid());

		// 数据库中的最新时间
		String hourstr = data.get(data.size() - 1).getUpdate_time()
				.substring(11, 13);
		int hour = Integer.parseInt(hourstr);
		// 当前时间
		Calendar currentTime = Calendar.getInstance();
		int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
		if (hour != currentHour)
			return true;

		return false;
	}

	public static void main(String[] args) {
		byte[] b = new byte[3];
		b[0] = 0x01;
		b[1] = 0x02;
		b[2] = 0x0A;
		// String bb = String.valueOf(b);
		// System.out.println(HexConvert.bytesToHexString(b));
		// System.out.println(b[2]);
		// Timestamp t = new Timestamp(System.currentTimeMillis());
		// System.out.println(t.getHours());
		// Calendar time = Calendar.getInstance();
		// System.out.println(time.get(Calendar.HOUR_OF_DAY));

		SimpleDateFormat dateformat1 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String a1 = dateformat1.format(new Date());
		System.out.println(a1);
	}

	@Override
	public boolean addNewGprsName(String name) {
		// TODO Auto-generated method stub
		Gprs gprs = gprsDao.getByName(name);
		System.out.println("name:" + name);
		if (gprs == null) {
			AttributeName.newGprsName = name;
			return true;
		} else {
			return false;
		}
	}

	public Gprs getGprsByMac(byte[] b) {
		byte[] mac = new byte[15];
		for (int i = 4; i < 19; i++) {
			mac[i - 4] = b[i];
		}

		String macStr = HexConvert.bytesToHexString(mac);
		Gprs gprs = gprsDao.getByMac(macStr);
		return gprs;
	}

	@Override
	public List<GprsBean> getAllGprsBeans() {
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

		// System.out.println("gbeans size: "+gbeans.size());
		return gbeans;
	}

	private List<ZigbeeBean> getZigbeeBean(int gid) {
		List<Zigbee> zigbeeList = (List<Zigbee>) zigbeeDao.getByGid(gid);
		List<ZigbeeBean> zigbeeBeanList = new ArrayList<ZigbeeBean>();
		for (Zigbee z : zigbeeList) {
			ZigbeeBean zbean = new ZigbeeBean();
			zbean.setZigbee(z);
			zbean.setSwitchList(switchDao.getSwitchsByZid(z.getId()));
			zigbeeBeanList.add(zbean);
		}
		// System.out.println(gid +
		// " zigbeeBeanList size: "+zigbeeBeanList.size());
		return zigbeeBeanList;
	}

	@Override
	public List<?> getAllGprs() {
		// TODO Auto-generated method stub
		return gprsDao.getAll();
	}

	@Override
	public List<?> getDataByGid(Integer gid) {
		// TODO Auto-generated method stub
		return historydataDao.getByGid(gid);
	}

	@Override
	public List<?> getTodayDataByGid(Integer gid) {
		// TODO Auto-generated method stub
		return historydataDao.getTodayByGid(gid);
	}

	@Override
	public GprsBean getGprsBean(String name) {
		// TODO Auto-generated method stub
		Gprs gprs = gprsDao.getByName(name);
		GprsBean bean = null;
		if(gprs != null){
			bean = new GprsBean();
			bean.setGprs(gprs);
			List<ZigbeeBean> zbeans = getZigbeeBean(gprs.getId());
			bean.setZigbeelist(zbeans);
		}
		

		// System.out.println("gbeans size: "+gbeans.size());
		return bean;
	}

}
