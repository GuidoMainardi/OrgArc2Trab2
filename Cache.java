public class Cache {
    private Pair<Integer, Integer>[][] cache;
    private int[][] memoria_associaiva;
    private int vias;
    private int palavra_bloco;
    private int linhas;
    private Memoria hierarquia;
    

    public Cache(int tamanho, int palavra_bloco, int tamanho_palavra, int vias){
        this.vias = vias;
        this.palavra_bloco = palavra_bloco;
        this.linhas = tamanho / (palavra_bloco * tamanho_palavra);
        cache = (Pair<Integer, Integer>[][]) new Object[linhas][palavra_bloco + 1];
        memoria_associaiva = new int[vias][linhas / vias];
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

        int p = endereco & andNum(palavra_bloco);
        int c = (endereco >> tamPow(palavra_bloco)) & andNum(vias);
        int tag = endereco >> (tamPow(palavra_bloco) + tamPow(vias));

        for(int j = 0; j < memoria_associaiva[c].length; j++){

            int tagAtual = memoria_associaiva[c][j];
            int linha_cache = (c * (linhas / vias)) + j;
            int bitValidade = cache[linha_cache][0].getA();

            if(tagAtual == tag &&  bitValidade == 1){
                // mundo feliz e contente aonde tudo é maravilhoso
                return new Pair(0, cache[linha_cache][p]);
            }
        }
        //caos e destruição, um lugar que ngm jamais quer que chegue
        return miss(endereco);
    }

    private Pair<Integer, Pair<Integer, Integer>> miss(int endereco){ // fazer o miss
        return null;
    }
}