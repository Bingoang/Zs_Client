package ZS.RXTX.Comm;
 
import gnu.io.SerialPort;
import java.util.Observer;
import java.util.*;

public class ZS_RXTX_Test implements Observer{ 
	 
	ZS_RXTX_SerialReader sr=new ZS_RXTX_SerialReader(); 
    public ZS_RXTX_Test()
    {    
       //openSerialPort("COM3","9600"); //打开串口
    	super();
    } 
    public boolean close()
    {
    	return sr.close();
    }
    
    private IZS_RXTX_ReceiveString data;
    public interface IZS_RXTX_ReceiveString{
    	
    	void zS_RXTX_ReceiveString(String ReceiveString,byte [] byt);//这个接口用来传递接收到的数据给调用的类
    	
    }
    public void addIZS_RXTX_ReceiveString(IZS_RXTX_ReceiveString data){
    	this.data =data;
    }
    
    
    public void update(Observable o, Object arg){  
    	String mt=new String((byte[])arg);  
    	System.out.println("---"+mt); //串口数据 自动更新！
    	data.zS_RXTX_ReceiveString(mt,sr.getreadBuffer());
    	//System.out.println("dsdfdsfsd");
    } 
    
    /**
     * 往串口发送数据,实现双向通讯.
     * @param string message
     */
    public void send(String message,String port,String rate)
    {
    	ZS_RXTX_Test test = new ZS_RXTX_Test();
    	test.openSerialPort(message,port,rate);
    }
    public void send(byte[] message,String port,String rate)
    {
    	ZS_RXTX_Test test = new ZS_RXTX_Test();
    	test.openSerialPort(message,port,rate);
    }
	
	
    /**
     * 打开串口
     * @param String message
     */
	public boolean openSerialPort(String message,String Port,String Rate)
    { 
        HashMap<String, Comparable> params = new HashMap<String, Comparable>();  
        String port=Port;
        String rate = Rate;
        String dataBit = ""+SerialPort.DATABITS_8;
        String stopBit = ""+SerialPort.STOPBITS_1;
        String parity = ""+SerialPort.PARITY_NONE;    
        int parityInt = SerialPort.PARITY_NONE; 
        params.put( ZS_RXTX_SerialReader.PARAMS_PORT, port ); // 端口名称
        params.put( ZS_RXTX_SerialReader.PARAMS_RATE, rate ); // 波特率
        params.put( ZS_RXTX_SerialReader.PARAMS_DATABITS,dataBit  ); // 数据位
        params.put( ZS_RXTX_SerialReader.PARAMS_STOPBITS, stopBit ); // 停止位
        params.put( ZS_RXTX_SerialReader.PARAMS_PARITY, parityInt ); // 无奇偶校验
        params.put( ZS_RXTX_SerialReader.PARAMS_TIMEOUT,100 ); // 设备超时时间1秒
        params.put( ZS_RXTX_SerialReader.PARAMS_DELAY, 100 ); // 端口数据准备时间 1秒
        try {
			sr.open(params);
		    sr.addObserver(this);
			if(message!=null&&message.length()!=0)
			 {  
				sr.start();  
				sr.run(message); 
			 } 
			return true;
		} catch (Exception e) { 
			System.out.println("错误端口！");
			return false;
		}
    }
	public boolean openSerialPort(byte[] message,String Port,String Rate)
    { 
        HashMap<String, Comparable> params = new HashMap<String, Comparable>();  
        String port=Port;
        String rate = Rate;
        String dataBit = ""+SerialPort.DATABITS_8;
        String stopBit = ""+SerialPort.STOPBITS_1;
        String parity = ""+SerialPort.PARITY_NONE;    
        int parityInt = SerialPort.PARITY_NONE; 
        params.put( ZS_RXTX_SerialReader.PARAMS_PORT, port ); // 端口名称
        params.put( ZS_RXTX_SerialReader.PARAMS_RATE, rate ); // 波特率
        params.put( ZS_RXTX_SerialReader.PARAMS_DATABITS,dataBit  ); // 数据位
        params.put( ZS_RXTX_SerialReader.PARAMS_STOPBITS, stopBit ); // 停止位
        params.put( ZS_RXTX_SerialReader.PARAMS_PARITY, parityInt ); // 无奇偶校验
        params.put( ZS_RXTX_SerialReader.PARAMS_TIMEOUT,100 ); // 设备超时时间1秒
        params.put( ZS_RXTX_SerialReader.PARAMS_DELAY, 100 ); // 端口数据准备时间 1秒
        try {
			sr.open(params);
		    sr.addObserver(this);
			if(message!=null)
			 {  
				sr.start();  
				sr.run(message); 
			 } 
			return true;
		} catch (Exception e) { 
			System.out.println("错误端口！");
			return false;
		}
    }
	
	 public String Bytes2HexString(byte[] b) { 
		   String ret = ""; 
		   for (int i = 0; i < b.length; i++) { 
			     String hex = Integer.toHexString(b[i] & 0xFF); 
			     if (hex.length() == 1) { 
			       hex = '0' + hex; 
				     } 
			     ret += hex.toUpperCase(); 
			   }
		return ret;
	   }


	  public  String hexString2binaryString(String hexString) {
	  if (hexString == null || hexString.length() % 2 != 0)
		 return null;
		 String bString = "", tmp;
		 for (int i = 0; i < hexString.length(); i++) {
		 tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
			  bString += tmp.substring(tmp.length() - 4);
		  }
		 return bString;
	  } 
} 
 
