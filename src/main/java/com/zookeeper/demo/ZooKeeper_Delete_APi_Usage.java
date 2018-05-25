package com.zookeeper.demo;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

public class ZooKeeper_Delete_APi_Usage implements Watcher {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {

        ZooKeeper zookeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZooKeeper_Delete_APi_Usage());

        countDownLatch.await();

        // 异步删除
        zookeeper.delete("/zk-book", 0, (i, s, o) -> System.out.println("delete: " + i + ", " + s + "," + o), "ctx");

        // 同步删除, 如果删除不存在的接口会抛出异常
        // org.apache.zookeeper.KeeperException$NoNodeException: KeeperErrorCode = NoNode for /zk-book0000000001
        zookeeper.delete("/zk-book0000000001", 0);

        Thread.sleep(Integer.MAX_VALUE);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            countDownLatch.countDown();
        }
    }
}
