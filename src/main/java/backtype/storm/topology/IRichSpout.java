package backtype.storm.topology;

import backtype.storm.collector.SpoutOutputCollector;

public abstract class IRichSpout extends IComponent {
    
	protected SpoutOutputCollector collector = new SpoutOutputCollector();
	
	public void close() {
		
	}

    public void activate() {
    	
    }

    public void deactivate() {
    	
    }

    public void ack(Object msgId) {
    	
    }
    
    public void fail(Object msgId) {
    	
    }

    public abstract void nextTuple();
    
    public SpoutOutputCollector getOutputCollector() {
    	return collector;
    }
}
