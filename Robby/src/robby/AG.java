package robby;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AG {

    public enum Mutacao {
        VIZINHANCA
    }

    public enum Cruzamento {
        UM_PONTO,
        MULTIPLOS_PONTOS,
        UNIFORME,
        SEGMENTADO
    }

    public enum Selecao {
        ROLETA,
        TORNEIO
    }

    private final double taxaMutacao;
    private final int tamanhoPopulacao;
    private final int numeroGeracoes;
    private final int tamanhoTorneio;
    private final int numeroCruzamentos;
    private final Mutacao operadorMutacao;
    private final Cruzamento operadorCruzamento;
    private final Selecao metodoSelecao;
    private final Random rng;

    public AG(double taxaCruzamento, double taxaMutacao, int tamanhoPopulacao,
            int numeroGeracoes, int tamanhoTorneio, Mutacao operadorMutacao,
            Cruzamento operadorCruzamento, Selecao metodoSelecao) {
        this.taxaMutacao = taxaMutacao;
        this.tamanhoPopulacao = tamanhoPopulacao;
        this.numeroGeracoes = numeroGeracoes;
        this.tamanhoTorneio = tamanhoTorneio;
        this.numeroCruzamentos = (int) (taxaCruzamento * tamanhoPopulacao) / 2;
        this.rng = new Random();

        this.operadorMutacao = operadorMutacao;
        this.operadorCruzamento = operadorCruzamento;
        this.metodoSelecao = metodoSelecao;
    }

    public Solucao resolver() {
        Solucao[] populacao = avaliar(gerarPopulacaoAleatoria());

        for (int i = 0; i < numeroGeracoes; i++) {
            Solucao[][] pais = selecionar(populacao);
            Cromossomo[] filhos = cruzamento(pais);
            mutacao(filhos);
            Solucao[] avaliados = avaliar(filhos);
            populacao = proximaGeracao(populacao, avaliados);
            System.out.println("Geracao " + (i + 1) + " FO: " + populacao[0].getFo());
        }

        return populacao[0];
    }

    private Cromossomo[] gerarPopulacaoAleatoria() {
        return Stream.generate(this::gerarSolucaoAleatoria)
                .limit(this.tamanhoPopulacao)
                .toArray(Cromossomo[]::new);
    }

    private Solucao[][] selecaoTorneio(Solucao[] populacao) {
        Solucao[][] pais = new Solucao[this.numeroCruzamentos][2];

        for (int i = 0; i < this.numeroCruzamentos; i++) {
            pais[i][0] = obterIndividuoTorneio(populacao);
            pais[i][1] = obterIndividuoTorneio(populacao);
        }

        return pais;
    }

    private Solucao[][] selecionar(Solucao[] populacao) {
        switch (metodoSelecao) {
            case ROLETA:
                return selecaoRoleta(populacao);
            case TORNEIO:
            default:
                return selecaoTorneio(populacao);
        }
    }

    private Solucao[][] selecaoRoleta(Solucao[] populacao) {
        Solucao[][] pais = new Solucao[this.numeroCruzamentos][2];
        double[] roleta = criarRoleta(populacao);

        for (int i = 0; i < this.numeroCruzamentos; i++) {
            pais[i][0] = populacao[obterIndiceRoleta(roleta)];
            pais[i][1] = populacao[obterIndiceRoleta(roleta)];
        }

        return pais;
    }

    private Cromossomo[] cruzamento(Solucao[][] pais) {
        Cromossomo[] filhos = new Cromossomo[this.numeroCruzamentos * 2];
        int i = 0;

        for (Solucao[] par : pais) {
            Cromossomo[] prole = cruzamento(par[0], par[1]);
            filhos[i] = prole[0];
            filhos[i + 1] = prole[1];
            i += 2;
        }

        return filhos;
    }

    private void mutacao(Cromossomo[] filhos) {
        for (Cromossomo filho : filhos) {
            if (rng.nextDouble() < this.taxaMutacao) {
                mutacao(filho);
            }
        }
    }

    private Solucao[] avaliar(Cromossomo[] cromossomos) {
        return Arrays.stream(cromossomos)
                .map(Solucao::new)
                .toArray(Solucao[]::new);
    }

    private Solucao[] proximaGeracao(Solucao[] geracaoAnterior, Solucao[] filhos) {
        Solucao[] proxima = new Solucao[this.tamanhoPopulacao];
        int numeroAncioes = this.tamanhoPopulacao - filhos.length;

        System.arraycopy(geracaoAnterior, 0, proxima, 0, numeroAncioes);
        System.arraycopy(filhos, 0, proxima, numeroAncioes, filhos.length);

        Arrays.sort(proxima, Collections.reverseOrder());

        return proxima;
    }

    private Cromossomo gerarSolucaoAleatoria() {
        int[] genes = IntStream.generate(() -> rng.nextInt(Movimentos.NUMERO_MOVIMENTOS))
                .limit(Cromossomo.TAMANHO)
                .toArray();
        return new Cromossomo(genes);
    }

    private Cromossomo[] cruzamentoUmPonto(Cromossomo pai1, Cromossomo pai2) {
        Cromossomo filho1 = new Cromossomo();
        Cromossomo filho2 = new Cromossomo();
        int pontoCruzamento = rng.nextInt(Cromossomo.TAMANHO);

        int i = 0;
        for (; i < pontoCruzamento; i++) {
            filho1.set(i, pai1.at(i));
            filho2.set(i, pai2.at(i));
        }
        for (; i < Cromossomo.TAMANHO; i++) {
            filho1.set(i, pai2.at(i));
            filho2.set(i, pai1.at(i));
        }

        return new Cromossomo[]{filho1, filho2};
    }

    // TODO
    private int[][] cruzamentoMultiplosPontos(Solucao pai1, Solucao pai2) {
        throw new UnsupportedOperationException();
    }

    // TODO
    private int[][] cruzamentoSegmentado(Solucao pai1, Solucao pai2) {
        throw new UnsupportedOperationException();
    }

    // TODO
    private int[][] cruzamentoUniforme(Solucao pai1, Solucao pai2) {
        throw new UnsupportedOperationException();
    }

    private void vizinhanca(Cromossomo filho) {
        int idx = rng.nextInt(Cromossomo.TAMANHO);
        filho.set(idx, rng.nextInt(Movimentos.NUMERO_MOVIMENTOS));
    }

    private double[] criarRoleta(Solucao[] populacao) {
        double[] aptidoes = normalizarPopulacao(populacao);
        double[] roleta = new double[this.tamanhoPopulacao];
        double aptidaoAcumulada = 0.0;

        for (int i = 0; i < this.tamanhoPopulacao; i++) {
            aptidaoAcumulada += aptidoes[i];
            roleta[i] = aptidaoAcumulada;
        }

        return roleta;
    }

    private int obterIndiceRoleta(double[] roleta) {
        // Gera um double entre [0; Ultima aptidão acumulada]
        double x = rng.nextDouble() * roleta[roleta.length - 1];

        for (int i = 0; i < roleta.length; i++) {
            if (roleta[i] > x) {
                return i;
            }
        }
        return -1;
    }

    private double[] normalizarPopulacao(Solucao[] populacao) {
        double min = populacao[populacao.length - 1].getFo();
        return Arrays.stream(populacao)
                .mapToDouble(s -> s.getFo() - min + 1)
                .toArray();
    }

    private Solucao obterIndividuoTorneio(Solucao[] populacao) {
        return Stream.generate(() -> populacao[rng.nextInt(this.tamanhoPopulacao)])
                .limit(this.tamanhoTorneio)
                .max(Comparator.naturalOrder())
                .get();
    }

    private Cromossomo[] cruzamento(Solucao pai1, Solucao pai2) {
        switch (this.operadorCruzamento) {
            case MULTIPLOS_PONTOS:
            case SEGMENTADO:
            case UNIFORME:
            case UM_PONTO:
            default:
                return cruzamentoUmPonto(pai1.getCromossomo(), pai2.getCromossomo());
        }
    }

    private void mutacao(Cromossomo filho) {
        switch (operadorMutacao) {
            case VIZINHANCA:
                vizinhanca(filho);
        }
    }
}
