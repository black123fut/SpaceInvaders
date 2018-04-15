package model;

public class LinkedList<T> implements ListModel<T>{
    private Node<T> head;
    private int size;
    private String type;

    /**
     * Construtor
     */
    public LinkedList(){
        head = null;
    }

    /**
     * Agrega un nuevo nodo al final de la lista.
     * @param item Valor del nuevo nodo.
     */
    public void add(T item){
        if (head == null)
            head = new Node<>(item, null);
        else{
            Node tmp = head;
            while(tmp.next != null)
                tmp = tmp.next;
            tmp.next = new Node<>(item, null);
        }
        size++;
    }

    /**
     * Elimina un nodo de la lista.
     * @param index Indice del nodo.
     */
    public void remove(int index) {
        try{
            Node tmp = head;
            int counter = 0;

            if (head == null){
                return;
            }

            if (index == 0) {
                head = head.next;
                size--;
                return;
            }

            counter++;

            while (tmp.next != null) {
                if (counter == index) {
                    tmp.next = tmp.next.next;
                    size--;
                    return;
                }
                else{
                    tmp = tmp.next;
                    counter++;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Obtiene el largo de la lista.
     * @return El largo de la lista.
     */
    public int length(){
        return size;
    }

    /**
     * Obtiene el valor del nodo.
     * @param indice Indice del nodo.
     * @return El valor del nodo.
     */
    public T get(int indice){
        Node<T> tmp = head;
        int cont = 0;

        while (tmp != null){
            if (cont == indice) {
                return tmp.data;
            }

            cont++;
            tmp = tmp.next;
        }
        return null;
    }

    /**
     * Asigna el tipo de la lista.
     * @param type Tipo de la lista.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Obtiene el valor de la lista.
     * @return El valor de la lista.
     */
    public String getType(){
        return type;
    }

    @Override
    public CircularNode<T> getNode(int index) {
        return null;
    }
}
