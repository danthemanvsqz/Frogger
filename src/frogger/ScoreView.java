package frogger;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScoreView extends JPanel implements IObserver {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2047153111122375823L;
	private JLabel scoreLabel, frogsLabel, timeLabel, soundLabel;
	public ScoreView( IObservable obs ) {
		obs.addObserver(this);
		add(timeLabel = new JLabel("Elasped Time: "));
		add(frogsLabel = new JLabel("Frogs Left: "));
		add(scoreLabel = new JLabel("Score: "));
		add(soundLabel = new JLabel("Sound: "));
		setVisible(true);
	}
	
	public void update( IGameWorld g, Object arg ) {
		timeLabel.setText("Elapsed Time: " + (int) g.getElapsedTime() + "   ");
		frogsLabel.setText("Frogs Left: " + g.getFrogCount() + "   ");
		scoreLabel.setText("Score: " + g.getScore() + "   ");
		if(g.getSound()) {
			soundLabel.setText("Sound: ON");	
		}
		else {
			soundLabel.setText("Sound: OFF");
		}
	}
}
