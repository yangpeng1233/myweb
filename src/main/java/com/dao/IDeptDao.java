package com.dao;
import  java.util.*;
import  com.entity.*;
public interface IDeptDao {
    List<Dept> selectAll();
    Dept selectByPrimaryKey(Integer deptno);
}
