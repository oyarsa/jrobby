package robby;

import java.util.Arrays;

/**
 *
 * @author Italo Silva
 */
public class Robby {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AG ag = new AGBuilder().setTaxaCruzamento(0.9).createAG();
        Solucao s = ag.resolver();
        System.out.println("FO: " + s.getFo());
        System.err.println("Cromossomo:" + Arrays.toString(s.getCromossomo()));
    }

}
