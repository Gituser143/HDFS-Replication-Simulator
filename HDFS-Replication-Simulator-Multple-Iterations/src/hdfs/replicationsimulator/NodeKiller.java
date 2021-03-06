package hdfs.replicationsimulator;

import java.util.List;

public class NodeKiller implements Runnable {

	@Override
	public void run() {
		
		//Simulator.addTrace(new SimTrace("Started NodeKiller"));
		
		long initialTime = 0;
		List<Event> failures = Simulator.getSimulationFailureEvents();
		//System.out.println("Failure list is: "+failures);
		for (Event e : failures) {
			
			try {
				Thread.sleep(e.getTime() - initialTime);
				initialTime = e.getTime();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//System.out.println("Event in node "+e.getSource());
			Simulator.getAllDatanodes().killNode(e.getSource());
		}
		
	}

}
