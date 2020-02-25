package com.xiaoshu.test;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

public class TestQueueProducer {

    @Test
    public void test01(){
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.109.129:61616");
        try {
            Connection conn = connectionFactory.createConnection();
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue=session.createQueue("test");
            MessageProducer messageProducer=session.createProducer(queue);
            TextMessage textMessage=session.createTextMessage("i am message");
            messageProducer.send(textMessage);
            messageProducer.close();
            session.close();
            conn.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }













}
