package GameState;

import character.Enemy;
import javafx.scene.canvas.GraphicsContext;
import model.CircularNode;
import model.ListModel;


public abstract class GameStateManager {

    public abstract void update();
    public abstract void render(GraphicsContext g);

    void bubbleSort(ListModel<Enemy> EnemyList){
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

    void positionChanger(ListModel<Enemy> EnemyList, String type){
        int change = (int) (Math.random() * EnemyList.length()), indBoss = 0;

        for (int i = 0; i < EnemyList.length(); i++) {
            if (EnemyList.get(i).getID().equals("BOSS"))
                indBoss = i;
        }
        if (EnemyList.length() != 0){
            while (change == indBoss){
                change = (int) (Math.random() * EnemyList.length());
            }
        }
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

            if (lastX >= 540 - 55 * EnemyList.length())
                lastX = (540 - 55 * EnemyList.length()) - 1;

            if (EnemyList.length() == 1)
                EnemyList.remove(indBoss);

            else{
                GameState.score += EnemyList.get(indBoss).getScore();
                EnemyList.remove(indBoss);
                EnemyList.get(change).setImages(bossImages);
                EnemyList.get(change).setID("BOSS");

                int count = EnemyList.length();

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
