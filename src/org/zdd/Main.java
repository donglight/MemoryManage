package org.zdd;

import org.zdd.pm.SwingProcessManager;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		// 1.手动调度
		// new CommandProcessManager().run(args);
		// 2.自动调度
		// new AutoTiemsliceProcessManager().run(args);
		// swing界面演示进程和内存管理
		new SwingProcessManager().run(args);
	}
}
