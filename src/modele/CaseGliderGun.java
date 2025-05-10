/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author mamadou and isaac
 */
package modele;

public class CaseGliderGun extends Case {
    public CaseGliderGun(Environnement e) {
        super(e);
    }

    @Override
    protected boolean computeNextState(boolean currentState, int livingNeighbors) {
        if (currentState) {
            return livingNeighbors == 1 || livingNeighbors == 3;
        } else {
            return livingNeighbors == 2 || livingNeighbors == 5;
        }
    }
}
