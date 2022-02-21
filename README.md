# TD5(bis) - Trains

Lien GitHub Classroom pour [faire un fork privé du TP](https://classroom.github.com/a/70jpayuf)

Le but de ce TD est d’utiliser des tâches et des sémaphores pour résoudre des problèmes d’exclusion mutuelle.

On s’intéresse à l’exemple de trains voulant emprunter une voie unique.

![voies](images/voies.png)

Des trains peuvent arriver sur le segment de voie unique par le point _A_ ou le point _B_, et donc traverser le segment dans un sens ou dans l’autre.

La traversée d’un train sera réalisée par une tâche `Train` dont le constructeur prend en argument le numéro du train (on numérote les trains pour pouvoir suivre l’évolution de l’ensemble) ainsi qu’un entier représentant le sens dans lequel le train se déplace (0 pour le sens _AB_ et 1 pour le sens _BA_).

La fonction `run` de la classe `Train` affiche un message lorsqu’elle est exécutée (ce qui correspond au moment où le train arrive devant le segment de voie unique), puis un message lorsque le train s’engage sur la voie unique (en précisant par quel point il entre), attend pendant un certain délai pour simuler le temps de traversée de la voie, et affiche enfin un message lorsque le train quitte le segment de voie unique.

On considère dans un premier temps la classe `Train` suivante (cf. fichier `src/Train.java`) :
```java
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
```

**Remarque :** Pour le moment, on ne met aucune contrainte pour traverser la voie donc les trains s’engagent dès qu’ils sont lancés ce qui va évidemment créer des situations qui provoqueraient des accidents (deux trains engagés en même temps sur la voie dans des sens opposés). Le but du TD sera justement de réfléchir aux moyens de résoudre ce problème.

1. En vous inspirant des TD précédents, écrivez la classe principale de l’application qui doit exécuter successivement 20 tâches `Train` dont le sens est décidé aléatoirement. La tâche principale devra attendre un délai aléatoire compris entre 0 et 300 ms entre le lancement de deux trains successifs.

## Une première solution (ou pas)

Une première idée pour limiter le traffic sur la voie est d’utiliser deux verrous `lockAB` et `lockBA` initialisés à 1 pour qu’il n’y ait qu’un seul train sur la voie unique dans chaque sens.

2. Modifiez votre programme pour que les trains qui arrivent dans le sens _AB_ (resp. _BA_) demandent le verrou `lockAB` (resp. `lockBA`) avant de s’engager sur la voie unique, et libèrent le verrou lorsqu’ils en sortent.

3. Expliquez en quoi cette méthode limite le traffic sur la voie unique mais ne résout pas le problème des accidents.

## Une solution qui marche

Comme ça ne marche pas avec un sémaphore pour chaque sens, on va protéger la voie unique en utilisant un sémaphore d’exclusion mutuelle `mutexVoie` commun aux deux sens.

4. Modifiez l’application en utilisant le sémaphore `mutexVoie` pour assurer qu’il n’y a jamais plus d’un train à la fois engagé sur la voie unique.

5. Exécutez l’application et vérifiez qu’il n’y a plus de situation provoquant des accidents sur la voie unique.

## Améliorations

Dans cette section, vous pouvez tester votre programme dans des conditions différentes en variant le délai entre deux trains successifs (si le délai est plus court, les trains auront tendance à se mettre en attente plus souvent)

6. Modifiez votre programme pour garantir l’alternance entre les deux sens : si un train passe sur la voie unique dans le sens _AB_ et qu’il y a au moins un train en attente dans le sens _BA_ lorsqu’il en sort, le prochain train qui s’engagera sur la voie unique sera un train se déplaçant dans le sens _BA_ (et inversement).

    **Attention :** il ne faut pas imposer une alternance stricte qui bloquerait la voie s’il n’y a pas de train en attente dans le sens opposé quand un train quitte la voie. S’il n’y a personne en face, un train dans le même sens peut s’engager sur la voie.

On va maintenant supposer que tous les trains roulent à la même vitesse, et qu’il n’est donc pas gênant que deux trains allant dans le même sens s’engagent en même temps sur la voie unique (modifiez la classe `Train` pour que tous les trains mettent 200 ms à traverser).

7. Modifiez votre programme pour permettre à plusieurs trains de s’engager en même temps sur la voie unique s’ils se déplacent dans le même sens. Lorsqu’un train arrive, si la voie est occupée par un train allant dans le même sens il peut traverser la voie, si par contre la voie est occupée par un train allant dans le sens inverse il doit attendre que la voie soit libre pour traverser.

    **Indication :** Vous pouvez vous inspirer des algorithmes sur les lecteurs et rédacteurs.

8. _(bonus)_ Modifiez encore le programme pour qu’il permette à plusieurs trains de s’engager en même temps sur la voie unique dans le même sens, tout en garantissant l’alternance des sens : s’il n’y a pas de train en attente dans le sens opposé, les trains peuvent s’engager à la suite sur la voie unique, mais s’il y a un train en attente en face, la voie est donnée alternativement à chaque sens.
