package org.zdd.mm.allocatee;

import java.util.Comparator;

import org.zdd.mm.MemoryLinkedList;
import org.zdd.mm.MemorySlice;

/**
 * 内存分配者借口，根据不同的算法找到可用的内存空闲块
 * 
 * @author zdd
 *
 */
public interface Allocatee {

	// 比较器,用于对象排序
	// 根据内存起始地址递增排序
	Comparator<MemorySlice> OFFSET_COMPARATOR = Comparator.comparingLong(MemorySlice::getOffset);
	// 根据内存大小递增排序
	Comparator<MemorySlice> SIZE_COMPARATOR = Comparator.comparingLong(MemorySlice::getSize);
	// 根据内存大小逆序排序
	Comparator<MemorySlice> SIZE_REVERSED_COMPARATOR = Comparator.comparingLong(MemorySlice::getSize).reversed();

	MemorySlice allocate(long size, MemoryLinkedList freeZone);

}
