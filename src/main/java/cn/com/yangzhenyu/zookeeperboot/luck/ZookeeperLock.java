package cn.com.yangzhenyu.zookeeperboot.luck;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZookeeperLock implements Lock {

    public static String root = "/lockRoot";

    protected ZooKeeper zooKeeper;

    private CountDownLatch countDownLatch = null;

    private String nodeKey = "";

    public ZookeeperLock(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    @Override
    public void lock() {
        try {
            Stat stat = zooKeeper.exists(root, false);
            if (stat == null){
                // 如果没有节点 创建根节点
                zooKeeper.create(root, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            // 创建子节点
            String lockTemp = root + "/temp";
            String myLockName = zooKeeper.create(lockTemp, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            nodeKey = myLockName;
            myLockName = myLockName.substring(root.length() + 1);

            List<String> list = zooKeeper.getChildren(root, false);
            Collections.sort(list);

            int i = list.indexOf(myLockName);
            if (i == 0) {
                // 放行
            } else if (list.size() > 1) {
                countDownLatch = new CountDownLatch(1);
                Stat stat1 = zooKeeper.exists(root + "/" + list.get(i - 1), event -> {
                    if (event.getType().equals(Watcher.Event.EventType.NodeDeleted) && event.getPath().equals(root + "/" + list.get(i - 1))) {
                        System.out.println("删除了:" + root + "/" + list.get(i - 1));
                        countDownLatch.countDown();
                    }
                });
                if (stat1 != null) countDownLatch.await();
            }

        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unLock() {
        try {
            zooKeeper.delete(nodeKey, -1);
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }

}
