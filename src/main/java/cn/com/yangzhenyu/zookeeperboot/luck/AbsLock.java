package cn.com.yangzhenyu.zookeeperboot.luck;

import org.apache.zookeeper.KeeperException;

public abstract class AbsLock implements Lock {

    public static String lockName = "/myLock";

    @Override
    public void lock() {
        if (!tryLock()) {
            waitLock();
            lock();
        }
    }

    public abstract void waitLock();

    public abstract boolean tryLock();

    public abstract void unLock();
}
