package org.zdd.pm;

import org.zdd.mm.MemorySlice;

/**
 * 进程控制块 在本程序代表一个进程
 * 
 * @author zdd
 *
 */
public class PCB {

	private Identification identification;// 进程标识信息

	private ProcessStatus status;// 进程状态

	private int times;// 占用CPU时间

	private int remainingTime;// 剩余时间片

	private MemorySlice memorySlice;// 内存信息

	public PCB(Identification identification, int times, MemorySlice memorySlice) {
		this.identification = identification;
		this.times = times;
		remainingTime = times;
		this.memorySlice = memorySlice;
	}

	public Identification getIdentification() {
		return identification;
	}

	public void setIdentification(Identification identification) {
		this.identification = identification;
	}

	public ProcessStatus getStatus() {
		return status;
	}

	public void setStatus(ProcessStatus status) {
		this.status = status;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(int remainingTime) {
		this.remainingTime = remainingTime;
	}

	public MemorySlice getMemorySlice() {
		return memorySlice;
	}

	public void setMemorySlice(MemorySlice memorySlice) {
		this.memorySlice = memorySlice;
	}

	@Override
	public String toString() {
		return "PCB [identification=" + identification + ", status=" + status + ", times=" + times + ", remainingTime="
				+ remainingTime + ", memorySlice=" + memorySlice + "]";
	}

}
