package org.zdd.pm;

/**
 * 进程标识信息
 * 
 * @author zdd
 *
 */
public class Identification {

	private int processID;// 进程ID

	private String processName;// 进程名字

	public Identification(int processID, String processName) {
		super();
		this.processID = processID;
		this.processName = processName;
	}

	public int getProcessID() {
		return processID;
	}

	public void setProcessID(short processID) {
		this.processID = processID;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	@Override
	public String toString() {
		return "Identification [processID=" + processID + ", processName=" + processName + "]";
	}

}
