package cn.com.yangzhenyu.zookeeperboot.luck;

import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseZookeeper {

    @Autowired
    private ZooKeeper zkClient;

    public ZooKeeper getZooKeeper() {
        return zkClient;
    }
}
