package robby;

public class AGBuilder {

    private double taxaCruzamento = 1;
    private double taxaMutacao = 0.005;
    private int tamanhoPopulacao = 200;
    private int numeroGeracoes = 50;
    private int tamanhoTorneio = 4;

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

    public AG createAG() {
        return new AG(taxaCruzamento, taxaMutacao, tamanhoPopulacao,
                numeroGeracoes, tamanhoTorneio);
    }

}
