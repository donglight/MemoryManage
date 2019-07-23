package org.zdd.mm.allocatee;

import org.zdd.mm.MemoryLinkedList;
import org.zdd.mm.MemorySlice;
import org.zdd.mm.MemoryStatus;

/**
 * 下次适应分配算法
 * 
 * @author zdd
 *
 */
public class NextFitAllocatee implements Allocatee {

	// 上次分配完内存后的位置
	private int lastIndex;

	/**
	 * 下次适应分配算法分配内存
	 */
	@Override
	public MemorySlice allocate(long size, MemoryLinkedList freeZone) {
		// size>剩余内存分区,不能分配
		if (size > freeZone.getRemainFreeSize()) {
			return null;
		}
		// 空闲区按起始地址从小到大排序
		freeZone.sort(Allocatee.OFFSET_COMPARATOR);
		// 从上一次扫描结束处遍历空闲分区链表，查找满足条件的空闲分区
		for (int i = lastIndex; i < freeZone.length(); i++) {
			// 获取空间的内存分区
			MemorySlice freeMemorySlice = freeZone.get(i);
			// 如果当前空闲区的大小大于需要分配的大小，则分割此内存区域，分配一部分给作业，另一部分仍未空闲区
			if (freeMemorySlice.getSize() > size) {
				// 新建要分配给进程的内存空间
				MemorySlice memorySliceAllocated = new MemorySlice(freeMemorySlice.getOffset(), size,
						MemoryStatus.ALLOCATED);
				// 修改空闲列表中的内存分区的‘起始地址’，因为它的部分内存已被分配
				// 当前起始地址+分配了的内存的大小
				freeMemorySlice.setOffset(freeMemorySlice.getOffset() + size);
				// 修改空闲分区‘大小’
				freeMemorySlice.setSize(freeMemorySlice.getSize() - size);
				// 修改空闲分区剩余大小
				freeZone.setRemainFreeSize(freeZone.getRemainFreeSize() - size);
				lastIndex = i;
				return memorySliceAllocated;
			} else if (freeMemorySlice.getSize() == size) {
				// 要分配的内存刚等于当前空闲区大小，去掉该空闲区,jdk封装的链表帮助链接前后空闲分区
				freeZone.remove(i);
				freeZone.setRemainFreeSize(freeZone.getRemainFreeSize() - size);
				lastIndex = i;
				// 分配一块内存给进程
				freeMemorySlice.setStatus(MemoryStatus.ALLOCATED);
				return freeMemorySlice;
			}
		}
		lastIndex = 0;
		// 否则return null
		return null;
	}

}
