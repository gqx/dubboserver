package cn.edu.nju.gqx.gprs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SocketHolder {
	private Map<String,Socket> socketMap = new HashMap<String,Socket>();
	private static SocketHolder holder = new SocketHolder();
	
	private SocketHolder(){}
	
	public static SocketHolder getInstance(){
		return holder;
	}

	public Map<String, Socket> getSocketMap() {
		return socketMap;
	}

	public void setSocketMap(Map<String, Socket> socketMap) {
		this.socketMap = socketMap;
	}
	
	public void setSocket(String key, Socket socket){
		if(socketMap.get(key) != null){
			socketMap.remove(key);		
		}
		socketMap.put(key, socket);
		if(socketMap.containsKey(key))
		System.out.println("post setSocket key:"+key);
//		Iterator iter =  socketMap.keySet().iterator();
//		while(iter.hasNext()){
//			String key1 = (String) iter.next();
//			System.out.println("key1: "+key1);
//		}
		
	}
	
	public Socket getSocket(String key){
		return socketMap.get(key);
	}
	
	/**
	 * 通过key找到socket，发送message
	 * @param key
	 * @param message
	 * @return
	 */
	public int sendMessage(String key,byte[] message){
//		System.out.println("pre sendMessage key:"+key);
//		Iterator iter =  socketMap.keySet().iterator();
//		while(iter.hasNext()){
//			String key1 = (String) iter.next();
//			System.out.println("key1: "+key1);
//		}

		Socket socket = getSocket(key);
		if(socket != null){
		System.out.println("get socket key="+key);
			try {
				OutputStream os = socket.getOutputStream();
				os.write(message);
				os.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return -1;
			}// 发送信息
			return 1;
		}else{
			System.out.println("socket null key="+key);
			return -1;
		}
	}
	
	public byte[] getMessage(String key){
		Socket socket = getSocket(key);
		if(socket != null){
			InputStream is;
			try {
				is = socket.getInputStream();
				byte[] b = new byte[1024];
				return b;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
		}
		return null;
	}
	
}
