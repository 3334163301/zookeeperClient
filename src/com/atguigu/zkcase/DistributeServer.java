package com.atguigu.zkcase;
import java.io.IOException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
/**
 * Created by Administrator on 2019/6/17.
 */
public class DistributeServer {

    private static String connectString =
            "hadoop102:2181,hadoop103:2181,hadoop104:2181";
    private static int sessionTimeout = 2000;
    private ZooKeeper zk = null;
    private String parentNode = "/servers";
    // 创建到 zk 的客户端连接
    public void getConnect() throws IOException{
        zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
            }
        });
    }
    // 注册服务器
    public void registServer(String hostname) throws Exception{
        //在这个节点parentNode + "/server"路径上注册的内容就是hostname.getBytes()
        String create = zk.create(parentNode + "/server", hostname.getBytes(),
                Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(hostname +" is noline "+ create);
    }
    // 业务功能
    public void business(String hostname) throws Exception{
        System.out.println(hostname+" is working ...");
        Thread.sleep(Long.MAX_VALUE);
    }
    public static void main(String[] args) throws Exception {
// 获取 zk 连接
        DistributeServer server = new DistributeServer();
        server.getConnect();
// 利用 zk 连接注册服务器信息
        server.registServer(args[0]);
        // 启动业务功能
        server.business(args[0]);
    }
}
