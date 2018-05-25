package com.zookeeper.demo;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

public class ZooKeeper_Create_API_ASync_Usage implements Watcher {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeper zookeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZooKeeper_Create_API_ASync_Usage());

        countDownLatch.await();

        zookeeper.create("/zk-book", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, new StringCallback(), "ctx");

        zookeeper.create("/zk-book", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, new StringCallback(), "ctx");

        zookeeper.create("/zk-book", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, new StringCallback(), "ctx");

        Thread.sleep(Integer.MAX_VALUE);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            countDownLatch.countDown();
        }
    }

}

class StringCallback implements AsyncCallback.StringCallback {

    @Override
    public void processResult(int i, String s, Object o, String s1) {
        System.out.println("Create path result: [" + i + ", " + s + ", " + o + ", " + "real path name: " + s1);
    }
}