import java.io.File;
import java.util.Random;
import java.util.HashMap;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class BomDia{
    public static void main(String[] args) {
        HashMap<Integer, Pair<Integer, Integer>> programa = load(); // map com o nosso programa
        int limInst = 2000; // limite de instruções a serem executadas, para evitar laços infinitos
        Random gerador = new Random();
        // simulando o programa
        for(int k = 0, i = 0; i < programa.size() && k < limInst; k++){
            // gera um valor aleatório, até 100 para avaliar se vai saltar ou não
            int probSalto = gerador.nextInt(100);
            if(probSalto < programa.get(i).getB()){ // confere se o salto vai acontecer
                i = programa.get(i).getA(); // caso o salto ocorra, maioria das intruções, vai para a instrução que ta na tupla (A)
            } else{
                i ++; // caso o salto falhe, vai para instrução seguinte
            }
        }
    }

    public static HashMap<Integer, Pair<Integer, Integer>> load(){ // lê o txt de entrada e carrega um map (memoria) com o tamanho do programa
        HashMap<Integer, Pair<Integer, Integer>> mem = new HashMap<>();
        /*
         * map é de int para um par (tupla) de interos
         * o int é a posição de memoria da instrução
         * quanto ao par, o primeiro valor (A) é a instrução que pula, caso não seja uma instrução de salto 
         * é pulado para a instrução seguinte. O segundo valor do par (B) é qual a chance de pular, 
         * para todos os casos essa chance é 100, exceto para os saltos condicionais que tem um valor menor.
         */
        try{
            // abrindo arquivo para ser lido
            File arquivo = new File("input.txt");
            Scanner lerArquivo = new Scanner(arquivo);
            // a primeira linha do programa deve informar qual endereço de memoria encerra o programa 
            // por exemplo: ep:100
            int fimProg = Integer.parseInt(lerArquivo.nextLine().split(":")[1]);
            /*
             * enche o map com instruções normais 
             * exemplo {0=(1,100), 1=(2,100) 2=(3,100), ..., fimProg=(fimProg+1,100)}
             */
            for(int i = 0; i <= fimProg; i ++){ 
                Pair<Integer, Integer> p = new Pair((i + 1), 100);
                mem.put(i, p);
            }
            /*
             * percorre o resto do arquivo atualizando as instruções que são de saltos
             * exemplo {0=(1,100)..., 45=(51,10), ..., 50=(40,100), ..., fimProg=(fimProg+1,100)}
             */
            while (lerArquivo.hasNextLine()) {
                String[] linha = lerArquivo.nextLine().split(":");
                int endereco = Integer.parseInt(linha[1]);
                int enderecoPulo = Integer.parseInt(linha[2]);
                int chance = 100;
                if(linha[0].equals("bi")){
                    chance = Integer.parseInt(linha[3]);
                }
                Pair<Integer, Integer> novoPar = new Pair(enderecoPulo, chance);
                mem.replace(endereco, novoPar);
            }
            lerArquivo.close();
        } catch(FileNotFoundException e){
            System.out.println(e);
        }
        // retorna o map que é a memoria com o "programa" carregado
        return mem;
    }
}