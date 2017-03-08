package org.roy;

/**
 * Created by kanglewang on 17/3/8.
 */

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.*;
import com.rabbitmq.client.AMQP.Queue.DeclareOk;

public class Producer {

    private final static String QUEUE_NAME = "UserLogin2";
    private final static String EXCHANGE_NAME = "user.login";

    /**
     * @param args
     */
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("CNCDS108");
        try {
            Connection conn = factory.newConnection();
            Channel channel = conn.createChannel();
            DeclareOk declareOk = channel.queueDeclare(QUEUE_NAME, true, true, false, null);

            channel.basicPublish("", QUEUE_NAME, null, "Hello".getBytes());

            //close the channel, check if the queue is deleted
            System.out.println("Try to close channel");
            channel.close();
            System.out.println("Channel closed");

            System.out.println("Create a new channel");
            Channel channel2 = conn.createChannel();
            DeclareOk declareOk2 = channel2.queueDeclarePassive(QUEUE_NAME);

            //we can access the exclusive queue from another channel
            System.out.println(declareOk2.getQueue()); //will output "UserLogin2"
            channel2.basicPublish("", QUEUE_NAME, null, "Hello2".getBytes());
            System.out.println("Message published through the new channel");

//            System.out.println("Try to close Connection");
//            conn.close();
//            System.out.println("Connection closed");


        } catch (IOException  | TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}