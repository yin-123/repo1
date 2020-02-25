package com.xiaoshu.test;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

public class TestQueueConsumer {

   @Test
    public void test01(){

        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.109.129:61616");
            Connection connection = connectionFactory.createConnection();
            //启动连接
            connection.start();
            //参数一代表是否启动事务，参数2代表消息确认模式
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //创建消息队列对象
            Queue queue = session.createQueue("test");
            MessageConsumer consumer = session.createConsumer(queue);
            //监听消息
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    TextMessage  textMessage= (TextMessage) message;

                    try {
                        System.out.println("得到消息:"+textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }

                }
            });
            //等待消息
            System.in.read();
            consumer.close();
            connection.close();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
