package modele;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.io.Serializable;
import java.io.*;

/**
 * Représente la grille du jeu de la vie, contenant toutes les cellules.
 */
public class Environnement extends Observable implements Runnable, Serializable {

    private Case[][] tab; // Tableau 2D des cellules
    private final Map<Case, Point> map = new HashMap<>(); // Map associant chaque cellule à sa position
    private int sizeX, sizeY; // Dimensions de la  /** Couleur pour les cellules vivantes */
    private Color couleurVivantes = Color.GREEN; // Couleur par défaut
    private Color couleurMortes = Color.WHITE;  // Couleur par défaut




    /**
     * Constructeur : initialise l'environnement avec une règle spécifique.
     *
     * @param _sizeX Largeur de la grille
     * @param _sizeY Hauteur de la grille
     */
    public Environnement(int _sizeX, int _sizeY) {
        sizeX = _sizeX;
        sizeY = _sizeY;
        tab = new Case[sizeX][sizeY];

        // Initialisation des cellules avec la fabrique
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                tab[i][j] = new Case(this);
                map.put(tab[i][j], new Point(i, j));
            }
        }

        // État initial aléatoire
        getClassiqueRules();
        //getGliderGun();
    }


    /************************************************** Regles de Canon a planeur ****************************************************************************/
    public void getGliderGun() {
        // Initialiser tout à mort
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                tab[i][j].setState(false);
            }
        }

        // Coordonnées pour construire un canon à planeur horizontal
        int x = 5; // Point de départ en X
        int y = 5; // Point de départ en Y (assurez-vous que sizeY est suffisant pour une disposition horizontale)

        // Partie gauche du canon
        tab[x + 4][y + 0].setState(true);
        tab[x + 5][y + 0].setState(true);
        tab[x + 4][y + 1].setState(true);
        tab[x + 5][y + 1].setState(true);

        // Partie centrale gauche
        tab[x + 4][y + 10].setState(true);
        tab[x + 5][y + 10].setState(true);
        tab[x + 6][y + 10].setState(true);
        tab[x + 3][y + 11].setState(true);
        tab[x + 7][y + 11].setState(true);
        tab[x + 2][y + 12].setState(true);
        tab[x + 8][y + 12].setState(true);
        tab[x + 2][y + 13].setState(true);
        tab[x + 8][y + 13].setState(true);
        tab[x + 5][y + 14].setState(true);
        tab[x + 3][y + 15].setState(true);
        tab[x + 7][y + 15].setState(true);
        tab[x + 4][y + 16].setState(true);
        tab[x + 5][y + 16].setState(true);
        tab[x + 6][y + 16].setState(true);
        tab[x + 5][y + 17].setState(true);

        // Partie centrale droite
        tab[x + 2][y + 20].setState(true);
        tab[x + 3][y + 20].setState(true);
        tab[x + 4][y + 20].setState(true);
        tab[x + 2][y + 21].setState(true);
        tab[x + 3][y + 21].setState(true);
        tab[x + 4][y + 21].setState(true);
        tab[x + 1][y + 22].setState(true);
        tab[x + 5][y + 22].setState(true);
        tab[x + 0][y + 24].setState(true);
        tab[x + 1][y + 24].setState(true);
        tab[x + 5][y + 24].setState(true);
        tab[x + 6][y + 24].setState(true);

        // Partie droite du canon
        tab[x + 2][y + 34].setState(true);
        tab[x + 3][y + 34].setState(true);
        tab[x + 2][y + 35].setState(true);
        tab[x + 3][y + 35].setState(true);
    }

   /***************************************************Regles de high life *******************************************************************************/
    public void getHighLifeRules() {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                tab[i][j].setState(false); // Tout est mort au début
            }
        }
        // Par exemple, un glider
        tab[1][2].setState(true);
        tab[2][3].setState(true);
        tab[3][1].setState(true);
        tab[3][2].setState(true);
        tab[3][3].setState(true);
    }
/*******************************************************Permet de recuperer les differents regles ***************************************************************/
    public void getRules(String rule) {
        sizeX = getSizeX();
        sizeY = getSizeY();
        tab = new Case[sizeX][sizeY];

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                tab[i][j] = new CaseHighLife(this);


                map.put(tab[i][j], new Point(i, j));
            }
        }


        switch (rule) {
            case "Classique":
                getClassiqueRules();
                break;
            case "HighLife":
                getHighLifeRules();
                break;
            case "Canon à planeur":
                getGliderGun();
                break;

            default:
                throw new IllegalArgumentException("Règle non reconnue : " + rule);
        }
    }


    /**
     * Retourne la largeur de la grille.
     */
    public int getSizeX() {
        return sizeX;
    }

    /**
     * Retourne la hauteur de la grille.
     */
    public int getSizeY() {
        return sizeY;
    }

    /************************************************************* une methode pour vider l'environnement ********************************************************/
    public void vider() {
        // Réinitialisation des cellules après redimensionnement
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                tab[i][j].setState(false);
            }


        }
    }
    /************************************************************* une methode pour vider les motifs ********************************************************/
    public void viderMotif() {

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                tab[i][j].setStateMotif(false);
            }


        }
    }


    /**
     * Change les dimensions de la grille.
     *
     * @param newSizeX Nouvelle largeur
     * @param newSizeY Nouvelle hauteur
     */
    public void setSize(int newSizeX, int newSizeY) {
        sizeX = newSizeX;
        sizeY = newSizeY;
        tab = new Case[sizeX][sizeY];
        map.clear();

        // Réinitialisation des cellules après redimensionnement
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                tab[i][j] = new Case(this); // Par défaut, règle classique
                map.put(tab[i][j], new Point(i, j));
            }
        }
    }




    /**
     * Retourne l'état d'une cellule donnée par ses coordonnées.
     *
     * @param x Coordonnée x
     * @param y Coordonnée y
     * @return État de la cellule (true = vivante, false = morte)
     */
    public boolean getState(int x, int y) {
        if (x < 0 || x >= sizeX || y < 0 || y >= sizeY) {
            return false; // Hors limites
        }
        return tab[x][y].getState();
    }

    public boolean getStateMotif(int x, int y) {
        if (x < 0 || x >= sizeX || y < 0 || y >= sizeY) {
            return false; // Hors limites
        }
        return tab[x][y].isStateMotif();
    }
    /*****************************************************************Retourne l'objet Case à ces coordonnées*********************************************/
    public Case getCell(int x, int y) {
        if (x >= 0 && x < sizeX && y >= 0 && y < sizeY) {
            return tab[x][y]; // Retourne l'objet Case à ces coordonnées
        }
        return null; // Retourne null si les coordonnées sont hors limites
    }


    // Getter et Setter pour la couleur des cellules vivantes
    public Color getCouleurVivantes() {
        return couleurVivantes;
    }

    public void setCouleurVivantes(Color couleurVivantes) {
        this.couleurVivantes = couleurVivantes;
    }

    public void setCouleurMortes(Color couleurMortes) {
        this.couleurMortes = couleurMortes;
    }


    // Getter et Setter pour la couleur des cellules mortes
    public Color getCouleurMortes() {
        return couleurMortes;
    }




    /**
     * Retourne la cellule voisine dans une direction donnée.
     *
     * @param source Cellule d'origine
     * @param d      Direction souhaitée
     * @return Cellule voisine ou null si hors limite
     */
    public Case getCase(Case source, Direction d) {
        // Récupération de la position actuelle de la cellule source
        Point pos = map.get(source);
        if (pos == null) {
            return null; // Si la cellule source n'est pas trouvée dans la map
        }

        int x = pos.x;
        int y = pos.y;

        // Calcul des nouvelles coordonnées en fonction de la direction
        switch (d) {
            case h:   // Haut
                y--;
                break;
            case hd:  // Haut-Droite
                y--;
                x++;
                break;
            case d:   // Droite
                x++;
                break;
            case db:  // Bas-Droite
                x++;
                y++;
                break;
            case b:   // Bas
                y++;
                break;
            case bg:  // Bas-Gauche
                y++;
                x--;
                break;
            case g:   // Gauche
                x--;
                break;
            case gh:  // Haut-Gauche
                y--;
                x--;
                break;
            default:
                return null; // Direction invalide
        }

        // Optionnel : Gestion torique des coordonnées
        // Pour éviter des dépassements des bords (grille connectée)
        x = (x + sizeX) % sizeX;
        y = (y + sizeY) % sizeY;

        // Vérification supplémentaire des coordonnées (dans les limites)
        if (x >= 0 && x < sizeX && y >= 0 && y < sizeY) {
            return tab[x][y]; // Retourne la cellule correspondante
        }

        return null; // Si les coordonnées sont en dehors des limites (impossible avec tore activé)
    }

    /**
     * Initialise aléatoirement les états des cellules.
     */
    public void getClassiqueRules() {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                tab[i][j].rndState();
                System.out.println("Cellule (" + i + ", " + j + ") initialisée à : " + tab[i][j].getState());
            }
        }
    }


    /**
     * Calcule la prochaine génération de cellules.
     */
    public void calculerGenerationSuivante() {
        // Calcul des états futurs
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                tab[i][j].nextState();
            }
        }

        // Mise à jour des états
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                tab[i][j].setState(tab[i][j].isBuffer());
            }
        }

        // Notifier les observateurs  : l'interface graphique c a d la vue
        setChanged();
        notifyObservers();
    }

    /************************************************** methode qui permet de changer l'etat d'une cellule*******************************/
    public void inverseretats(int x, int y) {
        getCell(x, y).inverseretats();
        setChanged();
        notifyObservers();
    }

    /********************************************************Placer motifs predefinit **********************************/
    public void placerMotif(String motif, int x, int y) {
        switch (motif) {
            case "Glider":
                Pattern.placePattern(this, Pattern.GLIDER, x, y);
                break;
            case "Block":
                Pattern.placePattern(this, Pattern.BLOCK, x, y);
                break;

            default:
                System.out.println("Motif inconnu");
        }
        setChanged();
        notifyObservers();
    }
    public void afficherMotif(String motif, int x, int y) {
        switch (motif) {
            case "Glider":
                Pattern.afficherPattern(this, Pattern.GLIDER, x, y);
                break;
            case "Block":
                Pattern.afficherPattern(this, Pattern.BLOCK, x, y);
                break;

            default:
                System.out.println("Motif inconnu");
        }
        setChanged();
        notifyObservers();
    }
    /*******************************************************La serelisation  pour l'exentension B2**********************************************************************************/
    public void sauvegarder(String cheminFichier) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(cheminFichier))) {
            oos.writeObject(this);  // Sérialisation de l'objet "Environnement"
            System.out.println("Environnement sauvegardé dans " + cheminFichier);
        }
    }

    /*********************************************** Désérialisation et retour de l'objet "Environnement"***************************************************************************/
    public static Environnement charger(String cheminFichier) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(cheminFichier))) {
            return (Environnement) ois.readObject();
        }
    }

    /**
     * Exécution dans un thread séparé.
     */
    @Override
    public void run() {
        calculerGenerationSuivante();
        setChanged();
        notifyObservers();
    }

  

}


