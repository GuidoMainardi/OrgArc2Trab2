public class Pair<T, E>{

    private T a;
    private E b;

    public Pair(T a, E b){
        this.a = a;
        this.b = b;
    }

    public T getA(){
        return a;
    }

    public E getB(){
        return b;
    }

    public void setA(T a){
        this.a = a;
    }

    public void setB(E b){
        this.b = b;
    }

    public String toString(){
        return "<" + a + ":" + b + ">";
    }
}