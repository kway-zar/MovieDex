/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.netnet.moviedex.components;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.netnet.moviedex.Movie;
import com.netnet.moviedex.Movie.MovieStatus;
import com.netnet.moviedex.QuickSort;
import com.netnet.moviedex.User;
import com.netnet.moviedex.UserData;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

/**
 *
 * @author quasar
 */
public class Main extends javax.swing.JFrame {

    /**
     * Creates new form previewFrame
     */
    Login loginPage;
    private HashMap<String, UserData> map = new HashMap<>();
    private Movie[] movies;
    private User[] users;
    private BufferedImage image;

    public Main() {
        setTitle("MovieDex");
        try {
                
                Image icon = ImageIO.read(getClass().getResource("/programIcon.png")); 
                setIconImage(icon);
            } catch (IOException e) {
                e.printStackTrace();
                
            }

        initComponents();

        this.movies = new Movie[]{
            new Movie("1917", "/movieCover/1917.jpg", 0, MovieStatus.UNRATED,
            new Movie.MovieGenre[]{Movie.MovieGenre.ACTION, Movie.MovieGenre.DRAMA, Movie.MovieGenre.WAR}),
            new Movie("Interstellar", "/movieCover/interstellar.jpg", 0, MovieStatus.UNRATED,
            new Movie.MovieGenre[]{Movie.MovieGenre.SCI_FI, Movie.MovieGenre.ADVENTURE, Movie.MovieGenre.DRAMA}),
            new Movie("Boyz n the Hood", "/movieCover/boyznthehood.png", 0, MovieStatus.UNRATED,
            new Movie.MovieGenre[]{Movie.MovieGenre.ACTION, Movie.MovieGenre.DRAMA}),
            new Movie("The Pianist", "/movieCover/thePianist.png", 0, MovieStatus.UNRATED,
            new Movie.MovieGenre[]{Movie.MovieGenre.DRAMA, Movie.MovieGenre.WAR}),
            new Movie("Spider-Man: Across the Spider-Verse", "/movieCover/spider-man_across the spider-verse.jpg", 0, MovieStatus.UNRATED,
            new Movie.MovieGenre[]{Movie.MovieGenre.ACTION,Movie.MovieGenre.SUPERHERO,Movie.MovieGenre.ADVENTURE}),
            new Movie("Inception", "/movieCover/inception.jpg", 0, MovieStatus.UNRATED,
            new Movie.MovieGenre[]{Movie.MovieGenre.ACTION,Movie.MovieGenre.SCI_FI, Movie.MovieGenre.ADVENTURE,Movie.MovieGenre.THRILLER}),
            new Movie("The Prestige", "/movieCover/the prestige.jpg", 0, MovieStatus.UNRATED,
            new Movie.MovieGenre[]{Movie.MovieGenre.DRAMA,Movie.MovieGenre.SCI_FI,Movie.MovieGenre.THRILLER}),
            new Movie("All Quiet on the Western Front", "/movieCover/all quiet on the western front.jpg", 0, MovieStatus.UNRATED,
            new Movie.MovieGenre[]{Movie.MovieGenre.ACTION, Movie.MovieGenre.WAR, Movie.MovieGenre.DRAMA}),
            new Movie("Home Alone", "/movieCover/home alone.jpg", 0, MovieStatus.UNRATED,
            new Movie.MovieGenre[]{Movie.MovieGenre.COMEDY}),
            new Movie("How to train your Dragon", "/movieCover/httyd.jpg", 0, MovieStatus.UNRATED,
            new Movie.MovieGenre[]{Movie.MovieGenre.ACTION,Movie.MovieGenre.ADVENTURE}),
            new Movie("Johnny English", "/movieCover/johnny english.jpg", 0, MovieStatus.UNRATED,
            new Movie.MovieGenre[]{Movie.MovieGenre.ACTION,Movie.MovieGenre.ADVENTURE,Movie.MovieGenre.COMEDY}),
            new Movie("Hotel Transylvania", "/movieCover/hotel transylvania.jpg", 0, MovieStatus.UNRATED,
            new Movie.MovieGenre[]{Movie.MovieGenre.COMEDY}),
            new Movie("The Wolf of Wall Street", "/movieCover/the wolf of wall street.jpg", 0, MovieStatus.UNRATED,
            new Movie.MovieGenre[]{Movie.MovieGenre.COMEDY, Movie.MovieGenre.DRAMA}),
            new Movie("Blade Runner 2049", "/movieCover/blade runner 2049.jpg", 0, MovieStatus.UNRATED,
            new Movie.MovieGenre[]{Movie.MovieGenre.ACTION,Movie.MovieGenre.SCI_FI, Movie.MovieGenre.THRILLER,Movie.MovieGenre.DRAMA}),
            new Movie("The Truman Show", "/movieCover/the truman show.jpg", 0, MovieStatus.UNRATED,
            new Movie.MovieGenre[]{Movie.MovieGenre.COMEDY,Movie.MovieGenre.DRAMA}),
            new Movie("La La Land", "/movieCover/la la land.jpg", 0, MovieStatus.UNRATED,
            new Movie.MovieGenre[]{Movie.MovieGenre.DRAMA, Movie.MovieGenre.ROMANCE, Movie.MovieGenre.COMEDY}),
            new Movie("Dunkirk", "/movieCover/dunkirk.jpg", 0, MovieStatus.UNRATED,
            new Movie.MovieGenre[]{Movie.MovieGenre.ACTION, Movie.MovieGenre.WAR}),
            new Movie("Dungeons & Dragons: Honour Among Thieves", "/movieCover/Dungeons & Dragons_Honour Among Thieves.jpg", 0, MovieStatus.UNRATED,
            new Movie.MovieGenre[]{Movie.MovieGenre.ACTION,Movie.MovieGenre.ADVENTURE, Movie.MovieGenre.COMEDY}),
            new Movie("Star Wars: Episode III", "/movieCover/Star Wars_Episode III.jpg", 0, MovieStatus.UNRATED,
            new Movie.MovieGenre[]{Movie.MovieGenre.ACTION, Movie.MovieGenre.SCI_FI}),
            new Movie("Gravity", "/movieCover/gravity.jpg", 0, MovieStatus.UNRATED,
            new Movie.MovieGenre[]{Movie.MovieGenre.THRILLER, Movie.MovieGenre.SCI_FI}),};
        //movies = QuickSort.sort(movies, false, false, false);

        this.users = new User[]{
            new User("Guil", "/userIcons/user1.jpg"),
            new User("Troy", "/userIcons/user2.jpg"),
            new User("Feliz Navidad", "/userIcons/user3.jpg"),
            new User("Bince", "/userIcons/user4.jpg"),};

        UserData[] userData = new UserData[users.length];
        for (int i = 0; i < movies.length; i++) {
            movies[i].setIndex(i);
        }

        for (int i = 0; i < users.length; i++) {
            userData[i] = new UserData(users[i], cloneMovies(this.movies));
            map.put(users[i].getName(), userData[i]);
        }

        loginPage = new Login(refreshUsers(userData));
        jPanel1.add(loginPage);

        loginPage.addPropertyChangeListener("username", evt -> {
            String selectedName = (String) evt.getNewValue();

            LandingPage landingPage1 = new LandingPage(map.get(selectedName), this);
            jPanel1.remove(loginPage);
            jPanel1.add(landingPage1);
            curved_Panel1.setVisible(true);

            landingPage1.addPropertyChangeListener("userData", e -> {
                UserData user = (UserData) e.getNewValue();

                map.put(user.getUser().getName(), user);
                landingPage1.refreshMovies(user.getMovies());
                Movie[] newMovieList = new Movie[getMovies().length];
                Movie[] oldList = getMovies();
                
                Map<String, Double> totalScores = new HashMap<>();
                Map<String, Integer> uniqueRatings = new HashMap<>();

                
                map.forEach((key, value) -> {
                    for (Movie m : value.getMovies()) {
                        String movieTitle = m.getTitle();
                        
                
                        if (m.getMovieStatus() == MovieStatus.RATED) {
                            
                            totalScores.put(movieTitle, 
                                totalScores.getOrDefault(movieTitle, 0.0) + m.getScore());
                            
                            
                            uniqueRatings.put(movieTitle, 
                                uniqueRatings.getOrDefault(movieTitle, 0) + 1);
                        }
                    }
                });
                
                //Updates the Global List for display
                for (int i = 0; i < newMovieList.length; i++) {
                    Movie oldMovie = oldList[i];
                    String movieTitle = oldMovie.getTitle();

                    Movie copy = new Movie(oldMovie.getTitle(), oldMovie.getCoverLink(),
                            0, MovieStatus.UNRATED, oldMovie.getGenre());
                    copy.setIndex(oldMovie.getIndex());
                    
                    double totalScore = totalScores.getOrDefault(movieTitle, 0.0);
                    int ratingCount = uniqueRatings.getOrDefault(movieTitle, 0);
                    
                    double avgScore = ratingCount > 0 ? totalScore / ratingCount : 0.0;
                    
                    copy.setScore(avgScore);
                    copy.setDisplayRated(ratingCount); 

                    newMovieList[i] = copy;

                }
                newMovieList = QuickSort.sort(newMovieList, false, false, true);

                setMovies(newMovieList);
                landingPage1.refreshMovies(this.movies);

                SwingUtilities.updateComponentTreeUI(jPanel4);
            });

            try {
                image = ImageIO.read(getClass().getResource(getMap().get(selectedName).getUser().getIconLink()));
                jLabel2.setText(selectedName);
                jPanel3.repaint();
                jPanel1.revalidate();
                jPanel1.repaint();

            } catch (Exception e) {
                e.printStackTrace();
            }

        });

    }

    private Movie[] cloneMovies(Movie[] original) {
        Movie[] copy = new Movie[original.length];
        for (int i = 0; i < original.length; i++) {
            Movie m = original[i];
            copy[i] = new Movie(m.getTitle(), m.getCoverLink(), 0, MovieStatus.UNRATED, m.getGenre());
        }
        return copy;
    }

    public HashMap<String, UserData> getMap() {
        return map;
    }

    public LoginCard[] refreshUsers(UserData[] userData) {
        LoginCard[] userCards = new LoginCard[userData.length];

        for (int i = 0; i < userCards.length; i++) {
            userCards[i] = new LoginCard(userData[i]);

        }
        return userCards;

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

        jPopupMenu1 = new javax.swing.JPopupMenu()
        ;
        Logout = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        curved_Panel1 = new com.netnet.moviedex.components.Curved_Panel(){
            @Override
            public void paintComponent(Graphics g){
                Graphics2D g2 = (Graphics2D)g;
                Shape clip = new java.awt.geom.RoundRectangle2D.Double(0, 0, this.getWidth(), this.getHeight(), 80, 80);

                // set the clip
                g2.setClip(clip);
                if (image != null) {
                    g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
                }
            }
        };
        jLabel2 = new javax.swing.JLabel();

        Logout.setText("Logout");
        Logout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                LogoutMousePressed(evt);
            }
        });
        jPopupMenu1.add(Logout);
        Logout.setUI(new CustomMenuUI());

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1278, 648));

        jPanel1.setBackground(new java.awt.Color(26, 26, 29));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel4.setOpaque(false);
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel3.setOpaque(false);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/title.png"))); // NOI18N

        curved_Panel1.setVisible(false);
        curved_Panel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                curved_Panel1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                curved_Panel1MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout curved_Panel1Layout = new javax.swing.GroupLayout(curved_Panel1);
        curved_Panel1.setLayout(curved_Panel1Layout);
        curved_Panel1Layout.setHorizontalGroup(
            curved_Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );
        curved_Panel1Layout.setVerticalGroup(
            curved_Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 71, Short.MAX_VALUE)
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(149, 7, 64));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 861, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(curved_Panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(curved_Panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jPanel1.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void showPopUp(MouseEvent e) {
        jPopupMenu1.show(jPanel3, curved_Panel1.getX() - 80, curved_Panel1.getY() + 50);
    }

    private void curved_Panel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_curved_Panel1MousePressed
        // TODO add your handling code here:
        showPopUp(evt);
    }//GEN-LAST:event_curved_Panel1MousePressed

    private void curved_Panel1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_curved_Panel1MouseReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_curved_Panel1MouseReleased

    private void LogoutMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LogoutMousePressed
        // TODO add your handling code here:

        if (SwingUtilities.isLeftMouseButton(evt)) {

            curved_Panel1.setVisible(false);

            jPanel1.remove(1);

            jLabel2.setText("");
            UserData[] userData = new UserData[map.size()];
            int i = 0;
            for (Map.Entry<String, UserData> entry : map.entrySet()) {
                Object key = entry.getKey();
                UserData val = entry.getValue();
                userData[i] = val;

                i++;
            }
            //loginPage.reloadUsers(refreshUsers(userData));
            

            jPanel1.add(loginPage);
            revalidate();
            repaint();

        }
    }//GEN-LAST:event_LogoutMousePressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FlatDarculaLaf.setup();
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem Logout;
    private com.netnet.moviedex.components.Curved_Panel curved_Panel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu jPopupMenu1;
    // End of variables declaration//GEN-END:variables

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

    /**
     * @param movies the movies to set
     */
}
