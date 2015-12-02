package edu.illinois.cs425.g06.app1;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.Config;
import backtype.storm.task.OutputCollector;
import backtype.storm.topology.IRichBolt;
import backtype.storm.tuple.Tuple;

public class PortCounter extends IRichBolt {
	Integer id;
	String name;
	Map<String, Integer> counters;
	private OutputCollector collector;
	
	/**
	* At the end of the spout (when the cluster is shutdown
	* We will show the word counters
	*/
	public void cleanup() {
		System.out.println("-- Port Counter ["+name+"-"+id+"] --");
		for(Map.Entry<String, Integer> entry : counters.entrySet()){
			System.out.println(entry.getKey()+": "+entry.getValue());
		}
	}
	
	/**
	* On each word We will count
	*/
	@Override
	public void execute(Tuple input) {
	String str = input.getString(0);
	/**
	* If the word dosn't exist in the map we will create
	* this, if not We will add 1
	*/
	if(!counters.containsKey(str)){
	counters.put(str, 1);
	}else{
	Integer c = counters.get(str) + 1;
	counters.put(str, c);
	}
	//Set the tuple as Acknowledge
	collector.ack(input);
	}
	
	/**
	* On create
	*/
	@Override
	public void prepare(Config stormConf, TopologyContext context, OutputCollector collector) {
		this.counters = new HashMap<String, Integer>();
		this.collector = collector;
		this.name = context.getThisComponentId();
		this.id = context.getThisTaskId();
	}
}
