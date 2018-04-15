package model;

public interface NodeModel<T> {

    /**
     * Obtiene el valor del nodo.
     * @return El valor del nodo.
     */
    T getData();

    /**
     * Asigna el valor al nodo.
     * @param data Valor del nodo.
     */
    void setData(T data);
}
