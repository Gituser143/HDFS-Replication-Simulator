package hdfs.replicationsimulator;
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
	public static void main(String[] args) throws InterruptedException {
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

		//TODO TRANSFER FROM HOT TO COLD

		//Simulator.printLastAccessed();
		//Simulator.transfer(hot to cold);

		System.out.println("END");
		
		Simulator.printResults();
		System.exit(0);
	}
	//failure_time=420
	//failing_node_id=12

}
