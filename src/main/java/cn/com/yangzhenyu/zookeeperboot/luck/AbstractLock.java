package cn.com.yangzhenyu.zookeeperboot.luck;

import org.apache.zookeeper.KeeperException;

public abstract class AbstractLock implements Lock {

    protected abstract void waitUnLock();

}
