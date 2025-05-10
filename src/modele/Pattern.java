package modele;

public class Pattern {

    // Exemple de motifs pré-définis
    public static final boolean[][] GLIDER = {
            {false, true, false},
            {false, false, true},
            {true, true, true}
    };

    public static final boolean[][] BLOCK = {
            {true, true},
            {true, true}
    };

    // Méthode pour placer un motif dans l'environnement
    public static void placePattern(Environnement env, boolean[][] pattern, int startX, int startY) {
        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern[i].length; j++) {
                if (startX + i < env.getSizeX() && startY + j < env.getSizeY()) {
                    env.getCell(startX + i, startY + j).setState(pattern[i][j]);
                }
            }
        }
    }

    public static void afficherPattern(Environnement env, boolean[][] pattern, int startX, int startY) {
        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern[i].length; j++) {
                if (startX + i < env.getSizeX() && startY + j < env.getSizeY()) {
                    env.getCell(startX + i, startY + j).setStateMotif(pattern[i][j]);
                }
            }
        }
    }
}
