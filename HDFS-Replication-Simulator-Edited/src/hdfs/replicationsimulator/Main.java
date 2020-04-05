package hdfs.replicationsimulator;

import java.net.SocketImpl;
import java.io.*;

/**
 * 
 * Launches the simulator
 * 
 * @author peteratt, Gituser143, Abhishek262, nsankethreddy
 * @version 1.1
 */


public class Main {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException, IOException {
		FileOutputStream fp=new FileOutputStream("data.txt",true);
		System.setOut(new PrintStream(fp));

		//for running the simulator a lot of times
		//delete for loop  to run it once
		for(int i = 0;i<100;i++) {
			System.out.println("iteration : " + i);
			int a[] = {-1, -1, -1, -1, -1, -1, -1, -1, i, -1, -1};
			changeConfig(a);

			String configFile;
			if (args.length < 1) {
				configFile = "config.txt";
			} else {
				configFile = args[0];
			}
			Simulator.init(configFile);
			Simulator.start();

			int sleepTime = 2000;

			Thread.sleep(sleepTime);
			Simulator.makeCold();

			//Simulator.printLastAccessed();
			Simulator.moveBlocks();
			//Simulator.printLastAccessed();
			Simulator.accessData();
			System.out.println("\n\nTotal power consumed = " + Simulator.getTotalPower());

			System.out.println("END\n");

			Simulator.printResults();
		}
	}

	//this is to change the config file
	public static void changeConfig(int confargs[]) throws IOException {
		/* the arguments are as follows in confargs, -1 to provide default args as in the config_old file
		0	replica_hot_zone
		1	replica_cold_zone
		2	bw
		3	block
		4	heartbeat
		5	timeout
		6	nodes
		7	nBlocks
		8	HZpercent
		9	Simulated_percentage_of_data_going_cold
		10	threshold
		 */
		int defaultargs[] = {3,2,1024,128,2000,3,100,500,30,80,5};

		for(int i = 0;i<confargs.length;i++){
			if(confargs[i] == -1){
				confargs[i] = defaultargs[i];
			}
		}

		FileWriter configWriter = new FileWriter("config.txt");
		configWriter.write("replica_hot_zone=" + confargs[0] + "\n");
		configWriter.write("replica_cold_zone=" + confargs[1] + "\n");
		configWriter.write("bw=" + confargs[2] + "\n");
		configWriter.write("block=" + confargs[3] + "\n");
		configWriter.write("heartbeat=" + confargs[4] + "\n");
		configWriter.write("timeout=" + confargs[5] + "\n");
		configWriter.write("nodes=" + confargs[6] + "\n");
		configWriter.write("nBlocks=" + confargs[7] + "\n");
		configWriter.write("HZpercent=" + confargs[8] + "\n");
		configWriter.write("Simulated_percentage_of_data_going_cold=" + confargs[9] + "\n");
		configWriter.write("threshold=" + confargs[10] + "\n");
		configWriter.close();

	}

}
