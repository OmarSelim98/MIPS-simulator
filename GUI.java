package org.architecture;

import javax.swing.*;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GUI extends JFrame {
    private JTextArea txtArea = new JTextArea();
    private JButton assembleBtn = new JButton("Assemble");
    private JButton runBtn = new JButton("Run");

    private JPanel codePanel = new JPanel();
    private JPanel topPanel = new JPanel();
    private JPanel regPanel = new JPanel();

    private JTextField [] regsNamesFields = new JTextField[32];

    private Container c;
    public GUI(){
        this.init();
        this.setVisible(true);
    }
    private void init(){
        this.setTitle("Mips Simulator");
        this.setSize(1080,720);
        this.setLocation(100,50);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new BorderLayout());

        this.c = this.getContentPane();

        this.topPanel.add(assembleBtn);
        this.topPanel.add(runBtn);




        this.codePanel.setName("Code");
        this.codePanel.setLayout(new BorderLayout());
        this.codePanel.add(this.txtArea,BorderLayout.CENTER);

        this.regPanel.setLayout(new GridLayout(32,1));



        this.c.add(this.topPanel,BorderLayout.NORTH);
        this.c.add(this.codePanel,BorderLayout.CENTER);
        this.c.add(this.regPanel,BorderLayout.EAST);


    }

    public void setRegs(String [] names){
        for(int i = 0; i < 32; i++){
            this.regsNamesFields[i] = new JTextField();
            this.regsNamesFields[i].setText(names[i]);
            this.regsNamesFields[i].setEditable(false);
            this.regsNamesFields[i].setColumns(4);

        }
        this.repaint();

    }
    public void addRegs(){
        for(int i = 0; i < 32; i++) {
            this.regPanel.add(this.regsNamesFields[i]);
            this.regsNamesFields[i].repaint();
            this.regPanel.repaint();
        }
    }
}
