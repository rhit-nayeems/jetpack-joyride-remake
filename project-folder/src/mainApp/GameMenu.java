package mainApp;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;  

public class GameMenu {
    private JFrame frame;
    private JButton startButton;

    /**
     * ensures: constructs the game menu on the screen
     * @param gameComponent
     */
    public GameMenu(GameComponent gameComponent) {
        frame = new JFrame("Jetpack Joyride Menu");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // shows the frame in the center
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        startButton = new JButton("Play Game");
        startButton.addActionListener(e -> {
            frame.setVisible(false);
            frame.dispose();
            startGame(gameComponent);
        });
        panel.add(startButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    /**
     * ensures: starts the game by setting the screen, calling the level constructor,
     * 			adding game objects, setting the listener, and starting the timer
     * @param gameComponent
     */
    private void startGame(GameComponent gameComponent) {
        JFrame gameFrame = new JFrame("Jetpack Joyride from Wish");
        gameFrame.setSize(1000, 500);
        gameFrame.add(gameComponent, BorderLayout.CENTER);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setVisible(true);

        
        LevelConstructor lc = new LevelConstructor(gameComponent);
        lc.constructLevel("Level1");

        gameComponent.addBarriers();
        gameComponent.addCoins();
        gameComponent.addMissiles();

        GameAdvanceListener advanceListener = new GameAdvanceListener(gameComponent);
        PressListener pressListener = new PressListener(gameComponent, lc);
        gameFrame.addKeyListener(pressListener);
        gameFrame.setFocusable(true);
        gameFrame.requestFocusInWindow();

        Timer timer = new Timer(MainApp.DELAY, advanceListener);
        timer.start();
    }
}
