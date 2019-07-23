package org.zdd.pm;

import java.util.Random;

import org.zdd.mm.MemoryManager;
import org.zdd.mm.MemorySlice;

/**
 * 功能类
 * 
 * @author zdd
 */
public abstract class AbstractProcessManager {

	protected ProcessQueue runQueue, readyQueue, waitQueue;

	protected Random r = new Random();

	protected int processIDCount = 0; // 进程ID号

	protected int processCount = 0;// 进程计数器

	protected MemoryManager memoryManager; // 内存管理者

	protected abstract void run(String[] args) throws InterruptedException;

	protected AbstractProcessManager() {
		// 内存管理者，默认采用最先适应内存分配算法
		this(new MemoryManager());
	}

	protected AbstractProcessManager(MemoryManager memoryManager) {
		// 初始化三个进程队列
		runQueue = new ProcessQueue();
		readyQueue = new ProcessQueue();
		waitQueue = new ProcessQueue();
		// 内存管理者，默认采用最先适应内存分配算法
		this.memoryManager = new MemoryManager();
	}

	protected PCB createProcess(String processName, int memorySize) {

		// 给进程分配内存
		MemorySlice allocated = memoryManager.allocate(memorySize);
		if (allocated == null) {
			System.out.println("进程分配内存失败，没有足夠大的空闲内存分区");
			return null;
		}
		// 创建进程并进入就绪队列
		int cpuTime = r.nextInt(3000);
		PCB pcb = new PCB(new Identification(processIDCount++, processName), cpuTime, allocated);
		pcb.setMemorySlice(allocated);
		Transformer.toReady(readyQueue, pcb);
		System.out.println(getProcessSimpleIdetification(pcb) + " 进程创建成功,进入就绪队列" + ",需要cpu执行时间为:" + cpuTime);
		processCount++;
		System.out.println(allocated);
		return pcb;
	}

	protected PCB endProcess(String processName) {
		processName = processName.trim();

		PCB process = runQueue.getProcess(processName);
		if (process == null) {
			process = readyQueue.getProcess(processName);
			if (process == null) {
				waitQueue.getProcess(processName);
			}
		}

		if (process != null) {
			return endProcess(process);
		} else {
			System.out.println("进程'" + processName + "'不存在或结束异常");
			return null;
		}
	}

	// 结束进程
	protected PCB endProcess(PCB process) {

		boolean isRemoved = false;

		if (process.getStatus() == ProcessStatus.RUNNING) {
			// 要删除的进程在运行队列
			isRemoved = runQueue.removeProcess(process);
		} else if (process.getStatus() == ProcessStatus.READY) {
			// 要删除的进程在等待队列
			isRemoved = readyQueue.removeProcess(process);
		} else {
			// 要删除的进程在等待队列
			isRemoved = waitQueue.removeProcess(process);
		}
		/*
		 * isRemoved = runQueue.removeProcess(process);
		 * 
		 * if (!isRemoved) { // 要删除的进程在就绪队列 isRemoved =
		 * readyQueue.removeProcess(process); if (!isRemoved) { // 要删除的进程在等待队列 isRemoved
		 * = waitQueue.removeProcess(process); } }
		 */

		if (isRemoved) {
			System.out.println("进程'" + getProcessSimpleIdetification(process) + "'结束");
			processCount--;
			// 回收内存空间
			memoryManager.recovery(process.getMemorySlice());
			return process;
		} else {
			System.out.println("进程'" + getProcessSimpleIdetification(process) + "'不存在或结束异常");
			return null;
		}
	}

	// 结束所有进程
	protected void endAllProcesses(int initTotalMemory) {
		endRuningProcesses();
		endReadyProcesses();
		endWaitingProcesses();
		memoryManager.recoveryAll();
		memoryManager.initMemorySize(initTotalMemory);
		memoryManager.traverse();
	}

	// 结束运行队列所有进程
	protected void endRuningProcesses() {
		runQueue.removeAll();
	}

	// 结束就绪队列所有进程
	protected void endReadyProcesses() {
		readyQueue.removeAll();
	}

	// 结束等待队列所有进程
	protected void endWaitingProcesses() {
		waitQueue.removeAll();
	}

	/*
	 * public void createProcess(String processName) { PCB pcb = new PCB(new
	 * Identification(Runner.processIDCount++, processName)); if (runQueue.size() <
	 * ComputerResource.processorCount) { TransformStatus.toOperate(runQueue, pcb);
	 * System.out.println("process" + pcb.getIdentification().getProcessID() +
	 * " running"); } else { TransformStatus.toReady(readyQueue, pcb);
	 * System.out.println("process" + pcb.getIdentification().getProcessID() +
	 * " 创建进程成功，进入就绪队列"); } }
	 */

	protected void runToReady() {

		if (runQueue.isEmpityQueue()) {
			System.out.println("没有运行中的进程");
		} else {
			PCB process = runQueue.getProcess();
			// 当前进程进入就绪状态
			if (Transformer.runToReady(runQueue, readyQueue))
				System.out.println("进程'" + getProcessSimpleIdetification(process) + "'从运行队列出队,运行态->就绪态");
		}

	}

	protected void readyToRun() {
		// 同一时间只能有小于计算机处理器个数的进程在运行
		if (runQueue.size() < ComputerSetting.processorCount) {
			PCB process = readyQueue.getProcess();
			if (Transformer.readyToRun(readyQueue, runQueue))
				System.out.println("进程'" + getProcessSimpleIdetification(process) + "'从就绪队列出队,就绪态->运行态");
		} else {
			System.out.println("就绪队列进程正在排队");
		}

	}

	protected void runToWait() {
		if (runQueue.isEmpityQueue()) {
			System.out.println("没有运行中的进程");
		} else {
			PCB process = runQueue.getProcess();
			if (Transformer.runToWait(runQueue, waitQueue))
				System.out.println("进程'" + getProcessSimpleIdetification(process) + "'从运行队列出队,运行态->等待态");
		}

	}

	protected void waitToReady() {
		if (waitQueue.isEmpityQueue()) {
			System.out.println("等待队列为空");
		} else {
			PCB process = waitQueue.getProcess();
			if (Transformer.waitToReady(waitQueue, readyQueue))
				System.out.println("进程'" + getProcessSimpleIdetification(process) + "'从等待队列出队,等待态->就绪态");
		}

	}

	protected void runSituation() {
		System.out.print("运行中的进程:");
		if (runQueue.isEmpityQueue()) {
			System.out.println("没有运行的进程");
		} else {
			System.out.println(runQueue.traversingQueue());
		}
	}

	protected void readySituation() {
		System.out.print("就绪队列:");
		if (readyQueue.isEmpityQueue()) {
			System.out.println("就绪队列为空");
		} else {
			System.out.println(readyQueue.traversingQueue());
		}
	}

	protected void waitSituation() {
		System.out.print("等待队列:");
		if (waitQueue.isEmpityQueue()) {
			System.out.println("等待队列为空");
		} else {
			System.out.println(waitQueue.traversingQueue());
		}
	}

	protected void allProcessesSituation() {
		runSituation();
		readySituation();
		waitSituation();
	}

	protected String getProcessSimpleIdetification(PCB process) {
		Identification identification = process.getIdentification();
		return "proc" + identification.getProcessID() + "-" + identification.getProcessName();
	}

}
