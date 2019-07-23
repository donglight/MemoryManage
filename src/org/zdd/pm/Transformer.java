package org.zdd.pm;

/**
 * 进程队列状态转换静态方法类
 * 
 * @author zdd
 *
 */
public class Transformer {

	public static void toRun(ProcessQueue runQueue, PCB pcb) {
		pcb.setStatus(ProcessStatus.RUNNING);
		runQueue.enQueue(pcb);
	}

	public static void toReady(ProcessQueue readyQueue, PCB pcb) {
		pcb.setStatus(ProcessStatus.READY);
		readyQueue.enQueue(pcb);
	}

	public static void toWait(ProcessQueue waitQueue, PCB pcb) {
		pcb.setStatus(ProcessStatus.WAIT);
		waitQueue.enQueue(pcb);
	}

	/**
	 * 把当前运行的进程状态改为就绪态
	 * 
	 * @param runQueue
	 * @param readyQueue
	 */
	public static boolean runToReady(ProcessQueue runQueue, ProcessQueue readyQueue) {
		try {
			PCB pcbOperating = runQueue.deQueue();
			if (pcbOperating != null) {
				pcbOperating.setStatus(ProcessStatus.READY);
			} else {
				return false;
			}
			readyQueue.enQueue(pcbOperating);
		} catch (Exception e) {
			System.out.println("状态转换异常！");
			return false;
		}
		return true;
	}

	public static boolean readyToRun(ProcessQueue readyQueue, ProcessQueue runQueue) {
		try {
			PCB pcbReady = readyQueue.deQueue();
			if (pcbReady != null) {
				pcbReady.setStatus(ProcessStatus.RUNNING);
			} else {
				return false;
			}
			runQueue.enQueue(pcbReady);
		} catch (Exception e) {
			System.out.println("状态转换异常！");
			return false;
		}
		return true;
	}

	public static boolean runToWait(ProcessQueue runQueue, ProcessQueue waitQueue) {
		try {
			PCB pcbOperating = runQueue.deQueue();
			if (pcbOperating != null) {
				pcbOperating.setStatus(ProcessStatus.WAIT);
			} else {
				return false;
			}
			waitQueue.enQueue(pcbOperating);
		} catch (Exception e) {
			System.out.println("状态转换异常！");
			return false;
		}
		return true;
	}

	public static boolean waitToReady(ProcessQueue waitQueue, ProcessQueue readyQueue) {
		try {
			PCB pcbWaiting = waitQueue.deQueue();
			if (pcbWaiting != null) {
				pcbWaiting.setStatus(ProcessStatus.READY);
			} else {
				return false;
			}
			pcbWaiting.setStatus(ProcessStatus.READY);
			readyQueue.enQueue(pcbWaiting);
		} catch (Exception e) {
			System.out.println("状态转换异常！");
			return false;
		}
		return true;
	}

}
