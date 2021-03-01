package cn.com.yangzhenyu.zookeeperboot.zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class App {
    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        String connStr = "ddns.yangzhenyu.com.cn:2181";
        CountDownLatch countDown = new CountDownLatch(1);

        Watcher watcher=new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    System.err.println("eventType:"+event.getType());
                    if(event.getType()==Event.EventType.None){
                        countDown.countDown();
                    }else if(event.getType()==Event.EventType.NodeCreated){
                        System.out.println("listen:节点创建");
                    }else if(event.getType()==Event.EventType.NodeChildrenChanged){
                        System.out.println("listen:子节点修改");
                    }
                }
            }
        };

        ZooKeeper zookeeper = new ZooKeeper(connStr, 5000,watcher );
        countDown.await();



        zookeeper.exists("/h1", watcher);

        while (true);
    }
}
