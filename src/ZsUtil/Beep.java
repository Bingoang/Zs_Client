package ZsUtil;

import java.io.FileNotFoundException;
import java.io.IOException;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Beep {
	private sun.audio.ContinuousAudioDataStream gg;
	private boolean isStart = false;
	public void start()
	{
		if(!isStart)
		{
			
			try {
				java.io.InputStream in = new java.io.FileInputStream(
						"music//button.wav");
				AudioStream as = new AudioStream(in);
				sun.audio.AudioData data = as.getData();
				gg = new sun.audio.ContinuousAudioDataStream(
						data);
				AudioPlayer.player.start(gg);
				isStart = true;//已经打开
			} catch (FileNotFoundException e) {
				System.out.print("FileNotFoundException");
			} catch (IOException e) {
				System.out.print("关闭失败");
			}
		}
	}
	public void stop()
	{
		if(isStart)
		{
			AudioPlayer.player.stop(gg);		
			isStart = false;
		}
	}
		
}
