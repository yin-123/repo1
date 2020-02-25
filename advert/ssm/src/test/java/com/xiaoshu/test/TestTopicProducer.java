package com.xiaoshu.test;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

public class TestTopicProducer {

    @Test
    public void test02(){
        try {
            ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.109.129:61616");
            Connection conn = connectionFactory.createConnection();
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic=session.createTopic("topic");
            MessageProducer messageProducer=session.createProducer(topic);
            TextMessage textMessage=session.createTextMessage("i am message");
            messageProducer.send(textMessage);
            messageProducer.close();
            session.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
