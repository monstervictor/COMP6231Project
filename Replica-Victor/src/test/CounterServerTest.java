package test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import Servers.CounterServer;
import test.infrastructure.CounterThread;


public class CounterServerTest {

	
	@Test
	public void passingTest(){}
	
    @Test
    public void multithreadTest() {
    	CounterServer leCounter = new CounterServer(); 
    	CounterThread[] leThreads = new CounterThread[10];
    	for(int i = 0; i<10; i++){
    		leThreads[i] = new CounterThread(leCounter);
    	}
    	for (CounterThread counterThread : leThreads) {
    		counterThread.start();
		}
    	
    	//wait for all threads to finish
    	Boolean threadsStillAlive = true;
    	while(threadsStillAlive){
    		threadsStillAlive = false;
    		for (CounterThread counterThread : leThreads) {
    			threadsStillAlive = counterThread.isAlive();
				if(!threadsStillAlive){
					break;
				}
			}
    	}

    	List<Integer> list = new ArrayList<>();
    	for (CounterThread counterThread : leThreads) {
			list.addAll(Arrays.asList(counterThread.getResults()));
		}
    	Set<Integer> set = new HashSet<>(list);
    	//set does not allow duplicates

    	
    	assertEquals(list.size(), set.size());
    	
    	
    	
    }
}
