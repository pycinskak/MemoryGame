package edu.pja.kasia;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ScoreBoard extends JFrame {

    ScoreBoard(){
        ArrayList<Score> scores = getScores();
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(400, 400));
        this.setLocationRelativeTo(null);
        DefaultListModel<String> listmodel = new DefaultListModel<>();
        Score s;
        Collections.sort(scores);
        for(int i=0; i<scores.size(); i++){
            s = scores.get(i);
            int min = (int)Math.floor(s.time/60);
            int sec = (int)Math.round(s.time%60);
            if(sec>=10)
                listmodel.add(i, s.name+":    "+s.size+"x"+s.size+"    time: "+min+":"+sec);
            else
                listmodel.add(i, s.name+":    "+s.size+"x"+s.size+"    time: "+min+":0"+sec);
        }

        JList list = new JList(listmodel);
        JPanel southPanel = new JPanel();
        southPanel.setBorder(
                BorderFactory.createTitledBorder("Winners"));
        southPanel.setLayout(new FlowLayout());
        southPanel.add(list);
        JPanel northPanel = new JPanel();
        northPanel.setBorder(BorderFactory.createDashedBorder(Color.BLUE));
        northPanel.setLayout(new FlowLayout());
        JLabel jl = new JLabel("High Scores");
        jl.setFont(new Font(jl.getFont().getName(), Font.PLAIN, 20));
        southPanel.add(list);
        northPanel.add(jl);
        northPanel.setPreferredSize(new Dimension(50, 40));
        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
    }

    private ArrayList<Score> getScores () {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Client.getInstance().getHighScores(), mapper.getTypeFactory().constructCollectionType(ArrayList.class, Score.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
