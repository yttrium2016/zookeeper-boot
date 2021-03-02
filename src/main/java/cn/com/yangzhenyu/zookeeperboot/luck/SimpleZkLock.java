package cn.com.yangzhenyu.zookeeperboot.luck;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class SimpleZkLock extends AbsLock {

    private ZooKeeper zooKeeper;

    private CountDownLatch waitLatch = null;

    public SimpleZkLock(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    @Override
    public void waitLock() {
        try {
            waitLatch = new CountDownLatch(1);
            Stat stat = zooKeeper.exists(lockName, event -> {
                if (event.getType() == Watcher.Event.EventType.NodeDeleted && event.getPath().equals(lockName)) {
                    System.out.println("删除了:"+lockName);
                    waitLatch.countDown();
                }
            });
            if (stat != null) {
                waitLatch.await();
            }
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean tryLock() {
        try {
            zooKeeper.create(lockName, lockName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void unLock() {
        try {
            zooKeeper.delete(lockName, -1);
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }
}
