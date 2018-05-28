package com.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class Create_Node_Simple {

    static String path = "/zk-book/c1";

    static CuratorFramework zkClient = CuratorFrameworkFactory.builder()
            .connectString("127.0.0.1:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();

    public static void main(String[] args) throws Exception {
        // 建立连接
        zkClient.start();

        // 创建节点
        String s = zkClient.create().creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path, "init".getBytes());
        System.out.println(s);

        // 节点是否存在
        Stat stat2 = zkClient.checkExists().forPath(path);
        System.out.println(stat2);

        // 获取数据
        Stat stat = new Stat();
        byte[] bytes = zkClient.getData().storingStatIn(stat).forPath(path);
        System.out.println(new String(bytes) + "   " + stat.getVersion());

        // 更新数据
        Stat stat1 = zkClient.setData().withVersion(stat.getVersion()).forPath(path, "123".getBytes());
        System.out.println(stat1.getDataLength() + "      " + stat1.getVersion());

        // 删除节点
        zkClient.delete().deletingChildrenIfNeeded().withVersion(-1).forPath("/zk-book");

    }
}
