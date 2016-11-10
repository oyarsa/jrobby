package robby;

public class AGBuilder {

    private double taxaCruzamento = 1;
    private double taxaMutacao = 0.005;
    private int tamanhoPopulacao = 200;
    private int numeroGeracoes = 500;
    private int tamanhoTorneio = 4;
    private AG.Mutacao operadorMutacao = AG.Mutacao.VIZINHANCA;
    private AG.Cruzamento operadorCruzamento = AG.Cruzamento.UM_PONTO;
    private AG.Selecao metodoSelecao = AG.Selecao.TORNEIO;

    public AGBuilder() {
    }

    public AGBuilder setTaxaCruzamento(double taxaCruzamento) {
        this.taxaCruzamento = taxaCruzamento;
        return this;
    }

    public AGBuilder setTaxaMutacao(double taxaMutacao) {
        this.taxaMutacao = taxaMutacao;
        return this;
    }

    public AGBuilder setTamanhoPopulacao(int tamanhoPopulacao) {
        this.tamanhoPopulacao = tamanhoPopulacao;
        return this;
    }

    public AGBuilder setNumeroGeracoes(int numeroGeracoes) {
        this.numeroGeracoes = numeroGeracoes;
        return this;
    }

    public AGBuilder setTamanhoTorneio(int tamanhoTorneio) {
        this.tamanhoTorneio = tamanhoTorneio;
        return this;
    }

    public AGBuilder setOperadorMutacao(AG.Mutacao operadorMutacao) {
        this.operadorMutacao = operadorMutacao;
        return this;
    }

    public AGBuilder setOperadorCruzamento(AG.Cruzamento operadorCruzamento) {
        this.operadorCruzamento = operadorCruzamento;
        return this;
    }

    public AGBuilder setMetodoSelecao(AG.Selecao metodoSelecao) {
        this.metodoSelecao = metodoSelecao;
        return this;
    }

    public AG createAG() {
        return new AG(taxaCruzamento, taxaMutacao, tamanhoPopulacao, numeroGeracoes,
                tamanhoTorneio, operadorMutacao, operadorCruzamento, metodoSelecao);
    }

}
