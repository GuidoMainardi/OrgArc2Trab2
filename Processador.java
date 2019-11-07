import java.util.Random;
import java.util.Scanner;

public class Processador{
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        System.out.println("Qual o tamanho da cache (em Bytes)? ");
        int tamanho = teclado.nextInt();
        System.out.println("Quantas palavras tem em cada linha da cache? ");
        int palavra_linha = teclado.nextInt();
        System.out.println("Qual o tamanho da palavra (em Bytes)? ");
        int tamanho_palavra = teclado.nextInt();
        System.out.println("Qual o numero de vias? ");
        int vias = teclado.nextInt();
        System.out.println("Você deseja usar a politica LFU [L] ou Random [R] ?");
        String respostas = teclado.next();
        boolean LFU = respostas.equalsIgnoreCase("R") ? false : true;
        Cache memoria = new Cache(tamanho, palavra_linha, tamanho_palavra, vias, LFU);
        int limInst = 2000; // limite de instruções a serem executadas, para evitar laços infinitos
        int instrucoes = 0;
        int atual = 0;
        int custo_total = 0;
        int hit = 0;
        int miss = 0;
        Random gerador = new Random();
        // simulando o programa
        try {
            while(instrucoes < limInst){
                int probSalto = gerador.nextInt(100);
                Pair<Integer, Pair<Integer,Integer>> instrucao = memoria.getInstrucao(atual);
                System.out.print("instrução: " + atual + "  ");
                System.out.println(instrucao.getFirst() == 0 ? "hit" : "miss");
                System.out.println(memoria);
                hit += instrucao.getFirst() == 0 ? 1 : 0;
                miss += instrucao.getFirst() == 0 ? 0 : 1;
                teclado.nextLine();
                custo_total += 1 + instrucao.getFirst();
                if(probSalto < instrucao.getSecond().getSecond()){
                    atual = instrucao.getSecond().getFirst();
                }else{
                    atual ++;
                }
                instrucoes ++;
                
            }
        } catch (IndexOutOfBoundsException e) {
            
        }
        System.out.printf("hits por instrução: %.2f\n", (float) hit / instrucoes);
        System.out.printf("miss por instrução: %.2f\n", (float) miss / instrucoes);
        System.out.println("custo total " + custo_total);
        System.out.println("custo medio " + custo_total / instrucoes);
        System.out.println("FIM! :)");
    }
}