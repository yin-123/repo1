package com.xiaoshu.service;

import com.xiaoshu.dao.TypeMapper;
import com.xiaoshu.entity.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeService {
    @Autowired
    private TypeMapper mapper;

    public List<Type> selectAll(){
        return mapper.selectAll();
    };
}
