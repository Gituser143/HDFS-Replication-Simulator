package hdfs.replicationsimulator;

public class Block {
	
	private int id;
	private double last_accessed;
	
	Block(int id) {
		this.id = id;
	}
	
	int getId() {
		return this.id;
	}
	double getLast_accessed() {	return this.last_accessed; }

}
