package com.xiaoshu.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.entity.*;
import com.xiaoshu.service.*;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.WriterUtil;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("device")
public class DeviceController {

    static Logger logger = Logger.getLogger(DeviceController.class);

    @Autowired
    private RoleService roleService ;

    @Autowired
    private OperationService operationService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private RedisTemplate redisTemplate;

    List<String> deleteJsonStr=new ArrayList<String>();

    @RequestMapping("deviceIndex")
    public String list(HttpServletRequest request, Integer menuid) throws Exception {
        // List<Role> roleList = roleService.findRole(new Role());
        List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
        request.setAttribute("operationList", operationList);
        // request.setAttribute("roleList", roleList);
        List<Type> typeList = typeService.selectAll();
        request.setAttribute("typeList", typeList);
        return "device";
    }

    @RequestMapping(value="devicelList",method= RequestMethod.POST)
    public void userList(HttpServletRequest request, HttpServletResponse response, String offset, String limit) throws Exception{
        try {
            Device device=new Device();
            String name = request.getParameter("name");
            String statu = request.getParameter("statu");
            String order = request.getParameter("order");
            String ordername = request.getParameter("ordername");
            if (StringUtil.isNotEmpty(name)) {
                device.setName(name);
            }
            if (StringUtil.isNotEmpty(statu)) {
                device.setStatu(statu);
            }
            Integer pageSize = StringUtil.isEmpty(limit)? ConfigUtil.getPageSize():Integer.parseInt(limit);
            Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;

            //PageInfo<Device> deviceList= deviceService.findDevicePage(device,pageNum,pageSize,ordername,order);

            PageInfo<Device> deviceList = null;
            System.out.println(deviceList);
            String schjsonStr= device.toString()+pageNum+pageSize;
            String jsonSchoolList=(String)redisTemplate.boundValueOps(schjsonStr).get();
            if (jsonSchoolList!=null){
                System.out.println("1111");
                deviceList= JSON.parseObject(jsonSchoolList,PageInfo.class);
            }
            if (jsonSchoolList==null){
                System.out.println("2222");
                deviceList=deviceService.findDevicePage(device,pageNum,pageSize,"","");
                String jsonStr=JSONArray.toJSONString(deviceList);
                redisTemplate.boundValueOps(schjsonStr).set(jsonStr);
                deleteJsonStr.add(schjsonStr);
            }

           /* request.setAttribute("username", username);
            request.setAttribute("roleid", roleid);
            request.setAttribute("usertype", usertype);*/
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("total",deviceList.getTotal() );
            jsonObj.put("rows", deviceList.getList());
            WriterUtil.write(response,jsonObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户展示错误",e);
            throw e;
        }
    }

    @RequestMapping("reserveDevice")
    public void reserveUser(HttpServletRequest request,Device device,HttpServletResponse response){
        Integer id = device.getId();
        JSONObject result=new JSONObject();
        try {
            if (id != null) {   // userId不为空 说明是修改
//                Device deviceName = deviceService.existDeviceWithName(device.getName());
//                if(deviceName != null && deviceName.getId().compareTo(id)==0){
//                    device.setId(id);
                System.out.println(id);
                System.out.println(device);
                    deviceService.updateDevice(device);
                    result.put("success", true);
                /*}else{
                    result.put("success", true);
                    result.put("errorMsg", "该用户名被使用");
                }*/

            }else {   // 添加
                if(deviceService.existDeviceWithName(device.getName())==null){  // 没有重复可以添加
                    if (deleteJsonStr.size()>0){
                        for(String deleteKey:deleteJsonStr){
                            redisTemplate.delete(deleteKey);
                        }
                    }
                    deviceService.addDevice(device);
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

    @RequestMapping("deleteDevice")
    public void delUser(HttpServletRequest request,HttpServletResponse response){
        JSONObject result=new JSONObject();
        try {
            String[] ids=request.getParameter("ids").split(",");
            System.out.println(Arrays.toString(ids));
            for (String id : ids) {
                System.out.println(id);
                deviceService.deleteDevice(Integer.parseInt(id));
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

    @RequestMapping("/out")
    public void outExcel(HttpServletResponse response){
        //创建一个WorkBook对象
        HSSFWorkbook hssfWorkbook=new HSSFWorkbook();
        //创建一个Sheet
        HSSFSheet sheet = hssfWorkbook.createSheet();
        //创建标题页
        HSSFRow titlerow = sheet.createRow(0);

        titlerow.createCell(0).setCellValue("id");
        titlerow.createCell(1).setCellValue("name");
        titlerow.createCell(2).setCellValue("tid");
        titlerow.createCell(3).setCellValue("nc");
        titlerow.createCell(4).setCellValue("color");
        titlerow.createCell(5).setCellValue("price");
        titlerow.createCell(6).setCellValue("statu");
        titlerow.createCell(7).setCellValue("time");
        titlerow.createCell(8).setCellValue("类型");
        titlerow.createCell(9).setCellValue("pinzhi");
        List<Device> devices = deviceService.selectAllDevice(null);
        for (Device device : devices) {
            //获取最后一行的行号
            int lastRowNum = sheet.getLastRowNum();
            HSSFRow dataRow = sheet.createRow(lastRowNum + 1);
            dataRow.createCell(0).setCellValue(device.getId());
            dataRow.createCell(1).setCellValue(device.getName());
            dataRow.createCell(2).setCellValue(device.getTid());
            dataRow.createCell(3).setCellValue(device.getNc());
            dataRow.createCell(4).setCellValue(device.getColor());
            dataRow.createCell(5).setCellValue(device.getPrice());
            dataRow.createCell(6).setCellValue(device.getStatu());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String s = format.format(device.getTime());
            dataRow.createCell(7).setCellValue(s);
            dataRow.createCell(8).setCellValue(device.getType().getTname());
            if (device.getPrice()>=1000){
                dataRow.createCell(9).setCellValue("高端机");
            }else{
                dataRow.createCell(9).setCellValue("平民机");
            }
        }

        //下载文件
        response.setHeader("Content-Disposition","attachment;filename=device.xls");
        response.setHeader("Content-Type","application/octet-stream");
        try {
            hssfWorkbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //按照文件输出
        /*try {
            FileOutputStream fileOutputStream = new FileOutputStream("D:\\poiDaoChu\\student.xls");
            hssfWorkbook.write(fileOutputStream);
            hssfWorkbook.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @RequestMapping("in")
    public String in(MultipartFile file,HttpServletResponse response) {
        try {
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(file.getInputStream());
            //获取sheet
            HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
            //获取总行数
            int lastRowNum = sheet.getLastRowNum();
            for (int i = 1; i <= lastRowNum; i++) {
                HSSFRow dataRow = sheet.getRow(i);
                //int id=(int) dataRow.getCell(0).getNumericCellValue();
                String name = dataRow.getCell(1).getStringCellValue();
                int tid=(int) dataRow.getCell(2).getNumericCellValue();
                String nc = dataRow.getCell(3).getStringCellValue();
                String color = dataRow.getCell(4).getStringCellValue();
                int price=(int) dataRow.getCell(5).getNumericCellValue();
                String statu = dataRow.getCell(6).getStringCellValue();
                String dateStr = dataRow.getCell(7).getStringCellValue();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date time = format.parse(dateStr);
                //String tname = dataRow.getCell(8).getStringCellValue();
                Device device = new Device(name, tid, nc, color, price, statu, time);
                deviceService.addDevice(device);
            }
            /*JSONObject result = new JSONObject();
            result.put("success",true);
            WriterUtil.write(response,result.toString());*/
            return "redirect:deviceIndex.htm?menuid=13";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping("selectGroupByType1")
    public void selectGroupByType(HttpServletResponse response){
        List<GroupByType> g= deviceService.selectGroupByType();
        String jsonStr= JSONArray.toJSONString(g);
        WriterUtil.write(response,jsonStr);
    }
}
