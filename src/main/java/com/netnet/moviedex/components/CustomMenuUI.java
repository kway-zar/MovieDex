/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.netnet.moviedex.components;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JMenuItem;
import javax.swing.plaf.basic.BasicMenuItemUI;

/**
 *
 * @author quasar
 */
public class CustomMenuUI extends BasicMenuItemUI{
    @Override
    protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
        if (menuItem.isArmed()) {
            g.setColor(Color.decode("#950740"));
            g.fillRoundRect(5, 0, menuItem.getWidth() -11, menuItem.getHeight(), 8,8);
        } else {
            super.paintBackground(g, menuItem, bgColor);
        }
    }
}
