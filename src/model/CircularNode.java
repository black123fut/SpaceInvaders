package model;

public class CircularNode<T> implements NodeModel<T>{
    private T data;
    private CircularNode<T> next;

    /**
     * Constructor.
     * @param data Objeto que el nodo contenera.
     */
    public CircularNode(T data){
        this.data = data;
        this.next = this;
    }

    /**
     * Cambia el valor de data.
     * @param data Valor que se le dara.
     */
    public void setData(T data){
        this.data = data;
    }

    /**
     * Obtiene el contenido del nodo.
     * @return El contenido que almacena el nodo.
     */
    public T getData() {
        return data;
    }

    /**
     * Agrega el nodo siguiente a este.
     * @param next Nodo que sera el siguiente a este.
     */
    void setNext(CircularNode<T> next) {
        this.next = next;
    }

    /**
     * Obtiene el nodo siguiente.
     * @return El siguiente nodo.
     */
    CircularNode<T> getNext() {
        return next;
    }
}