package org.zdd.pm;

import java.util.LinkedList;

/**
 * 进程队列 FIFO形式管理进程
 * 
 * @author zdd
 *
 */
public class ProcessQueue {

	private final LinkedList<PCB> queue = new LinkedList<>();// 双向链表

	private final int maxSize = 100;//

	public PCB deQueue() {
		if (!isEmpityQueue()) {
			return queue.removeFirst();
		}
		return null;
	}

	public boolean enQueue(PCB pcb) {
		if (!isFullQueue()) {
			queue.addLast(pcb);
			return true;
		}
		return false;
	}

	public PCB getProcess() {
		if (isEmpityQueue()) {
			return null;
		}
		return queue.getFirst();
	}

	public PCB getProcess(String processName) {
		for (PCB pcb : queue) {
			if (pcb.getIdentification().getProcessName().equals(processName)) {
				return pcb;
			}
		}
		return null;
	}

	public boolean removeProcess(PCB process) {
		return queue.remove(process);
	}

	public boolean removeAll() {
		return queue.removeAll(queue);
	}

	public void clearQueue() {
		queue.clear();
	}

	public boolean isEmpityQueue() {
		return queue.isEmpty();
	}

	public boolean isFullQueue() {
		return queue.size() > maxSize ? true : false;
	}

	public String traversingQueue() {
		StringBuilder queueSituation = new StringBuilder();
		if (isEmpityQueue()) {
			return "queue is empty";
		}
		queue.forEach((PCB pcb) -> {
			Identification identification = pcb.getIdentification();
			queueSituation.append("process").append(identification.getProcessID()).append("-")
					.append(identification.getProcessName()).append(" -> ");
		});
		return queueSituation.toString().substring(0, queueSituation.lastIndexOf(" -> "));
	}

	public int size() {
		return queue.size();
	}

}
