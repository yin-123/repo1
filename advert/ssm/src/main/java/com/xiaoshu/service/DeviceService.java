package com.xiaoshu.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.DeviceMapper;
import com.xiaoshu.entity.Device;
import com.xiaoshu.entity.GroupByType;
import com.xiaoshu.entity.School;
import com.xiaoshu.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    public PageInfo<Device> findDevicePage(Device device, int pageNum, int pageSize, String ordername, String order) {
        PageHelper.startPage(pageNum, pageSize);
        ordername = StringUtil.isNotEmpty(ordername)?ordername:"id";
        order = StringUtil.isNotEmpty(order)?order:"desc";

        List<Device> deviceList = deviceMapper.selectAllDevice(device);
        PageInfo<Device> pageInfo = new PageInfo<Device>(deviceList);
        return pageInfo;
    }

    public List<Device> selectAllStatu(){
        return deviceMapper.selectAll();
    }

    public Device existDeviceWithName(String name){
        List<Device> deviceList = deviceMapper.selectAllDevice(null);
        for (Device device : deviceList) {
            if (device.getName().equals(name)){
                return device;
            }
        }
        return null;
    }

    public void addDevice(Device device){
        deviceMapper.insert(device);
    }

    public void deleteDevice(Integer id) throws Exception{
        deviceMapper.deleteByPrimaryKey(id);
    }

    // 修改
    public void updateDevice(Device t) throws Exception {
        deviceMapper.updateDevice(t);
    };

    public List<Device> selectAllDevice(Device device){
        return deviceMapper.selectAllDevice(device);
    }

    public List<GroupByType> selectGroupByType(){
        return deviceMapper.selectGroupByType();
    }
}
