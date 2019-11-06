import java.util.Random;
import java.util.ArrayList;
public class Cache {

    private int blocos;
    private int linhas;
    private boolean LFU;
    private Random gerador = new Random();
    private int vias;
    private int palavra_linha;
    private Memoria hierarquia;
    private int[][] memoria_associaiva;
    private Pair<Integer, Integer>[][] cache;
    

    public Cache(int tamanho, int palavra_linha, int tamanho_palavra, int vias, boolean LFU){
        this.LFU = LFU;
        this.blocos = tamanho / (palavra_linha * tamanho_palavra) / vias;
        this.palavra_linha = palavra_linha;
        this.linhas = tamanho / (palavra_linha * tamanho_palavra);
        this.vias = vias;
        cache = new Pair[linhas][palavra_linha + 1];
        for(int i = 0; i < linhas; i++){
            cache[i][0] = new Pair(0, 0);
        }
        memoria_associaiva = new int[this.blocos][vias];
        hierarquia = new Memoria();
    }

    private int tamPow(int variavel){
        double log = Math.log(variavel) / Math.log(2);
        int teto = (int) Math.ceil(log);
        return teto;
    }

    private int andNum(int variavel){
        int x = 1;
        int lengthNum = tamPow(variavel);
        for(int i = 1; i < lengthNum; i ++){
            x = x << 1;
            x = x | 1;
        }
        return x;
    }

    public Pair<Integer, Pair<Integer, Integer>> getInstrucao(int endereco) {

        int p = endereco & andNum(palavra_linha);
        int c = (endereco >> tamPow(palavra_linha)) & andNum(blocos);
        int tag = endereco >> (tamPow(palavra_linha) + tamPow(blocos));
        for(int j = 0; j < memoria_associaiva[c].length; j++){

            int tagAtual = memoria_associaiva[c][j];
            int linha_cache = (c * vias) + j;
            int bitValidade = cache[linha_cache][0].getA();
            
            if(tagAtual == tag &&  bitValidade == 1){
                // mundo feliz e contente aonde tudo é maravilhoso
                cache[linha_cache][0].setB(cache[linha_cache][0].getB() + 1); // cona o numero de vesez que a instrução foi usada
                return new Pair(0, cache[linha_cache][p + 1]);
            }
        }
        //caos e destruição, um lugar que ngm jamais quer que chegue
        return miss(endereco);
    }

    private int[] montaLinha(int endereco){
        int inicio = endereco / palavra_linha;
        inicio = inicio * palavra_linha;
        int[] resposta = new int[palavra_linha];
        for(int i = 0; i < palavra_linha; i++){
            resposta[i] = inicio + i;
        }
        return resposta;
    }

    private Pair<Integer, Pair<Integer, Integer>> miss(int endereco) throws IndexOutOfBoundsException {
        int[] enderecos_pedidos = montaLinha(endereco);
        Pair<Integer, ArrayList<Pair<Integer, Integer>>> enderecos_carregados = hierarquia.miss(enderecos_pedidos);
        int c = (endereco >> tamPow(palavra_linha)) & andNum(blocos);
        int tag = endereco >> (tamPow(palavra_linha) + tamPow(blocos));
        int inicio_bloco = c * vias;
        int fim_bloco = (c + 1) * vias;
        int menor = cache[inicio_bloco][0].getB();
        int linha_menor = inicio_bloco;
        for(int i = inicio_bloco; i < fim_bloco; i ++){
            if(cache[i][0].getA() == 0){
                salvaChace(c, i, inicio_bloco, tag, enderecos_carregados.getB());
                // linha possivel de dar merda
                return new Pair(enderecos_carregados.getA(), cache[i][(endereco % palavra_linha) + 1]);
            }

            if(cache[i][0].getB() < menor){
                linha_menor = i;
                menor = cache[i][0].getB();
            }
        }
        salvaChace(c, politica(linha_menor, c), inicio_bloco, tag, enderecos_carregados.getB());

        return new Pair(enderecos_carregados.getA(), enderecos_carregados.getB().get(endereco % palavra_linha));
    }

    private void salvaChace(int c, int i, int inicio_bloco, int tag, ArrayList<Pair<Integer, Integer>> instrucoes){
        memoria_associaiva[c][i - inicio_bloco] = tag;
        cache[i][0].setB(cache[i][0].getB() + 1);
        for(int j = 1; j < cache[i].length; j++){
            cache[i][j] = instrucoes.get(j - 1);
        }
        cache[i][0].setA(1);
        cache[i][0].setB(1);
    }

    private int politica(int i, int c){
        if(LFU){
            return i;
        }
        i = gerador.nextInt(vias);
        return c * vias + i;
    }

    public String toString(){
        String resposta = "+";
        for(int i = 0; i < cache[0].length * 10; i ++){
            resposta += "-";
        }
        resposta += "+ \n";
        for(int i = 0; i < linhas; i ++){
            for(int j = 0; j < cache[0].length; j ++){
                resposta += "| " + cache[i][j] + " ";
            }
            resposta += "|\n";
        }
        resposta += "+";
        for(int i = 0; i < cache[0].length * 10; i ++){
            resposta += "-";
        }
        resposta += "+";
        return resposta;
    }
}