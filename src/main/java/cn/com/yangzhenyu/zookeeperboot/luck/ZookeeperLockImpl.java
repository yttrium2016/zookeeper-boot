package cn.com.yangzhenyu.zookeeperboot.luck;

import org.apache.zookeeper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class ZookeeperLockImpl extends AbstractLock implements Watcher {

    public static String luckPath = "/luckPath";

    @Autowired
    protected ZooKeeper zkClient;

    private CountDownLatch waitLatch = null;

    @Override
    protected void waitLuck() {
        waitLatch = new CountDownLatch(1);
        try {
            zkClient.exists(luckPath, this);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            waitLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean tryLock() {
        try {
            zkClient.create(luckPath, "luck".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void unLock() {
        try {
            zkClient.delete(luckPath, -1);
            System.out.println("解除锁");
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getType() == Watcher.Event.EventType.NodeDeleted) {
            if (luckPath.equals(event.getPath())) {
                System.out.println("OK DELETE");
                if (waitLatch != null) waitLatch.countDown();
            }
        }
    }
}
