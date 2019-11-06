import java.util.Random;
import java.util.Scanner;

public class Processador{
    public static void main(String[] args) {
        int tamanho = 128;
        int palavra_linha = 4;
        int tamanho_palavra = 4;
        int vias = 4;
        boolean LFU = false;
        Cache memoria = new Cache(tamanho, palavra_linha, tamanho_palavra, vias, LFU);
        int limInst = 2000; // limite de instruções a serem executadas, para evitar laços infinitos
        int instrucoes = 0;
        int atual = 0;
        int custo_total = 0;
        int total = 0;
        int hit = 0;
        int miss = 0;
        //for(int i = 0; i < 10000; i++){
            Random gerador = new Random();
            Scanner teclado = new Scanner(System.in);
            // simulando o programa
            try {
                while(instrucoes < limInst){
                    int probSalto = gerador.nextInt(100);
                    Pair<Integer, Pair<Integer,Integer>> instrucao = memoria.getInstrucao(atual);
                    System.out.print("instrução: " + atual + "  ");
                    System.out.println(instrucao.getA() == 0 ? "hit" : "miss");
                    System.out.println(memoria);
                    hit += instrucao.getA() == 0 ? 1 : 0;
                    miss += instrucao.getA() == 0 ? 0 : 1;
                    //teclado.nextLine();
                    custo_total += 1 + instrucao.getA();
                    if(probSalto < instrucao.getB().getB()){
                        atual = instrucao.getB().getA();
                    }else{
                        atual ++;
                    }
                    instrucoes ++;
                    
                }
            } catch (IndexOutOfBoundsException e) {
                
            }
            total += custo_total;
                System.out.printf("hits por instrução: %.2f\n", (float) hit / instrucoes);
                System.out.printf("miss por instrução: %.2f\n", (float) miss / instrucoes);
                System.out.println("custo total " + custo_total);
                System.out.println("custo medio " + custo_total / instrucoes);
                System.out.println("FIM! :)");
        /* }
         * String saida = LFU ? "LFU" : "Random";
         * System.out.println(saida);
         * System.out.println("hits: " + hit);
         * System.out.println("miss: " + miss);
         * System.out.println("Media de instruções em 10000 runs: " + total / instrucoes / 10000);
         */
    }
}