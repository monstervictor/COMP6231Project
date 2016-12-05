package infrastructure;

import java.util.*;

public class RWMonitor {

	int readerCount = 0;
	Queue<Long> writeQueue = new LinkedList<>();
	Boolean writerWriting = false;
	Object readerLock = new Object();
	Object writerLock = new Object();

	public synchronized void requestReadAccess() {
		try {

			if (writeQueue.size() > 0) {
				readerLock.wait();
			}
			readerCount++;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void finishedReadAccess() {
		readerCount--;
		if (!writeQueue.isEmpty()) {
			writerLock.notifyAll();
		}

	}

	public synchronized void requestWriteAccess() {
		try {
			long myId = Thread.currentThread().getId();
			writeQueue.add(myId);
			while (writeQueue.size() > 1 || writeQueue.peek() != myId) {
				writerLock.wait();
			}
			writerWriting = true;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public synchronized void finishedWriteAccess() {
		writerWriting = false;
		writeQueue.remove();
		if (writeQueue.size() > 1)
			writerLock.notifyAll();
		else if(readerCount > 1)
			readerLock.notifyAll();
	}

}
