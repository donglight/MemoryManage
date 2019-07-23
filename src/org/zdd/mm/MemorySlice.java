package org.zdd.mm;

/**
 * 分配的内存分区(切片)
 * 
 * @author zdd
 *
 */
public class MemorySlice {

	private long offset;// 起始地址

	private long size;// 内存大小/KB

	private MemoryStatus status;// 内存状态,默认是空闲的

	public MemorySlice() {
		this(128L);
	}

	public MemorySlice(long offset, long size) {
		this(offset, size, MemoryStatus.FREE);
	}

	public MemorySlice(long offset, long size, MemoryStatus status) {
		super();
		this.offset = offset;
		this.size = size;
		this.status = status;
	}

	public MemorySlice(long size) {
		// 用户想要分配到得内存大小,
		// 但是还没有确定有没有空闲内存,
		// 所以没有设置offset起始地址
		this.size = size;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public MemoryStatus getStatus() {
		return status;
	}

	public void setStatus(MemoryStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "MemorySlice [offset=" + offset + ", size=" + size + ", status=" + status + "]";
	}
}
