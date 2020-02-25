package com.xiaoshu.dao;

import com.xiaoshu.base.dao.BaseMapper;
import com.xiaoshu.entity.School;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository(value = "SchoolMapper")
public interface SchoolMapper extends BaseMapper<School> {
    List<School> selectAllSchool(School school);
    void deleteSchool(int id);
    /*School existSchoolWithName(String name);
    void addSchool(School school);*/
}
