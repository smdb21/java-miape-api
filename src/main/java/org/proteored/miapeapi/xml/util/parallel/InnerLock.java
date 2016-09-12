package org.proteored.miapeapi.xml.util.parallel;

/**
 * Simple lock class so the main thread can detect worker threads' completion.
 */
public class InnerLock {
	private int doneCount = 0;

	public synchronized void updateDoneCount() {
		doneCount++;
		notifyAll();
	}

	public synchronized boolean isDone(int totalCount) {
		while (doneCount < totalCount) {
			try {
				wait();
			} catch (InterruptedException ie) {
				System.err.println("I've been interrupted...");
			}
		}
		return true;
	}

	// /**
	// * Use it to know if has finished but no locking
	// *
	// * @param totalCount
	// * @return
	// */
	// public boolean isDoneNoWait(int totalCount) {
	// if (doneCount < totalCount)
	// return false;
	// return true;
	// }
}
