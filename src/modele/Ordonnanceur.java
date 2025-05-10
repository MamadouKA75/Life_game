package modele;

/**
 * Classe pour gérer le déroulement automatique des générations.
 */
public class Ordonnanceur extends Thread {
    private long sleepTime; // Temps entre chaque génération
    private Runnable runnable; // Tâche à exécuter (génération suivante)
    private boolean enpause = false; // État de pause
    private int generation = 0; // Compteur de générations
    private Thread thread;
    public Ordonnanceur(long _sleepTime, Runnable _runnable) {
        sleepTime = _sleepTime;
        runnable = _runnable;
    }

    public synchronized void setVitesse(long vitesse) {
        this.sleepTime = vitesse;
    }

    public int getGeneration() {
        return generation;
    }

    public synchronized void arreter() {
        enpause = true;
    }

    public synchronized void redemarre() {
        enpause = false;
        notify();
    }
    /* fonction qui permet de lancer le jeu */
    public synchronized  void lancer(){
        enpause=false;
        thread =new  Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            if (enpause) {
                try {
                    synchronized (this) {
                        wait(); //blocque le thread
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            runnable.run(); // Exécution du calcul
            generation++;
            try {
                sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
