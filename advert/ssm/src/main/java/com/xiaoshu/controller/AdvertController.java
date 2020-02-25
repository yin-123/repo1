package com.xiaoshu.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("advert")
public class AdvertController {

    static Logger logger = Logger.getLogger(AdvertController.class);

    @Autowired
    private RoleService roleService ;

    @Autowired
    private OperationService operationService;

    @Autowired
    private AdvertService advertService;

    @RequestMapping("advertIndex")
    public String list(HttpServletRequest request, Integer menuid) throws Exception {
        // List<Role> roleList = roleService.findRole(new Role());
        List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
        request.setAttribute("operationList", operationList);
        // request.setAttribute("roleList", roleList);
        List<Type2> type2List = advertService.selectAllType();
        request.setAttribute("type2List", type2List);
        return "advert";
    }

    @RequestMapping(value="advertlList",method= RequestMethod.POST)
    public void userList(HttpServletRequest request, HttpServletResponse response, String offset, String limit) throws Exception{
        try {
            Advert advert=new Advert();
            String name = request.getParameter("name");
            String tid = request.getParameter("tid");
            String order = request.getParameter("order");
            String ordername = request.getParameter("ordername");
            if (StringUtil.isNotEmpty(name)) {
                advert.setName(name);
            }
            if (StringUtil.isNotEmpty(tid) && !"0".equals(tid)) {
                advert.setTid(Integer.parseInt(tid));
            }
            Integer pageSize = StringUtil.isEmpty(limit)? ConfigUtil.getPageSize():Integer.parseInt(limit);
            Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
            PageInfo<Advert> advertList= advertService.findAdvertPage(advert,pageNum,pageSize,ordername,order);

            /*request.setAttribute("name", name);
            request.setAttribute("tid", tid);
            request.setAttribute("usertype", usertype);*/
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("total",advertList.getTotal() );
            jsonObj.put("rows", advertList.getList());
            WriterUtil.write(response,jsonObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户展示错误",e);
            throw e;
        }
    }

    @RequestMapping("reserveAdvert")
    public void reserveUser(HttpServletRequest request,Advert advert,HttpServletResponse response){
        Integer id = advert.getId();
        JSONObject result=new JSONObject();
        try {
            if (id != null) {   // userId不为空 说明是修改
//                Device deviceName = deviceService.existDeviceWithName(device.getName());
//                if(deviceName != null && deviceName.getId().compareTo(id)==0){
//                    device.setId(id);
                    advertService.updateAdvert(advert);
                    result.put("success", true);
                /*}else{
                    result.put("success", true);
                    result.put("errorMsg", "该用户名被使用");
                }*/

            }else {   // 添加
                if(advertService.existAdvertName(advert.getName())==null){  // 没有重复可以添加
                    advertService.addAdvert(advert);
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

    @RequestMapping("deleteAdvert")
    public void delUser(HttpServletRequest request,HttpServletResponse response){
        JSONObject result=new JSONObject();
        try {
            String[] ids=request.getParameter("ids").split(",");
            for (String id : ids) {
                advertService.deleteAdvert(Integer.parseInt(id));
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
        titlerow.createCell(3).setCellValue("img");
        titlerow.createCell(4).setCellValue("path");
        titlerow.createCell(5).setCellValue("price");
        titlerow.createCell(6).setCellValue("statu");
        titlerow.createCell(7).setCellValue("createtime");
        titlerow.createCell(8).setCellValue("类型");
        List<Advert> adverts = advertService.selectAllAdvert(null);
        for (Advert advert : adverts) {
            //获取最后一行的行号
            int lastRowNum = sheet.getLastRowNum();
            HSSFRow dataRow = sheet.createRow(lastRowNum + 1);
            dataRow.createCell(0).setCellValue(advert.getId());
            dataRow.createCell(1).setCellValue(advert.getName());
            dataRow.createCell(2).setCellValue(advert.getTid());
            dataRow.createCell(3).setCellValue(advert.getImg());
            dataRow.createCell(4).setCellValue(advert.getPath());
            dataRow.createCell(5).setCellValue(advert.getPrice());
            dataRow.createCell(6).setCellValue(advert.getStatu());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String s = format.format(advert.getCreatetime());
            dataRow.createCell(7).setCellValue(s);
            dataRow.createCell(8).setCellValue(advert.getType2().getTname());
        }

        //下载文件
        response.setHeader("Content-Disposition","attachment;filename=advert.xls");
        response.setHeader("Content-Type","application/octet-stream");
        try {
            hssfWorkbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //按照文件输出
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("D:\\poiDaoChu\\advert.xls");
            hssfWorkbook.write(fileOutputStream);
            hssfWorkbook.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                String img = dataRow.getCell(3).getStringCellValue();
                String path = dataRow.getCell(4).getStringCellValue();
                int price=(int) dataRow.getCell(5).getNumericCellValue();
                String statu = dataRow.getCell(6).getStringCellValue();
                String dateStr = dataRow.getCell(7).getStringCellValue();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date time = format.parse(dateStr);
                //String tname = dataRow.getCell(8).getStringCellValue();
                Advert advert = new Advert(name, tid, img, path, price, statu, time);
                advertService.addAdvert(advert);
            }
            /*JSONObject result = new JSONObject();
            result.put("success",true);
            WriterUtil.write(response,result.toString());*/
            return "redirect:advertIndex.htm?menuid=15";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping("selectGroupByType")
    public void selectGroupByType(HttpServletResponse response){
        List<GroupByType> g= advertService.selectGroupByType();
        String jsonStr= JSONArray.toJSONString(g);
        WriterUtil.write(response,jsonStr);
    }
}
