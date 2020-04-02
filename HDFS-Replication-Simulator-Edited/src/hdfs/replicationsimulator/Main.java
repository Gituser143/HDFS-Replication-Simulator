package hdfs.replicationsimulator;
/**
 * 
 * Launches the simulator
 * 
 * @author peteratt
 * @version 0.1
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
		Power power = new Power();
//		while (!isEnded) wait();
		Thread.sleep(2000);
//		Simulator.transfer(hot to cold);
//		Thread.sleep(2000);
//		Simulator.transfer(cold to hot);

		System.out.println("END");
		
		Simulator.printResults();
		System.exit(0);
	}
	//failure_time=420
	//failing_node_id=12

}
