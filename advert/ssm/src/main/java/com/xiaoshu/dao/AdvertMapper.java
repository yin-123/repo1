package com.xiaoshu.dao;

import com.xiaoshu.base.dao.BaseMapper;
import com.xiaoshu.entity.Advert;
import com.xiaoshu.entity.GroupByType;
import com.xiaoshu.entity.Type2;

import java.util.List;

public interface AdvertMapper extends BaseMapper<Advert> {
    List<Advert> selectAllAdvert(Advert advert);
    void deleteAdvert(int id);
    List<Type2> selectAllType();
    void updateAdvert(Advert advert);
    List<GroupByType> selectGroupByType();
}
