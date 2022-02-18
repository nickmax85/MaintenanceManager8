package heapspace;

import java.util.ArrayList;

public class TestHeapSpace {

	public static void main(String[] args) {

		new TestHeapSpace();

	}

	public TestHeapSpace() {

		// runtime();
		list();
	}

	private void runtime() {

		int dummyArraySize = 15;
		System.out.println("Max JVM memory: " + Runtime.getRuntime().maxMemory());
		System.out.println("Free JVM memory: " + Runtime.getRuntime().freeMemory());

		long memoryConsumed = 0;
		try {
			long[] memoryAllocated = null;
			for (int loop = 0; loop < Integer.MAX_VALUE; loop++) {
				memoryAllocated = new long[dummyArraySize];
				memoryAllocated[0] = 0;
				memoryConsumed += dummyArraySize * Long.SIZE;
				System.out.println("Memory Consumed till now: " + memoryConsumed);
				dummyArraySize *= dummyArraySize * 2;
				Thread.sleep(500);
			}
		} catch (OutOfMemoryError outofMemory) {
			System.out.println("Catching out of memory error");
			// Log the information,so that we can generate the statistics
			// (latter on).
			throw outofMemory;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void list() {
		try {
			ArrayList list = new ArrayList();
			Thread.sleep(5000);
			for (long l = 0; l < Long.MAX_VALUE; l++) {

				System.out.println("Free JVM memory: " + Runtime.getRuntime().freeMemory());
				list.add(new Long(l));

				// for (int i = 0; i < 5000; i++) {
				//
				// if (i == 5000)
				// break;
				// }

			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void memory() {

		long[] l = new long[Integer.MAX_VALUE];
	}

}
