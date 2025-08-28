/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.netnet.moviedex;

/**
 *
 * @author quasar
 */
public class UserData {

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the movies
     */
    public Movie[] getMovies() {
        return movies;
    }

    /**
     * @param movies the movies to set
     */
    public void setMovies(Movie[] movies) {
        this.movies = movies;
    }
    private User user;
    private Movie[] movies;

    public UserData(User user, Movie[] movies) {
        this.user = user;
        this.movies = movies;
    }
    
    
}
