package GameState;

import character.Enemy;
import javafx.scene.canvas.GraphicsContext;
import model.CircularNode;
import model.ListModel;


public abstract class GameStateManager {

    public abstract void update();
    public abstract void render(GraphicsContext g);

    public void bubbleSort(ListModel<Enemy> EnemyList){
        for (int i = 0; i < EnemyList.length() - 1; i++) {
            for (int j = 0; j < EnemyList.length() - 1 - i; j++) {
                if (EnemyList.get(j).getLife() < EnemyList.get(j + 1).getLife()){
                    CircularNode<Enemy> node1 = EnemyList.getNode(j);
                    CircularNode<Enemy> node2 = EnemyList.getNode(j + 1);
                    CircularNode<Enemy> tmp = new CircularNode<>(node1.getData());

                    node1.setData(node2.getData());
                    node2.setData(tmp.getData());
                }
            }
        }
    }
}
