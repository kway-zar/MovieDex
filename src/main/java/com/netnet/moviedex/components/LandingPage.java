/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.netnet.moviedex.components;

import com.netnet.moviedex.Movie;
import com.netnet.moviedex.QuickSort;
import com.netnet.moviedex.UserData;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import popUp.DetailPopup;
import popUp.UserPopup;

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
    private JDialog loadingDialog;
    private JProgressBar progressBar;
    private Main parent;
    private UserData user;

    public LandingPage() {
        setOpaque(false);
        initComponents();

    }

    public LandingPage(UserData user, Main parent) {
        UIManager.put("ScrollBar.track", new Color(26, 26, 29));
        this.parent = parent;
        this.user = user;
        this.displayList = user.getMovies();
        String defaultSort = "Popular";
        setOpaque(false);
        initComponents();
        LoadingDialogBox(parent);

        showLoadingDialog();
        new Thread(() -> {
            try {

                viewType(isMovieTab, user);
                sortCard(defaultSort);
                jScrollPane1.setViewportView(jPanel1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            hideLoadingDialog();
        }).start();

        sortType.addActionListener((ActionEvent e) -> {
            showLoadingDialog();
            new Thread(() -> {
                try {
                    String preferredSorting = (String) sortType.getSelectedItem();

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
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                hideLoadingDialog();
            }).start();
        });

        genre.addActionListener((ActionEvent e) -> {
            showLoadingDialog();
            new Thread(() -> {
                try {
                    String preferredGenre = (String) genre.getSelectedItem();
                    String preferredSorting = (String) sortType.getSelectedItem();

                    setDisplayList(user.getMovies());

                    switch (preferredGenre) {
                        default -> {
                            ArrayList<Integer> indexes = similarity(displayList, preferredGenre);
                            filterGenre(indexes);
                        }
                        case "SCI-FI" -> {
                            ArrayList<Integer> indexes = similarity(displayList, "sci_fi");
                            filterGenre(indexes);
                        }

                    }

                    sortCard(preferredSorting);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                hideLoadingDialog();
            }).start();
        });

        UserLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    showLoadingDialog();

                    new Thread(() -> {
                        try {
                            isMovieTab = false;
                            jPanel3.setVisible(false);
                            viewType(isMovieTab, user);
                            
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        hideLoadingDialog();
                        jPanel3.setVisible(true);
                    }).start();

                }
            }

        });
        MovieLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    showLoadingDialog();

                    new Thread(() -> {
                        try {
                            isMovieTab = true;
                            viewType(isMovieTab, user);
                            String[] type = {"Popular", "Highest", "Lowest"};
                            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(type);
                            sortType.setModel(model);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        hideLoadingDialog();
                    }).start();
                }
            }

        });

    }

    public void LoadingDialogBox(Main parentFrame) {

        loadingDialog = new JDialog(parentFrame, "Loading...", true);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        loadingDialog.setSize(300, 100);
        loadingDialog.setLocationRelativeTo(parentFrame);

        javax.swing.JPanel panel = new javax.swing.JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        javax.swing.JLabel messageLabel = new javax.swing.JLabel("Please wait while data is loading...");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(messageLabel, BorderLayout.NORTH);

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        panel.add(progressBar, BorderLayout.CENTER);

        loadingDialog.add(panel);
    }

    public void showLoadingDialog() {

        SwingUtilities.invokeLater(() -> loadingDialog.setVisible(true));
    }

    public void hideLoadingDialog() {

        SwingUtilities.invokeLater(() -> loadingDialog.dispose());
    }

    /*
    Handles tab switching logic
     */
    public void viewType(boolean isMovieTab, UserData user) {
        // Important: This is called when switching tabs
        this.user = user;
        displayList = QuickSort.sort(user.getMovies(), false, false, false);

        if (isMovieTab) {

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

            jPanel1.setLayout(new GridLayout(4, 5, 5, 10));
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
        jPanel3.setVisible(false);
        for (Map.Entry<String, UserData> entry : parent.getMap().entrySet()) {
            usersList[i] = new userCard(entry.getValue());
            usersList[i].getCard().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JDialog dialog = new JDialog(parent, "User Details", false);
                    dialog.setUndecorated(true);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setSize(840, 558);
                    dialog.setLocationRelativeTo(parent);

                    UserPopup userPopup = new UserPopup(entry.getValue());
                    dialog.setContentPane(userPopup);
                    dialog.setVisible(true);
                    parent.setEnabled(false);
                    userPopup.addPropertyChangeListener("closePanel", evt -> {
                        parent.setEnabled(true);
                        dialog.dispose();

                    });
                }

            });

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
            i = 1;
            for (userCard user : usersList) {
                user.getUserData().getUser().assignRanking(i);
                firePropertyChange("userData", null, user.getUserData());

                i++;
            }
            

        }
        jPanel3.setVisible(true);
        renderUserCard(usersList);

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
            if ("SCI-FI".equals(preferredGenre)) {
                genreForFilter = "SCI_FI";
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

        for (int i = 0; i < displayList.length; i++) {

            Movie movieWithGlobalScore = new Movie(displayList[i]);

            Movie.MovieStatus userStatus = displayList[i].getMovieStatus();
            movieWithGlobalScore.setStatus(userStatus);

            Movie globalMovie = findMovieInGlobalList(displayList[i].getTitle(), globalMovies);
            if (globalMovie != null) {

                double globalScore = globalMovie.getScore();
                int globalTimesRated = globalMovie.getTimesRated();

                movieWithGlobalScore.setScore(globalScore);
                movieWithGlobalScore.setDisplayRated(globalTimesRated);
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
                parent.setEnabled(false);
                detailPopup.addPropertyChangeListener("closePanel", evt -> {
                    parent.setEnabled(true);
                    dialog.dispose();
                });
                detailPopup.addPropertyChangeListener("rating", evt -> {
                    detailPopup.closePanel(true);
                    double newUserRating = (double) evt.getNewValue();

                    Movie userMovie = displayList[j];

                    boolean isFirstTimeRating = userMovie.getMovieStatus() == Movie.MovieStatus.UNRATED;
                    double previousUserRating = userMovie.getScore(); // 0 if UNRATED initially

                    if (isFirstTimeRating) {

                        user.getUser().INCREASE_RATING_COUNT();
                        userMovie.setTimesRated();
                        userMovie.setStatus(Movie.MovieStatus.RATED);

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

                } else {

                    double currentScore = globalMovie.getScore();
                    int currentTimesRated = globalMovie.getTimesRated();

                    double currentTotal = currentScore * currentTimesRated;
                    double adjustedTotal = currentTotal - previousUserRating + newUserRating;
                    double newAverage = adjustedTotal / currentTimesRated;

                    globalMovie.setScore(newAverage);

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

        for (int i = 0; i < displayList.length; i++) {
            tempDisplay[i] = new Movie(displayList[i]);

            Movie.MovieStatus userStatus = displayList[i].getMovieStatus();
            Movie globalMovie = findMovieInGlobalList(displayList[i].getTitle(), globalMovies);
            if (globalMovie != null) {

                tempDisplay[i].setScore(globalMovie.getScore());
                tempDisplay[i].setDisplayRated(globalMovie.getTimesRated());

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
        int rows = (list.length <= 12) ? 0 : 4;
        //int cols = (list.length < =)
        jPanel1.setLayout(new GridLayout(rows, 5, 10, 10));
        if (jPanel1.getComponentCount() >= 1) {
            jPanel1.removeAll();

        }
        int i = 0;
        for (MovieCard card : list) {

            jPanel1.add(card);
        }

        parent.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
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

    public void renderUserCard(userCard[] list) {

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

        for (int i : indexes) {
            Movie userMovie = displayList[i];

            Movie movieCopy = new Movie(userMovie);

            Movie.MovieStatus userStatus = userMovie.getMovieStatus();
            Movie globalMovie = findMovieInGlobalList(userMovie.getTitle(), globalMovies);
            if (globalMovie != null) {

                movieCopy.setScore(globalMovie.getScore());
                movieCopy.setDisplayRated(globalMovie.getTimesRated());

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
        jScrollPane1.setOpaque(false);

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

        genre.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ALL", "ACTION", "ADVENTURE", "COMEDY", "DRAMA", "ROMANCE", "SCI-FI", "SUPERHERO", "THRILLER", "WAR" }));
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
