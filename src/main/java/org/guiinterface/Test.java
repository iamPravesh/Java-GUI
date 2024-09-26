package org.guiinterface;

import javax.swing.*;

public class Test {
    private JFrame jf;
    public Test(){
        jf = new JFrame("My first GUI");
        jf.setSize(500, 300);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }

    public static void main(String[] args) {
        new Test();
    }
}
