package robby;

import java.util.Random;

public class Tabuleiro {

    private static final int TAMANHO = 12;

    private final int[][] grid;

    private Tabuleiro() {
        grid = new int[TAMANHO][TAMANHO];
    }

    public static Tabuleiro novoAleatorio() {
        Tabuleiro novo = new Tabuleiro();
        Random rng = new Random();

        for (int i = 0; i < TAMANHO; i++) {
            novo.grid[0][i] = Conteudos.PAREDE;
            novo.grid[i][0] = Conteudos.PAREDE;
            novo.grid[i][TAMANHO - 1] = Conteudos.PAREDE;
            novo.grid[TAMANHO - 1][i] = Conteudos.PAREDE;
        }

        for (int i = 1; i < TAMANHO - 1; i++) {
            for (int j = 1; j < TAMANHO - 1; j++) {
                novo.grid[i][j] = rng.nextDouble() <= 0.5 ? 1 : 0;
            }
        }

        return novo;
    }

    public int at(int x, int y) {
        return this.grid[y][x];
    }

    public void printTabuleiro() {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                System.out.print("" + this.grid[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("-----------------------\n");
    }

    void set(int x, int y, int conteudo) {
        this.grid[y][x] = conteudo;
    }
}
