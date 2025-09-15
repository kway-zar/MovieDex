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
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

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

    public LandingPage() {
        setOpaque(false);
        initComponents();

    }

    public LandingPage(UserData user, Main parent) {
        this.parent = parent;
        setOpaque(false);
        initComponents();

        sortType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String preferredSorting = (String) sortType.getSelectedItem();
                sortCard(preferredSorting);

            }
        });

        genre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String preferredGenre = (String) genre.getSelectedItem();
                String preferredSorting = (String) sortType.getSelectedItem();
                setDisplayList(user.getMovies());
                sortCard(preferredSorting);

                System.out.println(preferredGenre);

                switch (preferredGenre) {
                    case "Action" -> {
                        ArrayList<Integer> indexes = similarity(displayList, preferredGenre);
                        filterGenre(indexes);

                    }
                    case "Drama" -> {
                        ArrayList<Integer> indexes = similarity(displayList, preferredGenre);
                        filterGenre(indexes);

                    }
                    case "Sci-fi" -> {
                        preferredGenre = "sci_fi";
                        ArrayList<Integer> indexes = similarity(displayList, preferredGenre);
                        filterGenre(indexes);
                    }
                    case "Romance" -> {
                        ArrayList<Integer> indexes = similarity(displayList, preferredGenre);
                        filterGenre(indexes);
                    }
                    case "All" -> {
                        MovieCard[] movieList = new MovieCard[user.getMovies().length];

                        for (int i = 0; i < user.getMovies().length; i++) {
                            movieList[i] = new MovieCard(displayList[i]);

                        }

                        renderCard(movieList);
                        jScrollPane1.getVerticalScrollBar().setValue(0);
                        SwingUtilities.updateComponentTreeUI(genre);
                    }

                }

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
                }
            }

        });

        viewType(isMovieTab, user);
        jScrollPane1.setViewportView(jPanel1);

    }

    public void viewType(boolean isMovieTab, UserData user) {

        if (isMovieTab == true) {
            
            UserLabel.setForeground(Color.WHITE);
            MovieLabel.setForeground(Color.decode("#950740"));
            genre.setVisible(true);
            jLabel2.setVisible(true);
            
            MovieCard[] movieList = new MovieCard[user.getMovies().length];
            displayList = QuickSort.sort(user.getMovies(), false, false);
            
            String[] type = {"Popular","Highest", "Lowest"};
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(type);
            sortType.setModel(model);
            
            for (int i = 0; i < user.getMovies().length; i++) {
                movieList[i] = new MovieCard(displayList[i]);

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

    public void filterGenre(ArrayList<Integer> indexes) {
        MovieCard[] movieList = new MovieCard[indexes.size()];
        int j = 0;
        for (int i : indexes) {
            movieList[j] = new MovieCard(displayList[i]);
            j++;
        }

        setDisplayList(indexToMovie(indexes));

        renderCard(movieList);
        jScrollPane1.getVerticalScrollBar().setValue(0);

    }

    public void sortCard(String preferredSorting) {
        if (preferredSorting.equals("Popular")) {
            if (isMovieTab == true) {
                MovieCard[] movieList = new MovieCard[displayList.length];
                setDisplayList(QuickSort.sort(displayList, false, false));
                for (int i = 0; i < displayList.length; i++) {
                    movieList[i] = new MovieCard(displayList[i]);

                }
                renderCard(movieList);
            }

        } else if (!preferredSorting.equals("Lowest")) {
            if (isMovieTab == true) {
                MovieCard[] movieList = new MovieCard[displayList.length];
                setDisplayList(QuickSort.sort(displayList, true, false));
                for (int i = 0; i < displayList.length; i++) {
                    movieList[i] = new MovieCard(displayList[i]);

                }
                renderCard(movieList);
            }

        } else {
            if (isMovieTab == true) {
                MovieCard[] movieList = new MovieCard[displayList.length];
                setDisplayList(QuickSort.sort(displayList, true, true));
                for (int i = 0; i < displayList.length; i++) {
                    movieList[i] = new MovieCard(displayList[i]);

                }
                renderCard(movieList);
            }
        }
        jScrollPane1.getVerticalScrollBar().setValue(0);

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
        int k = 0;
        for (int i : indexes) {
            newList[k] = displayList[i];
            k++;
        }
        return newList;

    }

    public ArrayList similarity(Movie[] m, String genre) {
        ArrayList<Integer> index = new ArrayList<>();
        if (m != null) {
            for (int i = 0; i < m.length; i++) {
                Movie.MovieGenre[] genres = m[i].getGenre();
                for (Movie.MovieGenre j : genres) {
                    if (j.name().equalsIgnoreCase(genre)) {
                        index.add(i);

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
}
