package com.dacarex.capital.vista;

import java.awt.Component;
import java.awt.Frame;

public class SwingUtilities_AWT {

    public static Frame getParentFrame(Component c) {
        while (c != null) {
            if (c instanceof Frame) return (Frame) c;
            c = c.getParent();
        }
        return new Frame();
    }
}