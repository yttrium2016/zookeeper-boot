package cn.com.yangzhenyu.zookeeperboot.controller;

import cn.com.yangzhenyu.zookeeperboot.luck.ZookeeperLockImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;

@RestController
public class TestController {

    @Autowired
    private ZookeeperLockImpl zookeeperLock;

    private int sum = 0;

    @RequestMapping("get")
    public Object get() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);

        for (int i = 0; i < 3; i++) {
            new Thread(()->{
                zookeeperLock.lock();
                for (int j = 0; j < 10000; j++) {
                    sum++;
                }
                countDownLatch.countDown();
                zookeeperLock.unLock();
            }).start();
        }

        countDownLatch.await();
        System.out.println(sum);
        return sum;
    }
}
