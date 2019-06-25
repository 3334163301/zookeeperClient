package com.atguigu.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by Administrator on 2019/6/15.
 */
public class TestZookeeper {
    private static String connectString = "hadoop102:2181,hadoop103:2181,hadoop104:2181";
    private static int sessionTimeout = 2000;
    private ZooKeeper zkClient = null;
    @Before
    public void init() throws Exception {
        /*连接集群并设置监听器内部类---监听集群*/
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
//process回调方法可以进行反复监听
            public void process(WatchedEvent event) {
// 收到事件通知后的回调函数（用户的业务逻辑）
                System.out.println(event.getType() + "--" + event.getPath());

// 再次启动监听
                try {
                    Stat stat = zkClient.exists("/eclipse", true);
                   // zkClient.getChildren("/", true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    // 创建子节点
    @Test
    public void create() throws Exception {
// 数据的增删改查
// 参数 1：要创建的节点的路径； 参数 2：节点数据 ； 参数 3：节点权限 ；参数 4：节点的类型
        String nodeCreated = zkClient.create("/eclipse", "hello zk".getBytes(),
               ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    // 获取子节点
    @Test
    public void getChildren() throws Exception {
        //true表示注册一次监听事件，监听时会调用初始化连接 init方法中的监听器
        List<String> children = zkClient.getChildren("/", true);
        for (String child : children) {
            System.out.println(child);
        }
// 延时阻塞
        Thread.sleep(Long.MAX_VALUE);
    }
    // 判断 znode 是否存在
    @Test
    public void exist() throws Exception {
        //不启动监听
    Stat stat = zkClient.exists("/eclipse", true);
System.out.println(stat == null ? "*****not exist*****" : "######exist######");
        //System.out.println(stat);
        // 延时阻塞
        Thread.sleep(Long.MAX_VALUE);
}
}
