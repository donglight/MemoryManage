package org.zdd.mm;

public class TestMM {

	public static void main(String[] args) {

		MemoryManager memoryManager = new MemoryManager();
		memoryManager.traverse();
		System.out.println("-------------");

		MemorySlice allocate = memoryManager.allocate(1024);
		MemorySlice allocate2 = memoryManager.allocate(1024);
		MemorySlice allocate3 = memoryManager.allocate(2048);

		memoryManager.recovery(allocate3);
		memoryManager.recovery(allocate);
		MemorySlice allocate4 = memoryManager.allocate(514);
		MemorySlice allocate5 = memoryManager.allocate(1612);
		memoryManager.traverse();
		memoryManager.recovery(allocate4);
		memoryManager.recovery(allocate5);
		memoryManager.recovery(allocate2);

		System.out.println("-------------");
		memoryManager.traverse();
	}
}
