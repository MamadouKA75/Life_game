/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modele;

/**
 *
 * @author mamadou
 */


import java.util.function.BiFunction;

public class LeniaRules {
    private double[] kernel; // Noyau utilisé pour le filtrage spatial
    private double mu;       // Valeur cible
    private double sigma;    // Déviation standard pour les règles de Lenia

    public double[] getKernel() {
        return kernel;
    }

    public LeniaRules(double[] kernel, double mu, double sigma) {
        this.kernel = kernel;
        this.mu = mu;
        this.sigma = sigma;
    }

    /**
     * Génère un noyau gaussien circulaire.
     * @param size Taille du noyau
     * @return Tableau contenant les valeurs du noyau
     */
    public static double[] generateGaussianKernel(int size) {
        double[] kernel = new double[size];
        double sum = 0;
        for (int i = 0; i < size; i++) {
            double x = i - (size - 1) / 2.0;
            kernel[i] = Math.exp(-x * x / (2 * size / 2.0 * size / 2.0));
            sum += kernel[i];
        }
        // Normalisation
        for (int i = 0; i < size; i++) {
            kernel[i] /= sum;
        }
        return kernel;
    }

    /**
     * Applique les règles Lenia pour calculer le nouvel état d'une cellule.
     * @param neighbors Nombre total de cellules voisines vivantes (normalisé)
     * @return Nouvel état de la cellule
     */
    public boolean applyRule(double neighbors) {
        // Application de la règle Lenia
        double value = Math.exp(-(neighbors - mu) * (neighbors - mu) / (2 * sigma * sigma));
        return value > 0.5; // Retourne vrai si la cellule reste vivante/mort
    }

    /**
     * Applique un filtre spatial à la grille.
     * @param grid Grille binaire d'états (vivante ou morte)
     * @param size Taille du filtre
     * @return Grille mise à jour
     */
    public static double[][] applyFilter(boolean[][] grid, double[] kernel) {
        int rows = grid.length;
        int cols = grid[0].length;
        double[][] filteredGrid = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double sum = 0;

                for (int ki = 0; ki < kernel.length; ki++) {
                    for (int kj = 0; kj < kernel.length; kj++) {
                        int ni = i + ki - kernel.length / 2;
                        int nj = j + kj - kernel.length / 2;

                        if (ni >= 0 && ni < rows && nj >= 0 && nj < cols && grid[ni][nj]) {
                            sum += kernel[ki] * kernel[kj];
                        }
                    }
                }

                filteredGrid[i][j] = sum;
            }
        }

        return filteredGrid;
    }

    /**
     * Création d'une règle Lenia prédéfinie.
     * Exemple : Orbium
     */
    public static LeniaRules createOrbium() {
        double[] kernel = generateGaussianKernel(5);
        return new LeniaRules(kernel, 0.3, 0.1);
    }

}
