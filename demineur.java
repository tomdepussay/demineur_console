
// Démineur v2 by Tom Depussay :)

import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;

public class demineur {
    public static void main(String[] args){

        // Initialisation du jeu

        int difficulty[] = set_difficulty();
        int tab_user[][] = initialisation(difficulty[0]);
        affichage(tab_user);
        int position[] = tour(tab_user);
        int tab_final[][] = create_all_tab_final(create_tab_final(difficulty[0], difficulty[1], position));
        tab_user[position[0]][position[1]] = tab_final[position[0]][position[1]];
        tab_user = claim_zone(tab_user, tab_final, position);
        play(tab_user, difficulty,tab_final);
    }
    public static int[] set_difficulty(){

        // Modifier la difficulté de jeu
        // De 1 à 2 en modifiant la taille de la grille et le nombre de mine

        Scanner sc = new Scanner(System.in);
        int d;
        int dif[] = new int[2];
        do {
            System.out.println("Les difficultés : ");
            System.out.println("1 - Grille de 9x9 avec 9 mines.");
            System.out.println("2 - Grille de 9x9 avec 25 mines.");
            System.out.print("Quel difficultés voulez-vous ? ");
            d = sc.nextInt();
        } while(d != 1 && d != 2 && d != 3);
        if (d == 1){

            // Si la difficulté est de 1 alors une grille de 9x9 et 9 mines.

            dif[0] = 9;
            dif[1] = 9;
        }
        if (d == 2){

            // Si la difficulté est de 1 alors une grille de 9x9 et 25 mines.

            dif[0] = 9;
            dif[1] = 25;
        }
        return dif;
    }
    public static int[][] initialisation(int l){

        // Création d'un tableau selon une taille a double dimension, générée par la fonction set_difficulty() 

        int tab[][] = new int[l][l];
        for (int i = 0; i < tab.length; i++){
            for (int j = 0; j < tab.length; j++){
                tab[i][j] = 0;
            }
        }
        return tab;
    }
    public static void affichage(int[][] tab){

        // Affichage d'un tableau dans la console
        // Prends un autre tableau en entrer avec des données bruts.

        // Génération des variables pour changer la couleur du texte

        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_RED = "\u001B[31m";
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_YELLOW = "\u001B[33m";
        final String ANSI_BLUE = "\u001B[34m";
        final String ANSI_PURPLE = "\u001B[35m";
        final String ANSI_CYAN = "\u001B[36m";
        String tab_str[][] = new String[tab.length+2][tab.length+2];
        String str = "";

        // Création des nombres de ligne et de colonne sur le côté de la grille pour se retrouver facilement

        for (int i = 0; i < tab_str.length; i++){
            for (int j = 0; j < tab_str.length; j++){
                if (i == 0){
                    tab_str[i][j] = ANSI_YELLOW + j + ANSI_RESET + " ";
                }
                if (j == 0){
                    tab_str[i][j] = ANSI_YELLOW + i + ANSI_RESET + " ";
                }
            }
        }

        // Création du coeur de la grille 

        for (int i = 0; i < tab.length; i++){
            for (int j = 0; j < tab.length; j++){
                switch (tab[i][j]) {
                    case 10:
                        // Zone protégé (les 8 cases autour de la position de départ)
                        tab_str[i+1][j+1] = "P ";
                        break;
                    case -1:
                        // Affichage de case "Mine"
                        tab_str[i+1][j+1] = ANSI_RED + "M " + ANSI_RESET;
                        break;
                    case 0:
                        // Affichage de case non découverte par le joueur
                        tab_str[i+1][j+1] = "? ";
                        break;
                    case 1:
                        // Affichage de case 0 mine au alentour.
                        tab_str[i+1][j+1] = "- ";
                        break;
                    case 2:
                        // Affichage de case 1 mine au alentour.
                        tab_str[i+1][j+1] = ANSI_CYAN + "1 " + ANSI_RESET;
                        break;
                    case 3:
                        // Affichage de case 2 mine au alentour.
                        tab_str[i+1][j+1] = ANSI_GREEN + "2 " + ANSI_RESET;
                        break;
                    case 4:
                        // Affichage de case 3 mine au alentour.
                        tab_str[i+1][j+1] = ANSI_RED + "3 " + ANSI_RESET;
                        break;
                    case 5:
                        // Affichage de case 4 mine au alentour.
                        tab_str[i+1][j+1] = ANSI_PURPLE + "4 " + ANSI_RESET;
                        break;
                    case 6:
                        // Affichage de case 5 mine au alentour.
                        tab_str[i+1][j+1] = ANSI_BLUE + "5 " + ANSI_RESET;
                        break;
                    case 7:
                        // Affichage de case 6 mine au alentour.
                        tab_str[i+1][j+1] = ANSI_RED + "6 " + ANSI_RESET;
                        break;
                    case 8:
                        // Affichage de case 7 mine au alentour.
                        tab_str[i+1][j+1] = ANSI_RED + "7 " + ANSI_RESET;
                        break;
                    case 9:
                        // Affichage de case 8 mine au alentour.
                        tab_str[i+1][j+1] = ANSI_RED + "8 " + ANSI_RESET;
                        break;
                }
            }
        }

        // Création des nombres de ligne et de colonne sur le côté de la grille pour se retrouver facilement

        for (int i = 0; i < tab_str.length; i++){
            for (int j = 0; j < tab_str.length; j++){
                if (i == tab.length+1){
                    tab_str[i][j] = ANSI_YELLOW + j + ANSI_RESET + " ";
                }
                if (j == tab.length+1){
                    tab_str[i][j] = ANSI_YELLOW + i + ANSI_RESET + " ";
                }
            }
        }
        tab_str[tab.length+1][tab.length+1] = ANSI_YELLOW + 0 + ANSI_RESET + " ";

        // Mise au format String du tableau pour un affichage

        for (int i = 0; i < tab_str.length; i++){
            for (int j = 0; j < tab_str.length; j++){
                str = str + tab_str[i][j];
            }
            str = str + "\n";
        }
        System.out.print(str);
    }
    public static int[] tour (int[][] tab){

        // Demande de la prochaine position du joueur dans la grille
        // Renvoie la position dans un tableau [x,y] quand le joueur envoie une position valide dans le tableau.

        Scanner sc = new Scanner(System.in);
        int position[] = new int [2];
        do {
            System.out.print("Où voulez-vous jouer (ligne) ? ");
            position[0] = sc.nextInt();
            System.out.print("Où voulez-vous jouer (colonne) ? ");
            position[1] = sc.nextInt();
        } while((position[0] < 1 || position[0] > tab.length) || (position[1] < 1 || position[1] > tab.length));
        position[0] = position[0] - 1;
        position[1] = position[1] - 1;
        return position;
    }    
    public static int[][] possible(int[][] tab, int[] position){

        // Permet de savoir les coordonnées possibles pour une position dans le tableau.

        // Compteur de position possible afin d'initialiser une liste avec le nombre exacte de donnée.

        int compteur = 0;
        for (int i = -1; i < 2; i++){
            for (int j = -1; j < 2; j++){
                if (!(i == 0 && j == 0)){
                    if ((position[0] + i >= 0 && position[0] + i < tab.length) && (position[1] + j >= 0 && position[1] + j < tab.length)){ 
                        compteur++;
                    }
                }
            }
        }
        int position_possible[][] = new int[compteur][2];
        int compteur2 = 0;

        // Création de la liste des coordonnées possibles sous la forme d'une liste

        for (int i = -1; i < 2; i++){
            for (int j = -1; j < 2; j++){
                if (!(i == 0 && j == 0)){
                    if (position[0] + i >= 0 && position[0] + i < tab.length && position[1] + j >= 0 && position[1] + j < tab.length){ 
                        position_possible[compteur2][0] = position[0] + i;
                        position_possible[compteur2][1] = position[1] + j;
                        compteur2++;
                    }
                }
            }
        }
        return position_possible;
    }
    public static int[][] create_tab_final(int l, int mine, int[] position){

        // Création du tableau de fin de jeu afin de permettre de savoir la localisation des mines

        Random rd = new Random();
        int tab_final[][] = initialisation(l);
        int possible[][] = possible(tab_final,position);
        tab_final[position[0]][position[1]] = 10;
        for (int elt[] : possible){
            tab_final[elt[0]][elt[1]] = 10;
        }
        for (int i = 0; i < mine; i++){
            int x, y;
            do {
                x = 0+rd.nextInt(l);
                y = 0+rd.nextInt(l);
            } while(tab_final[x][y] == 10 || tab_final[x][y] == -1);
            tab_final[x][y] = -1;
        }
        return tab_final;
    }
    public static int bomb_counter(int[][] tab_final, int[] position){

        // Renvoie le nombre de bombe se trouvant dans les cases a proximité

        int possible[][] = possible(tab_final, position);
        int counter = 0;
        for (int elt[] : possible){
            if (tab_final[elt[0]][elt[1]] == -1){
                counter++;
            }
        }
        return counter;
    }
    public static int[][] create_all_tab_final(int[][] tab_final){

        // Modification du tableau de fin de jeu avec les différentes cases numérotées

        int nb_bomb = 0;
        int position[] = new int[2];
        for (int i = 0; i < tab_final.length; i++){
            for (int j = 0; j < tab_final.length; j++){
                position[0] = i;
                position[1] = j;
                nb_bomb = 0;
                if (tab_final[i][j] == 0 || tab_final[i][j] == 10){
                    nb_bomb = bomb_counter(tab_final, position);
                    tab_final[position[0]][position[1]] = nb_bomb + 1;
                }
            }
        }
        return tab_final;
    }
    public static Boolean fini(int[][] tab_user, int m, int[][] tab_final){

        // Permet de savoir quand le démineur est fini
        // Renvoie true si le joueur a découvert toutes les cases sauf les bombes

        int mine = 0;
        for (int i = 0; i < tab_user.length; i++){
            for (int j = 0; j < tab_user.length; j++){
                if (tab_final[i][j] == -1 && tab_user[i][j] == 0){
                    mine++;
                }
                if (tab_user[i][j] == 0 && tab_final[i][j] != - 1){
                    return false;
                }
            }
        }
        if (mine == m){
            return true;
        }
        else {
            return false;
        }
    }
    public static int[][] claim_zone(int[][] tab_user, int[][] tab_final, int[] position){

        // Permet de créer une zone si toutes les cases ont 0 mine a proximité.

        int possible[][] = possible(tab_final, position);
        ArrayList<Integer> x = new ArrayList<Integer>();
        ArrayList<Integer> y = new ArrayList<Integer>();

        // Création d'une liste modulable avec les premières cases a proximité de la position donnée.

        for (int i = 0; i < possible.length; i++){
            if (tab_user[possible[i][0]][possible[i][1]] == 0){
                x.add(possible[i][0]);
                y.add(possible[i][1]);
            }
        }

        // Boucle tant que les positions possibles a proximité d'une case 0 ne sont pas toute vérifiée.

        while (x.size() > 0){
            if (tab_final[x.get(0)][y.get(0)] != 1){
                tab_user[x.get(0)][y.get(0)] = tab_final[x.get(0)][y.get(0)];
                x.remove(0);
                y.remove(0);
            } else {
                position[0] = x.get(0);
                position[1] = y.get(0);
                possible = possible(tab_final, position);
                for (int i = 0; i < possible.length; i++){
                    if (tab_user[possible[i][0]][possible[i][1]] == 0){
                        x.add(possible[i][0]);
                        y.add(possible[i][1]);
                    }
                }
                tab_user[x.get(0)][y.get(0)] = tab_final[x.get(0)][y.get(0)];
                x.remove(0);
                y.remove(0);
            }
        }
        return tab_user;
    }
    public static void play(int[][] tab_user, int[] difficulty, int[][] tab_final){

        // Fonction de jeu qui boucle tant que le jeu n'est pas fini ou que le joueur perd

        boolean defeat = false;
        do {
            int position[] = new int[2];
            do {
                affichage(tab_user);
                position = tour(tab_user);
                if (tab_user[position[0]][position[1]] != 0){
                    System.out.println("Emplacement non valide");
                }
            }  while (tab_user[position[0]][position[1]] != 0);
            tab_user[position[0]][position[1]] = tab_final[position[0]][position[1]];
            if (tab_user[position[0]][position[1]] == 1){
                tab_user = claim_zone(tab_user, tab_final, position);
            }
            if (tab_final[position[0]][position[1]] == -1){
                defeat = true;
            }
        } while(!(defeat) && !(fini(tab_user,difficulty[1] ,tab_final)));

        // Message de fin en cas de victoire ou de défaite

        if (defeat){
            affichage(tab_final);
            System.out.print("Vous avez perdu");
        }
        else {
            affichage(tab_final);
            System.out.print("Vous avez gagné, bravo !");
        }
    }
}