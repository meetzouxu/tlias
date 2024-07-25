package com.github.service.impl;

import com.github.mapper.DeptMapper;
import com.github.mapper.EmpMapper;
import com.github.pojo.Dept;
import com.github.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

// 部门业务实现类
@Slf4j
@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private EmpMapper empMapper;

    @Override
    public List<Dept> list() {
        return deptMapper.list();
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        deptMapper.deleteById(id);
        empMapper.deleteByDeptId(id);
    }

    @Override
    public void add(Dept dept) {
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());

        deptMapper.insert(dept);
    }

    @Override
    public List<Dept> listbyId(Integer id) {
        return deptMapper.listbyId(id);
    }

    @Override
    public void update(Integer id, Dept dept) {
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.update(id, dept);
    }
}