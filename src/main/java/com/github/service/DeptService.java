package com.github.service;

import com.github.pojo.Dept;

import java.util.List;

//部门业务规则
public interface DeptService {
    /**
     * 查询全部部门数据
     * @return
     */
    List<Dept> list();

    /**
     * 删除部门及对应员工
     * @param id
     */
    void delete(Integer id);

    /**
     * 新增部门
     * @param dept
     */
    void add(Dept dept);

    /**
     * 根据id查询部门
     * @param id
     * @return
     */
    List<Dept> listbyId(Integer id);

    /**
     * 根据id修改部门
     * @param id
     * @param dept
     */
    void update(Integer id, Dept dept);
}