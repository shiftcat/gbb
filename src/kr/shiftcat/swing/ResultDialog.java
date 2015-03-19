package kr.shiftcat.swing;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * 
 * @author yhlee
 *
 */
public class ResultDialog extends JDialog
{
	private int win, lose, tie;
	private JFrame owner;
	private JLabel laTot, laWin, laLose, laTie, laPer, lacomm;
	private JPanel panel;
	private JButton okBtn, exitBtn;
	private JPanel bpane;
	
	public ResultDialog(JFrame owner, int win, int lose, int tie)
	{
		this.owner = owner;
		this.win = win;
		this.lose = lose;
		this.tie = tie;
		this.init();
		this.setEventHandler();
		this.display();
	}
	
	private void init()
	{
		this.laTot = new JLabel();
		this.laLose = new JLabel();
		this.laPer = new JLabel();
		this.laTie = new JLabel();
		this.laWin = new JLabel();
		this.lacomm = new JLabel();
		
		this.okBtn = new JButton("재시작");
		this.exitBtn = new JButton("종료");
		
		this.bpane = new JPanel();
		this.panel = new JPanel();
		this.panel.setLayout(new GridLayout(6, 1));
	}
	
	private void setEventHandler()
	{
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		
		this.okBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				new ParentRestartThread().start();
				setVisible(false);
                dispose();
			}
		});
		
		this.exitBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
	}
	
	private class ParentRestartThread extends Thread
	{
		public void run()
		{
			((GFrame)owner).startGame();
		}
	}
	
	private int totalRound()
	{
		return this.win + this.lose + this.tie;
	}
	
	private double getPercent()
	{
		return this.win / (double)this.totalRound() * 100;
	}
	
	private void display()
	{
		this.laTot.setText("Total Round : " + this.totalRound());
		this.laWin.setText("승 : " + this.win);
		this.laLose.setText("패 : " + this.lose);
		this.laTie.setText("무 : " + this.tie);
		this.laPer.setText("승률 : " + this.getPercent());
		this.lacomm.setText("게임을 다시 시작하시겠습니까?");
		
		this.panel.add(this.laTot);
		this.panel.add(this.laWin);
		this.panel.add(this.laLose);
		this.panel.add(this.laTie);
		this.panel.add(this.laPer);
		this.panel.add(this.lacomm);
		
		this.bpane.add(this.okBtn);
		this.bpane.add(this.exitBtn);
		
		add(this.bpane, "South");
		add(this.panel);
		
		setModal(true);
		setSize(500, 300);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

}
