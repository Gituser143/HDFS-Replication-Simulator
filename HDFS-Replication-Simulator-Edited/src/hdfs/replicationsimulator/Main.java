package hdfs.replicationsimulator;

import java.io.*;
import java.net.SocketImpl;

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
		PrintWriter writer = new PrintWriter("data.txt");
		writer.print("");
		writer.close();


		FileOutputStream fp = new FileOutputStream("data.txt",true);
		System.setOut(new PrintStream(fp));

		//int i;
		for(int i = 1000; i < 5000; i += 50){
			Simulator.setTotalPower();

			int newArgs[] = {
					-1, // replica_hot_zone
					-1, // replica_cold_zone
					-1, // bw
					-1, // block
					-1, // heartbeat
					-1, // timeout
					-1, // dn_capacity
					-1, // nodes
					i,  // nBlocks
					-1, // HZpercent
					-1, // Simulated_percentage_of_data_going_cold
					-1  // threshold
			};
			changeConfig(newArgs);
			String configFile;
			if (args.length < 1) {
				configFile = "config.txt";
			} else {
				configFile = args[0];
			}
			Simulator.init(configFile);
			Simulator.start();

			int sleepTime = 0;

			Thread.sleep(sleepTime);
			Simulator.makeCold();


			//Simulator.printLastAccessed();
			Simulator.moveBlocks();
			//Simulator.printLastAccessed();
			Simulator.balance();
			//System.out.println("NEW\n\n\n");
			//Simulator.printLastAccessed();
			Simulator.accessData();
			System.out.println("Total power consumed = " + Simulator.getTotalPower());

			//System.out.println("END");

			Simulator.printResults();

		}
		System.exit(0);

	}

	public static void changeConfig(int confargs[]) throws IOException {

		/* the arguments are as follows in confargs, -1 to provide default args as in the config_old file */
		int defaultArgs[] = {
				3, 		// replica_hot_zone
				2, 		// replica_cold_zone
				1024, // bw
				128, 	// block
				2000, // heartbeat
				3, 		// timeout
				3200, // dn_capacity
				100, 	// nodes
				5000, // nBlocks
				25, 	// HZpercent
				80, 	// Simulated_percentage_of_data_going_cold
				5 		// threshold
		};


		for(int i = 0; i < confargs.length; i++){
			if(confargs[i] == -1){
				confargs[i] = defaultArgs[i];
			}
		}

		FileWriter configWriter = new FileWriter("config.txt");
		configWriter.write("replica_hot_zone=" + confargs[0] + "\n");
		configWriter.write("replica_cold_zone=" + confargs[1] + "\n");
		configWriter.write("bw=" + confargs[2] + "\n");
		configWriter.write("block=" + confargs[3] + "\n");
		configWriter.write("heartbeat=" + confargs[4] + "\n");
		configWriter.write("timeout=" + confargs[5] + "\n");
		configWriter.write("dn_capacity=" + confargs[6] + "\n");
		configWriter.write("nodes=" + confargs[7] + "\n");
		configWriter.write("nBlocks=" + confargs[8] + "\n");
		configWriter.write("HZpercent=" + confargs[9] + "\n");
		configWriter.write("Simulated_percentage_of_data_going_cold=" + confargs[10] + "\n");
		configWriter.write("threshold=" + confargs[11] + "\n");
		configWriter.close();

	}

	//failure_time=420
	//failing_node_id=12

}
