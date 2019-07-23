package org.zdd.pm;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.zdd.mm.MemoryAllocateeContext;
import org.zdd.mm.MemorySlice;

/**
 * 内存管理swing界面
 * 
 * @author ANS
 *
 */
public class SwingProcessManager extends AbstractProcessManager {

	private JTextField memorySizeField;
	private JTextField randomMemoryField;
	private JComboBox<String> processesComboBox;
	private JComboBox<String> allocationAlgorithmComboBox;
	private JComboBox<String> memoryComboBox;
	private JButton allocateButton;
	private JButton endProcessButton;
	private MyPanelAndMouseListener memoryPanelAndMouseListener;
	private JButton randomButton;
	private JTextField remainMemoryField;

	private int totalMemory;
	private char processName = 'A';

	private int y = 2;

	private double panelHeight;
	private double panelWidth;
	// 比例尺
	private double scale;

	@Override
	public void run(String[] args) throws InterruptedException {
		Font f = new Font("宋体", Font.PLAIN, 14);
		UIManager.put("Label.font", f);
		UIManager.put("ComboBox.font", f);
		UIManager.put("Button.font", f);
		JFrame jFrame = new JFrame("內存管理");
		jFrame.setLayout(null);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setResizable(false);
		int screenWidth = ((int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
		int screenHeight = ((int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
		jFrame.setBounds((screenWidth - 400) / 2, (screenHeight - 400) / 2, 410, 450);
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(0, 0, 410, 450);

		allocateButton = new JButton("分配内存给进程");
		allocateButton.setBounds(30, 150, 150, 20);
		allocateButton.setContentAreaFilled(false);
		memorySizeField = new JTextField();
		memorySizeField.setBounds(200, 150, 150, 20);
		JLabel jl1 = new JLabel("KB");
		jl1.setBounds(350, 150, 20, 20);

		randomButton = new JButton("随机分配内存");
		randomButton.setBounds(30, 200, 150, 20);
		randomButton.setContentAreaFilled(false);
		randomMemoryField = new JTextField();
		randomMemoryField.setBounds(200, 200, 150, 20);
		randomMemoryField.setEditable(false);
		JLabel jl2 = new JLabel("KB");
		jl2.setBounds(350, 200, 20, 20);

		endProcessButton = new JButton("结束进程");
		endProcessButton.setBounds(30, 370, 100, 20);
		endProcessButton.setContentAreaFilled(false);
		processesComboBox = new JComboBox<>();
		processesComboBox.setBounds(150, 370, 240, 20);

		JLabel jl4 = new JLabel("选择内存分配算法:");
		jl4.setBounds(60, 50, 120, 20);
		String[] allocationAlgorithmComboBoxList = new String[] { "最先分配", "下次分配", "最优分配", "最坏分配" };
		allocationAlgorithmComboBox = new JComboBox<>(allocationAlgorithmComboBoxList);
		allocationAlgorithmComboBox.setBounds(200, 50, 150, 22);
		allocationAlgorithmComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					memoryManager.setMemoryAllocatee(MemoryAllocateeContext.getAllocatee(e.getItem().toString()));
					processName = 'A';
				} else {
					return;
				}
			}
		});

		JLabel jl5 = new JLabel("初始化内存大小:");
		jl5.setBounds(70, 100, 120, 20);
		String[] memorySizeList = new String[] { "64MB", "128MB", "512MB", "1024MB" };
		memoryComboBox = new JComboBox<>(memorySizeList);
		memoryComboBox.setBounds(200, 100, 150, 20);
		memoryComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String remainMemoryStr = (String) e.getItem().toString();
					totalMemory = Integer.parseInt(remainMemoryStr.substring(0, remainMemoryStr.indexOf('M'))) * 1024;
					remainMemoryField.setText(totalMemory + "");
					// 修改比例尺
					scale = panelWidth / totalMemory;
					endAllProcesses(totalMemory);
					memoryPanelAndMouseListener.updateUI();
					processesComboBox.removeAllItems();
					processName = 'A';
				}
			}
		});

		JLabel jl8 = new JLabel("内存状态(绿色为空闲分区,灰色为被占用)");
		jl8.setBounds(20, 230, 300, 20);
		memoryPanelAndMouseListener = new MyPanelAndMouseListener();
		memoryPanelAndMouseListener.setBounds(20, 250, 364, 50);
		memoryPanelAndMouseListener.setBorder(BorderFactory.createEtchedBorder());

		JLabel jl6 = new JLabel("剩余内存:");
		jl6.setBounds(115, 320, 150, 20);
		remainMemoryField = new JTextField();
		remainMemoryField.setBounds(200, 320, 150, 20);
		remainMemoryField.setEditable(false);
		String remainingMemoryStr = memoryComboBox.getSelectedItem().toString();
		int tempTotalMemory = Integer.parseInt(remainingMemoryStr.substring(0, remainingMemoryStr.indexOf('M'))) * 1024;
		remainMemoryField.setText(
				Integer.parseInt(remainingMemoryStr.substring(0, remainingMemoryStr.indexOf('M'))) * 1024 + "");
		totalMemory = tempTotalMemory;
		JLabel jl7 = new JLabel("KB");
		jl7.setBounds(350, 320, 20, 20);

		panel.add(remainMemoryField);
		panel.add(jl6);
		panel.add(jl7);
		panel.add(jl8);
		panel.add(processesComboBox);
		panel.add(memoryPanelAndMouseListener);
		panel.add(allocationAlgorithmComboBox);
		panel.add(memoryComboBox);
		panel.add(jl4);
		panel.add(jl5);
		panel.add(memorySizeField);
		panel.add(endProcessButton);
		panel.add(memorySizeField);
		panel.add(allocateButton);
		panel.add(jl1);
		panel.add(randomButton);
		panel.add(jl2);
		panel.add(randomMemoryField);
		jFrame.setContentPane(panel);
		jFrame.setVisible(true);

		// Graphics必须在setVisible后面获取，否则为null
		memoryPanelAndMouseListener.setGraphics(memoryPanelAndMouseListener.getGraphics());
		// 得到面板信息，初始化当前类的成员变量
		panelHeight = memoryPanelAndMouseListener.getSize().getHeight();
		panelWidth = memoryPanelAndMouseListener.getSize().getWidth() - 3;
		// 初始化比例尺
		scale = panelWidth / totalMemory;

		memorySizeField.requestFocus();

		// 添加鼠标监听器方法
		allocateButton.addMouseListener(memoryPanelAndMouseListener);
		endProcessButton.addMouseListener(memoryPanelAndMouseListener);
		randomButton.addMouseListener(memoryPanelAndMouseListener);
	}

	class MyPanelAndMouseListener extends JPanel implements MouseListener {

		private static final long serialVersionUID = 1L;

		private Graphics g;

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			List<MemorySlice> memoryLinkedList = memoryManager.getMemoryLinkedList();
			for (MemorySlice memorySlice : memoryLinkedList) {

				int fillOffset = (int) Math.ceil((memorySlice.getOffset() * scale));
				int fillSize = (int) Math.ceil((memorySlice.getSize() * scale));
				g.setColor(Color.green);
				if (memorySlice.getOffset() == 0) {
					fillOffset += 1;
				}
				g.fillRect(fillOffset, y, fillSize, (int) panelHeight - 5);
			}
		}

		public void setGraphics(Graphics graphics) {
			this.g = graphics;
		}

		@Override
		// 鼠标点击按钮事件来了
		public void mouseClicked(MouseEvent e) {

			String memorySizeStr = memorySizeField.getText().trim();

			// 正则表达式判断数字
			Pattern pattern = Pattern.compile("^[0-9]*$");
			Object source = e.getSource();
			if (source == allocateButton) {
				if ("".equals(memorySizeStr) || memorySizeStr == null) {
					JOptionPane.showMessageDialog(null, "内存不能为空!", "警告", JOptionPane.WARNING_MESSAGE);
					return;
				}

				if (!pattern.matcher(memorySizeStr).matches()) {
					JOptionPane.showMessageDialog(null, "输入格式错误!", "警告", JOptionPane.WARNING_MESSAGE);
					return;
				}
				createProcessAndFillInColors(Integer.parseInt(memorySizeStr), scale);

			} else if (source == randomButton) {

				int randomMemory = r.nextInt((int) (totalMemory * 0.2));
				randomMemoryField.setText(randomMemory + "");
				createProcessAndFillInColors(randomMemory, scale);

			} else if (source == endProcessButton) {
				Object selectedProcess = processesComboBox.getSelectedItem();
				if (selectedProcess != null) {
					String pString = selectedProcess.toString();
					endProcessAndClearColors(
							selectedProcess.toString().substring(pString.indexOf('-') + 1, pString.indexOf(';')),
							scale);
				}
			}
			remainMemoryField.setText(memoryManager.getRemainMemory() + "");
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		// 创建进程并且填充面板颜色
		private void createProcessAndFillInColors(int memory, double scale) {
			
			PCB process = createProcess(String.valueOf((char) processName++), memory);
			if (processName >= 'Z') {
				processName = 'A';
			}
			if (process != null) {
				MemorySlice memorySlice = process.getMemorySlice();
				int fillOffset = (int) Math.ceil((memorySlice.getOffset() * scale));
				int fillSize = (int) Math.ceil((memorySlice.getSize() * scale));
				g.setColor(getBackground());
				if (memorySlice.getOffset() == 0) {
					fillOffset += 1;
				}
				processesComboBox.addItem(getProcessSimpleIdetification(process) + ";offset:" + memorySlice.getOffset()
						+ ";" + memorySlice.getSize() + "KB");
				g.fillRect(fillOffset, y, fillSize, (int) panelHeight - 5);
			} else {
				JOptionPane.showMessageDialog(null, "没有足够大的内存分区!", "错误", JOptionPane.ERROR_MESSAGE);
			}
		}

		private void endProcessAndClearColors(String processName, double scale) {
			PCB process = endProcess(processName);
			if (process != null) {
				MemorySlice memorySlice = process.getMemorySlice();
				int fillOffset = (int) Math.ceil((memorySlice.getOffset() * scale));
				int fillSize = (int) Math.ceil((memorySlice.getSize() * scale));
				g.setColor(Color.GREEN);
				if (memorySlice.getOffset() == 0) {
					fillOffset += 1;
				}
				processesComboBox.removeItem(getProcessSimpleIdetification(process) + ";offset:" + memorySlice.getOffset()
				+ ";" + memorySlice.getSize() + "KB");
				g.fillRect(fillOffset, y, fillSize, (int) panelHeight - 5);
			} else {
				JOptionPane.showMessageDialog(null, "进程不存在!", "错误", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// 设置swing全局统一字体
	/*
	 * private static void InitGlobalFont(Font font) { FontUIResource fontRes = new
	 * FontUIResource(font); for (Enumeration<Object> keys =
	 * UIManager.getDefaults().keys(); keys.hasMoreElements(); ) { Object key =
	 * keys.nextElement(); Object value = UIManager.get(key); if (value instanceof
	 * FontUIResource) { UIManager.put(key, fontRes); } } }
	 */

}
