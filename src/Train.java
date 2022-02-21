import java.util.Random;

class Train implements Runnable {
    String sens;
    int numero;

    Train(int numero, int sens) {
        if (sens == 0) {
            this.sens = "AB";
        } else {
            this.sens = "BA";
        }
        this.numero = numero;
    }

    public void run() {
        Random rand = new Random(); // générateur de nombres aléatoires
        try {
            System.out.println(this.numero + ": (" + this.sens.charAt(0) + ") ... ");
            Thread.sleep(100 + rand.nextInt(200)); // simule le temps de traversée
            System.out.println(this.numero + ": ... (" + this.sens.charAt(1) + ")");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}