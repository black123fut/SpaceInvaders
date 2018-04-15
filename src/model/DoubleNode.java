package model;

public class DoubleNode<T> implements NodeModel<T> {
    private T data;
    private DoubleNode<T> next;
    private DoubleNode<T> prev;

    /**
     * Contructor.
     * @param data Objeto que almacenara el nodo.
     */
    DoubleNode(T data){
        this(data, null, null);
    }

    /**
     * Constructor.
     * @param data Objeto que almacenara el nodo.
     * @param next Siguiente nodo.
     * @param prev nodo anterior.
     */
    DoubleNode(T data, DoubleNode<T> next, DoubleNode<T> prev){
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

    /**
     * Obtiene el objeto que almacena el nodo.
     * @return  El objeto del nodo.
     */
    public T getData(){
        return this.data;
    }

    /**
     * Da el valor del nodo.
     * @param data Valor del nodo.
     */
    @Override
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Obtiene el siguiente nodo.
     * @return El siguiente nodo.
     */
    DoubleNode<T> getNext(){
        return next;
    }

    /**
     * Da valor al siguiente nodo.
     * @param next Siguiente nodo.
     */
    void setNext(DoubleNode<T> next) {
        this.next = next;
    }

    /**
     * Da valor al nodo previo.
     * @param prev Valor del nodo previo.
     */
    void setPrev(DoubleNode<T> prev) {
        this.prev = prev;
    }


}
