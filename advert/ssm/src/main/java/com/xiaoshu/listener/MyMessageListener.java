package com.xiaoshu.listener;

import com.xiaoshu.entity.City;
import com.xiaoshu.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyMessageListener implements MessageListener {

    @Autowired
    private CityService cityService;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage=(TextMessage)message;
        try {
            System.out.println("得到消息:"+textMessage.getText());
            City city=new City();
            city.setCname(textMessage.getText());
            cityService.save(city);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
