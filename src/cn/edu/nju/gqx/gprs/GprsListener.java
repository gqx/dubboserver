package cn.edu.nju.gqx.gprs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import cn.edu.nju.gqx.provider.impl.Provider;

@Component("gprsListener")
@Scope("prototype")
public class GprsListener implements Runnable{
	private int port = 2020;  
    private static ServerSocket serverSocket; 
    private boolean runflag = true;
    
    MessageHandler messageHandler;
    
    public boolean isRunflag() {
		return runflag;
	}
	public void setRunflag(boolean runflag) {
		this.runflag = runflag;
	}
	public GprsListener() throws IOException {  
        serverSocket = new ServerSocket(port);  
        System.out.println("The server is starting");  
          
    }  
    public void run() { 
    	System.out.println("gprs listener start!");
        while(runflag) {  
            Socket socket = null;  
            try {  
                socket = serverSocket.accept();  
                System.out.println("new connection is completed " + socket.getInetAddress() + ":" + socket.getPort());
                if (socket != null) {  
                    //开启接收与处理信息的服务  
                    Executor es = Executors.newCachedThreadPool();//线程池  
                    messageHandler = (MessageHandler) Provider.getContext().getBean("messageHandler");
                    messageHandler.setClient(socket);
                    es.execute(messageHandler);//开启新的线程  
                }  
            } catch(IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
	
    public static void main(String args[]){
    	try {
			GprsListener g = new GprsListener();
			Thread t = new Thread(g);
			t.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

}
