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
 * 线程:用于接收与处理信息
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
				System.out.println("正在接收消息...");
				InputStream is = client.getInputStream();
				String ip = client.getInetAddress().getHostAddress() + ": ";
				byte[] b = new byte[1024];
				int length = is.read(b);// 读取信息
	System.out.println("new message:"+length);
				processMessage(b,ip);

//				ip = ip + new String(b, 0, length);
//				System.out.println(Integer.toOctalString(b[0]) + " " + b[1]);
//				System.out.println(ip);
//				System.out.println("正在发送信息...");
//				OutputStream os = client.getOutputStream();
//				os.write(ip.getBytes("gbk"));// 发送信息
//				os.flush();
//				ss.switchOn("A1");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 注意:这里不要关闭客户端的流
	}

	/**
	 * 校验数据
	 * 
	 * @param b
	 * @return
	 */
	private boolean processMessage(byte[] b,String ip) {
		byte cmd = b[3];
		
		int v = cmd & 0xFF;  
	    String hv = Integer.toHexString(v);  
		
		System.out.println(hv);
		
		if (cmd == AttributeName.GPRS_START && HexConvert.FCS(b, 1, 18) == b[19]) {//来自gprs启动时的数据
			System.out.println("GPRS_START");
			//如果不在命名模式下
			if(AttributeName.newGprsName == null){
				//首先检查数据库中有没有此gprs，如果有责更新socket
				updateSocket(b);
			}else{//如果在命名模式下
				
					GprsService gs = (GprsService) Provider.getContext().getBean("gprsService");
					gs.start(b, ip);
					setSocket(b);	
			}
				
		} else if (cmd == AttributeName.ZIGBEE_START) {//来自zigbee（子端）启动时的数据
			if (HexConvert.FCS(b, 1, 30) == b[31]) {
				System.out.println("ZIGBEE_START");
				ZigbeeService zs = (ZigbeeService) Provider.getContext().getBean("zigbeeService");
				zs.start(b);
			}
		} else if (cmd == AttributeName.FROM_MAINZIGBEE && HexConvert.FCS(b, 1, 21) == b[22]) {//来自zigbee（总端）上传的数据
			//更新gprs的socket
			updateSocket(b);
			
		} else if (cmd == AttributeName.SUBZIGBEE_ERROR &&HexConvert.FCS(b, 1, 31) == b[32]) {//来自zigbee（子端）水阀状态有误报警数据
			//更新gprs的socket
			updateSocket(b);
		}else if(cmd == AttributeName.ZIGBEE_RESPONSE){//来自zigbee（子端）反馈
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

	
	
	//将socket存入socketHolder
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
