/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.netnet.moviedex.components;

import com.netnet.moviedex.Movie;
import com.netnet.moviedex.QuickSort;
import com.netnet.moviedex.UserData;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import popUp.DetailPopup;

/**
 *
 * @author quasar
 */
public class LandingPage extends javax.swing.JPanel {

    /**
     * Creates new form LandingPage
     */
    private Movie[] displayList;
    boolean isMovieTab = true;
    private Main parent;
    private UserData user;

    public LandingPage() {
        setOpaque(false);
        initComponents();

    }

    public LandingPage(UserData user, Main parent) {
        this.parent = parent;
        this.user = user;
        this.displayList = user.getMovies();
        String defaultSort = "Popular";
        setOpaque(false);

        initComponents();

        sortType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String preferredSorting = (String) sortType.getSelectedItem();
                System.out.println("Sort type changed to: " + preferredSorting + " (isMovieTab: " + isMovieTab + ")");
                
                if (isMovieTab) {
                    // Movie tab sorting
                    String preferredGenre = (String) genre.getSelectedItem();
                    ArrayList<Integer> indexes = similarity(displayList, preferredGenre);
                    filterGenre(indexes);
                    sortCard(preferredSorting);
                } else {
                    // User tab sorting
                    sortUserCards(preferredSorting);
                }
            }
        });

        genre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String preferredGenre = (String) genre.getSelectedItem();
                String preferredSorting = (String) sortType.getSelectedItem();

                setDisplayList(user.getMovies());

                System.out.println(preferredGenre);

                switch (preferredGenre) {
                    case "All", "Action", "Drama", "Romance" -> {
                        ArrayList<Integer> indexes = similarity(displayList, preferredGenre);
                        filterGenre(indexes);
                    }
                    case "Sci-fi" -> {
                        ArrayList<Integer> indexes = similarity(displayList, "sci_fi");
                        filterGenre(indexes);
                    }

                }

                sortCard(preferredSorting);

            }

        });

        UserLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    isMovieTab = false;
                    viewType(isMovieTab, user);
                }
            }

        });
        MovieLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    isMovieTab = true;
                    viewType(isMovieTab, user);
                    String[] type = {"Popular", "Highest", "Lowest"};
                    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(type);
                    sortType.setModel(model);
                }
            }

        });

        viewType(isMovieTab, user);
        sortCard(defaultSort);
        jScrollPane1.setViewportView(jPanel1);

    }

    /*
    Handles tab switching logic
     */
    public void viewType(boolean isMovieTab, UserData user) {
        // Important: This is called when switching tabs
        this.user = user;
        displayList = QuickSort.sort(user.getMovies(), false, false, false);

        // Debug Purposes may delete later
        System.out.println("Viewing for user: " + user.getUser().getName());
        for (Movie m : displayList) {
            System.out.println("Movie: " + m.getTitle() + ", Status: " + m.getMovieStatus() + ", TimesRated: " + m.getTimesRated());
        }

        if (isMovieTab == true) {

            UserLabel.setForeground(Color.WHITE);
            MovieLabel.setForeground(Color.decode("#950740"));
            genre.setVisible(true);
            jLabel2.setVisible(true);

            MovieCard[] movieList = new MovieCard[user.getMovies().length];

            Movie[] globalList = QuickSort.sort(parent.getMovies(), false, false, true);

            for (int i = 0; i < user.getMovies().length; i++) {

                final int j = i;

                Movie globalMovie = findMovieInGlobalList(displayList[i].getTitle(), globalList);

                Movie movieWithGlobalScore = new Movie(displayList[i]);
                if (globalMovie != null) {
                    movieWithGlobalScore.setScore(globalMovie.getScore());
                    movieWithGlobalScore.setDisplayRated(globalMovie.getTimesRated());

                    movieWithGlobalScore.setStatus(displayList[i].getMovieStatus());
                }

                movieList[i] = new MovieCard(movieWithGlobalScore);
                addInteraction(movieList[i], i);

            }

            jPanel1.setLayout(new GridLayout(4, 5, 10, 10));
            renderCard(movieList);
        } else {

            
            UserLabel.setForeground(Color.decode("#950740"));
            MovieLabel.setForeground(Color.WHITE);
            genre.setVisible(false);
            jLabel2.setVisible(false);

            String[] type = {"Highest", "Lowest"};
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(type);
            sortType.setModel(model);
            
            sortUserCards(sortType.getSelectedItem().toString());
            
            
            jPanel1.setLayout(new GridLayout(0, 5, 10, 10));

        }
        SwingUtilities.updateComponentTreeUI(jPanel1);
    }

    private void sortUserCards(String sort) {
        userCard[] usersList = new userCard[4];
        int i = 0;

        for (Map.Entry<String, UserData> entry : parent.getMap().entrySet()) {
            usersList[i] = new userCard(entry.getValue());
            i++;
        }
        //Use insertion since the users size is small
        if (!sort.equals("Highest")) {
            for (i = 0; i < usersList.length; i++) {
                int j = i;

                while (j > 0 && (usersList[j - 1].getUser().getCount() > usersList[j].getUser().getCount())) {
                    userCard temp = usersList[j];
                    usersList[j] = usersList[j - 1];
                    usersList[j - 1] = temp;
                    j--;
                }

            }

        } else {
            for (i = 0; i < usersList.length; i++) {
                int j = i;

                while (j > 0 && (usersList[j - 1].getUser().getCount() < usersList[j].getUser().getCount())) {
                    userCard temp = usersList[j];
                    usersList[j] = usersList[j - 1];
                    usersList[j - 1] = temp;
                    j--;
                }

            }

        }
        renderCard(usersList);
        

    }

    /**
     * Refreshes the display after a new rating is submitted. Applies current
     * genre filter and sorting settings.
     */
    private void refreshDisplayAfterRating() {
        if (isMovieTab) {

            String preferredGenre = (String) genre.getSelectedItem();
            String preferredSorting = (String) sortType.getSelectedItem();

            setDisplayList(user.getMovies());

            String genreForFilter = preferredGenre;
            if ("Sci-fi".equals(preferredGenre)) {
                genreForFilter = "sci_fi";
            }

            ArrayList<Integer> indexes = similarity(displayList, genreForFilter);
            filterGenre(indexes);
            sortCard(preferredSorting);

            refreshMovieCardsWithInteractions();
        }

        revalidate();
        repaint();
    }

    /**
     * Refreshes movie cards and re-adds mouse interactions
     */
    private void refreshMovieCardsWithInteractions() {
        MovieCard[] movieList = new MovieCard[displayList.length];
        Movie[] globalMovies = parent.getMovies();

        System.out.println("REFRESHING MOVIE CARDS");

        for (int i = 0; i < displayList.length; i++) {

            Movie movieWithGlobalScore = new Movie(displayList[i]);

            Movie.MovieStatus userStatus = displayList[i].getMovieStatus();
            movieWithGlobalScore.setStatus(userStatus);

            Movie globalMovie = findMovieInGlobalList(displayList[i].getTitle(), globalMovies);
            if (globalMovie != null) {

                double globalScore = globalMovie.getScore();
                int globalTimesRated = globalMovie.getTimesRated();

                System.out.println("Movie: " + movieWithGlobalScore.getTitle()
                        + ", User Status: " + userStatus
                        + ", Global Score: " + globalScore
                        + ", Global Times Rated: " + globalTimesRated);

                movieWithGlobalScore.setScore(globalScore);
                movieWithGlobalScore.setDisplayRated(globalTimesRated);
            } else {
                System.out.println("WARNING: Global movie not found for " + movieWithGlobalScore.getTitle());
            }

            movieList[i] = new MovieCard(movieWithGlobalScore);
            addInteraction(movieList[i], i);
        }

        renderCard(movieList);
    }

    public void addInteraction(MovieCard m, int j) {
        m.getCard().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                JDialog dialog = new JDialog(parent, "Movie Details", false);
                dialog.setUndecorated(true);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setSize(572, 500);
                dialog.setLocationRelativeTo(parent);

                DetailPopup detailPopup = new DetailPopup(displayList[j]);
                dialog.setContentPane(detailPopup);

                dialog.setVisible(true);

                detailPopup.addPropertyChangeListener("closePanel", evt -> {
                    dialog.dispose();
                });
                detailPopup.addPropertyChangeListener("rating", evt -> {
                    detailPopup.closePanel(true);
                    double newUserRating = (double) evt.getNewValue();

                    Movie userMovie = displayList[j];

                    boolean isFirstTimeRating = userMovie.getMovieStatus() == Movie.MovieStatus.UNRATED;
                    double previousUserRating = userMovie.getScore(); // 0 if UNRATED initially

                    System.out.println("======= RATING DETAILS =======");
                    System.out.println("User: " + user.getUser().getName());
                    System.out.println("Movie: " + userMovie.getTitle());
                    System.out.println("Current movie status: " + userMovie.getMovieStatus());
                    System.out.println("Current times rated: " + userMovie.getTimesRated());
                    System.out.println("Is first time rating: " + isFirstTimeRating);

                    if (isFirstTimeRating) {

                        user.getUser().INCREASE_RATING_COUNT();
                        userMovie.setTimesRated();
                        userMovie.setStatus(Movie.MovieStatus.RATED);

                        System.out.println("First time rating for this user: " + user.getUser().getName());
                        System.out.println("New times rated: " + userMovie.getTimesRated());
                    } else {
                        System.out.println("Updating existing rating for user: " + user.getUser().getName());
                        System.out.println("Times rated unchanged: " + userMovie.getTimesRated());

                    }

                    userMovie.setScore(newUserRating);

                    updateGlobalMovies(userMovie, previousUserRating, newUserRating, isFirstTimeRating);

                    user.setMovies(displayList);
                    setDisplayList(user.getMovies());
                    firePropertyChange("userData", null, user);

                    refreshDisplayAfterRating();
                });
            }

        });

    }

    private Movie findMovieInGlobalList(String title, Movie[] globalList) {
        for (Movie globalMovie : globalList) {
            if (globalMovie.getTitle().equals(title)) {
                return globalMovie;
            }
        }
        return null;
    }

    /*
     *Updates movie list array in the main class.
     */
    private void updateGlobalMovies(Movie ratedMovie, double previousUserRating, double newUserRating, boolean isFirstTimeRating) {
        Movie[] globalMovies = parent.getMovies();
        for (Movie globalMovie : globalMovies) {
            if (globalMovie.getTitle().equals(ratedMovie.getTitle())) {
                System.out.println("Global movie before update: " + globalMovie.getTitle()
                        + ", times rated: " + globalMovie.getTimesRated()
                        + ", score: " + globalMovie.getScore());

                if (isFirstTimeRating == true) {

                    double currentScore = globalMovie.getScore();
                    int currentTimesRated = globalMovie.getTimesRated();

                    // Calculate new values
                    double currentTotal = currentScore * currentTimesRated;
                    double newTotal = currentTotal + newUserRating;
                    int newTimesRated = currentTimesRated + 1;
                    double newAverage = newTotal / newTimesRated;

                    globalMovie.setDisplayRated(newTimesRated);
                    globalMovie.setScore(newAverage);

                    System.out.println("FIRST TIME RATING - Global movie after update: " + globalMovie.getTitle()
                            + ", NEW times rated: " + globalMovie.getTimesRated()
                            + ", NEW score: " + globalMovie.getScore());

                } else {

                    double currentScore = globalMovie.getScore();
                    int currentTimesRated = globalMovie.getTimesRated();

                    double currentTotal = currentScore * currentTimesRated;
                    double adjustedTotal = currentTotal - previousUserRating + newUserRating;
                    double newAverage = adjustedTotal / currentTimesRated;

                    globalMovie.setScore(newAverage);

                    System.out.println("RATING UPDATE - Global movie after update: " + globalMovie.getTitle()
                            + ", times rated unchanged: " + globalMovie.getTimesRated()
                            + ", NEW score: " + globalMovie.getScore());

                }
                break;
            }
        }

        parent.setMovies(globalMovies);

    }

    /*Filters the display movie cards*/
    public void filterGenre(ArrayList<Integer> indexes) {

        Movie[] filteredMovies = indexToMovie(indexes);
        MovieCard[] movieList = new MovieCard[filteredMovies.length];

        for (int i = 0; i < filteredMovies.length; i++) {
            movieList[i] = new MovieCard(filteredMovies[i]);

        }

        this.displayList = filteredMovies;

        renderCard(movieList);
        jScrollPane1.getVerticalScrollBar().setValue(0);

    }

    /*Sorts the cards*/
    public void sortCard(String preferredSorting) {

        Movie[] tempDisplay = new Movie[displayList.length];
        Movie[] globalMovies = parent.getMovies();

        System.out.println("SORTING CARDS WITH: " + preferredSorting);

        for (int i = 0; i < displayList.length; i++) {
            tempDisplay[i] = new Movie(displayList[i]);

            Movie.MovieStatus userStatus = displayList[i].getMovieStatus();
            Movie globalMovie = findMovieInGlobalList(displayList[i].getTitle(), globalMovies);
            if (globalMovie != null) {

                tempDisplay[i].setScore(globalMovie.getScore());
                tempDisplay[i].setDisplayRated(globalMovie.getTimesRated());

                System.out.println("Movie: " + tempDisplay[i].getTitle()
                        + ", User Status: " + userStatus
                        + ", Global Score: " + globalMovie.getScore()
                        + ", Global Times Rated: " + globalMovie.getTimesRated());
            }

            tempDisplay[i].setStatus(userStatus);
        }

        if (preferredSorting.equals("Popular")) {
            tempDisplay = QuickSort.sort(tempDisplay, false, false, false);
        } else if (preferredSorting.equals("Highest")) {
            tempDisplay = QuickSort.sort(tempDisplay, true, false, false);
        } else if (preferredSorting.equals("Lowest")) {
            tempDisplay = QuickSort.sort(tempDisplay, true, true, false);
        }

        MovieCard[] movieList = new MovieCard[tempDisplay.length];
        for (int i = 0; i < tempDisplay.length; i++) {
            movieList[i] = new MovieCard(tempDisplay[i]);
        }

        this.displayList = tempDisplay;

        renderCard(movieList);
        refreshMovieCardsWithInteractions();
        jScrollPane1.getVerticalScrollBar().setValue(0);

    }

    public void setDisplayList(Movie[] m) {
        Movie[] old = this.displayList;
        this.displayList = m;
        firePropertyChange("list", old, this.displayList);
        SwingUtilities.updateComponentTreeUI(jPanel1);

    }

    public void renderCard(MovieCard[] list) {
        Main parent = this.parent;

        if (jPanel1.getComponentCount() >= 1) {
            jPanel1.removeAll();

        }
        int i = 0;
        for (MovieCard card : list) {

            jPanel1.add(card);
        }
        revalidate();
        repaint();
    }

    public void refreshMovies(Movie[] movies) {
        jPanel1.removeAll();

        for (Movie m : movies) {
            MovieCard card = new MovieCard(m);
            jPanel1.add(card);
        }
        revalidate();
        repaint();
    }

    public void renderCard(userCard[] list) {

        jPanel1.removeAll();
        for (userCard card : list) {

            jPanel1.add(card);
        }
        revalidate();
        repaint();
    }

    /*
        Turns the filtered movie indexes into an array of movies
     */
    public Movie[] indexToMovie(ArrayList<Integer> indexes) {
        Movie[] newList = new Movie[indexes.size()];
        Movie[] globalMovies = parent.getMovies();
        int k = 0;

        System.out.println("FILTERING BY GENRE - Creating filtered movie list");

        for (int i : indexes) {
            Movie userMovie = displayList[i];

            Movie movieCopy = new Movie(userMovie);

            Movie.MovieStatus userStatus = userMovie.getMovieStatus();
            Movie globalMovie = findMovieInGlobalList(userMovie.getTitle(), globalMovies);
            if (globalMovie != null) {

                movieCopy.setScore(globalMovie.getScore());
                movieCopy.setDisplayRated(globalMovie.getTimesRated());

                System.out.println("Movie: " + movieCopy.getTitle()
                        + ", User Status: " + userStatus
                        + ", Global Score: " + globalMovie.getScore()
                        + ", Global Times Rated: " + globalMovie.getTimesRated());
            }

            movieCopy.setStatus(userStatus);

            newList[k] = movieCopy;
            k++;
        }
        return newList;
    }

    /*
       *Finds the similar movies by genre and returns their indexes
     */
    public ArrayList<Integer> similarity(Movie[] m, String genre) {
        ArrayList<Integer> index = new ArrayList<>();
        //m = QuickSort.sort(m, false, false, true);
        if (m != null) {
            for (int i = 0; i < m.length; i++) {
                if (genre.equalsIgnoreCase("All")) {
                    index.add(i);

                } else {

                    Movie.MovieGenre[] genres = m[i].getGenre();
                    for (Movie.MovieGenre j : genres) {
                        if (j.name().equalsIgnoreCase(genre)) {
                            index.add(i);

                        }
                    }
                }

            }

        }

        return index;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        UserLabel = new javax.swing.JLabel();
        MovieLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        genre = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        sortType = new javax.swing.JComboBox<>();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        jPanel3.setOpaque(false);
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 64)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(149, 7, 64));
        jLabel1.setText("   RANKINGS");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = -12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(jLabel1, gridBagConstraints);

        jScrollPane1.setBackground(new java.awt.Color(26, 26, 29));
        jScrollPane1.setBorder(null);
        jScrollPane1.setForeground(new java.awt.Color(26, 26, 29));
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel1.setBackground(new java.awt.Color(26, 26, 29));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2823, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2029, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jPanel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1191;
        gridBagConstraints.ipady = 432;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(24, 51, 0, 0);
        jPanel3.add(jScrollPane1, gridBagConstraints);

        jPanel5.setOpaque(false);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setPreferredSize(new java.awt.Dimension(1188, 5));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        UserLabel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        UserLabel.setForeground(new java.awt.Color(255, 255, 255));
        UserLabel.setText("USERS");
        UserLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        MovieLabel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        MovieLabel.setForeground(new java.awt.Color(149, 7, 64));
        MovieLabel.setText("MOVIES");
        MovieLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(107, 107, 107));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Genre");

        genre.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Action", "Sci-fi", "Romance", "Drama" }));
        genre.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(107, 107, 107));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Sort by");

        sortType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Popular", "Highest", "Lowest" }));
        sortType.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(MovieLabel)
                        .addGap(53, 53, 53)
                        .addComponent(UserLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2175, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(genre, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(6, 6, 6)
                        .addComponent(sortType, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 2766, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(UserLabel)
                        .addComponent(MovieLabel))
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(genre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sortType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 48, 0, 48);
        jPanel3.add(jPanel5, gridBagConstraints);

        jPanel2.add(jPanel3);

        add(jPanel2);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel MovieLabel;
    private javax.swing.JLabel UserLabel;
    private javax.swing.JComboBox<String> genre;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> sortType;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the user
     */
    public UserData getUser() {
        return user;
    }
}
