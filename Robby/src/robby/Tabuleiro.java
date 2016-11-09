package robby;

import java.util.Random;

public class Tabuleiro {

    private int[][] grid;

    private Tabuleiro() {
        grid = new int[Definicoes.TAMANHO_GRID][Definicoes.TAMANHO_GRID];
    }

    public static Tabuleiro novoAleatorio() {
        Tabuleiro novo = new Tabuleiro();
        Random rng = new Random();

        for (int i = 0; i < Definicoes.TAMANHO_GRID; i++) {
            novo.grid[0][i] = Definicoes.PAREDE;
            novo.grid[i][0] = Definicoes.PAREDE;
            novo.grid[i][Definicoes.TAMANHO_GRID - 1] = Definicoes.PAREDE;
            novo.grid[Definicoes.TAMANHO_GRID - 1][i] = Definicoes.PAREDE;
        }

        for (int i = 1; i < Definicoes.TAMANHO_GRID - 1; i++) {
            for (int j = 1; j < Definicoes.TAMANHO_GRID - 1; j++) {
                novo.grid[i][j] = Math.round(rng.nextFloat());
            }
        }

        return novo;
    }

    public int at(int i, int j) {
        return this.grid[i][j];
    }

    public void printTabuleiro() {
        for (int i = 0; i < Definicoes.TAMANHO_GRID; i++) {
            for (int j = 0; j < Definicoes.TAMANHO_GRID; j++) {
                System.out.print("" + this.grid[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("-----------------------\n");
    }
}