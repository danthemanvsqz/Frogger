// Game class
package frogger;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.border.LineBorder;



public class Game extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7969861536601576538L;
	private GameWorld gw;
	private MapView mv;
	private ScoreView sv;
	private Timer timer;
	private boolean timerFlag;
	private JButton pauseButton = new JButton("Pause");
	private JButton deleteButton = new JButton("Delete Item");
	private SoundCommand soundCommand;
	
	public static final int DELAY_IN_MSEC = 20;
	public static final String GAME_TITLE = "Frogger";
	public static final int GAME_WIDTH = 1280;
	public static final int GAME_HEIGHT = 720;
	
	//Constructor
	public Game() {
		gw = new GameWorld();
		mv = new MapView(gw);
		sv = new ScoreView(gw);
		setSize(GAME_WIDTH,GAME_HEIGHT);
		setTitle(GAME_TITLE);
		this.add(mv,BorderLayout.CENTER);
		this.add(sv, BorderLayout.NORTH);
		
		
		
		// Instantiate Commands
		NewCommand newCommand = new NewCommand();
		SaveCommand saveCommand = new SaveCommand();
		UndoCommand undoCommand = new UndoCommand();
		soundCommand = new SoundCommand();
		AboutCommand aboutCommand = new AboutCommand();
		QuitCommand quitCommand = new QuitCommand();
		//AddFrogCommand addFrogCommand = new AddFrogCommand();
		HopNorthCommand hopNorthCommand = new HopNorthCommand();
		HopSouthCommand hopSouthCommand = new HopSouthCommand();
		HopEastCommand hopEastCommand = new HopEastCommand();
		HopWestCommand hopWestCommand = new HopWestCommand();
		PauseCommand pauseCommand = new PauseCommand();
		DeleteCommand deleteCommand = new DeleteCommand();
		// End Command Instantiation
		
		// Build Menu Bar
		JMenuBar bar = new JMenuBar();
		setJMenuBar(bar);
		
		JMenu fileMenu = new JMenu("File");
		bar.add(fileMenu);
		
		//JMenu commandMenu = new JMenu("Commands");
		//bar.add(commandMenu);
		
		// Build File Menu Items
		JMenuItem fileNew = new JMenuItem("New");
		JMenuItem fileSave = new JMenuItem("Save");
		JMenuItem fileUndo = new JMenuItem("Undo");
		JCheckBoxMenuItem fileSound = new JCheckBoxMenuItem("Sound", true);
		JMenuItem fileAbout = new JMenuItem("About");
		JMenuItem fileQuit = new JMenuItem("Quit");	
		
		// Build Command Menu Items

		
		// Add File Menu Items to File Menu
		fileMenu.add(fileNew);
		fileMenu.add(fileSave);
		fileMenu.add(fileUndo);
		fileMenu.add(fileSound);
		fileMenu.add(fileAbout);
		fileMenu.add(fileQuit);
		
		// Add Command Menu Items to Command Menu

		
		// Bind Commands to Menu items
		fileNew.setAction(newCommand);
		fileSave.setAction(saveCommand);
		fileUndo.setAction(undoCommand);
		fileSound.setAction(soundCommand);
		fileAbout.setAction(aboutCommand);
		fileQuit.setAction(quitCommand);
		// End Menu Bar
		
		
		// Build Command Panel
		JPanel commandPanel = new JPanel();
		commandPanel.setBorder(new LineBorder(Color.blue,2));
		commandPanel.setLayout(new GridLayout(10,2,4,20));
		this.add(commandPanel,BorderLayout.WEST);
		
		// Build JButtons
		pauseButton = new JButton("Pause");
		deleteButton = new JButton("Delete Item");
		JButton quitButton = new JButton("Quit");
		

		
		
		// Add Buttons to command panel
		commandPanel.add(pauseButton);
		commandPanel.add(deleteButton);
		commandPanel.add(quitButton);
		
		
		// Bind commands to buttons
		quitButton.setAction(quitCommand);
		quitButton.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "none");
		pauseButton.setAction(pauseCommand);
		pauseButton.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "none");
		deleteButton.setAction(deleteCommand);
		deleteButton.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "none");
		// End Command Panel
		
		
		// Build KeyMappings
		int mapName = JComponent.WHEN_IN_FOCUSED_WINDOW;
		InputMap imap = mv.getInputMap(mapName);
		ActionMap amap = mv.getActionMap();
				
		//KeyStroke spaceKey = KeyStroke.getKeyStroke("SPACE");
		KeyStroke upArrow = KeyStroke.getKeyStroke("UP");
		KeyStroke downArrow = KeyStroke.getKeyStroke("DOWN");
		KeyStroke rightArrow = KeyStroke.getKeyStroke("RIGHT");
		KeyStroke leftArrow = KeyStroke.getKeyStroke("LEFT");
		
		//imap.put(spaceKey, "New Frog");
		imap.put(upArrow, "Hop North");
		imap.put(downArrow, "Hop South");
		imap.put(rightArrow, "Hop East");
		imap.put(leftArrow, "Hop West");
		
		//amap.put("New Frog", addFrogCommand);
		amap.put("Hop North", hopNorthCommand);
		amap.put("Hop South", hopSouthCommand);
		amap.put("Hop East", hopEastCommand);
		amap.put("Hop West", hopWestCommand);
		// End KeyMapping		
		
		deleteButton.setEnabled(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		gw.notifyObservers();
		this.requestFocus();
		timer = new Timer(DELAY_IN_MSEC, this);
		timer.start();
		timerFlag = true;
		setVisible(true);	
	}
	
	/********************************************************************************************************************
	 * 		Private Abstract Action classes used to bind functionality to GUI items	 *
	 ********************************************************************************************************************/
	
	
	
	
	/*
	private class AddFrogCommand extends AbstractAction {
		private static final long serialVersionUID = -1487797647215921972L;
		public AddFrogCommand() {
			super("Add Frog");
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println("addFrog() requested from " + e.getActionCommand() + " " + e.getSource().getClass() );
			gw.addFrog();
		}
	}
*/
	
		
	private class HopNorthCommand extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = -4604544846871461409L;
		public HopNorthCommand() {
			super("Hop North");
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println("hopFrog(North) requested from " + e.getActionCommand() + " " + e.getSource().getClass() );
			gw.hopFrog("North");
		}
	}
	
	private class HopSouthCommand extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8049454782253632807L;
		public HopSouthCommand() {
			super("Hop South");
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println("hopFrog(South) requested from " + e.getActionCommand() + " " + e.getSource().getClass() );
			gw.hopFrog("South");
		}
	}
	
	private class HopWestCommand extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = -3850804802860370037L;
		public HopWestCommand() {
			super("Hop West");
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println("hopFrog(West) requested from " + e.getActionCommand() + " " + e.getSource().getClass() );
			gw.hopFrog("West");
		}
	}
	
	private class HopEastCommand extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1042725857100817196L;
		public HopEastCommand() {
			super("Hop East");
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println("hopFrog(East) requested from " + e.getActionCommand() + " " + e.getSource().getClass() );
			gw.hopFrog("East");
		}
	}
	
	
	private class QuitCommand extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3408725298002997802L;
		public QuitCommand() {
			super("Quit");
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println("Quit requested from " + e.getActionCommand() + " " + e.getSource().getClass() );
			int result = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if( result == JOptionPane.YES_OPTION ) {
				System.exit(0);
			}
			return;
		}
	}
	
	private class AboutCommand extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1828285065940971834L;
		public AboutCommand() {
			super("About");
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println("About requested from "+ e.getActionCommand() + " " + e.getSource().getClass() );
			String aboutMessage = "Copyright 2012 Dan Vasquez <dan@dan-HP-ProBook-4320s>\n"
									+ "This program is free software; you can redistribute it and/or modify\n"
									+ "it under the terms of the GNU General Public License as published by\n"
									+ "the Free Software Foundation; either version 2 of the License, or\n"
									+ "(at your option) any later version.\n\n"
									+ "This program is distributed in the hope that it will be useful,\n"
									+ "but WITHOUT ANY WARRANTY; without even the implied warranty of\n"
									+ "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n"
									+ "GNU General Public License for more details.\n\n"
									+ "You should have received a copy of the GNU General Public License\n"
									+ "along with this program; if not, write to the Free Software\n"
									+ "Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,\n"
									+ "MA 02110-1301, USA.";
			JOptionPane.showMessageDialog(null, aboutMessage, "Frogger", JOptionPane.OK_OPTION);
		}
	}
	
	private class SoundCommand extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = -1335538470644728193L;
		public SoundCommand() {
			super("Sound");
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println("Sound requested from "+ e.getActionCommand() + " " + e.getSource().getClass() );
			JCheckBoxMenuItem s = (JCheckBoxMenuItem) e.getSource();
			if(s.isSelected()) {
				gw.setSound(true);
			}
			else {
				gw.setSound(false);
			}
			
			
		}
	}
	
	private class NewCommand extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = -1831310339962182824L;
		public NewCommand() {
			super("New");
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println("New requested from "+ e.getActionCommand() + " " + e.getSource().getClass() );
		}
	}
	
	private class SaveCommand extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1330865552247585305L;
		public SaveCommand() {
			super("Save");
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println("Save requested from "+ e.getActionCommand() + " " + e.getSource().getClass() );
		}
	}
	
	private class UndoCommand extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 5753328332994991614L;
		public UndoCommand() {
			super("Undo");
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println("Undo requested from "+ e.getActionCommand() + " " + e.getSource().getClass() );
		}
	}

	private class PauseCommand extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 5753328332994991614L;
		public PauseCommand() {
			super("Pause");
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println("Pause requested from "+ e.getActionCommand() + " " + e.getSource().getClass() );
			if(timerFlag) {
				mv.setPaused(true);
				pauseButton.setText("Play");
				deleteButton.setEnabled(true);
				if(gw.getSound()) {
					gw.stopBackgroundMusic();
				}
				timerFlag = false;
				timer.stop();
			}
			else {
				mv.setPaused(false);
				pauseButton.setText("Pause");
				deleteButton.setEnabled(false);
				if(gw.getSound()) {
					gw.startBackgroundMusic();
				}
				timerFlag = true;
				timer.restart();
				
			}
		}
	}
	
	private class DeleteCommand extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 5753328332994991614L;
		public DeleteCommand() {
			super("Delete Selected");
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println("Delete requested from "+ e.getActionCommand() + " " + e.getSource().getClass() );
			Iterator<GameObject> i = gw.getIterator();
			while(i.hasNext()) {
				GameObject g = i.next();
				if(g instanceof ISelectable) {
					if(((ISelectable) g).isSelected()) {
						g.setDeletion(true);
					}
				}
			}
			gw.notifyObservers();
			
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		gw.clockTick();
	}
	
	
}
