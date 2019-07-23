package org.zdd.pm;

import java.util.Scanner;

/**
 * 命令行‘手动调度’ 进程管理
 * 
 * @author zdd
 *
 */
public class CommandProcessManager extends AbstractProcessManager {

	@Override
	public void run(String[] args) {

		Scanner scanner = new Scanner(System.in);
		System.out.println("------命令式进程管理(FIFO队列形式)");
		System.out.println("1、创建进程并进入就绪队列;2、运行->就绪;3、就绪->运行;4、运行->等待;5、等待->就绪");
		System.out.println("6、查看进程队列;7、结束进程;8、退出程序");
		while (true) {
			switch (scanner.next()) {
			case "1":
				System.out.println("请输入进程名字");
				createProcess(scanner.next(), r.nextInt(1024));
				break;
			case "2":
				runToReady();
				break;
			case "3":
				readyToRun();
				break;
			case "4":
				runToWait();
				break;
			case "5":
				waitToReady();
				break;
			case "6":
				allProcessesSituation();
				break;
			case "7":
				System.out.println("请输入要结束的进程名字");
				endProcess(scanner.next());
				break;
			case "8":
				/*
				 * System.exit(0); break;
				 */
				endAllProcesses(4096);
				scanner.close();
				return;
			case "9":
				memoryManager.traverse();
				break;
			default:
				System.out.println("指令输入错误，请重新输入");
				break;
			}
		}

	}
}
