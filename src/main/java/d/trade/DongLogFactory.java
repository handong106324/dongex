package d.trade;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.io.FileUtils;

public class DongLogFactory {
	static {
		LogInstance logInstance = new LogInstance();
		logInstance.start();
	}
	 static File judgeFile = new File("C://judge.log");
	 static File dealFile = new File("C://deal.log");
	 static ArrayBlockingQueue<String> judgeQueue = new ArrayBlockingQueue<>(1000000);
	 static ArrayBlockingQueue<String> dealQueue = new ArrayBlockingQueue<>(1000000);
	

	
	public static void addJudge(String str) {
		judgeQueue.add(str);
	}
	
	public static void addDeal(String delMsg) {
		dealQueue.add(delMsg);
	}


}

class LogInstance extends Thread {
	 
	@Override
	public void run() {
		while(true) {
		
			List<String> juLogs = new ArrayList<>();
				while(!DongLogFactory.judgeQueue.isEmpty()) {
					String str = DongLogFactory.judgeQueue.poll();
					juLogs.add(str);
				}
			
				List<String> dealLogs = new ArrayList<>();
				while(!DongLogFactory.dealQueue.isEmpty()) {
					String str = DongLogFactory.dealQueue.poll();
					dealLogs.add(str);
				}
			if(juLogs.size() > 0)
				try {
					FileUtils.writeLines(DongLogFactory.judgeFile, juLogs);
					if(dealLogs.size() > 0)FileUtils.writeLines(DongLogFactory.dealFile, dealLogs);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			

			
		}
	}

}