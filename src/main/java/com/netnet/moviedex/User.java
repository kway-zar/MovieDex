/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.netnet.moviedex;

import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author quasar
 */
public class User {

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the list
     */
//    public Movie[] getList() {
//        return list;
//    }
    private String name;
    String iconLink;
//    private Movie[] list;
    
    public User(String name, String link){
        this.name = name;
        this.iconLink = link;
        
        
    
    }
    
    
    
    public Icon toIcon(){
        ImageIcon originalIcon = new ImageIcon(iconLink);
        Image scaledIcon = new ImageIcon(getClass().getResource(iconLink)).getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        
        return new ImageIcon(scaledIcon);
    }
    //array of movies
}
