package kr.shiftcat.swing;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * 
 * @author yhlee
 *
 */
public class Winner extends JPanel 
{
	private JFrame parent;
	
	private Font FONT;
	
	private String STRING = "Input message.";

	private float alpha = 0.0f;
	
	private int imgWidth = 280;
	
	private int imgHeight = 150;
	
	private Color color;
	
	
	public Winner(JFrame frame) 
	{
		this.parent = frame;
		init();
	}
	
	private void init()
	{
		this.FONT = new Font("Serif", Font.BOLD, 38);
        this.setSize(new Dimension(0, 0));
        this.setOpaque(false);
        //this.setBackground(Color.white);
        setLocation(0, 0);
	}
	
	

	@Override
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setFont(FONT);
		int xx = this.getWidth();
		int yy = this.getHeight();
		int w2 = g.getFontMetrics().stringWidth(STRING) / 2;
		int h2 = g.getFontMetrics().getDescent();
		//g2d.fillRect(0, 0, xx, yy);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, alpha));
		g2d.setPaint(color);
		g2d.drawString(STRING, xx / 2 - w2, yy / 2 + h2);
		//g2d.dispose();
	}
	
	private static final int DURATION = 300;
	
	protected void moveTo(int x, int y, float f, double durationRate)
	{
		System.out.println("input : (x:" + x + ", y:" + y +")");
		
		this.alpha = f;
		
		double w = this.imgWidth;
		double h = this.imgHeight;
		
		w = w * durationRate;
		h = h * durationRate;
		super.setSize((int)w, (int)h);
		
		int xx = (int)(x-(w/2));
		int yy = (int)(y-(h/2));
		
		super.setLocation( xx, yy );
		
		repaint();
		
		System.out.println("position : (x:" + xx + ", y:" + yy +")");
		System.out.println("w: " + w + ", h: " + h + ", alpha:" + f);
		
//		if(i==MAX) {
//			//parent.getLayeredPane().remove(this);
//		}
	}
	
	
	public void setMessage(String msg, Color color)
	{
		this.STRING = msg;
		this.color = color;
	}
	
	private class MoveThread extends Thread
	{
		private boolean bool;
		
		public MoveThread(boolean b)
		{
			bool = b;
		}
		
		public void run()
		{
			Dimension d = parent.getSize();
			double w = d.getWidth()/2;
			double h = d.getHeight()/2;
			
			double ww = bool?0:d.getWidth();
			double hh = d.getHeight();
			
			
			System.out.println("parent size : (w:" + w + ", h:" + h +")");
			
			
			try {
				int turn = DURATION-20;
				int i = 0;
				do{
					double durationRate = i/Double.valueOf(DURATION);
					
					float f = i/Float.valueOf(DURATION);
					double x = ww + ((w * durationRate) * (bool?1:-1));
					double y = hh + ((h * durationRate) * -1);
					
					moveTo((int)x, (int)y, f, durationRate);
					
					if(i<=turn) {
						i+=5;
						Thread.sleep(5);
					}
					else {
						i++;
						Thread.sleep(50);
					}
				}
				while(i<=DURATION);
				
				for(;;) {
					for(int ii=100; ii==1; ii--) {
						alpha = ii/100f;
						repaint();
						Thread.sleep(3);
					}
					for(int ii=1; ii<=100; ii++) {
						alpha = ii/100f;
						repaint();
						Thread.sleep(10);
					}
				}
				
			}
			catch(InterruptedException ie) {}
		}
	}
	
	private MoveThread mover;
	
	public void moveStart(boolean bool)
	{
		parent.getLayeredPane().add(this);
		mover = new MoveThread(bool);
		mover.start();
	}
	
	public void moveStop()
	{
		parent.getLayeredPane().remove(this);
		if(mover != null) {
			mover.interrupt();
		}
	}
	
	
	
	
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setVisible(true);
		
		Winner win = new Winner(frame);
		frame.getLayeredPane().add(win);
		win.setMessage("YOU WIN!!", Color.magenta);
		win.moveStart(true);
		
	}

}
