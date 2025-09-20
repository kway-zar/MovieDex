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
    private String iconLink;
    public int moviesRated = 0;
    public int ranking = 0;
//    private Movie[] list;
    
    public User(String name, String link){
        this.name = name;
        this.iconLink = link;
        
        
    
    }
    public void assignRanking(int rank) {
        this.ranking = rank;
    }
    
    public int getRanking() {
        return ranking;
    }
    public void INCREASE_RATING_COUNT(){
        moviesRated++;
    }
    public int getCount(){
        return moviesRated;
    }
    
    public Icon toIcon(){
        ImageIcon originalIcon = new ImageIcon(getIconLink());
        Image scaledIcon = new ImageIcon(getClass().getResource(getIconLink())).getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        
        return new ImageIcon(scaledIcon);
    }
    //array of movies

    /**
     * @return the iconLink
     */
    public String getIconLink() {
        return iconLink;
    }
}
