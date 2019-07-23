package org.zdd.mm;

import org.zdd.mm.allocatee.Allocatee;

/**
 * 内存回收器
 * @author ANS
 *
 */
public class Recycler {

	boolean recovery(MemorySlice reMemorySlice, MemoryLinkedList freeZone) {

		if (reMemorySlice == null) {
			return false;
		}
		// 回收时按起始地址从小到大排序，方便合并相邻空闲分区
		freeZone.sort(Allocatee.OFFSET_COMPARATOR);

		long offset = reMemorySlice.getOffset();
		long size = reMemorySlice.getSize();
		int length = freeZone.length();
		long remainFreeSize = freeZone.getRemainFreeSize();

		if (length == 0) {
			// 没有空闲分区
			reMemorySlice.setStatus(MemoryStatus.FREE);
			freeZone.add(reMemorySlice);
			freeZone.setRemainFreeSize(size);
			return true;
		}

		for (int i = 0; i < length; i++) {

			MemorySlice nextFree = freeZone.get(i);
			long noffset = nextFree.getOffset();
			long nsize = nextFree.getSize();

			long poffset = 0, psize = 0;
			MemorySlice prevFree = null;
			if (i != 0) {
				prevFree = freeZone.get(i - 1);
				poffset = prevFree.getOffset();
				psize = prevFree.getSize();
			}

			// 寻找合适的链入位置，要回收的分区的起始地址小于循环当前分区的起始地址的分区
			if (offset < noffset) {
				if (prevFree != null && offset + size == noffset && offset == poffset + psize) {
					// 前后都是相邻分区,合并分区
					freeZone.remove(i);
					prevFree.setSize(size + psize + nsize);
					freeZone.setRemainFreeSize(size + remainFreeSize);
					return true;
				} else if (offset + size == noffset) {
					// 跟下一个分区是相邻分区，合并分区
					nextFree.setOffset(offset);
					nextFree.setSize(size + nsize);
					freeZone.setRemainFreeSize(size + remainFreeSize);
					return true;
				} else if (prevFree != null && offset == poffset + psize) {
					// 跟前一个分区是相邻分区，合并分区
					prevFree.setSize(size + psize);
					freeZone.setRemainFreeSize(size + remainFreeSize);
					return true;
				} else if (offset + size < noffset) {
					reMemorySlice.setStatus(MemoryStatus.FREE);
					freeZone.add(i, reMemorySlice);
					freeZone.setRemainFreeSize(size + remainFreeSize);
					return true;
				}
			}
			// 最后一个分区
			if (i == length - 1) {
				if (offset == noffset + nsize) {
					// 刚好跟前一个分区相邻，合并分区
					nextFree.setSize(nsize + size);
					freeZone.setRemainFreeSize(size + remainFreeSize);
					return true;
				} else {
					reMemorySlice.setStatus(MemoryStatus.FREE);
					freeZone.addLast(reMemorySlice);
					freeZone.setRemainFreeSize(size + remainFreeSize);
					return true;
				}
			}
		}
		return false;
	}

}
