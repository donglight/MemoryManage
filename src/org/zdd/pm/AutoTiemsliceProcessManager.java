package org.zdd.pm;

/**
 * 命令行‘时间片轮转’自动调度 进程管理
 * 
 * @author zdd
 *
 */
public class AutoTiemsliceProcessManager extends AbstractProcessManager {

	// cpu时间片
	private int tiemslice = ComputerSetting.timeslice;

	@Override
	public void run(String[] args) throws InterruptedException {
		// 创建10个进程,A->J
		for (int i = 0; i < 5; i++) {
			createProcess(String.valueOf((char) ('A' + i)), r.nextInt(1024));
		}
		// 此循环模拟进程随机执行
		while (true) {

			// 运行进程(就绪队列中的进程调入到运行队列)
			readyToRun();

			if (processCount == 0) {
				System.out.println("所有进程运行完毕！");
				break;
			}
			PCB process = runQueue.getProcess();

			// 把等待队列的进程拉到就绪队列，再由就绪态转到运行态
			if (process == null) {
				waitToReady();
				readyToRun();
				process = runQueue.getProcess();
			}

			int tiemsliceTemp = tiemslice;
			int remainingTime = process.getRemainingTime();
			System.out.print(getProcessSimpleIdetification(process) + " 正在运行");

			// 此循环模拟消耗时间片，每次消耗随机时间的时间片
			while (true) {
				// 随机执行时间，直到时间片用完了
				int randomTime = r.nextInt(200);
				remainingTime -= randomTime;
				tiemsliceTemp -= randomTime;

				if (remainingTime < 0) {
					// remainingTime<0,说明当前进程执行完了
					// 结束就绪队列队列中的进程
					endProcess(process);
					// 当前进程执行完了，转去执行其他进程
					readyToRun();
					process = runQueue.getProcess();
					if (process == null) {
						break;
					} else {
						// 当前进程执行完了，还有时间片，则转去执行就绪队列的进程
						System.out.print(getProcessSimpleIdetification(process) + " 正在运行");
						remainingTime = process.getRemainingTime();
						remainingTime -= randomTime;
						if (tiemsliceTemp > 0) {
							Thread.sleep(randomTime);
							// .表示进程正在运行
							System.out.print(".");
						} else {
							Thread.sleep(tiemsliceTemp + randomTime);
							System.out.print(".");
							break;
						}
					}

				} else {
					if (tiemsliceTemp > 0) {
						Thread.sleep(randomTime);
						System.out.print(".");
					} else {
						Thread.sleep(tiemsliceTemp + randomTime);
						System.out.print(".");
						break;
					}
				}
				process.setRemainingTime(remainingTime);
				// 模拟发生等待事件，由运行态->阻塞态
				if (r.nextInt(8) == 5) {
					System.out.println("([随机]发生io事件,'" + getProcessSimpleIdetification(process) + "'进程进入等待队列！)");
					runToWait();
					new Thread(() -> {
						try {
							// 模拟等待事件
							Thread.sleep(r.nextInt(1000));
							if (processCount != 0) {
								System.out.print(" (--io执行完毕--) ");
								waitToReady();
							}

						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}).start();
					// 就绪->运行
					readyToRun();
					process = runQueue.getProcess();
					if (process != null) {
						System.out.print(getProcessSimpleIdetification(process) + " 正在运行");
					} else if (process == null && processCount != 0) {
						waitToReady();
						readyToRun();
						process = runQueue.getProcess();
					}
					remainingTime = process.getRemainingTime();
					System.out.print(getProcessSimpleIdetification(process) + " 正在运行");
				}
			}
			if (remainingTime > 0) {
				System.out.print("剩余时间:" + remainingTime + "ms ");
			}
			if (processCount != 0) {

				// 进程时间片使用完了，时间片轮转
				System.out.println("时间片用完了，进程转换");
				// 把当前进程状态改为就绪态，并把'运行队列中'的进程转存到'就绪队列'尾部
				runToReady();
			}
		}
	}
}
