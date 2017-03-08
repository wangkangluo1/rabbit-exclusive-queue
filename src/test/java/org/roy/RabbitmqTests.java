package org.roy;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by kanglewang on 17/3/8.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqTests {

    @Test
    public void produceTest() throws Exception{
        //创建链接工厂
        ConnectionFactory connFac = new ConnectionFactory() ;

        //默认链接的主机名,RabbitMQ-Server安装在本机，所以可以直接用127.0.0.1
        connFac.setHost("127.0.0.1");

        //创建链接
        Connection conn = connFac.newConnection() ;

        //创建信息管道
        Channel channel = conn.createChannel() ;

        // 创建一个名为queue01的队列，防止队列不存在
        String queueName = "queue01" ;

        //进行信息声明        1.队列名2.是否持久化，3是否局限与链接，4不再使用是否删除，5其他的属性
        channel.queueDeclare(queueName, false, false, false, null) ;
        String msg = "Hello World!";

        //发送消息
        // 在RabbitMQ中，消息是不能直接发送到队列，它需要发送到交换器（exchange）中。
        // 第一参数空表示使用默认exchange，第二参数表示发送到的queue，第三参数是发送的消息是（字节数组）
        channel.basicPublish("", queueName , null , msg.getBytes());

        System.out.println("发送  message[" + msg + "] to "+ queueName +" success!");

        //关闭管道
        channel.close();
        //关闭连接
        conn.close();
    }

    /**
     * 关闭后自动删除
    */
    @Test
    public void produceADTest() throws Exception{
        //创建链接工厂
        ConnectionFactory connFac = new ConnectionFactory() ;

        //默认链接的主机名,RabbitMQ-Server安装在本机，所以可以直接用127.0.0.1
        connFac.setHost("127.0.0.1");

        //创建链接
        Connection conn = connFac.newConnection() ;

        //创建信息管道
        Channel channel = conn.createChannel() ;

        // 创建一个名为queue01的队列，防止队列不存在
        String queueName = "queue02" ;

        //进行信息声明        1.队列名2.是否持久化，3是否局限与链接，4不再使用是否删除，5其他的属性
        channel.queueDeclare(queueName, false, false, true, null) ;
        String msg = "Hello World!";

        //发送消息
        // 在RabbitMQ中，消息是不能直接发送到队列，它需要发送到交换器（exchange）中。
        // 第一参数空表示使用默认exchange，第二参数表示发送到的queue，第三参数是发送的消息是（字节数组）
        channel.basicPublish("", queueName , null , msg.getBytes());

        System.out.println("发送  message[" + msg + "] to "+ queueName +" success!");

        //关闭管道
        channel.close();
        //关闭连接
        conn.close();
    }

    /**
     * 排他队列
     */
    @Test
    public void produceEXTest() throws Exception{
        //创建链接工厂
        ConnectionFactory connFac = new ConnectionFactory() ;

        //默认链接的主机名,RabbitMQ-Server安装在本机，所以可以直接用127.0.0.1
        connFac.setHost("127.0.0.1");

        //创建链接
        Connection conn = connFac.newConnection() ;

        //创建信息管道
        Channel channel = conn.createChannel() ;

        // 创建一个名为queue01的队列，防止队列不存在
        String queueName = "queue03" ;

        //进行信息声明        1.队列名2.是否持久化，3是否局限与链接，4不再使用是否删除，5其他的属性
        channel.queueDeclare(queueName, false, true, true, null) ;
        String msg = "Hello World!";

        //发送消息
        // 在RabbitMQ中，消息是不能直接发送到队列，它需要发送到交换器（exchange）中。
        // 第一参数空表示使用默认exchange，第二参数表示发送到的queue，第三参数是发送的消息是（字节数组）
        channel.basicPublish("", queueName , null , msg.getBytes());

        System.out.println("发送  message[" + msg + "] to "+ queueName +" success!");

        //关闭管道
        channel.close();
        //关闭连接
        conn.close();
    }

    @Test
    public void consumerTest() throws Exception{
        // 创建链接工厂
        ConnectionFactory connFac = new ConnectionFactory() ;

        //默认链接的主机名,RabbitMQ-Server安装在本机，所以可以直接用127.0.0.1
        connFac.setHost("127.0.0.1");

        //创建链接
        Connection conn = connFac.newConnection() ;

        //创建信息管道
        Channel channel = conn.createChannel() ;

        //定义Queue名称
        String queueName = "queue02";
        //1.队列名 2.是否持久化，3是否排他，4不再使用是否删除，5其他的属性
        channel.queueDeclare(queueName, false, true, true, null) ;

        //上面的部分，与Test01是一样的

        //声明一个消费者,配置好获取消息的方式
        QueueingConsumer consumer = new QueueingConsumer(channel) ;
        channel.basicConsume(queueName, true, consumer) ;

        //循环获取消息
        while(true){

            //循环获取信息
            //指向下一个消息，如果没有会一直阻塞
            QueueingConsumer.Delivery delivery = consumer.nextDelivery() ;

            String msg = new String(delivery.getBody()) ;

            System.out.println("接收 message[" + msg + "] from " + queueName);
        }
    }
}
