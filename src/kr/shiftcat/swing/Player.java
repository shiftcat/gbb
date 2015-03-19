package kr.shiftcat.swing;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * 
 * @author yhlee
 *
 */
public class Player extends JPanel
{
	
	private JLabel label;
	
	
	private ImageIcon[] imageIcon;
	
	
	final int GAWI = 0;
	final int BAWI = 1;
	final int BO = 2;
	final int GBB = 3;
	
	Random random;
	
	public static enum PlayerType
	{
		USER, COMPUTER
	}
	
	
	public Player(PlayerType type)
	{
		init();
		if(type == PlayerType.USER) {
			initUser();
		}
		else {
			initCom();
		}
	}
	
	
	private void init()
	{
		this.label = new JLabel();
		this.random = new Random();
		
		add(label);
	}
	
	
	private void initUser()
	{
		this.imageIcon = new ImageIcon[GBB];
		this.imageIcon[GAWI] = new ImageIcon(getImageFromJAR("kr/shiftcat/images/gawi.jpg"));
		this.imageIcon[BAWI] = new ImageIcon(getImageFromJAR("kr/shiftcat/images/bawi.jpg"));
		this.imageIcon[BO] = new ImageIcon(getImageFromJAR("kr/shiftcat/images/bo.jpg"));
	}
	
	
	private void initCom()
	{
		this.imageIcon = new ImageIcon[GBB];
		this.imageIcon[GAWI] = new ImageIcon(getImageFromJAR("kr/shiftcat/images/gawi1.jpg"));
		this.imageIcon[BAWI] = new ImageIcon(getImageFromJAR("kr/shiftcat/images/bawi1.jpg"));
		this.imageIcon[BO] = new ImageIcon(getImageFromJAR("kr/shiftcat/images/bo1.jpg"));
	}

	
	
	private Image getImageFromJAR(String fileName)
	{
       if(fileName == null) return null;    
	   Image image = null;
	   byte[] byteimage = null;
	   Toolkit toolkit = Toolkit.getDefaultToolkit();
	   InputStream in = ClassLoader.getSystemResourceAsStream(fileName);
	   try{
		   int length = in.available();       
		   byteimage = new byte[length];       
		   in.read(byteimage);
		   image = toolkit.createImage(byteimage);    
	   }
	   catch(IOException e){
		   JOptionPane.showMessageDialog(this, e);
		   return null; 
	   }
	   finally {
		   try{ in.close(); } catch(Exception e){}
	   }
	   return image; 
	}
	
	
	private class RandomDisplay extends Thread
	{
		public void run()
		{
			int i = 0;
			while(true) {
				i = random.nextInt(GBB);
				label.setIcon(imageIcon[i]);
				try {
					Thread.sleep(100);
				}catch(InterruptedException e){
					return;
				}
			}
		}
	}
	
	
	
	private RandomDisplay randomDisplay = null;
	
	
	public void startRandomDisply()
	{
		randomDisplay = new RandomDisplay();
		randomDisplay.start();
	}
	
	
	public void stopRandomDisply()
	{
		if(randomDisplay != null) {
			randomDisplay.interrupt();
			randomDisplay = null;
		}
	}
	
	public void setPalyValue(int val)
	{
		this.label.setIcon(this.imageIcon[val]);
	}
	
	
	
	public static void main(String[] args)
	{
		Player p = new Player(Player.PlayerType.USER);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));
		panel.add(p);
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(400, 300);
		frame.setVisible(true);
		frame.add(panel);
		
		
		p.startRandomDisply();
	}
	
}
