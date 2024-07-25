package com.github.service;

import com.github.pojo.Emp;
import com.github.pojo.PageBean;

import java.time.LocalDate;
import java.util.List;

//员工业务规则
public interface EmpService {
    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    /*PageBean page(Integer page, Integer pageSize);*/

    /**
     * 条件分页查询
     * @param page
     * @param pageSize
     * @param name
     * @param gender
     * @param begin
     * @param end
     * @return
     */
    PageBean page(Integer page, Integer pageSize, String name, Short gender, LocalDate begin, LocalDate end);

    /**
     * 删除员工
     * @param ids
     */
    void delete(List<Integer> ids);

    /**
     * 新增员工
     * @param emp
     */
    void save(Emp emp);

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    Emp getById(Integer id);

    /**
     * 更新员工
     * @param emp
     */
    void update(Emp emp);

    /**
     * 员工登陆
     * @param emp
     * @return
     */
    Emp login(Emp emp);
}