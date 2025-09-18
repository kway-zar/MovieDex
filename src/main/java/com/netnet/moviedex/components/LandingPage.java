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
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import test.DetailPopup;

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
        setOpaque(false);

        initComponents();

        sortType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String preferredSorting = (String) sortType.getSelectedItem();
                String preferredGenre = (String) genre.getSelectedItem();
                ArrayList<Integer> indexes = similarity(displayList, preferredGenre);
                filterGenre(indexes);
                viewType(isMovieTab, user);
                sortCard(preferredSorting);
                refreshMovies(displayList);

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
                    viewType(!isMovieTab, user);
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
        jScrollPane1.setViewportView(jPanel1);

    }

    public void viewType(boolean isMovieTab, UserData user) {
        displayList = QuickSort.sort(user.getMovies(), false, false, true);
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

                // Create a copy of the movie with the global score
                Movie movieWithGlobalScore = new Movie(displayList[i]);
                if (globalMovie != null) {
                    movieWithGlobalScore.setScore(globalMovie.getScore());
                    movieWithGlobalScore.setDisplayRated(globalMovie.getTimesRated());
                }

                movieList[i] = new MovieCard(movieWithGlobalScore);
                addInteraction(movieList[i], i);
                

            }

            jPanel1.setLayout(new GridLayout(4, 5, 10, 10));
            renderCard(movieList);
        } else {

            this.isMovieTab = false;
            UserLabel.setForeground(Color.decode("#950740"));
            MovieLabel.setForeground(Color.WHITE);
            genre.setVisible(false);
            jLabel2.setVisible(false);

            String[] type = {"Highest", "Lowest"};
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(type);
            sortType.setModel(model);

            userCard[] usersList = new userCard[4];

            int i = 0;
            for (Map.Entry<String, UserData> entry : parent.getMap().entrySet()) {
                usersList[i] = new userCard(entry.getValue());
                i++;
            }
            jPanel1.setLayout(new GridLayout(0, 5, 10, 10));
            renderCard(usersList);
        }
        SwingUtilities.updateComponentTreeUI(jPanel1);
    }
    
    private void addInteraction(MovieCard m, int j) {
        m.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {

                        JDialog dialog = new JDialog(parent, "Movie Details", false);
                        dialog.setUndecorated(true);
                        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                        dialog.setSize(572, 500); // Adjust to match your DetailPopup design
                        dialog.setLocationRelativeTo(parent); // Center relative to main window

                        // Add your custom JPanel form to the dialog
                        DetailPopup detailPopup = new DetailPopup(displayList[j]);
                        dialog.setContentPane(detailPopup);

                        dialog.setVisible(true); // Show it

                        detailPopup.addPropertyChangeListener("closePanel", evt -> {
                            dialog.dispose();
                        });
                        detailPopup.addPropertyChangeListener("rating", evt -> {
                            double userRating = (double) evt.getNewValue();
                            user.getUser().INCREASE_RATING_COUNT();
                            if (displayList[j].getStatus().equalsIgnoreCase("UNRATED")) {
                                displayList[j].setTimesRated();
                                displayList[j].setStatus(Movie.MovieStatus.RATED);

                            }

                            displayList[j].setScore(userRating);

                            updateGlobalMovies(displayList[j], userRating);
                            user.setMovies(displayList);
                            setDisplayList(user.getMovies());
                            firePropertyChange("userData", null, user);
                            ArrayList<Integer> indexes = similarity(displayList, (String) sortType.getSelectedItem());
                            filterGenre(indexes);
                            sortCard((String) sortType.getSelectedItem());
                            viewType(isMovieTab, user);

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

    private void updateGlobalMovies(Movie ratedMovie, double userRating) {

        Movie[] globalMovies = parent.getMovies();
        for (Movie globalMovie : globalMovies) {
            if (globalMovie.getTitle().equals(ratedMovie.getTitle())) {

                double currentTotal = globalMovie.getScore() * globalMovie.getTimesRated();
                double newAverage = (currentTotal + userRating) / globalMovie.getTimesRated();

                globalMovie.setScore(newAverage);

                globalMovie.setTimesRated();

                break;
            }
        }
        parent.setMovies(globalMovies);
    }

    private void updateGlobalMovies(Movie ratedMovie) {

        Movie[] globalMovies = parent.getMovies();
        for (Movie globalMovie : globalMovies) {
            if (globalMovie.getTitle().equals(ratedMovie.getTitle())) {

                globalMovie = ratedMovie;

                break;
            }
        }
        parent.setMovies(globalMovies);
    }

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

    public void sortCard(String preferredSorting) {
        if (isMovieTab == true) {
            Movie[] tempDisplay = new Movie[displayList.length];
            Movie[] globalMovies = parent.getMovies();

            for (int i = 0; i < displayList.length; i++) {
                tempDisplay[i] = new Movie(displayList[i]);
                Movie globalMovie = findMovieInGlobalList(displayList[i].getTitle(), globalMovies);
                if (globalMovie != null) {
                    tempDisplay[i].setScore(globalMovie.getScore());
                }
            }

            if (preferredSorting.equals("Popular")) {
                tempDisplay = QuickSort.sort(tempDisplay, false, false, false);
            } else if (preferredSorting.equals("Highest")) {
                tempDisplay = QuickSort.sort(tempDisplay, true, false, false);
            } else if (preferredSorting.equals("Lowest")) {
                tempDisplay = QuickSort.sort(tempDisplay, true, true, false);
            }

            // Update display and render
            MovieCard[] movieList = new MovieCard[tempDisplay.length];
            for (int i = 0; i < tempDisplay.length; i++) {
                movieList[i] = new MovieCard(tempDisplay[i]);
            }

            this.displayList = tempDisplay;
            
            // Update the display list
            renderCard(movieList);
            jScrollPane1.getVerticalScrollBar().setValue(0);
        } else {

        }

    }

    public void setDisplayList(Movie[] m) {
        Movie[] old = this.displayList;
        this.displayList = m;
        firePropertyChange("list", old, this.displayList);
        SwingUtilities.updateComponentTreeUI(jPanel1);

    }

    public void renderCard(MovieCard[] list) {

        if (jPanel1.getComponentCount() >= 1) {
            jPanel1.removeAll();

        }
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

        if (jPanel1.getComponentCount() >= 1) {
            jPanel1.removeAll();

        }
        for (userCard card : list) {

            jPanel1.add(card);
        }
        revalidate();
        repaint();
    }

    public Movie[] indexToMovie(ArrayList<Integer> indexes) {
        Movie[] newList = new Movie[indexes.size()];
        Movie[] globalMovies = parent.getMovies();
        int k = 0;

        for (int i : indexes) {
            // Get the original movie from user's list

            Movie userMovie = displayList[i];

            // Create a copy
            Movie movieCopy = new Movie(userMovie);

            // Find the corresponding global movie to get the latest rating data
            Movie globalMovie = findMovieInGlobalList(userMovie.getTitle(), globalMovies);
            if (globalMovie != null) {
                // Always use the global data for display purposes
                movieCopy.setScore(globalMovie.getScore());
                movieCopy.setDisplayRated(globalMovie.getTimesRated());
                // Also make sure the status is synced
                movieCopy.setStatus(userMovie.getMovieStatus());
            }

            newList[k] = movieCopy;
            k++;
        }
        return newList;
    }

    public ArrayList similarity(Movie[] m, String genre) {
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
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jPanel1.setBackground(new java.awt.Color(26, 26, 29));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2813, Short.MAX_VALUE)
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
