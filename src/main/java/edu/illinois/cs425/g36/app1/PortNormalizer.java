package edu.illinois.cs425.g36.app1;

import java.util.ArrayList;
import java.util.List;

import backtype.storm.Config;
import backtype.storm.collector.OutputCollector;
import backtype.storm.topology.IRichBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class PortNormalizer extends IRichBolt {
	
	@Override
	public void execute(String input) {
		String[] strs = input.split(" ");
		for(String str : strs) {
			System.out.println(str);
		}
		String port = input.split(" ")[2];
		collector.emit(port);	
	}
	
}
