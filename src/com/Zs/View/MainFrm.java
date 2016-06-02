/*
 * MainFrm.java
 *
 * Created on __DATE__, __TIME__
 */

package com.Zs.View;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import sun.java2d.pipe.DrawImage;
import sun.rmi.transport.LiveRef;

import ZS.JLable.Camera.CameraCapture.IgetBufferedImag;
import ZS.Net.CarmeraTherad;
import ZS.Net.NetThread;
import ZS.RXTX.Comm.ZS_RXTX_Test;
import ZS.RXTX.Comm.ZS_RXTX_Test.IZS_RXTX_ReceiveString;
import ZS.Util.Bgp.BackgroundPanel;
import ZS.Util.Time.TestDate;
import ZsUtil.Beep;

/**
 *
 * @author  __USER__
 */
public class MainFrm extends javax.swing.JFrame implements
		IZS_RXTX_ReceiveString, IgetBufferedImag {
	//******************报警***********************//
	public Beep beep = new Beep();
	//***********************NET ****************/
	private VideoThread videoThread = null;
	private String imageURL = "http://192.168.8.1:8083/?action=snapshot";
	public int linkStation = 0;// 0 断开 1 正在连接 2以连接
	private NetThread netThread = null;
	public CarmeraTherad carmeraTherad;
	//******************************************//
	private BackgroundPanel bgp;
	private ZS_RXTX_Test zS_RXTX_Test = new ZS_RXTX_Test();
	private String Port;
	private Integer ComNumber = 1;
	private static byte[] byt = { 0x10, 0x00, 0x11 };//用来发送命令！
	private static ZS.JLable.Camera.CameraCapture carCameraCapture = new ZS.JLable.Camera.CameraCapture(); //声明全局变量CameraCapture，BufferedImage
	private BufferedImage imgForSave = null;
	private int cameraFlag = 0;
	//建立一个线程MyThread（它也是一个类），在这个线程中是获得视频流的方法
	private MyThread mThred = new MyThread();/*把类MyThread实体化变成对象，
																				即对象是mThred（通常首字母改成小写）*/

	public class MyThread extends Thread { /*定义类MyThread，因为要使用到对话框的类jLabel1，所以不能
											                             另建一个.java文件来定义MyThread，
											                             而CameraCapture，TestDate则可以*/
		public void run() {
			try {
				carCameraCapture.addIgetBufferedImag(new IgetBufferedImag() {
					@Override
					//重写串口函数
					public void getGrabImage(BufferedImage bufferedImage) {
						// TODO Auto-generated method stub
						jLabel2.setIcon(new ImageIcon(bufferedImage));//从CameraCapture传过来数据
						imgForSave = bufferedImage;
					}
				});
				carCameraCapture.creatCarmera();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/** Creates new form MainFrm 
	 * @throws MalformedURLException */
	public MainFrm() throws MalformedURLException {

		//改变系统默认字体
		Font font = new Font("Dialog", Font.PLAIN, 12);
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof javax.swing.plaf.FontUIResource) {
				UIManager.put(key, font);
			}
		}

		initComponents();//所有控件等类都付处置，记住此初始化函数一定要放在第一位，否则出现空指针现象！
		initBackground(); //背景初始化一般放第二位！
		this.SearchingTabbedPane.addTab("简单检索", new SearchingSimplePanel());
		this.SearchingTabbedPane.addTab("专家检索", new SearchingExpertPanel());
		zS_RXTX_Test.addIZS_RXTX_ReceiveString(this);
		this.initSerialPort();
		videoThread = new VideoThread();
	}

	//第1步：自己写一个初始化装背景的容器函数；第2步，单独写一个类BackgroundPanel；
	private void initBackground() {
		bgp = new BackgroundPanel((new ImageIcon("images\\9.jpg")).getImage());//引入背景图片
		//bgp.setBounds(0,0,400,300);
		this.jPanelNB.add(bgp);
	}

	public void initSerialPort() {
		if (this.zS_RXTX_Test.openSerialPort("OK",
				"COM" + ComNumber.toString(), "9600")) {
			JOptionPane.showMessageDialog(null, "自动串口打开成功，串口号为：" + "COM"
					+ ComNumber.toString());
			//System.out.println("串口打开成功，串口号为："+"COM"+ComNumber.toString());
		} else if (ComNumber < 10) {
			ComNumber++;
			initSerialPort();
		} else {
			JOptionPane.showMessageDialog(null, "自动打开串口失败,请手动开启！");
		}
	}

	//GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		jPanelNB = new javax.swing.JPanel();
		jLabel2 = new javax.swing.JLabel();
		SearchingTabbedPane = new javax.swing.JTabbedPane();
		linkStationLabel = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		mainTextArea = new javax.swing.JTextArea();
		jLabel4 = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		linkStationLabel.setForeground(new java.awt.Color(255, 255, 255));
		linkStationLabel.setText("\u8fde\u63a5\u72b6\u6001:\u672a\u8fde\u63a5");

		mainTextArea.setColumns(20);
		mainTextArea.setRows(5);
		jScrollPane1.setViewportView(mainTextArea);

		jLabel4.setIcon(new javax.swing.ImageIcon(
				"D:\\JAVA2\\Zs_Fender_Client\\images\\wifi-NO.png")); // NOI18N
		jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jLabel4MouseClicked(evt);
			}
		});

		jLabel5.setIcon(new javax.swing.ImageIcon(
				"D:\\JAVA2\\Zs_Fender_Client\\images\\admini.png")); // NOI18N
		jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jLabel5MouseClicked(evt);
			}
		});

		jLabel6.setIcon(new javax.swing.ImageIcon(
				"D:\\JAVA2\\Zs_Fender_Client\\images\\eye-paly.png")); // NOI18N
		jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jLabel6MouseClicked(evt);
			}
		});

		jLabel7.setIcon(new javax.swing.ImageIcon(
				"D:\\JAVA2\\Zs_Fender_Client\\images\\camera.png")); // NOI18N
		jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jLabel7MouseClicked(evt);
			}
		});

		javax.swing.GroupLayout jPanelNBLayout = new javax.swing.GroupLayout(
				jPanelNB);
		jPanelNB.setLayout(jPanelNBLayout);
		jPanelNBLayout
				.setHorizontalGroup(jPanelNBLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanelNBLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanelNBLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																jPanelNBLayout
																		.createSequentialGroup()
																		.addGap(20,
																				20,
																				20)
																		.addComponent(
																				linkStationLabel,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				95,
																				javax.swing.GroupLayout.PREFERRED_SIZE))
														.addGroup(
																jPanelNBLayout
																		.createSequentialGroup()
																		.addGroup(
																				jPanelNBLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.TRAILING)
																						.addComponent(
																								jLabel5)
																						.addComponent(
																								jLabel4))
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addGroup(
																				jPanelNBLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addComponent(
																								jLabel7)
																						.addComponent(
																								jLabel6)))
														.addComponent(
																jLabel2,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																545,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(203, 203, 203)
										.addGroup(
												jPanelNBLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING,
																false)
														.addComponent(
																jScrollPane1)
														.addComponent(
																SearchingTabbedPane,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																568,
																Short.MAX_VALUE))
										.addContainerGap(1370, Short.MAX_VALUE)));
		jPanelNBLayout
				.setVerticalGroup(jPanelNBLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanelNBLayout
										.createSequentialGroup()
										.addGroup(
												jPanelNBLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																jPanelNBLayout
																		.createSequentialGroup()
																		.addGap(40,
																				40,
																				40)
																		.addComponent(
																				jLabel2,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				350,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addGap(38,
																				38,
																				38)
																		.addComponent(
																				linkStationLabel)
																		.addGap(10,
																				10,
																				10)
																		.addGroup(
																				jPanelNBLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addGroup(
																								jPanelNBLayout
																										.createSequentialGroup()
																										.addComponent(
																												jLabel4)
																										.addPreferredGap(
																												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																										.addComponent(
																												jLabel5,
																												javax.swing.GroupLayout.PREFERRED_SIZE,
																												116,
																												javax.swing.GroupLayout.PREFERRED_SIZE))
																						.addGroup(
																								jPanelNBLayout
																										.createSequentialGroup()
																										.addComponent(
																												jLabel6)
																										.addPreferredGap(
																												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																										.addGroup(
																												jPanelNBLayout
																														.createParallelGroup(
																																javax.swing.GroupLayout.Alignment.LEADING)
																														.addComponent(
																																jScrollPane1,
																																javax.swing.GroupLayout.PREFERRED_SIZE,
																																javax.swing.GroupLayout.DEFAULT_SIZE,
																																javax.swing.GroupLayout.PREFERRED_SIZE)
																														.addComponent(
																																jLabel7,
																																javax.swing.GroupLayout.PREFERRED_SIZE,
																																116,
																																javax.swing.GroupLayout.PREFERRED_SIZE)))))
														.addGroup(
																jPanelNBLayout
																		.createSequentialGroup()
																		.addGap(32,
																				32,
																				32)
																		.addComponent(
																				SearchingTabbedPane,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				536,
																				javax.swing.GroupLayout.PREFERRED_SIZE)))
										.addContainerGap(659, Short.MAX_VALUE)));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addComponent(jPanelNB,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				jPanelNB, javax.swing.GroupLayout.DEFAULT_SIZE,
				javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

		pack();
	}// </editor-fold>
	//GEN-END:initComponents

	private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {
		SignFrm a = new SignFrm();
		a.setVisible(true);
		a.setMain(this);
		this.setEnabled(false);
	}

	private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {
		try {
			ImageIO.write(imgForSave, "jpg",
					new File("c:\\tmp\\" + TestDate.getTime() + ".jpg"));
			ImageIcon imageicon = new ImageIcon("images\\camera-yes.png");
			jLabel7.setIcon(imageicon);
			//			sleep(1000);
			ImageIcon imageicon1 = new ImageIcon("images\\camera.png");
			jLabel7.setIcon(imageicon1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //调用了TestDate类中的getTime()函数
		System.out.println("成功");
	}

	private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {
		openVideo();
	}

	private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {
		linkNetClick();
	}

	private void linkNetClick() {
		switch (linkStation) {
		case 0:
			netThread = new NetThread(this, carCameraCapture);
			netThread.start();
			linkStation = 1;
			setlinkStationLabelText("连接状态：正在连接");
			ImageIcon imageicon = new ImageIcon("images\\wifi-middle.png");
			jLabel4.setIcon(imageicon);
			break;
		case 1:
			netThread.interrupt();
			linkStation = 0;
			setlinkStationLabelText("连接扎状态：断开");
			ImageIcon imageicon1 = new ImageIcon("images\\wifi-NO.png");
			jLabel4.setIcon(imageicon1);
			break;
		case 2:
			netThread.interrupt();
			linkStation = 0;
			setlinkStationLabelText("连接扎状态：断开");
			ImageIcon imageicon2 = new ImageIcon("images\\wifi-NO.png");
			jLabel4.setIcon(imageicon2);
			break;
		default:
			break;
		}
	}

	public void openVideo() {
		switch (cameraFlag) {
		case 0:
			cameraFlag = 1;
			ImageIcon imageicon = new ImageIcon("images\\eye-pause.png");
			jLabel6.setIcon(imageicon);
			videoThread.start();

			break;
		case 1:
			ImageIcon imageicon1 = new ImageIcon("images\\eye-play.png");
			jLabel6.setIcon(imageicon1);
			cameraFlag = 2;
			videoThread.suspend();
			this.jLabel2.setIcon(null);

			break;
		case 2:
			ImageIcon imageicon2 = new ImageIcon("images\\eye-pause.png");
			jLabel6.setIcon(imageicon2);
			cameraFlag = 1;
			videoThread.resume();
			break;

		default:
			break;
		}
	}

	public void openCarmera() {
		switch (cameraFlag) {
		case 0:
			cameraFlag = 1;
			ImageIcon imageicon = new ImageIcon("images\\eye-pause.png");
			jLabel6.setIcon(imageicon);
			mThred.start();
			netThread.send("to phone&视频已打开");

			break;
		case 1:
			ImageIcon imageicon1 = new ImageIcon("images\\eye-play.png");
			jLabel6.setIcon(imageicon1);
			cameraFlag = 2;
			mThred.suspend();
			netThread.send("to phone&视频已关闭");
			this.jLabel2.setIcon(null);

			break;
		case 2:
			ImageIcon imageicon2 = new ImageIcon("images\\eye-pause.png");
			jLabel6.setIcon(imageicon2);
			cameraFlag = 1;
			mThred.resume();
			netThread.send("to phone&视频已打开");

			break;

		default:
			break;
		}

	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainFrm().setVisible(true);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	//GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JTabbedPane SearchingTabbedPane;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JPanel jPanelNB;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JLabel linkStationLabel;
	private javax.swing.JTextArea mainTextArea;

	// End of variables declaration//GEN-END:variables
	/**
	 * 接收到的数据在这里
	 */
	@Override
	public void zS_RXTX_ReceiveString(String arg0, byte[] byt) {
		// TODO Auto-generated method stub
		int i = 0;
		System.out.println("接收到：" + arg0);
		while (byt[i] != 0) {
			System.out.println("接收到：" + byt[i]);
			i++;
		}

	}

	@Override
	public void getGrabImage(BufferedImage bufferedImage) {
		// TODO Auto-generated method stub

	}

	//******************NET****************//
	public void setLinkButtonText(String str) {
		jLabel4.setText(str);
	}

	public void setlinkStationLabelText(String str) {
		linkStationLabel.setText(str);
	}

	public int getCameraFlag() {
		return cameraFlag;
	}

	public void setCameraFlag(int cameraFlag) {
		this.cameraFlag = cameraFlag;
	}

	public void catchCarmera() {
		try {
			ImageIO.write(imgForSave, "jpg", new File("D:\\carmera.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //调用了TestDate类中的getTime()函数
		System.out.println("截图成功  for send");
	}

	//**************Public 函数*************//
	public void setMainTextArea(String str) {
		mainTextArea.setText(mainTextArea.getText() + TestDate.getTime()
				+ "----->" + str + "\n");
	}

	class VideoThread extends Thread {
		URL url = new URL(imageURL);

		public VideoThread() throws MalformedURLException {
		}

		@Override
		public void run() {
			while (true) {
				InputStream inputStream;
				try {
					inputStream = url.openConnection().getInputStream();
					BufferedImage image = ImageIO.read(inputStream);//对得到的bufferimage进行缩放处理
					BufferedImage newImage = new BufferedImage(
							image.getWidth() * 2, image.getHeight() * 2,
							BufferedImage.TYPE_INT_BGR);
					Graphics graphics = newImage.createGraphics();
					graphics.drawImage(image, 0, 0, image.getWidth() * 2,
							image.getHeight() * 2, null);
					System.out.println("running");
					jLabel2.setIcon(new ImageIcon(newImage));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}