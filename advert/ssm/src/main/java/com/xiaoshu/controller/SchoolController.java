package com.xiaoshu.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.entity.*;
import com.xiaoshu.service.CityService;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.service.RoleService;
import com.xiaoshu.service.SchoolService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.WriterUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("school")
public class SchoolController extends LogController {

    static Logger logger = Logger.getLogger(SchoolController.class);


    @Autowired
    private RoleService roleService ;

    @Autowired
    private OperationService operationService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private CityService service;



    @Autowired
    private RedisTemplate redisTemplate;

    List<String> deleteJsonStr=new ArrayList<String>();

    @RequestMapping("schoolIndex")
    public String list(HttpServletRequest request, Integer menuid) throws Exception {
       // List<Role> roleList = roleService.findRole(new Role());
        List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
        request.setAttribute("operationList", operationList);
       // request.setAttribute("roleList", roleList);
        List<City> cityList = service.selectAll();
        request.setAttribute("cityList", cityList);
        return "school";
    }

    @RequestMapping(value="schoolList",method= RequestMethod.POST)
    public void userList(HttpServletRequest request, HttpServletResponse response, String offset, String limit) throws Exception{
        try {
            School school=new School();
            String phone = request.getParameter("phone");
            String cid = request.getParameter("cid");
            /*String usertype = request.getParameter("usertype");
            String order = request.getParameter("order");
            String ordername = request.getParameter("ordername");*/
            if (StringUtil.isNotEmpty(phone)) {
                school.setPhone(phone);
            }
            if (StringUtil.isNotEmpty(cid) && !"0".equals(cid)) {
                school.setCid(Integer.parseInt(cid));
            }
            Integer pageSize = StringUtil.isEmpty(limit)? ConfigUtil.getPageSize():Integer.parseInt(limit);
            Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
            //PageInfo<School> schoolList= schoolService.findSchoolPage(school,pageNum,pageSize,"","");
            PageInfo<School> schoolList = null;
            System.out.println(school);
            String schjsonStr= school.toString()+pageNum+pageSize;
            String jsonSchoolList=(String)redisTemplate.boundValueOps(schjsonStr).get();
            if (jsonSchoolList!=null){
                System.out.println("1111");
                schoolList=JSON.parseObject(jsonSchoolList,PageInfo.class);
            }
            if (jsonSchoolList==null){
                System.out.println("2222");
                schoolList=schoolService.findSchoolPage(school,pageNum,pageSize,"","");
                String jsonStr=JSONArray.toJSONString(schoolList);
                redisTemplate.boundValueOps(schjsonStr).set(jsonStr);
                deleteJsonStr.add(schjsonStr);
            }
            /*PageInfo<School> schoolList = null;
            String schjsonStr= JSON.toJSONString(school.toString()+pageNum+pageSize);
            String jsonSchoolList=(String)redisTemplate.boundHashOps(schjsonStr).get(school.toString()+pageNum+pageSize);
            if (jsonSchoolList!=null){
                System.out.println("1111");
                schoolList=JSON.parseObject(jsonSchoolList,PageInfo.class);
            }
            if (jsonSchoolList==null){
                System.out.println("2222");
                schoolList=schoolService.findSchoolPage(school,pageNum,pageSize,"","");;
                String jsonStr= JSONArray.toJSONString(schoolList);
                redisTemplate.boundHashOps(schjsonStr).put(school.toString()+pageNum,JSON.toJSONString(schoolList));
                //deleteJsonStr.add(schjsonStr);
            }*/

           /* request.setAttribute("username", username);
            request.setAttribute("roleid", roleid);
            request.setAttribute("usertype", usertype);*/
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("total",schoolList.getTotal() );
            jsonObj.put("rows", schoolList.getList());
            WriterUtil.write(response,jsonObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户展示错误",e);
            throw e;
        }
    }

    @RequestMapping("reserveSchool")
    public void reserveUser(HttpServletRequest request,School school,HttpServletResponse response){
        Integer id = school.getId();
        JSONObject result=new JSONObject();
        try {
            if (id != null) {   // userId不为空 说明是修改
                /*User userName = userService.existUserWithUserName(user.getUsername());
                if(userName != null && userName.getUserid().compareTo(userId)==0){
                    user.setUserid(userId);
                    userService.updateUser(user);
                    result.put("success", true);
                }else{
                    result.put("success", true);
                    result.put("errorMsg", "该用户名被使用");
                }*/

            }else {   // 添加
                if(schoolService.existSchoolWithName(school.getName())==null){  // 没有重复可以添加
                    if (deleteJsonStr.size()>0){
                        for(String deleteKey:deleteJsonStr){
                            redisTemplate.delete(deleteKey);
                        }
                    }
                    schoolService.addSchool(school);
                    result.put("success", true);
                } else {
                    result.put("success", true);
                    result.put("errorMsg", "该用户名被使用");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("保存用户信息错误",e);
            result.put("success", true);
            result.put("errorMsg", "对不起，操作失败");
        }
        WriterUtil.write(response, result.toString());
    }

    @RequestMapping("deleteSchool")
    public void delUser(HttpServletRequest request,HttpServletResponse response){
        JSONObject result=new JSONObject();
        try {
            String[] ids=request.getParameter("ids").split(",");
            System.out.println(Arrays.toString(ids));
            for (String id : ids) {
                System.out.println(id);
                schoolService.deleteSchool(Integer.parseInt(id));
            }
            result.put("success", true);
            result.put("delNums", ids.length);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除用户信息错误",e);
            result.put("errorMsg", "对不起，删除失败");
        }
        WriterUtil.write(response, result.toString());
    }

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Destination queueTextDestination;

    @RequestMapping("sendMsg")
    public void sengMessage(HttpServletResponse response,final String cname){
        //发送消息,消息的生成者
        jmsTemplate.send(queueTextDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(cname);
            }
        });
        JSONObject result = new JSONObject();
        result.put("success",true);
        WriterUtil.write(response,result.toString());
    }
}
