import java.io.File;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileNotFoundException;

public class Memoria{

        private ArrayList<Pair<Integer, Integer>> memoria = new ArrayList<>();
        private ArrayList<Pair<Integer, Integer>> hierarquia;
        private Random gerador = new Random();


        public Memoria(){
            loadMemoria();
            loadHierarquia();
        }

        public Pair<Integer, ArrayList<Pair<Integer, Integer>>> miss(int[] enderecos){
            int custo   = hierarquia.get(0).getA();
            int chance  = hierarquia.get(0).getB();
            int probHit = gerador.nextInt(100);
            if(probHit < chance){
                Pair<Integer, ArrayList<Pair<Integer, Integer>>> resposta = new Pair(0, new ArrayList<>());
                for(int i = 0; i < enderecos.length; i++){
                    resposta.getB().add(memoria.get(enderecos[i]));
                }
                return resposta;
            } 
            return miss(1, custo, enderecos);
        }

        private Pair<Integer, ArrayList<Pair<Integer, Integer>>> miss(int mem, int custo, int[] enderecos){
            custo      += hierarquia.get(mem).getA();
            int chance  = hierarquia.get(mem).getB();
            int probHit = gerador.nextInt(100);
            if(probHit < chance){
                Pair<Integer, ArrayList<Pair<Integer, Integer>>> resposta = new Pair(0, new ArrayList<>());
                for(int i = 0; i < enderecos.length; i++){
                    resposta.getB().add(memoria.get(enderecos[i]));
                }   
                return resposta;
            } 
            return miss(mem + 1, custo, enderecos);
        }

        public void loadHierarquia(){
            try{
                File arquivo = new File("hierarquia.txt");
                Scanner lerArquivo = new Scanner(arquivo);
                while (lerArquivo.hasNextLine()) {
                    String[] linha = lerArquivo.nextLine().split(":");
                    hierarquia.add(new Pair(Integer.parseInt(linha[1]), Integer.parseInt(linha[2])));
                }
                lerArquivo.close();
            } catch(FileNotFoundException e){
                System.out.println(e);
            }
        }

        public void loadMemoria(){ // lê o txt de entrada e carrega um map (memoria) com o tamanho do programa
        /*
         * map é de int para um par (tupla) de interos
         * o int é a posição de memoria da instrução
         * quanto ao par, o primeiro valor (A) é a instrução que pula, caso não seja uma instrução de salto 
         * é pulado para a instrução seguinte. O segundo valor do par (B) é qual a chance de pular, 
         * para todos os casos essa chance é 100, exceto para os saltos condicionais que tem um valor menor.
         */
        try{
            // abrindo arquivo para ser lido
            File arquivo = new File("programa.txt");
            Scanner lerArquivo = new Scanner(arquivo);
            /*
             * a primeira linha do programa deve informar qual endereço de memoria encerra o programa 
             * por exemplo: ep:100
             */
            int fimProg = Integer.parseInt(lerArquivo.nextLine().split(":")[1]);
            /*
             * enche o map com instruções normais 
             * exemplo {0=(1,100), 1=(2,100) 2=(3,100), ..., fimProg=(fimProg+1,100)}
             */
            for(int i = 0; i <= fimProg; i ++){ 
                Pair p = new Pair((i + 1), 100);
                memoria.add(i, p);
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
                Pair novoPar = new Pair(enderecoPulo, chance);
                memoria.add(endereco, novoPar);
            }
            lerArquivo.close();
        } catch(FileNotFoundException e){
            System.out.println(e);
        }
    }
}