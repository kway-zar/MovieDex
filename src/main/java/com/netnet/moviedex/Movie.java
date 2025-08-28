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
public class Movie {

    /**
     * @return the status
     */
    public String getStatus() {
        if(status == MovieStatus.RATED){
            return "RATED";
        }
        return "UNRATED";
    }

    /**
     * @param status the status to set
     */
    public void setStatus(MovieStatus status) {
        this.status = status;
    }
    private String title;
    private String coverLink;
    private int timesRated = 0;
    private MovieStatus status;
    private double score;

    public Movie(String title, String coverLink, double score, MovieStatus status) {
        this.title = title;
        this.coverLink = coverLink;
        this.score = score;
        this.status = status;
    }
    
    
    
    public enum MovieStatus{
        RATED,
        UNRATED,
        
    }

    /**
     * @return the score
     */
    public double getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the coverLink
     */
    public Icon toIcon(){
        ImageIcon originalIcon = new ImageIcon(coverLink);
        Image scaledIcon = new ImageIcon(getClass().getResource(coverLink)).getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        
        return new ImageIcon(scaledIcon);
    }

    /**
     * @return the timesRated
     */
    public int getTimesRated() {
        return timesRated;
    }

    /**
     * @param timesRated the timesRated to set
     */
    public void setTimesRated(int timesRated) {
        this.timesRated = timesRated;
    }
}
