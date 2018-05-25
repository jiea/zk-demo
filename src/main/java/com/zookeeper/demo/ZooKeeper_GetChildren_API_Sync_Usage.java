package com.zookeeper.demo;

import org.apache.zookeeper.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZooKeeper_GetChildren_API_Sync_Usage implements Watcher {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    private static ZooKeeper zk = null;

    public static void main(String[] args) throws Exception {
        zk = new ZooKeeper("127.0.0.1:2181", 5000, new ZooKeeper_GetChildren_API_Sync_Usage());

        countDownLatch.await();

        String path = "/zk-book";
        String s = zk.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("Success Create: " + s);

        String s1 = zk.create(path + "/c1", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("Success Create: " + s1);

        List<String> children = zk.getChildren(path, true);
        System.out.println("Get Children: " + children);

        String s2 = zk.create(path + "/c2", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("Success Create: " + s2);

        Thread.sleep(Integer.MAX_VALUE);
    }


    @Override
    public void process(WatchedEvent watchedEvent) {
        // 因为连接成功通知和子节点变化通知都使用了这个Watcher, 所以增加判断是哪个通知
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            // 无任何事件通知
            if (Event.EventType.None == watchedEvent.getType() && watchedEvent.getPath() == null) {
                countDownLatch.countDown();
            } else if (Event.EventType.NodeChildrenChanged == watchedEvent.getType()) {
                // 子节点变化通知, 再次获取子节点
                try {
                    System.out.println("ReGet Children: " + zk.getChildren(watchedEvent.getPath(), true));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
