/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.netnet.moviedex;

/**
 *
 * @author quasar
 */
public class Movie {

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status.name();
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
    private int index = 0;
    private MovieStatus status;
    private MovieGenre[] genre;
    private double score;

    public Movie(String title, String coverLink, double score, MovieStatus status, MovieGenre[] genre) {
        this.title = title;
        this.coverLink = coverLink;
        this.score = score;
        this.status = status;
        this.genre = genre;
    }
    
    public enum MovieGenre{
        SCI_FI,
        ROMANCE,
        ACTION
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
    public int getTimesRated() {
        return timesRated;
    }



    public void setTimesRated() {
        this.timesRated++;
    }

    /**
     * @return the coverLink
     */
    public String getCoverLink() {
        return coverLink;
    }

    /**
     * @return the genre
     */
    public MovieGenre[] getGenre() {

        return genre;
    }
}
