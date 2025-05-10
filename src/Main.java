
import modele.Environnement;
import modele.Ordonnanceur;
import vue_controleur.FenetrePrincipale;

import javax.swing.SwingUtilities;

/**
 *
 * @author Mamadou
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                Environnement e = new Environnement(100, 100);
                // e.calculerGenerationSuivante(); // Calcul d'une génération
                FenetrePrincipale fenetre = new FenetrePrincipale(e);
                fenetre.setVisible(true);

                e.addObserver(fenetre);

                Ordonnanceur o = new Ordonnanceur(500, e);
                fenetre.setOrdonnanceur(o);
                o.start();
               
            }
        });

    }

}
