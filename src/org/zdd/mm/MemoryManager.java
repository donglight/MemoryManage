package org.zdd.mm;

import java.util.List;

import org.zdd.mm.allocatee.Allocatee;
import org.zdd.mm.allocatee.FirstFitAllocatee;

/**
 * 内存管理者
 * 
 * @author zdd
 *
 */
public class MemoryManager {

	// 空闲区链表
	private MemoryLinkedList freeZone;

	// 内存分配者，用具体算法找到可用的内存空闲块
	private Allocatee allocatee;

	// 内存回收者
	private Recycler recycler = new Recycler();

	public MemoryManager() {
		// 默认是最先适应分配算法
		this(new FirstFitAllocatee());
	}

	public MemoryManager(long freeSize) {
		// 默认是最先适应分配算法
		this(new FirstFitAllocatee(), freeSize);
	}

	public MemoryManager(Allocatee allocatee) {
		this(allocatee, 65535L);// 若没有指定大小，则默认分配64MB
	}

	public List<MemorySlice> getMemoryLinkedList() {
		return freeZone.getMemoryLinkedList();
	}

	public MemoryManager(Allocatee allocatee, long freeSize) {
		this.allocatee = allocatee;
		// 初始化空闲区内存大小
		freeZone = new MemoryLinkedList(freeSize);
	}

	// 设置或改变具体的内存分配者
	public void setMemoryAllocatee(Allocatee allocatee) {
		this.allocatee = allocatee;
	}

	// 分配内存
	public MemorySlice allocate(int size) {
		MemorySlice allocate = allocatee.allocate(size, freeZone);
		return allocate;
	}

	// 回收内存
	public boolean recovery(MemorySlice memorySlice) {
		/*
		 * boolean isRecovered = false; if(allocatee instanceof FirstFitAllocatee ||
		 * allocatee instanceof NextFitAllocatee ) { isRecovered =
		 * recycler.recovery(memorySlice,freeZone);
		 * freeZone.sort(Allocatee.OFFSET_COMPARATOR); }else if(allocatee instanceof
		 * BestFitAllocatee) { isRecovered = recycler.recovery(memorySlice,freeZone);
		 * freeZone.sort(Allocatee.SIZE_COMPARATOR); }else if(allocatee instanceof
		 * WorstFitAllocatee) { isRecovered = recycler.recovery(memorySlice,freeZone);
		 * freeZone.sort(Allocatee.SIZE_REVERSED_COMPARATOR); }
		 */

		return recycler.recovery(memorySlice, freeZone);
	}

	// 遍历空闲分区链表
	public void traverse() {
		freeZone.traverse((memorySlice) -> {
			System.out.println(memorySlice);
		});
	}

	public void recoveryAll() {
		// 回收所有内存空间，重新初始化空闲分区链表
		freeZone.initial();
	}

	public long getRemainMemory() {
		return freeZone.getRemainFreeSize();
	}

	// 初始化内存
	public void initMemorySize(int totalFreeSize) {
		freeZone.setTotalFreeSize(totalFreeSize);
		freeZone.setRemainFreeSize(totalFreeSize);
		freeZone.initial();
	}
}
