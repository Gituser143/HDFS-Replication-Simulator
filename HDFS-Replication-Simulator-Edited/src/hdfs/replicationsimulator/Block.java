package hdfs.replicationsimulator;

public class Block {
	
	private int id;
	//private double last_accessed;
	private long lastAccessed = 0;

	Block(int id) {
		this.id = id;
	}
	
	int getId() {
		return this.id;
	}
	//double getLast_accessed() {	return this.last_accessed; }
	public void accessBlock() {
		this.lastAccessed += 1;
	}

	public long getLastAccessed(){
		return this.lastAccessed;
	}
}
