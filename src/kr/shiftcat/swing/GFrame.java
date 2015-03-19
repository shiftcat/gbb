package kr.shiftcat.swing;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;


/**
 * 
 * @author Y.Han Lee
 * @since 2008.03.24
 */
public class GFrame extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1709738382093876017L;

	String title = "가위바위보 게임";
	
	int roundCount, tie;
	
	final int GAWI = 0;
	final int BAWI = 1;
	final int BO = 2;
	final int GBB = 3;
	
	JPanel pane, bottomPane, topPane;
	JLabel roundLabel;
	
	Player playerUser, playerCom;
	
	JProgressBar comProgress, userProgress;
	
	JButton startBtn, gawiBtn, bawiBtn, boBtn, ranBtn;
	
	Random ran;
	
	private Winner winner;
	
	public GFrame()
	{
		super();
		setTitle(title);
		init();
		setComponent();
		setEventHandler();
		display();
		this.setUserEnabled(false);
		startGame();
		setValue();
	}
	
	
	private void init()
	{
		this.pane = new JPanel();
		this.pane.setLayout(new GridLayout(0, 2));
		this.bottomPane = new JPanel();
		this.topPane = new JPanel();
		
		this.startBtn = new JButton("시작");
		
		this.gawiBtn = new JButton("가위");
		this.bawiBtn = new JButton("바위");
		this.boBtn = new JButton("보");
		this.ranBtn = new JButton("랜덤");
		
		this.playerUser = new Player(Player.PlayerType.USER);
		this.playerCom = new Player(Player.PlayerType.COMPUTER);
		
		this.roundLabel = new JLabel("ROUND");
		
		this.userProgress = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
		this.comProgress = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
		
		
		this.ran = new Random();
		
		this.winner = new Winner(this);
	}
	

	
	public void startGame()
	{
		this.roundCount = 0;
		this.startBtn.setEnabled(false);
		this.playerCom.startRandomDisply();
		this.playerUser.startRandomDisply();
		this.getLayeredPane().remove(winner);
		
		ProgressThread thpu = new ProgressThread(this.comProgress);
		ProgressThread thpc = new ProgressThread(this.userProgress);
		thpu.start();
		thpc.start();
		
		try{
			Thread.sleep(1000 * 5);
			playerCom.stopRandomDisply();
			playerUser.stopRandomDisply();
			thpu.interrupt();
			thpc.interrupt();
			Thread.sleep(100);
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally {
			this.setValue();
		}
	}
	
	private void setValue()
	{
		this.roundLabel.setText("ROUND");
		this.startBtn.setEnabled(true);
		this.userProgress.setValue(0);
		this.comProgress.setValue(0);
		this.tie = 0;
	}
	
	private void setEventHandler()
	{
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		
		
		this.startBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				if(startBtn.isEnabled()) {
					startBtn.setEnabled(false);
					setUserEnabled(true);
					playerUser.startRandomDisply();
					winner.moveStop();
					++roundCount;
					roundLabel.setText("ROUND " + roundCount);
				}
			}
		});
		
		this.gawiBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				setUserValue(GAWI);
				
			}
		});
		
		this.bawiBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				setUserValue(BAWI);
			}
		});
		
		this.boBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				setUserValue(BO);
			}
		});
		
		this.ranBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				setUserValue(getUserRandomValue());
			}
		});
		
	}

	
	private void setUserEnabled(boolean b)
	{
		gawiBtn.setEnabled(b);
		bawiBtn.setEnabled(b);
		boBtn.setEnabled(b);
		ranBtn.setEnabled(b);
	}
	
	private void setUserValue(int val)
	{
		setUserEnabled(false);
		this.playerUser.stopRandomDisply();
		this.playerUser.setPalyValue(val);
		int comVal = getComRandomValue();
		showWinner(comVal, val);
	}
	
	private int getComRandomValue()
	{
		int val = ran.nextInt(GBB);
		this.playerCom.setPalyValue(val);
		return val;
	}
	
	private int getUserRandomValue()
	{
		int val = ran.nextInt(GBB);
		return val;
	}
	
	private int getWinner(int com, int user)
	{
		int[] c;
		switch(user) {
		case GAWI:
			c = new int[]{0, -1, 1};
			break;
			
		case BAWI:
			c = new int[]{1, 0, -1};
			break;
			
		case BO:
			c = new int[]{-1, 1, 0};
			break;
			
			default :
				return -2;
		}
		return c[com] + c[user];
	}
	
	
	private void showWinner(int com, int user)
	{
		switch(getWinner(com, user)) {
		case 1:
			this.userProgress.setValue(this.userProgress.getValue() + 10);
			this.winner.setMessage("YOU WIN!!", Color.green);
			this.winner.moveStart(true);
			break;
		case -1:
			this.comProgress.setValue(this.comProgress.getValue() + 10);
			this.winner.setMessage("YOU LOSE!!", Color.red);
			this.winner.moveStart(false);
			break;
		case 0:
		default :
			++this.tie;
			this.showMessage("무승부 입니다.");
		}
		
		if(this.roundCount > 4) {
			startBtn.setEnabled(false);
			new ResultDialog(this, this.userProgress.getValue()/10, this.comProgress.getValue()/10, this.tie);
		}
		else {
			startBtn.setEnabled(true);
		}
	}
	
	
	
	private void showMessage(String msg)
	{
		JOptionPane.showMessageDialog(this, msg);
	}
	
	
	
	private void setComponent()
	{
		this.bottomPane.add(this.startBtn);
		this.bottomPane.add(this.gawiBtn);
		this.bottomPane.add(this.bawiBtn);
		this.bottomPane.add(this.boBtn);
		this.bottomPane.add(this.ranBtn);
		
		this.topPane.add(this.userProgress);
		this.topPane.add(this.roundLabel);
		this.topPane.add(this.comProgress);
		
		this.pane.add(this.playerUser);
		this.pane.add(this.playerCom);
		
		add(topPane, "North");
		add(bottomPane, "South");
		add(pane);
	}
	
	
	private void display()
	{
		setResizable(false);
		setSize(500, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

}