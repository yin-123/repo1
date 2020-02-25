package com.xiaoshu.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.SchoolMapper;
import com.xiaoshu.entity.School;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SchoolService {

    @Autowired
    private SchoolMapper mapper;

    @Resource
    private RedisTemplate redisTemplate;

    List<String> deleteJsonStr=new ArrayList<String>();

    public PageInfo<School> findSchoolPage(School school, int pageNum, int pageSize, String ordername, String order) {
        PageHelper.startPage(pageNum, pageSize);
        ordername = StringUtil.isNotEmpty(ordername)?ordername:"userid";
        order = StringUtil.isNotEmpty(order)?order:"desc";
        /*List<School> schoolList = null;
        System.out.println(school);
        String schjsonStr= JSON.toJSONString(school);
        String jsonSchoolList=(String)redisTemplate.boundValueOps(schjsonStr).get();
        if (jsonSchoolList!=null){
            System.out.println("1111");
            schoolList=JSON.parseArray(jsonSchoolList,School.class);
        }
        if (jsonSchoolList==null){
            System.out.println("2222");
            schoolList=mapper.selectAllSchool(school);
            String jsonStr=JSONArray.toJSONString(schoolList);
            redisTemplate.boundValueOps(schjsonStr).set(jsonStr);
            deleteJsonStr.add(schjsonStr);
        }*/
                List<School> schoolList = mapper.selectAllSchool(school);

        PageInfo<School> pageInfo = new PageInfo<School>(schoolList);
        return pageInfo;
    }

    public School existSchoolWithName(String name){
        List<School> schoolList = mapper.selectAllSchool(null);
        for (School school : schoolList) {
            if (school.getName().equals(name)){
                return school;
            }
        }
        return null;
    }

    public void addSchool(School school){
        /*String name=JSONArray.toJSONString(school.getName());
        String phone=JSONArray.toJSONString(school.getPhone());
        redisTemplate.boundValueOps(name).set(phone);
        if (deleteJsonStr.size()>0){
            for(String deleteKey:deleteJsonStr){
                redisTemplate.delete(deleteKey);
            }
        }*/
        mapper.insert(school);
    }

    public void deleteSchool(Integer id) throws Exception{
        mapper.deleteSchool(id);
    }


}
