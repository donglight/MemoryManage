package org.zdd.mm;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 内存链表数据结构
 * 
 * @author lenovo
 *
 */
public class MemoryLinkedList {

	// jdk链表
	private LinkedList<MemorySlice> memorySliceList = new LinkedList<>();

	// 空闲区总大小/KB
	private long totalFreeSize;

	// 空闲区当前大小/KB
	private long remainFreeSize;

	public MemoryLinkedList() {
		this(4096L);// 若没有指定大小，则默认分配4MB
	}

	public MemoryLinkedList(long totalFreeSize) {
		this.totalFreeSize = totalFreeSize;
		this.remainFreeSize = totalFreeSize;
		// 初始化空闲区内存大小，一开始只有一块空闲区
		memorySliceList.add(new MemorySlice(0, totalFreeSize, MemoryStatus.FREE));
	}

	public MemorySlice get(int index) {
		return memorySliceList.get(index);
	}

	public List<MemorySlice> getMemoryLinkedList() {
		return memorySliceList;
	}

	public void add(int index, MemorySlice memorySlice) {
		memorySliceList.add(index, memorySlice);
	}

	public void add(MemorySlice memorySlice) {
		memorySliceList.add(memorySlice);
	}

	public void addLast(MemorySlice memorySlice) {
		memorySliceList.addLast(memorySlice);
	}

	public void addFirst(MemorySlice memorySlice) {
		memorySliceList.addFirst(memorySlice);
	}

	public void remove(int index) {
		memorySliceList.remove(index);
	}

	public int length() {
		return memorySliceList.size();
	}

	public void sort(Comparator<MemorySlice> comparator) {
		memorySliceList.sort(comparator);
	}

	public void traverse(Consumer<MemorySlice> consumer) {
		memorySliceList.forEach(consumer);
	}

	public void initial() {
		memorySliceList.removeAll(memorySliceList);
		memorySliceList.add(new MemorySlice(0, totalFreeSize, MemoryStatus.FREE));
	}

	public long getTotalFreeSize() {
		return totalFreeSize;
	}

	public void setTotalFreeSize(long totalFreeSize) {
		this.totalFreeSize = totalFreeSize;
	}

	public long getRemainFreeSize() {
		return remainFreeSize;
	}

	public void setRemainFreeSize(long remainFreeSize) {
		this.remainFreeSize = remainFreeSize;
	}
}
