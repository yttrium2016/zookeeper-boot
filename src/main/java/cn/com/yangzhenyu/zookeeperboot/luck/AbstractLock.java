package cn.com.yangzhenyu.zookeeperboot.luck;

import org.apache.zookeeper.KeeperException;

public abstract class AbstractLock implements Lock {

    /**
     * 重试
     */
    @Override
    public void lock() {
        if (tryLock()){
            System.out.println("获取锁");
        }else {
            System.out.println("等待锁");
            waitLuck();
            lock();
        }
    }

    protected abstract void waitLuck();

    protected abstract boolean tryLock();

}
