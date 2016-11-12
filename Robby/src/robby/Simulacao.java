package robby;

import java.util.concurrent.ThreadLocalRandom;

public class Simulacao {

    private final static int PENALIDADE_PEGAR = 1;
    private final static int PENALIDADE_PAREDE = 5;
    private final static int PONTUACAO_PEGAR = 10;

    private final Tabuleiro tabuleiro;
    private int posicaoX;
    private int posicaoY;
    private int pontuacao;

    public Simulacao() {
        this.tabuleiro = Tabuleiro.novoAleatorio();
        this.posicaoX = 1;
        this.posicaoY = 1;
        this.pontuacao = 0;
    }

    private int getCenario() {
        int norte = tabuleiro.at(posicaoX, posicaoY - 1);
        int sul = tabuleiro.at(posicaoX, posicaoY + 1);
        int leste = tabuleiro.at(posicaoX + 1, posicaoY);
        int oeste = tabuleiro.at(posicaoX - 1, posicaoY);
        int atual = tabuleiro.at(posicaoX, posicaoY);

        return 81 * norte + 27 * sul + 9 * leste + 3 * oeste + atual;
    }

    public int executar(Cromossomo cromossomo) {
        for (int i = 0; i < Definicoes.NUMERO_ACOES; i++) {
            int cenario = getCenario();
            int movimento = cromossomo.at(cenario);
            proximoEstado(movimento);
        }

        return pontuacao;
    }

    private void moveNorte() {
        if (tabuleiro.at(posicaoX, posicaoY - 1) == Conteudos.PAREDE) {
            pontuacao -= PENALIDADE_PAREDE;
        } else {
            posicaoY--;
        }
    }

    private void moveSul() {
        if (tabuleiro.at(posicaoX, posicaoY + 1) == Conteudos.PAREDE) {
            pontuacao -= PENALIDADE_PAREDE;
        } else {
            posicaoY++;
        }
    }

    private void moveLeste() {
        if (tabuleiro.at(posicaoX + 1, posicaoY) == Conteudos.PAREDE) {
            pontuacao -= PENALIDADE_PAREDE;
        } else {
            posicaoX++;
        }
    }

    private void moveOeste() {
        if (tabuleiro.at(posicaoX - 1, posicaoY) == Conteudos.PAREDE) {
            pontuacao -= PENALIDADE_PAREDE;
        } else {
            posicaoX--;
        }
    }

    private void pegaLata() {
        if (tabuleiro.at(posicaoX, posicaoY) == Conteudos.LATA) {
            tabuleiro.set(posicaoX, posicaoY, Conteudos.VAZIO);
            pontuacao += PONTUACAO_PEGAR;
        } else {
            pontuacao -= PENALIDADE_PEGAR;
        }
    }

    private void moveAleatorio() {
        int movimento = ThreadLocalRandom.current().nextInt(4);
        proximoEstado(movimento);
    }

    private void proximoEstado(int movimento) {
        switch (movimento) {
            case Movimentos.MOVE_NORTE:
                moveNorte();
                break;
            case Movimentos.MOVE_SUL:
                moveSul();
                break;
            case Movimentos.MOVE_LESTE:
                moveLeste();
                break;
            case Movimentos.MOVE_OESTE:
                moveOeste();
                break;
            case Movimentos.MOVE_ALEATORIO:
                moveAleatorio();
                break;
            case Movimentos.FICAR_PARADO:
                break;
            case Movimentos.PEGAR_LATA:
                pegaLata();
                break;
        }
    }

}
