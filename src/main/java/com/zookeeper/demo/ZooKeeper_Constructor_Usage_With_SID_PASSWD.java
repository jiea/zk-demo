package com.zookeeper.demo;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZooKeeper_Constructor_Usage_With_SID_PASSWD implements Watcher {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    private static final String addr = "127.0.0.1:2181";
    private static final int timeout = 5000;

    public static void main(String[] args) throws IOException {
        ZooKeeper zooKeeper = new ZooKeeper(addr, timeout, new ZooKeeper_Constructor_Usage_With_SID_PASSWD());

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long sessionId = zooKeeper.getSessionId();
        byte[] sessionPasswd = zooKeeper.getSessionPasswd();

        // 错误的sessionId and sessionPasswd
        zooKeeper = new ZooKeeper(addr, timeout, new ZooKeeper_Constructor_Usage_With_SID_PASSWD(), 1, "test".getBytes());

        // 正确的sessionId and sessionPasswd
        zooKeeper = new ZooKeeper(addr, timeout, new ZooKeeper_Constructor_Usage_With_SID_PASSWD(), sessionId, sessionPasswd);

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("Receive watched: " + watchedEvent);
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()){
            countDownLatch.countDown();
        }
    }
}
