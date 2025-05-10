/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author mamadou
 */
package modele;

public class CaseHighLife extends Case {

    public CaseHighLife(Environnement e) {
        super(e);
    }

    @Override
    protected boolean computeNextState(boolean currentState, int livingNeighbors) {
        if (currentState) {
            return livingNeighbors == 2 || livingNeighbors == 3;
        } else {
            return livingNeighbors == 3 || livingNeighbors == 6;
        }
    }
}

