package vue_controleur;

import modele.Environnement;
import modele.Ordonnanceur;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

import java.io.IOException;

import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

public class FenetrePrincipale extends JFrame implements Observer {

    private JPanel[][] tab;
    private Environnement env;
    private Ordonnanceur ordonnanceur;

    private JLabel lblGeneration; // Label pour afficher le numéro de génération
    private JLabel lblCellulesVivantes; // Label pour afficher le nombre de cellules vivantes

    private JPanel gridPanel; // Panneau contenant la grille
    private JScrollPane scrollPane; // ScrollPane pour la grille


    private String motif;
    private boolean isPlacingMotif = false;


    public FenetrePrincipale(Environnement _env) {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        motif = "";
        this.env = _env;
        build();

    }

    public void setOrdonnanceur(Ordonnanceur ordonnanceur) {
        this.ordonnanceur = ordonnanceur;
    }

    public void build() {
        setTitle("Jeu de la Vie");
        setSize(1000, 700); // Dimensions  pour une interface lisible

        // **Panneau principal**
        JPanel mainPanel = new JPanel(new BorderLayout());
        JMenuBar jm =new JMenuBar();
        JMenu m=new JMenu("Fichier");
        JMenuItem mi= new JMenuItem("Sauvegarder");
        JMenuItem mii= new JMenuItem("Charger");
        JMenu pm=new JMenu("Placer Motifs");
        JMenuItem pmi= new JMenuItem("GLIDER");
        JMenuItem pmii= new JMenuItem("BLOCK");
        m.add(mi);
        m.add(mii);
        jm.add(m);
        pm.add(pmi);
        pm.add(pmii);
        jm.add(pm);

        setJMenuBar(jm);
/*************************************** Gestion des motifs******************************************************/
        pmi.addActionListener(e ->{
            motif= "GLIDER";
            isPlacingMotif=true;
        }  );


        pmii.addActionListener(e ->{
            motif= "BLOCK";
            isPlacingMotif=true;
        }  );
/******************************************************Gestion de sauvegarde de l'environnemnt dans un fichier *********************************************************/
        mi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Sauvegarder l'environnement dans un fichier
                try {
                    env.sauvegarder("monEnvironnement.ser");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        mii.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Charger un fichier d'environnement
                Environnement envCharge = null;
                try {
                    envCharge = Environnement.charger("monEnvironnement.ser");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                if (envCharge != null) {
                    env = envCharge;  // Remplacer l'environnement actuel par celui chargé
                    //updateGridSize(env.getSizeX(), env.getSizeY(), mainPanel);  // Mettre à jour la taille de la grille
                    //updateCellState(env.getSizeX(), env.getSizeY());
                    refreshGridColors();  // Rafraîchir les couleurs de la grille
                    System.out.println("Environnement chargé avec succès !");
                } else {
                    System.out.println("Erreur lors du chargement de l'environnement.");
                }
            }
        });
        /*************************************Gestion barre de chargement initial de l'appli ***********************************/
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

// Créer un panneau contenant la barre de progression
        JPanel panel = new JPanel();
        panel.add(progressBar);

// Afficher la boîte de dialogue
        JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
        JDialog dialog = optionPane.createDialog(this, "Chargement...");
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Empêche la fermeture pendant le chargement

// SwingWorker pour mettre à jour la barre de progression dans un thread séparé
        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                // Simuler une tâche avec mise à jour de la barre
                for (int i = 0; i <= 100; i++) {
                    try {
                        Thread.sleep(15); // Simuler un temps de chargement (50ms par incrément)
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    publish(i); // Publier les progrès
                }
                return null;
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                // Mettre à jour la barre de progression avec la dernière valeur publiée
                int value = chunks.get(chunks.size() - 1);
                progressBar.setValue(value);
            }

            @Override
            protected void done() {
                // Fermer la boîte de dialogue une fois le chargement terminé
                dialog.dispose();
            }
        };

       // Lancer le worker
        worker.execute();
        dialog.setVisible(true); // Afficher la boîte de dialogue




        /********* *******************************************************Grille centrale*********************************************************************/
        gridPanel = new JPanel(new GridLayout(env.getSizeX(), env.getSizeY()));
        tab = new JPanel[env.getSizeX()][env.getSizeY()];
        Border blackline = BorderFactory.createLineBorder(Color.white, 1);
        gridPanel.setBorder(blackline);

        // Remplir la grille avec des cellules
        for (int i = 0; i < env.getSizeX(); i++) {
            for (int j = 0; j < env.getSizeY(); j++) {
                tab[i][j] = new JPanel();
                tab[i][j].setBorder(BorderFactory.createLineBorder(Color.black, 1));
                final int x=i;
                final  int y=j;
                tab[i][j].addMouseListener(new MouseAdapter() {
                    @Override

                    public void mouseClicked(MouseEvent e) {
                        System.out.println("mamadou");
                        // Inverser l'état de la cellule selon les règles du jeu de la vie
                        //env.inverseretats(x,y);  // Inverse l'état de la cellule

                        if(motif == "") {
                            env.inverseretats(x,y);
                        } else if (motif=="GLIDER" && isPlacingMotif){
                            env.placerMotif("Glider", x, y);
                            motif = "";
                        }else if(motif=="BLOCK" && isPlacingMotif) {
                            env.placerMotif("Block",x,y);
                            motif="";
                        }
                    }



                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if(motif != "" && motif=="GLIDER"){
                            env.afficherMotif("Glider", x, y);

                        }else if(motif != "" && motif=="BLOCK" ){
                            env.afficherMotif("Block",x,y);
                        }


                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if(motif != ""){
                            env.viderMotif();
                            ///System.out.println("ffffff");
                        }
                    }
                });




                gridPanel.add(tab[i][j]);
            }
        }
   
       
              

     /******************************************Gestion barre de delifilement ************************************************/
        scrollPane = new JScrollPane(gridPanel);

        // Toujours afficher les barres de défilement
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Personnalisation des couleurs des barres de défilement
        customizeScrollBars();

        // Ajout du panneau de défilement au panneau principal
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // **Panneau des contrôles (latéral droit)**
        JPanel controlPanel = createControlPanel(mainPanel);
        mainPanel.add(controlPanel, BorderLayout.EAST);

        // Définir le panneau principal comme contenu de la fenêtre
        setContentPane(mainPanel);
    }

    private void customizeScrollBars() {
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setBackground(Color.LIGHT_GRAY); // Couleur de fond
        verticalScrollBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.gray; // Couleur du "thumb" (bouton à glisser)
            }
        });

        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
        horizontalScrollBar.setBackground(Color.LIGHT_GRAY);
        horizontalScrollBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.gray; // Couleur du "thumb" (bouton à glisser)
            }
        });
    }

    private JPanel createControlPanel(JPanel mainPanel) {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridBagLayout());
        controlPanel.setPreferredSize(new Dimension(200, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Boutons et contrôles
        JButton btnLancer = new JButton("Lancer", new ImageIcon(getClass().getResource("assets/play.png")));
        JButton btnPause = new JButton("Pause", new ImageIcon(getClass().getResource("assets/pause.png")));

        JButton btnReset = new JButton("Réinitialiser", new ImageIcon(getClass().getResource("assets/reset.png")));


        /******************************************Gestion des touches************************************************************************/
        // Actions sur les boutons
        btnLancer.addActionListener(e -> ordonnanceur.redemarre());
        btnPause.addActionListener(e -> ordonnanceur.arreter());


       btnReset.addActionListener(l -> {
    if (env != null) { // Vérifiez que l'objet 'env' n'est pas nul
        try {
            env.vider(); // Réinitialise l'environnement
            env.notifyObservers(); // Notifie les observateurs du changement d'état
        } catch (Exception e) {
            // Gérer les exceptions potentielles
            System.err.println("Erreur lors de la réinitialisation de l'environnement : " + e.getMessage());
            e.printStackTrace();
        }
    } else {
        System.err.println("L'environnement 'env' est nul. Impossible de réinitialiser.");
    }
});

        // Slider pour la vitesse
        JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 50, 5000, 2000);
        Hashtable<Integer, JLabel> sliderLabels = new Hashtable<>();
        sliderLabels.put(5000, new JLabel("Lent"));
        sliderLabels.put(50, new JLabel("Rapide"));
        speedSlider.setLabelTable(sliderLabels);
        speedSlider.setMinorTickSpacing(100);
        speedSlider.setMajorTickSpacing(1000);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        // Listener pour ajuster la vitesse
        // Gestion de la vitesse
        speedSlider.addChangeListener(e -> {
            int sliderValue = speedSlider.getValue();
            ordonnanceur.setVitesse(sliderValue);
        });

        // Indicateurs pour le nombre de générations et cellules vivantes
        lblGeneration = new JLabel("Génération : 0");
        lblCellulesVivantes = new JLabel("Cellules vivantes : 0");

        // Taille de la grille
        JLabel lblGrille = new JLabel("Taille de la grille :");
        JComboBox<String> gridSizeBox = new JComboBox<>(new String[]{"20x20", "50x50", "100x100","170X170"});

        // Action dynamique pour changer la taille de la grille
        gridSizeBox.addActionListener(e -> {
            String selectedSize = (String) gridSizeBox.getSelectedItem();
            if (selectedSize != null) {
                String[] dimensions = selectedSize.split("x");
                int newSizeX = Integer.parseInt(dimensions[0]);
                int newSizeY = Integer.parseInt(dimensions[1]);
                updateGridSize(newSizeX, newSizeY, mainPanel);

            }
        });
       // Choix des règles
JLabel lblChoixRegles = new JLabel("Choix des règles :");
JComboBox<String> ruleBox = new JComboBox<>(new String[]{"Classique", "HighLife", "Canon à planeur"});

ruleBox.addActionListener(e -> {
    String selectedRule = (String) ruleBox.getSelectedItem();
    if (selectedRule != null) {
        env.getRules(selectedRule);
        //env.setRule(selectedRule);
        //updateGridSize(env.getSizeX(), env.getSizeY(), mainPanel);
    }
});

        // Choix des couleurs 
        
        JLabel lblchoixCouleur = new JLabel("Cellules Vivants");
        JLabel lblCouleur = new JLabel("Cellules Morts");
        JComboBox<String> couleur = new JComboBox<>(new String[]{"Vert", "bleu", "Rouge","Noir","Marron","Orange","Violet"});
        JComboBox<String> couleurM = new JComboBox<>(new String[]{"Blanc","Gris"});
        // Gestion des couleurs

        // Action quand une couleur est sélectionnée pour les cellules vivantes
        couleur.addActionListener(e -> {
            String selectedColor = (String) couleur.getSelectedItem();
            Color newColor = null;

            // Convertir la couleur sélectionnée en un objet Color
            switch (selectedColor) {
                case "Vert":
                    newColor = Color.GREEN;
                    break;
                case "bleu":
                    newColor = Color.BLUE;
                    break;
                case "Rouge":
                    newColor = Color.RED;
                    break;
                case "Noir":
                    newColor = Color.BLACK;
                    break;
                case "Marron":
                    newColor = new Color(139, 69, 19); // Marron
                    break;
                case "Orange":
                    newColor = Color.ORANGE;
                    break;
                case "Violet":
                    newColor = new Color(128, 0, 128); // Violet
                    break;
            }

            if (newColor != null) {
                env.setCouleurVivantes(newColor); // Met à jour la couleur dans le modèle
                refreshGridColors(); // Rafraîchit la grille avec la nouvelle couleur
            }
        });

// Action quand une couleur est sélectionnée pour les cellules mortes
        couleurM.addActionListener(e -> {
            String selectedColorM = (String) couleurM.getSelectedItem();
            Color newColorM = null;

            // Convertir la couleur sélectionnée pour les cellules mortes
            switch (selectedColorM) {
                case "Blanc":
                    newColorM = Color.WHITE;
                    break;
                case "Gris":
                    newColorM = Color.GRAY;
                    break;
            }

            if (newColorM != null) {
                env.setCouleurMortes(newColorM); // Met à jour la couleur dans le modèle
                refreshGridColors(); // Rafraîchit la grille avec la nouvelle couleur
            }
        });

        // Ajout des composants au panneau des contrôles
        gbc.gridx = 0;
        gbc.gridy = 0;
       
        controlPanel.add(lblGeneration, gbc);

         gbc.gridy++;
        controlPanel.add(lblCellulesVivantes, gbc);
        gbc.gridy++;
        controlPanel.add(btnLancer, gbc);

        gbc.gridy++;
        controlPanel.add(btnPause, gbc);

        gbc.gridy++;
        controlPanel.add(btnReset, gbc);

        gbc.gridy++;
        controlPanel.add(new JLabel("Vitesse :"), gbc);

        gbc.gridy++;
        controlPanel.add(speedSlider, gbc);


       
        
         gbc.gridy++;
        controlPanel.add(lblChoixRegles, gbc);
        
        gbc.gridy++;
        controlPanel.add(ruleBox, gbc);
        

        gbc.gridy++;
        controlPanel.add(lblchoixCouleur , gbc);
        gbc.gridy++;
        controlPanel.add(couleur , gbc);
         gbc.gridy++;
         controlPanel.add(lblCouleur , gbc);
         gbc.gridy++;
         controlPanel.add(couleurM , gbc);
        gbc.gridy++;
        controlPanel.add(lblGrille, gbc);

        gbc.gridy++;
        controlPanel.add(gridSizeBox, gbc);


        return controlPanel;
    }

    private void updateGridSize(int newSizeX, int newSizeY, JPanel mainPanel) {
        env.setSize(newSizeX, newSizeY); // Assurez-vous que Environnement prend en charge cette méthode
        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(newSizeX, newSizeY));
        tab = new JPanel[newSizeX][newSizeY];

        for (int i = 0; i < newSizeX; i++) {
            for (int j = 0; j < newSizeY; j++) {
                final int x=i;
                final int y=j;
                tab[i][j] = new JPanel();
                tab[i][j].setBorder(BorderFactory.createLineBorder(Color.black, 1));
                   // Action de donne vie par simple clic

                env.getCell(x,y).inverseretats();

              
                gridPanel.add(tab[i][j]);
            }
        }

        // Rafraîchir l'interface
        gridPanel.revalidate();
        gridPanel.repaint();
        scrollPane.revalidate();
        scrollPane.repaint();
    }
/***********************************************************Permet demettre a jour les couleurs de la grille *************************/
    private void refreshGridColors() {
        for (int i = 0; i < env.getSizeX(); i++) {
            for (int j = 0; j < env.getSizeY(); j++) {
                if (env.getState(i, j)) {
                    tab[i][j].setBackground(env.getCouleurVivantes()); // Récupère la couleur des cellules vivantes depuis le modèle
                } else {
                    tab[i][j].setBackground(env.getCouleurMortes()); // Récupère la couleur des cellules mortes depuis le modèle
                }
            }
        }
    }

    @Override
public void update(Observable o, Object arg) {
    int nbCellulesVivantes = 0;

    for (int i = 0; i < env.getSizeX(); i++) {
        for (int j = 0; j < env.getSizeY(); j++) {
            if (env.getState(i, j)) {
                tab[i][j].setBackground(env.getCouleurVivantes());

                nbCellulesVivantes++;
            } else {
                tab[i][j].setBackground(env.getCouleurMortes());
            }

            if(motif != ""){
                if (env.getStateMotif(i, j)) {
                    tab[i][j].setBackground(Color.BLUE);

                }
            }
        }
    }

   // System.out.println("Cellules vivantes affichées : " + nbCellulesVivantes);
    lblGeneration.setText("Génération : " + ordonnanceur.getGeneration());
    lblCellulesVivantes.setText("Cellules vivantes : " + nbCellulesVivantes);
}
}
