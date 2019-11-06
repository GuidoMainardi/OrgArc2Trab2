import java.io.File;
import java.util.Random;
import java.util.HashMap;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class Processador{
    public static void main(String[] args) {
        int tamanho = 16;
        int palavra_linha = 2;
        int tamanho_palavra = 4;
        int vias = 2;
        boolean LFU = true;
        Cache memoria = new Cache(tamanho, palavra_linha, tamanho_palavra, vias, LFU);
        int limInst = 2000; // limite de instruções a serem executadas, para evitar laços infinitos
        int instrucoes = 0;
        int atual = 0;
        int custo_total = 0;
        Random gerador = new Random();
        Scanner teclado = new Scanner(System.in);
        // simulando o programa
        try {
            while(instrucoes < limInst){
                System.out.println(atual);
                //teclado.nextLine();
                int probSalto = gerador.nextInt(100);
                Pair<Integer, Pair<Integer,Integer>> instrucao = memoria.getInstrucao(atual);
                custo_total += 1 + instrucao.getFirst();
                if(probSalto < instrucao.getSecond().getSecond()){
                    atual = instrucao.getSecond().getFirst();
                }else{
                    atual ++;
                }
                instrucoes ++;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("custo total " + custo_total);
            System.out.println("custo medio " + custo_total / instrucoes);
            System.out.println("FIM! :)");
            teclado.close();
        }
    }
}