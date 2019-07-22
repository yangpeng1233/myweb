package com.service.impl;
import  java.util.*;
import  com.entity.*;
import  com.dao.*;
import com.service.IDeptService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@MapperScan(basePackages ="com.dao")
@Service(value = "deptService")
public class DeptService implements IDeptService{

    @Autowired
    private IDeptDao dao;

    @Override
    public List<Dept> selectAll() {
        return dao.selectAll();
    }

    @Override
    public Dept selectByPrimaryKey(Integer deptno) {
        return dao.selectByPrimaryKey(deptno);
    }
}
