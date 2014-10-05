package cn.edu.nju.gqx.util;

public class AttributeName {
	public static final byte GPRS_START = (byte) 0xD1;		//来自gprs启动时的数据
	public static final byte ZIGBEE_START = (byte) 0xD2;		//来自zigbee（子端）启动时的数据
	public static final byte FROM_MAINZIGBEE = (byte) 0xD3;		//来自zigbee（总端）上传的数据
	public static final byte PRESSURE_ZIGBEE = (byte) 0xD4;		//来自zigbee（子端）水阀状态有误报警数据
	public static final byte TO_GPRS_CMD = (byte) 0xD5;		//发送至gprs阀门控制数据
	public static final byte ZIGBEE_RESPONSE = (byte) 0xD6;		//zigbee反馈
	
	public static final byte ZIGBEE_TYPE_SWITCH_FLAG = (byte) 0x00;	//zigbee类型：普通开关
	public static final byte ZIGBEE_TYPE_WATER_PUMP_FLAG = (byte) 0x01;//zigbee类型：水泵
	public static final byte ZIGBEE_TYPE_PRESSURE_SENSOR_FLAG = (byte) 0x02;//zigbee类型：压力传感器
	
	public static final int ZIGBEE_TYPE_SWITCH = 0;
	public static final int ZIGBEE_TYPE_WATER_PUMP = 1;
	public static final int ZIGBEE_TYPE_PRESSURE_SENSOR = 2;
	
	public static String newGprsName;
	public static String newZigbeeName;
	
	public static final byte STAR_TFLAG = (byte) 0xfe;
	public static final byte TOGPRS_LENGTH = (byte) 0x0c;
	public static final byte SWITCH_ON_CMD = (byte) 0x01;
	public static final byte SWITCH_OFF_CMD = (byte) 0x00;
	public static final byte SWITCH_DO_NOTHING = (byte) 0x03;
	
	public static final String TURNGROUP_FILE_PATH = "turngroup.xml";
	public static final String TURNTASK_FILE_PATH = "turntask.xml";
	
	public static final int TASK_TO_RUN_TOKEN = 0;
	public static final int TASK_SHOULD_RUN_TOKEN = 1;
	public static final int TASK_SHOULD_FINISHED_TOKEN = -1;
	
	public static final int SWITCH_ON_STATE = 1;
	public static final int SWITCH_OFF_STATE = 0;
	
	
}
