package ZS.JLable.Camera;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.Timer;
import javax.swing.text.html.HTMLDocument.Iterator;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


import ZS.Util.Time.TestDate;


/**
 * 
 * Use JavaCV/OpenCV to capture camera images
 * 
 * There are two functions in this demo:
 * 1) show real-time camera images 
 * 2) capture camera images by mouse-clicking anywhere in the JFrame, 
 * the jpg file is saved in a hard-coded path. 
 * 
 * @author ljs
 * 2011-08-19
 *
 */
public class CameraCapture {
	
	//timer for image capture animation
	static class TimerAction implements ActionListener {
		private Graphics2D g;
		//private CanvasFrame canvasFrame;
		private int width,height;
		private int delta=10;
		private int count = 0;
		private Timer timer;
		public void setTimer(Timer timer){
		this.timer = timer;
		}

		public TimerAction(CanvasFrame canvasFrame){
			this.g = (Graphics2D)canvasFrame.getCanvas().getGraphics();	
			this.width = canvasFrame.getCanvas().getWidth();
			this.height = canvasFrame.getCanvas().getHeight();
		}
        public void actionPerformed(ActionEvent e) {
        	int offset = delta*count;
        	if(width-offset>=offset && height-offset >= offset) {        
        		g.drawRect(offset, offset, width-2*offset, height-2*offset);
        		//canvasFrame.repaint();
                count++;
        	}else{
        		//when animation is done, reset count and stop timer.
        		timer.stop();
        		count = 0;
        	}            
        }
    }
	//接口 用来将截取图像 传到调用它的类中 格式BufferedImage
	private Vector<IgetBufferedImag> bufferedImags = new Vector<CameraCapture.IgetBufferedImag>();
	private IgetBufferedImag bufferedImag;
	public interface IgetBufferedImag
	{
		void getGrabImage(BufferedImage bufferedImage);
	}
	public void addIgetBufferedImag(IgetBufferedImag i )
	{
		this.bufferedImags.add(i);
	}
	
	public void creatCarmera() throws Exception 
	{
		//open camera source
				OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
				grabber.start();
				//create a frame for real-time image display
				Label label = new Label();
				IplImage image = grabber.grab();
				int width = image.width();
				int height = image.height();
		        final BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		        Graphics2D bGraphics = bImage.createGraphics();     
		        while( (image=grabber.grab()) != null){
		        		for(int i=0;i<bufferedImags.size();i++)//遍历的方式把数据传输到添加过这个接口的类中去
		        		{
		        			bufferedImags.get(i).getGrabImage(image.getBufferedImage());
		        		}
//			        	bufferedImag.getGrabImage(image.getBufferedImage());//将数据传到调用的类中去
			        	bGraphics.drawImage(image.getBufferedImage(),null,0,0);  
		        }
//		        InputStream fis = new InputStream(image.getBufferedImage());
		       // cvReleaseImage(image);	 
		        grabber.stop();
		        
	}

}

//public InputStream getImageStream(String layerName,List colors,String[] pixels){
//    InputStream is = null;
//    BufferedImage bi ;
//    bi.flush();
//    ByteArrayOutputStream bs = new ByteArrayOutputStream();
//    ImageOutputStream imOut;
//   
//    try {
//    imOut = ImageIO.createImageOutputStream(bs);
//    ImageIO.write(bi, "png",imOut);
//    is= new ByteArrayInputStream(bs.toByteArray());
//    } catch (IOException e) {
//    e.printStackTrace();
//    }
//    return is;
//    }
