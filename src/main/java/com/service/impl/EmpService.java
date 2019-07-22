package com.service.impl;
import  java.util.*;
import  com.entity.*;
import  com.dao.*;
import com.service.IEmpService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@MapperScan(basePackages ="com.dao")
@Service(value = "empService")
public class EmpService implements IEmpService{
    @Autowired
    private IEmpDao dao;

}
