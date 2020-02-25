package com.xiaoshu.service;

import com.xiaoshu.dao.CityMapper;
import com.xiaoshu.entity.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityMapper mapper;

    public List<City> selectAll(){
        return mapper.selectAll();
    };

    public void save(City city){
        mapper.insert(city);
    }

}
