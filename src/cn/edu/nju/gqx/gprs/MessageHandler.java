package cn.edu.nju.gqx.gprs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.edu.nju.gqx.db.po.Gprs;
import cn.edu.nju.gqx.provider.GprsService;
import cn.edu.nju.gqx.provider.SwitchService;
import cn.edu.nju.gqx.provider.ZigbeeService;
import cn.edu.nju.gqx.provider.impl.Provider;
import cn.edu.nju.gqx.provider.impl.SwitchServiceImpl;
import cn.edu.nju.gqx.util.AttributeName;
import cn.edu.nju.gqx.util.HexConvert;

/**
 * �߳�:���ڽ����봦����Ϣ
 */
@Component("messageHandler")
@Scope("prototype")
public class MessageHandler implements Runnable {
	Socket client;

	SwitchServiceImpl ss;

	public SwitchServiceImpl getSs() {
		return ss;
	}

	@Required
	@Resource(name = "switchService")
	public void setSs(SwitchServiceImpl ss) {
		this.ss = ss;
	}

	public Socket getClient() {
		return client;
	}

	public void setClient(Socket client) {
		this.client = client;
	}

	public MessageHandler() {
	}

	public MessageHandler(Socket cleint) {
		this.client = cleint;
	}

	@Override
	public void run() {
		try {
			while(true){
				System.out.println("���ڽ�����Ϣ...");
				InputStream is = client.getInputStream();
				String ip = client.getInetAddress().getHostAddress() + ": ";
				byte[] b = new byte[1024];
				int length = is.read(b);// ��ȡ��Ϣ
	System.out.println("new message:"+length);
				processMessage(b,ip);

//				ip = ip + new String(b, 0, length);
//				System.out.println(Integer.toOctalString(b[0]) + " " + b[1]);
//				System.out.println(ip);
//				System.out.println("���ڷ�����Ϣ...");
//				OutputStream os = client.getOutputStream();
//				os.write(ip.getBytes("gbk"));// ������Ϣ
//				os.flush();
//				ss.switchOn("A1");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ע��:���ﲻҪ�رտͻ��˵���
	}

	/**
	 * У������
	 * 
	 * @param b
	 * @return
	 */
	private boolean processMessage(byte[] b,String ip) {
		byte cmd = b[3];
		
		int v = cmd & 0xFF;  
	    String hv = Integer.toHexString(v);  
		
		System.out.println(hv);
		
		if (cmd == AttributeName.GPRS_START && HexConvert.FCS(b, 1, 18) == b[19]) {//����gprs����ʱ������
			System.out.println("GPRS_START");
			//�����������ģʽ��
			if(AttributeName.newGprsName == null){
				//���ȼ�����ݿ�����û�д�gprs������������socket
				updateSocket(b);
			}else{//���������ģʽ��
				
					GprsService gs = (GprsService) Provider.getContext().getBean("gprsService");
					gs.start(b, ip);
					setSocket(b);	
			}
				
		} else if (cmd == AttributeName.ZIGBEE_START) {//����zigbee���Ӷˣ�����ʱ������
			if (HexConvert.FCS(b, 1, 30) == b[31]) {
				System.out.println("ZIGBEE_START");
				ZigbeeService zs = (ZigbeeService) Provider.getContext().getBean("zigbeeService");
				zs.start(b);
			}
		} else if (cmd == AttributeName.FROM_MAINZIGBEE && HexConvert.FCS(b, 1, 21) == b[22]) {//����zigbee���ܶˣ��ϴ�������
			//����gprs��socket
			updateSocket(b);
			
		} else if (cmd == AttributeName.SUBZIGBEE_ERROR &&HexConvert.FCS(b, 1, 31) == b[32]) {//����zigbee���Ӷˣ�ˮ��״̬���󱨾�����
			//����gprs��socket
			updateSocket(b);
		}else if(cmd == AttributeName.ZIGBEE_RESPONSE){//����zigbee���Ӷˣ�����
			if (HexConvert.FCS(b, 1, 15) == b[16]) {
				byte[] mac = new byte[8];
				for(int i = 0; i < 8;i++){
					mac[i] = b[i+4];	
				}
				String macStr = HexConvert.bytesToHexString(mac);
				System.out.println("mac: "+HexConvert.bytesToHexString(mac));
				ZigbeeService zs = (ZigbeeService) Provider.getContext().getBean("zigbeeService");
				zs.updateSwitchByMac(macStr, b);
//				System.out.println("mac: "+HexConvert.bytesToHexString(mac));
			}
		}
		
		return true;
	}

	
	
	//��socket����socketHolder
	private void setSocket(byte[] b){
		byte[] gprsMac = new byte[15];
		for(int i = 16; i < 31;i++){
			gprsMac[i-16] = b[i];
		}
		
		String mac = HexConvert.bytesToHexString(b);
		SocketHolder.getInstance().setSocket(mac, client);
		
	}
	
	private void updateSocket(byte[] b){
		GprsService gs = (GprsService) Provider.getContext().getBean("gprsService");
		Gprs gprs = gs.getGprsByMac(b);
		if(gprs != null){
			System.out.println("updateSocket: "+client.getInetAddress().getHostAddress());
			SocketHolder.getInstance().setSocket(gprs.getMac(), client);
			System.out.println("updateSocket succeed: "+client.getInetAddress().getHostAddress());
		}
	}

	public static void main(String[] args) {
		MessageHandler m = new MessageHandler();
		byte[] b = new byte[5];
		b[0] = (byte) 0xFE;
		b[1] = (byte) 0x12;
		b[2] = (byte) 0x00;
		b[3] = (byte) 0x5A;
		b[4] = (byte) 0x34;
		System.out.println(Integer.toHexString(HexConvert.FCS(b, 1, 3)));

	}

}
