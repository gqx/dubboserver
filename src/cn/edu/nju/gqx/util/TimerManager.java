package cn.edu.nju.gqx.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TimerManager {
	
	public static HashMap<String,Timer> timerMap = new HashMap<String, Timer>();

	 //时间间隔
	 private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;
	 
	 public TimerManager(int hour,int minute,SwitchTask task) {
	  Calendar calendar = Calendar.getInstance(); 
	       
	  /*** 定制每日xx:xx执行方法 ***/ 

	  calendar.set(Calendar.HOUR_OF_DAY, hour);
	  calendar.set(Calendar.MINUTE, minute);
	  calendar.set(Calendar.SECOND, 0);
	  
	  Date date=calendar.getTime(); //第一次执行定时任务的时间
	  
	  //如果第一次执行定时任务的时间 小于 当前的时间
	  //此时要在 第一次执行定时任务的时间 加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
	  if (date.before(new Date())) {
	      date = this.addDay(date, 1);
	  }
	  
	  Timer timer = new Timer();
	  
	  //安排指定的任务在指定的时间开始进行重复的固定延迟执行。
	  timer.schedule(task,date,PERIOD_DAY);
	  timerMap.put(task.getTaskId()+"", timer);
	 }

	 // 增加或减少天数
	 public Date addDay(Date date, int num) {
	  Calendar startDT = Calendar.getInstance();
	  startDT.setTime(date);
	  startDT.add(Calendar.DAY_OF_MONTH, num);
	  return startDT.getTime();
	 }
	 
	 public static void cancelAll(){
		 Iterator<?> iter = timerMap.entrySet().iterator(); 
		 while (iter.hasNext()) { 
		     Map.Entry entry = (Map.Entry) iter.next(); 
		     Timer t = (Timer) entry.getValue(); 
		     t.cancel();
		     timerMap.remove(t);
		 } 
		 timerMap = new HashMap<String, Timer>();
	 }
	 
	 public static void cancelTask(String key){
		 Timer t = timerMap.get(key);
		 if(t != null){
			 t.cancel();
			 timerMap.remove(key);
		 }
	 }
	}