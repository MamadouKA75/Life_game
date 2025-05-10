
package modele;

import java.util.Random;
import java.io.Serializable;


/**
 * Représente une cellule dans le jeu de la vie.
 */
public class Case implements Serializable {
    private static final long serialVersionUID = 1L; // Pour la compatibilité des versions

    private static final Random rnd = new Random();
    private boolean state;
    private boolean buffer;
    protected Environnement env;

    private boolean stateMotif;

    public void setBuffer(boolean buffer) {
        this.buffer = buffer;
    }

    public Case(Environnement e) {
        stateMotif = false;
        env = e;
    }

    public void setStateMotif(boolean stateMotif) {
        this.stateMotif = stateMotif;
    }

    public boolean isStateMotif() {
        return stateMotif;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void rndState() {
        state = rnd.nextBoolean();
    }

    public boolean isBuffer() {
        return buffer;
    }
    /************************************************ Inverser les etats *****************************************************************************/
    public void inverseretats() {
    // Inverse l'état actuel
    this.state = !this.state;

    // Affiche un message de log pour vérifier l'inversion
    System.out.println("État inversé. Nouveau état : " + (state ? "Vivant" : "Mort"));
}


    /***************************** Méthode calculant l'état futur de la cellule.******************************************************************************/



    public void nextState() {
    boolean newState = computeNextState(state, countLivingNeighbors());
    //System.out.println("Cellule : " + this + ", État actuel : " + state + ", État futur : " + newState);
    buffer = newState;
}


    /**
     * Calcule l'état futur en fonction des règles personnalisées.
     * @param currentState L'état actuel de la cellule.
     * @param voisinsVivants Le nombre de voisins vivants.
     * @return Le nouvel état (`true` pour vivante, `false` pour morte).
     */
    protected boolean computeNextState(boolean currentState, int voisinsVivants) {
        // Règles classiques de Conway
        if (currentState) {
            return voisinsVivants == 2 || voisinsVivants == 3;
        } else {
            return voisinsVivants == 3;
        }
    }
    
/**********************************************Permet de calculer le nombre de cellule vivants***************************************************/
  private int countLivingNeighbors() {
    int voisinsVivants = 0;

    // Toutes les directions possibles
    Direction[] directions = Direction.values();

    for (Direction dir : directions) {
        // Obtenir le voisin dans cette direction
        Case voisin = env.getCase(this, dir); // `this` est la cellule courante

        // Vérifier si le voisin est vivant
        if (voisin != null && voisin.getState()) {
            voisinsVivants++;
        }
    }

   // System.out.println("Cellule (" + this + ") voisins vivants : " + voisinsVivants);
    return voisinsVivants;
}


}
