package cn.com.yangzhenyu.zookeeperboot.controller;

import cn.com.yangzhenyu.zookeeperboot.luck.SimpleZkLock;
import cn.com.yangzhenyu.zookeeperboot.luck.ZookeeperLock;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;

@RestController
public class TestController {


    @Autowired
    protected ZooKeeper zkClient;

    private int sum = 0;

    @RequestMapping("get")
    public Object get() throws InterruptedException {
        sum = 0;
        long start = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(300);

        for (int i = 0; i < 300; i++) {
            new Thread(() -> {
                ZookeeperLock zookeeperLock = new ZookeeperLock(zkClient);
//                SimpleZkLock zookeeperLock = new SimpleZkLock(zkClient);
                zookeeperLock.lock();
                for (int j = 0; j < 100; j++) {
                    sum++;
                }
                countDownLatch.countDown();
                zookeeperLock.unLock();
            }).start();
        }

        countDownLatch.await();
        System.out.println(sum);
        return "结果:" + sum + ",时间:" + (System.currentTimeMillis() - start);
    }

    @RequestMapping("get2")
    public Object get2() throws InterruptedException {
        sum = 0;
        long start = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(300);

        for (int i = 0; i < 300; i++) {
            new Thread(() -> {
//                ZookeeperLockImpl zookeeperLock = new ZookeeperLockImpl(zkClient);
                SimpleZkLock zookeeperLock = new SimpleZkLock(zkClient);
                zookeeperLock.lock();
                for (int j = 0; j < 100; j++) {
                    sum++;
                }
                countDownLatch.countDown();
                zookeeperLock.unLock();
            }).start();
        }

        countDownLatch.await();
        System.out.println(sum);
        return "结果:" + sum + ",时间:" + (System.currentTimeMillis() - start);
    }

    @RequestMapping("get3")
    public Object get3() throws InterruptedException {
        sum = 0;
        long start = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(300);

        for (int i = 0; i < 300; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    sum++;
                }
                countDownLatch.countDown();
            }).start();
        }

        countDownLatch.await();
        System.out.println(sum);
        return "结果:" + sum + ",时间:" + (System.currentTimeMillis() - start);
    }
}
