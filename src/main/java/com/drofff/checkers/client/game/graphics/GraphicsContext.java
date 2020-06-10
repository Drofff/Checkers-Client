package com.drofff.checkers.client.game.graphics;

import javax.swing.*;
import java.awt.*;

public class GraphicsContext {

    private static JFrame jFrame;

    private GraphicsContext() {}

    public static void initJFrame() {
        jFrame = new JFrame("The Checkers Game");
        jFrame.setResizable(false);
    }

    public static void addComponent(Component component) {
        jFrame.add(component);
        jFrame.pack();
        jFrame.setVisible(true);
    }

}