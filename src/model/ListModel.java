package model;

public interface ListModel<T> {

    /**
     * Agrega un nodo al final de la lista.
     * @param data Valor del nuevo nodo.
     */
    void add(T data);

    /**
     * Elimina un nodo.
     * @param index Indice del nodo.
     */
    void remove(int index);

    /**
     * Obtiene el valor del nodo buscado.
     * @param index Indice del nodo.
     * @return El valor del nodo.
     */
    T get(int index);

    /**
     * Obtiene el largo de la lista.
     * @return El largo de la lista.
     */
    int length();

    /**
     * Asigna el tipo de hilera.
     * @param type Tipo de hilera.
     */
    void setType(String type);

    /**
     * Obtiene el tipo de la hilera.
     * @return El tipo de la hilera.
     */
    String getType();

    /**
     * Obtiene el nodo buscado.
     * @param index Indice edel nodo.
     * @return El nodo.
     */
    NodeModel<T> getNode(int index);
}
