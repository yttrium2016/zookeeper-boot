package cn.com.yangzhenyu.zookeeperboot.luck;

import org.apache.zookeeper.KeeperException;

public interface Lock {

    void lock();

    void unLock() throws KeeperException, InterruptedException;
}
