package com.xiaoshu.dao;

import com.xiaoshu.base.dao.BaseMapper;
import com.xiaoshu.entity.Device;
import com.xiaoshu.entity.GroupByType;

import java.util.List;

public interface DeviceMapper extends BaseMapper<Device> {
    List<Device> selectAllDevice(Device device);
    void updateDevice(Device device);
    List<GroupByType> selectGroupByType();
}
