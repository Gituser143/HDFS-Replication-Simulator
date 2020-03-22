package hdfs.replicationsimulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

/*
 * Object used to Initialize the system. Create all the objects and processes.
 */
public class Simulator {

	private static Namenode namenode;
	private static AllDatanode allDatanodes;

	private static Queue<Event> toNamenode;
	private static Queue<Event> toDatanodes;

	private static List<SimTrace> traceList;

	/*
	 * Settings (defaults)
	 */
	private static int numberofBlocks = 75000;
	private static int numberofReplicas = 3;
	private static int numberofDatanodes = 10000;
	private static int dataNodeCapacity = 320000;
	private static int bandwidth = 1024;
	private static int heartbeat = 1000;
	private static int timeout = 3;
	private static int blockSize = 64;
	private static int numberofSSDs = 64;

	private static List<Event> simulationFailureEvents;

	public static void init(String configFile) {
		// Read the test.txt file: CHANGE THE NAME!
		try {
			FileReader fr = new FileReader(configFile);
			BufferedReader in = new BufferedReader(fr);
			String data;

			int lastFailureTime = 0;
			int lastFailingNode = 0;
			int fraction = 50;
			boolean timeRetrieved = false;
			boolean nodeRetrieved = false;

			simulationFailureEvents = new ArrayList<Event>();
			traceList = new ArrayList<SimTrace>();

			while ((data = in.readLine()) != null) {

				if (data.contains("replica=")) {
					numberofReplicas = Integer.parseInt(data.split("=")[1]);
				} else if (data.contains("nodes=")) {
					numberofDatanodes = Integer.parseInt(data.split("=")[1]);
				} else if (data.contains("bw=")) {
					bandwidth = Integer.parseInt(data.split("=")[1]);
				} else if (data.contains("heartbeat=")) {
					heartbeat = Integer.parseInt(data.split("=")[1]);
				} else if (data.contains("timeout=")) {
					timeout = Integer.parseInt(data.split("=")[1]);
				} else if (data.contains("block=")) {
					blockSize = Integer.parseInt(data.split("=")[1]);
				} else if (data.contains("nBlocks=")) {
					numberofBlocks = Integer.parseInt(data.split("=")[1]);
				} else if (data.contains("HZpercent")) {
					fraction = Integer.parseInt(data.split("=")[1]);
				} else if (data.contains("dn_capacity=")) {
					dataNodeCapacity = Integer.parseInt(data.split("=")[1]);
				} else if (data.contains("failure_time=") && !timeRetrieved) {
					lastFailureTime = Integer.parseInt(data.split("=")[1]);
					timeRetrieved = true;
				} else if (data.contains("failing_node_id=") && !nodeRetrieved) {
					lastFailingNode = Integer.parseInt(data.split("=")[1]);
					nodeRetrieved = true;
				}

				// Fill the list of events
				if (timeRetrieved && nodeRetrieved) {
					//simulationFailureEvents.add(new Event(lastFailingNode,
						//	Event.FAILURE, lastFailureTime));
					timeRetrieved = false;
					nodeRetrieved = false;
				}
				numberofSSDs = (fraction * numberofDatanodes)/100;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		toNamenode = new ConcurrentLinkedQueue<Event>();
		toDatanodes = new ConcurrentLinkedQueue<Event>();

		// Create the Namenode
		initializeNamenode();
		// Create the datanode distribution and fill the Datanode map
		initializeDatanodes();
		// Create the blocks distribution, attribute them to datanodes and fill
		// the blocksMap
		initializeBlocks();

	}

	private static void startFailure() {
		
		Thread killer = new Thread(new NodeKiller());
		killer.start();
		
	}

	private static void initializeNamenode() {
		namenode = new Namenode();
		System.out.print("Namenode Created.\n");
	}

	private static void initializeDatanodes() {

		// Create all the datanodes
		allDatanodes = new AllDatanode();
		//int numberofSSDs = numberofDatanodes/3;
		for (int i = 0; i < numberofSSDs; i++) {
			allDatanodes.addNode(new Datanode(i, dataNodeCapacity, 1));

			DatanodeInfo datanodeInfo = new DatanodeInfo(i, dataNodeCapacity, 1);
			namenode.addNode(datanodeInfo);
			System.out.println("Created SSD with ID " + i);

		}

		for (int i = numberofSSDs; i < numberofDatanodes; i++) {
			allDatanodes.addNode(new Datanode(i, dataNodeCapacity, 0));

			DatanodeInfo datanodeInfo = new DatanodeInfo(i, dataNodeCapacity, 0);
			namenode.addNode(datanodeInfo);
			System.out.println("Created HDD with ID " + i);

		}

		System.out.print(numberofDatanodes + " Datanodes Created.\n");
	}

	private static void initializeBlocks() {
		int currentDN = 0;
		Power power = new Power();
		for (int i = 0; i < numberofBlocks; i++) {
			// Create the Block
			Block block = new BlockInfo(i);

			// Add the block to the namenode index
			namenode.addBlock((BlockInfo)block);

			//System.out.println("Block Number = " + i);
			/* THIS VERSION USE RANDOM AND SO IS NOT GOOD.
			// Adds the block randomly to a node
			 * Random r = new Random();
			for (int j = 0; j < numberofReplicas; j++) {
				int idDatanode = r.nextInt(numberofDatanodes - 1) ;
				//int index = idDatanode - 1;
				Datanode dn = allDatanodes.getNode(idDatanode);//TODO
				dn.addBlock(block);
				namenode.initAddBlock(idDatanode, (BlockInfo)block);
			}
			*/
			//adds the blocks to a node (chained mode)
			
			for (int j = 0; j < numberofReplicas; j++) {
				int idDatanode = currentDN;
				//int index = idDatanode - 1;
				Datanode dn = allDatanodes.getNode(idDatanode);
				dn.addBlock(block);
				if(dn.getType() == 1) {
					power.totalPower += power.writeSsd;
				}
				else {
					power.totalPower += power.writeHdd;
				}
				namenode.initAddBlock(idDatanode, (BlockInfo)block);
				currentDN = (currentDN == numberofSSDs-1)? 0: currentDN+1; // Initialize all blocks to SSD
				//currentDN = (currentDN == numberofDatanodes-1)? 0: currentDN+1; // Initialize sequentially across both SSD and HDD
			}
		}
		System.out.print(numberofBlocks + " Blocks distributed\n");
		System.out.print(power.totalPower + " Watts of Power consumed for Initialization\n");
	}

	public static void start() {
		allDatanodes.start();
		namenode.start();
		startFailure();
	}

	public static int getBandwidth() {
		return bandwidth;
	}

	public static int getHeartbeat() {
		return heartbeat;
	}

	public static int getTimeout() {
		return timeout;
	}

	public static int getBlockSize() {
		return blockSize;
	}
	
	public static int getNumberofReplicas() {
		return numberofReplicas;
	}

	public static Queue<Event> getToNamenode() {
		return toNamenode;
	}

	public static Queue<Event> getToDatanodes() {
		return toDatanodes;
	}

	public static Namenode getNamenode() {
		return namenode;
	}

	public static void addTrace(SimTrace st) {
		traceList.add(st);
		System.out.println(st.toString());
	}

	public static void printResults() {

		File f = new File("results.log");
		try {
			FileWriter fw = new FileWriter(f);
			int dataLosses = 0;

			List<Long> ttrs = new ArrayList<Long>();
			Map<Integer, Long> failureEvents = new HashMap<Integer, Long>();
			for (SimTrace st : traceList) {
				fw.write(st.toString());
				if (st.isLossEvent()) {
					dataLosses++;
				}

				if (st.getAction() == SimTrace.FAILURE_DETECTION) {
					failureEvents.put(st.getId(), st.getTimestamp());
				} else if (st.getAction() == SimTrace.BLOCK_RECEIVED) {
					long failTime = 0;
					try {
						failTime = failureEvents.get(st.getIdBlock());
						long ttr = st.getTimestamp() - failTime;
						ttrs.add(ttr);
					}
					catch (NullPointerException e) {
						System.out.println("Block no "+st.getIdBlock()+"hasn't failed");
					}
					
				}
			}
			fw.write(processMTTR(ttrs));
			fw.write(processDurability(dataLosses));
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static String processDurability(int losses) {
		double durability = 1 - (losses / numberofBlocks);
		return "DURABILITY: " + durability;
	}

	private static String processMTTR(List<Long> ttrs) {

		double sum = 0;
		for (Long l : ttrs) {
			sum += l;
		}
		double mttr;
		if(ttrs.size()!=0)
			mttr = sum / ttrs.size();
		else
			mttr=0;
		return "MTTR: " + mttr;
	}

	public static List<Event> getSimulationFailureEvents() {
		return simulationFailureEvents;
	}

	public static AllDatanode getAllDatanodes() {
		return allDatanodes;
	}

}
