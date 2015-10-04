package edu.illinois.cs425_mp1.monitor;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.illinois.cs425_mp1.adapter.Adapter;
import edu.illinois.cs425_mp1.network.Sender;
import edu.illinois.cs425_mp1.network.UDPSender;
import edu.illinois.cs425_mp1.types.Command;
import edu.illinois.cs425_mp1.types.NodeStatus;
import edu.illinois.cs425_mp1.types.Request;

public class HeartbeatBroadcaster implements Runnable {
	
	private final static int totalNumNode = HeartbeatAdapter.membershipList.size();
	
	//Means among broadcastRate of neighbors, randomly pick one for broadcasting
	private final static int broadcastRate = 2; 
	
	//Broadcast frequency in ms
	private final static long sleepInterval = 200;
	
	private Logger log = LogManager.getLogger("heartbeatLogger");
	
	final UDPSender[] senders = new UDPSender[7];

	static final int selfId = HeartbeatAdapter.getMembershipList().getSelfId();
	
	static int randomSeed = 56; 
	
	static Random r = new Random(randomSeed);
	
	public void run() {
		log.trace("Broadcaster runs");
		String[] addr = Adapter.getNeighbors();
		for(int i = 0; i < addr.length; i++) {
			if(i != selfId) {
				senders[i] = new UDPSender(addr[i], HeartbeatAdapter.port);
				senders[i].run();
			}
		}
		while(true) {
			try {
				Thread.sleep(sleepInterval);
			} catch (InterruptedException e) {
				log.trace("HeartbeatBroadcaster stopped");
			}
			HeartbeatAdapter.membershipList.updateSelfTimeStamp();
			broadcast();
		}
		
	}
	
	private void broadcast() {
		synchronized(HeartbeatAdapter.membershipList) {
			for(int i = 0; i < totalNumNode; i++) {
				if(i != selfId) {
					//senders[i].send(HeartbeatAdapter.membershipList);
					senders[i].send(new Request(Command.GREP, "ewf"));
					System.out.println("SEND FOR " + i);
				}
			}
			/*
			for(int i = 0; i < totalNumNode / broadcastRate; i++) {
				int index = Math.abs(r.nextInt()) % totalNumNode;
				if(index == selfId) {
					i--;
					continue;
				}
				senders[index].send(HeartbeatAdapter.membershipList);
			}*/
		}
	}
	
	public void broadcastLeave() {
		HeartbeatAdapter.membershipList.updateSelfTimeStamp();
		HeartbeatAdapter.membershipList.updateSelfStatus(NodeStatus.LEAVE);
		broadcast();
	}
	
	public void broadcastJoin() {
		HeartbeatAdapter.membershipList.updateSelfTimeStamp();
		HeartbeatAdapter.membershipList.updateSelfStatus(NodeStatus.ACTIVE);
		broadcast();
	}
	
}
