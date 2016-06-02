package ZS.Net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import ZS.JLable.Camera.CameraCapture;
import ZS.Util.Time.TestDate;

import com.Zs.DbUtil.DbUtil;
import com.Zs.View.MainFrm;
/**
 * 用来连接服务器
 * @author AL
 *
 */
public class NetThread extends Thread{
	public static final  String IP = "127.0.0.1";
	private String str;
	private Socket socket;
	private MainFrm mainFrm;
	private PrintWriter printWriter;
	private CameraCapture cameraCapture;
	@Override
	public void run() {
		try {
			Socket socket = new Socket(IP,8089);
			mainFrm.setLinkButtonText("断开");
			mainFrm.setlinkStationLabelText("连接状态：已连接");
			printWriter = new PrintWriter(socket.getOutputStream(),true);
			printWriter.println("computer client is connected");
			InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			while(true)
			{
				str=null;
				str = bufferedReader.readLine();
				System.out.println("str");
				if(str!=null)
				{
					
					String str1=null;
					str1 = str.split("&")[0];//取得接受数据的第一位
					if(str1.equals("record")&&str.length()>3)//如果是记录用的数据
					{
						String[] strTemp={str.split("&")[1],str.split("&")[2],str.split("&")[3]};
						mainFrm.setMainTextArea(DbUtil.changeNetRecordToStr(strTemp)+" "+TestDate.getTime());
					}
					
					
					if(str.equals("打开视频")&&(mainFrm.getCameraFlag()==0 ||mainFrm.getCameraFlag()==2  ))
					{
						mainFrm.openCarmera();
					}
					if(str.equals("关闭视频")&&(mainFrm.getCameraFlag()==1))
					{
						mainFrm.openCarmera();
					}
					if(str.equals("phone get carmera"))
					{
						mainFrm.carmeraTherad=new CarmeraTherad(mainFrm,cameraCapture);
						mainFrm.carmeraTherad.start();
					}
					if(str.equals("开始报警")||str.equals("start alarm"))
					{
						mainFrm.beep.start();
					}else if(str.equals("停止报警")||str.equals("stop alarm"))
					{
						mainFrm.beep.stop();
					}
		
					
					
					System.out.println("Client computer get --->"+str);
				}else
				{
					break;
				}
			}
			
		} catch (UnknownHostException e) {
			mainFrm.setLinkButtonText("连接");
			mainFrm.setlinkStationLabelText("连接状态：网络错误");
			socket = null;
			this.interrupt();
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	public void send(String str) {
		printWriter.println(str);
	}
	
	public NetThread(MainFrm mainFrm,CameraCapture cameraCapture) 
	{
		this.mainFrm = mainFrm;
		this.cameraCapture = cameraCapture;
	}
		
}
