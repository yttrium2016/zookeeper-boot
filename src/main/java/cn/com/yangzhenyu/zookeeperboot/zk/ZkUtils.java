package cn.com.yangzhenyu.zookeeperboot.zk;

import org.apache.zookeeper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ZkUtils {

    @Autowired
    protected ZooKeeper zkClient;

    public void createTemp(String path, String value) {
        try {
            zkClient.create(path, value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(String path) {
        try {
            zkClient.delete(path, -1);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean exists(String path) {
        try {
            zkClient.exists(path, event -> {
                System.out.println("XXX");
                if (event.getType() == Watcher.Event.EventType.NodeDeleted) {
                    System.out.println("OK DELETE");
                }
            });
            return true;
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
