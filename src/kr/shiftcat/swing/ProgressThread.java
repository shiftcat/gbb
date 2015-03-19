package kr.shiftcat.swing;
import java.util.Random;

import javax.swing.JProgressBar;


public class ProgressThread extends Thread
{
	private JProgressBar jb;
	
	public ProgressThread(JProgressBar jb)
	{
		this.jb = jb;
	}
	
	public void run()
	{
		
		while(true ) {
			try{
				up();
				down();
			}catch(InterruptedException e){
				return;
			}
		}
	}
	
	private synchronized void up() throws InterruptedException
	{
		int maxVal = new Random().nextInt(100);
		for(int i = 0; i < maxVal; i++) {
			jb.setValue(i);
			Thread.sleep(1);
		}
	}
	
	private synchronized void down() throws InterruptedException
	{
		for(int i = jb.getValue(); i > 0; i--) {
			jb.setValue(i);
			Thread.sleep(3);
		}
	}
}