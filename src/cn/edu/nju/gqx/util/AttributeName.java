package cn.edu.nju.gqx.util;

public class AttributeName {
	public static final byte GPRS_START = (byte) 0xD1;		//����gprs����ʱ������
	public static final byte ZIGBEE_START = (byte) 0xD2;		//����zigbee���Ӷˣ�����ʱ������
	public static final byte FROM_MAINZIGBEE = (byte) 0xD3;		//����zigbee���ܶˣ��ϴ�������
	public static final byte SUBZIGBEE_ERROR = (byte) 0xD4;		//����zigbee���Ӷˣ�ˮ��״̬���󱨾�����
	public static final byte TO_GPRS_CMD = (byte) 0xD5;		//������gprs���ſ�������
	public static final byte ZIGBEE_RESPONSE = (byte) 0xD6;		//zigbee����
	
	
	public static String newGprsName;
	public static String newZigbeeName;
	
	public static final byte STAR_TFLAG = (byte) 0xfe;
	public static final byte TOGPRS_LENGTH = (byte) 0x0c;
	public static final byte SWITCH_ON_CMD = (byte) 0x01;
	public static final byte SWITCH_OFF_CMD = (byte) 0x00;
	public static final byte SWITCH_DO_NOTHING = (byte) 0x03;
	
	
}
