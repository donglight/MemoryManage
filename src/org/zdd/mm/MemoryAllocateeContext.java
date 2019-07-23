package org.zdd.mm;

import org.zdd.mm.allocatee.Allocatee;
import org.zdd.mm.allocatee.BestFitAllocatee;
import org.zdd.mm.allocatee.FirstFitAllocatee;
import org.zdd.mm.allocatee.NextFitAllocatee;
import org.zdd.mm.allocatee.WorstFitAllocatee;

/**
 * 获取分配者上下文对象或者工厂对象
 * 
 * @author zdd
 *
 */
public class MemoryAllocateeContext {

	private static Allocatee firstFitAllocatee;
	private static Allocatee nextFitAllocatee;
	private static Allocatee bestFitAllocatee;
	private static Allocatee worstFitAllocatee;

	public static Allocatee getAllocatee(String allocateAlgorithm) {

		switch (allocateAlgorithm) {
		case "最先分配":
			if (firstFitAllocatee == null)
				firstFitAllocatee = new FirstFitAllocatee();
			return firstFitAllocatee;
		case "下次分配":
			if (nextFitAllocatee == null)
				nextFitAllocatee = new NextFitAllocatee();
			return nextFitAllocatee;
		case "最优分配":
			if (bestFitAllocatee == null)
				bestFitAllocatee = new BestFitAllocatee();

			return bestFitAllocatee;
		case "最坏分配":
			if (worstFitAllocatee == null)
				worstFitAllocatee = new WorstFitAllocatee();
			return worstFitAllocatee;
		default:
			if (firstFitAllocatee == null) {
				firstFitAllocatee = new FirstFitAllocatee();
			}
			return firstFitAllocatee;
		}
	}
}
