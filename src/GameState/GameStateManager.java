package GameState;

import character.Enemy;
import javafx.scene.canvas.GraphicsContext;
import model.CircularNode;
import model.ListModel;
import model.NodeModel;


public abstract class GameStateManager {

    public abstract void update();
    public abstract void render(GraphicsContext g);

    /**
     * Acomoda los nodos de la lista de mayor a menor.
     * @param EnemyList La lista que se quiere acomodar.
     */
    void bubbleSort(ListModel<Enemy> EnemyList){
        for (int i = 0; i < EnemyList.length() - 1; i++) {
            for (int j = 0; j < EnemyList.length() - 1 - i; j++) {
                if (EnemyList.get(j).getLife() < EnemyList.get(j + 1).getLife()){
                    //Nodo del alien con mas resistencia
                    NodeModel<Enemy> node1 = EnemyList.getNode(j);
                    //Nodo del alien mas debil
                    NodeModel<Enemy> node2 = EnemyList.getNode(j + 1);
                    //Copia del nodo del alien mas resistente
                    CircularNode<Enemy> tmp = new CircularNode<>(node1.getData());

                    //Cambia los datos
                    node1.setData(node2.getData());
                    node2.setData(tmp.getData());
                }
            }
        }
    }

    /**
     * Al jefe lo cambia de posicion segun el tipo de hilera.
     * @param EnemyList La lista que se va a utilizar.
     * @param type El tipo de hilera.
     */
    void positionChanger(ListModel<Enemy> EnemyList, String type){
        int change = (int) (Math.random() * EnemyList.length()), indBoss = 0;

        //Busca la posicion del jefe
        for (int i = 0; i < EnemyList.length(); i++) {
            if (EnemyList.get(i).getID().equals("BOSS"))
                indBoss = i;
        }
        //Cambia change si es el mismo indecice del jefe
        if (EnemyList.length() != 0){
            while (change == indBoss){
                change = (int) (Math.random() * EnemyList.length());
            }
        }
        //Guarda los datos importantes que se ocupan para el cambio de posicion del jefe y otro alien.
        double Xchange = EnemyList.get(change).getX(),
                Ychange = EnemyList.get(change).getY(),
                iRchange = EnemyList.get(change).getiR(),
                iLchange = EnemyList.get(change).getiL();

        double Xboss = EnemyList.get(indBoss).getX()
                , Yboss = EnemyList.get(indBoss).getY()
                , iRboss = EnemyList.get(indBoss).getiR()
                , iLboss = EnemyList.get(indBoss).getiL();

        if (type.equals("ClaseC")){
            String[] bossImages = {"resources/AlienBoss1.png", "resources/AlienBoss2.png"};

            double lastX = EnemyList.get(0).getX();

            //Si toma un numero diferente al rango de la pantalla, lo cambia por uno que sea funcional.
            if (lastX >= 540 - 55 * EnemyList.length())
                lastX = (540 - 55 * EnemyList.length()) - 1;

            if (EnemyList.length() == 1)
                EnemyList.remove(indBoss);

            else{
                GameState.score += EnemyList.get(indBoss).getScore();
                EnemyList.remove(indBoss);
                //A un alien normal, lo convierte en jefe
                EnemyList.get(change).setImages(bossImages);
                EnemyList.get(change).setID("BOSS");

                int count = EnemyList.length();

                //Hace que la hilera de aliens se una
                for (int k = 0; k < EnemyList.length(); k++) {
                    EnemyList.get(k).setPosition(
                            lastX + 55 * k,
                            EnemyList.get(k).getY(),
                            count, k);
                    count--;
                }
            }
        }
        else {
            //Cambia la posicion del jefe con la de un alien normal, y la del alien normal con la del jefe.
            EnemyList.get(indBoss).setPosition(Xchange
                    ,Ychange
                    ,iRchange
                    ,iLchange);

            EnemyList.get(change).setPosition(Xboss
                    , Yboss
                    , iRboss
                    ,iLboss);
        }
    }
}
