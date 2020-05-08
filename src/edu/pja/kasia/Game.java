package edu.pja.kasia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.Collections;

public class Game extends JFrame implements ActionListener {

    int size;
    ArrayList<JButton> buttons;
    ArrayList<Card> cards;
    ArrayList<JButton> paired;
    int STATE;
    private JButton previous;
    int numberOfMoves = 0;
    int goodMoves = 0;
    JLabel mTimer = new JLabel();
    int time = 240;

    // osobny watek ktory liczy i wyswietla czas
    Thread timer = new Thread(() -> {
        try {
            while (time > 0) {
                time--;
                int min = time / 60;
                int sec = time % 60;
                if (sec >= 10)
                    mTimer.setText(min + ":" + sec);
                else
                    mTimer.setText(min + ":0" + sec);
                Thread.sleep(1000); // usypia obecny watek na sekunde
            }
            endGame(0);
        } catch (InterruptedException e) {
        }
    });

    public Game(int size) {
        super();
        this.size = size;
        this.buttons = new ArrayList<>(size * size);
        this.STATE = 0;
        this.cards = Card.createCards(size * size / 2);
        this.buttons = buttonAssignment(this.cards);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);

        JPanel southPanel = new JPanel();
        southPanel.setBorder(
                BorderFactory.createTitledBorder("Timer"));
        southPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        southPanel.add(mTimer);
        add(southPanel, BorderLayout.SOUTH);

        JPanel northPanel = new JPanel();
        northPanel.setBorder(BorderFactory.createTitledBorder("Memory"));
        GridLayout gl = new GridLayout(size, size);
        northPanel.setLayout(gl);
        for (JButton b : buttons)
            northPanel.add(b);
        add(northPanel, BorderLayout.NORTH);
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        timer.start();

    }

    private ArrayList<JButton> buttonAssignment(ArrayList<Card> cards) {
        ArrayList<JButton> buttons = new ArrayList<>();
        for (Card c : cards) {
            JButton b = new JButton();
            JButton b2 = new JButton();
            b.setName(c.id + "");
            b.setIcon(new ImageIcon("bin/images/empty.jpg"));
            b.addActionListener(this);
            b2.setName(c.id + "");
            b2.setIcon(new ImageIcon("bin/images/empty.jpg"));
            b2.addActionListener(this);
            buttons.add(b);
            buttons.add(b2);
        }
        Collections.shuffle(buttons);
        return buttons;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JButton b =(JButton) e.getSource();
        Thread t = new Thread(() -> {
            synchronized (b){
                b.setIcon(new ImageIcon(cards.get(Integer.parseInt(b.getName())).f));
                takeMove(b);
            }
        });
        t.start();
    }

    void takeMove(JButton b) { //prez niezalezne watki
        synchronized ((Integer) numberOfMoves) {
            if (previous != b) {
                numberOfMoves++;
                if (numberOfMoves % 2 == 0) {//drugi ruch
                    if (b.getName().equals(previous.getName())) {//jesli zgadles
                        b.setEnabled(false);
                        b.setIcon(new ImageIcon(cards.get(Integer.parseInt(b.getName())).f));
                        previous.setIcon(new ImageIcon(cards.get(Integer.parseInt(previous.getName())).f));
                        previous.setEnabled(false);
                        previous = b;
                        goodMoves++;
                        if (goodMoves == cards.size()) //koniec gry
                            endGame(1);
                    } else {//jesli nie zgadles
                        try {
                            previous.setIcon(new ImageIcon(Statics.emptyLoc));
                            previous = b;
                            Thread.sleep(2000);
                            b.setIcon(new ImageIcon(Statics.emptyLoc)); //empty na obu
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        int temp = numberOfMoves;
                        previous = b;
                        Thread.sleep(2000);
                        if (numberOfMoves == temp) //odwraca karte jesli nikt nie kliknal w ciagu 2 sekund
                            b.setIcon(new ImageIcon(Statics.emptyLoc));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    void endGame(int state)
    {
        timer.interrupt();
        if (state == 0)
        {
            JOptionPane.showMessageDialog(null,"Przegrales");
            setVisible(false);
            dispose();
        } else
        {
            String name = JOptionPane.showInputDialog("Gratulacje, wpisz swoje imie: ");
            Score score = new Score(this.size, 240 - this.time, name);
            Client.getInstance().sendScore(score.toJson());
            setVisible(false);
            dispose();
        }
    }
}
