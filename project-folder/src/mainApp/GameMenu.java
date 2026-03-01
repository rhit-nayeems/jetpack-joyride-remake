package mainApp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Class: GameMenu
 * Purpose: menu and game frame setup
 */
public class GameMenu {
	private JFrame frame;
	private JButton startButton;
	private JComboBox<DifficultyPreset> difficultySelector;

	/**
	 * ensures: constructs the game menu on the screen
	 *
	 * @param gameComponent
	 */
	public GameMenu(GameComponent gameComponent) {
		frame = new JFrame("Jetpack Joyride Menu");
		frame.setSize(360, 240);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new GridBagLayout());

		JPanel centeredPanel = new JPanel();
		centeredPanel.setLayout(new BoxLayout(centeredPanel, BoxLayout.Y_AXIS));

		JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
		difficultyPanel.add(new JLabel("Difficulty:"));
		difficultySelector = new JComboBox<>(DifficultyPreset.values());
		difficultySelector.setSelectedItem(DifficultyPreset.NORMAL);
		difficultyPanel.add(difficultySelector);

		startButton = new JButton("Play Game");
		startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		startButton.addActionListener(e -> {
			DifficultyPreset selected = (DifficultyPreset) difficultySelector.getSelectedItem();
			gameComponent.setDifficulty(selected);
			frame.setVisible(false);
			frame.dispose();
			startGame(gameComponent);
		});

		centeredPanel.add(difficultyPanel);
		centeredPanel.add(Box.createRigidArea(new Dimension(0, 12)));
		centeredPanel.add(startButton);

		frame.add(centeredPanel);
		frame.setVisible(true);
	}

	/**
	 * ensures: starts the game and registers listeners
	 *
	 * @param gameComponent
	 */
	private void startGame(GameComponent gameComponent) {
		JFrame gameFrame = new JFrame("Jetpack Joyride Java Remake");
		gameFrame.setSize(1000, 500);
		gameFrame.setLayout(new BorderLayout());
		gameFrame.add(gameComponent, BorderLayout.CENTER);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setLocationRelativeTo(null);
		gameFrame.setResizable(false);

		gameComponent.startNewGame();

		GameAdvanceListener advanceListener = new GameAdvanceListener(gameComponent);
		PressListener pressListener = new PressListener(gameComponent);
		gameFrame.addKeyListener(pressListener);

		gameFrame.setVisible(true);
		gameFrame.setFocusable(true);
		gameFrame.requestFocusInWindow();

		Timer timer = new Timer(MainApp.DELAY, advanceListener);
		timer.start();
	}
}
