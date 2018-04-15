package model;

public class DoubleCircularNode<T> implements NodeModel<T> {
    private T data;
    private DoubleCircularNode<T> next;
    private DoubleCircularNode<T> prev;

    /**
     * Constructor.
     * @param data Objeto que guardara el nodo.
     */
    public DoubleCircularNode(T data){
        this.data = data;
        this.next = this;
        this.prev = this;
    }

    /**
     * Cambia el dato del nodo.
     * @param data Objeto al que se va a cambiar.
     */
    public void setData(T data){
        this.data = data;
    }

    /**
     * Obtiene el dato del nodo.
     * @return Dato del nodo.
     */
    public T getData(){
        return data;
    }

    /**
     * Asigna el siguiente nodo.
     * @param next El siguiente nodo.
     */
    public void setNext(DoubleCircularNode<T> next){
        this.next = next;
    }

    /**
     * Obtiene el siguiente nodo.
     * @return El siguinte nodo.
     */
    public DoubleCircularNode<T> getNext(){
        return next;
    }

    /**
     * Asigna el nodo anterior.
     * @param prev El nodo anterior.
     */
    public void setPrev(DoubleCircularNode<T> prev){
        this.prev = prev;
    }
}