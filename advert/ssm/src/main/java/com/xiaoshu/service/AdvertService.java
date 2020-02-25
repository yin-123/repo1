package com.xiaoshu.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.AdvertMapper;
import com.xiaoshu.entity.Advert;
import com.xiaoshu.entity.GroupByType;
import com.xiaoshu.entity.Type2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AdvertService {

    @Resource
    private AdvertMapper advertMapper;

    public PageInfo<Advert> findAdvertPage(Advert advert, int pageNum, int pageSize, String ordername, String order) {
        PageHelper.startPage(pageNum, pageSize);
        ordername = StringUtil.isNotEmpty(ordername)?ordername:"id";
        order = StringUtil.isNotEmpty(order)?order:"desc";

        List<Advert> advertList = advertMapper.selectAllAdvert(advert);
        PageInfo<Advert> pageInfo = new PageInfo<Advert>(advertList);
        return pageInfo;
    }

    public List<Type2> selectAllType(){
        return advertMapper.selectAllType();
    }

    public Advert existAdvertName(String name){
        List<Advert> advertList = advertMapper.selectAllAdvert(null);
        for (Advert advert : advertList) {
            if (advert.getName().equals(name)){
                return advert;
            }
        }
        return null;
    }

    public void addAdvert(Advert advert){
        advertMapper.insert(advert);
    }

    public void deleteAdvert(Integer id) throws Exception{
        advertMapper.deleteAdvert(id);
    }

    // 修改
    public void updateAdvert(Advert advert) {
        advertMapper.updateAdvert(advert);
    };

    public List<Advert> selectAllAdvert(Advert advert){
        return advertMapper.selectAllAdvert(advert);
    }

    public List<GroupByType> selectGroupByType(){
        return advertMapper.selectGroupByType();
    }
}
