package hdfs.replicationsimulator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 
 * A datanode is storing data and execute commands to transfer files.
 * 
 * 
 * @author peteratt
 * @version 0.1
 */
public class Datanode extends Node {
	private int id;
	
	private long lastHB;
	
	private List<Block> blocks;

	private int type;

	private int freeSpace;

	private boolean active;
	/* 
	 * Status
	 */
	private long time_up;
	private long time_down;
	private boolean failed;
	
	private List<Event> commandQueue;
	
	private Queue<Block> pendingBlocks;

	public boolean blockChecking = false;
	
	public Datanode(int id, int capacity, int type) {
		this.id=id;
		this.blocks = new ArrayList<Block>();
		this.lastHB = Node.now()-3000;
		this.commandQueue = new ArrayList<Event>();
		this.pendingBlocks = new ConcurrentLinkedQueue<Block>();
		this.type = type; // 1 for SSD 0 for HDD
		this.freeSpace = Simulator.getCapacity();
		this.active = false;
	}
	
	int getId(){
		return this.id;
	}
	int getType(){
		return this.type;
	}
	int getFreeSpace() { return this.freeSpace; }
	boolean getState() {return this.active; }

	
	/**
	 * Adds a command to the FIFO queue of the NameNode
	 * 
	 * @param e
	 * @return
	 */
	public boolean addCommand(Event e) {
		commandQueue.add(e);
		return true;
	}
	
	/*
	 * Add a block to the Local list of Nodes (Local)
	 */
	public void addBlock(Block block){
		blocks.add(block);
		this.freeSpace -= 1;
	}

	public void setState(int i) {
		this.active = i == 1;

	}
	public void removeBlock(int id) {
		List<Block> blocks = this.blocks;
		Iterator itr = blocks.iterator();
		while (itr.hasNext())
		{
			Block currentBlock = (Block) itr.next();
			if(currentBlock.getId() == id)
			{
				itr.remove();
			}
		}
		this.freeSpace += 1;
	}

	boolean hasFailed(){
		return failed;
	}

	public long getLastHB() {
		return lastHB;
	}

	public void setLastHB(long lastHB) {
		this.lastHB = lastHB;
	}

	public void setUploadingTime(long time) {
		this.time_up = time;
	}
	
	public long getUploadingTime() {
		return this.time_up;
	}
	
	public void setDownloadingTime(long time) {
		this.time_down = time;
	}
	
	public long getDownloadingTime() {
		return this.time_down;
	}

	public List<Block> getBlocks() { return this.blocks; }
	/*
	public void setPendingBlock(int idBlock) {
		Block b = findBlockById(idBlock);
		pendingBlocks.add(b);
		if (blockChecking) {
			Thread t = new Thread(new BlockChecker());
			t.start();
		}
	}
	
	private Block findBlockById(int idBlock) {
		Block b = Simulator.getNamenode().getBlocksMap().getBlockInfo(idBlock);
		return b;
	}

	*/

	public boolean kill() {
		this.failed=true;
		return this.hasFailed();
	}
	
}
