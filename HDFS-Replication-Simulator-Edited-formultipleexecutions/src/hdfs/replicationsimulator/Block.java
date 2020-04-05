package hdfs.replicationsimulator;

public class Block {
	
	private int id;
	//private double last_accessed;
	private long lastAccessed = 0; //Number of days since block was last accessed
	private long timesAccessed = 0;
	Block(int id) {
		this.id = id;
	}
	
	int getId() {
		return this.id;
	}
	//double getLast_accessed() {	return this.last_accessed; }

	public void access() {
		this.lastAccessed = 0;
		this.timesAccessed += 1;
	}

	public void changeLastAccess() {
		this.lastAccessed += 1;
	}

	public long getLastAccessed(){
		return this.lastAccessed;
	}

	public long getTimesAccessed() {
		return this.timesAccessed;
	}
}
