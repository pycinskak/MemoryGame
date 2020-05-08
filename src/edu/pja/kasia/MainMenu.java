package edu.pja.kasia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame implements ActionListener {
    MainMenu(){
        this.setLocationRelativeTo(null);
        this.setPreferredSize(new Dimension(800, 600));
        JButton newgame = new JButton("New Game");
        JButton highscores = new JButton("High Scores");
        JButton exit = new JButton("Exit");
        newgame.addActionListener(this);
        highscores.addActionListener(this);
        exit.addActionListener(this);
        setLayout(new GridLayout(5, 1));
        JLabel title = new JLabel("Memory Game");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, 20));
        JLabel student = new JLabel("Katarzyna");
        student.setHorizontalAlignment(SwingConstants.CENTER);
        student.setFont(new Font(student.getFont().getName(), Font.BOLD, 20));
        add(title);
        add(student);
        add(newgame);
        add(highscores);
        add(exit);
        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton b = (JButton) e.getSource();
        switch (b.getText()){
            case "New Game":
                int size = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter board size (must be even number): "));
                if(size%2==0)
                    new Game(size);
                else
                    JOptionPane.showMessageDialog(null, "Number must be even!");
                break;
            case "High Scores":
                new ScoreBoard();
                break;
            case "Exit":
                Client.getInstance().closeConnection();
                System.exit(0);
        }
    }
}
