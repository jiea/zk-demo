package com.zookeeper.demo;

import org.apache.zookeeper.*;
import sun.java2d.SurfaceDataProxy;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZooKeeper_Create_API_Sync_Usage implements Watcher {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZooKeeper_Create_API_Sync_Usage());

        countDownLatch.await();

        String path1 = zooKeeper.create("/zk-book", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        System.out.println("Success Create znode: " + path1);

        String path2 = zooKeeper.create("/zk-book", "456".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        System.out.println("Success Create znode: " + path2);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            countDownLatch.countDown();
        }

    }
}
