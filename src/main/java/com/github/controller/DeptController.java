package com.github.controller;

import com.github.pojo.Dept;
import com.github.pojo.Result;
import com.github.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//部门管理控制器
@Slf4j
@RestController
public class DeptController {
    @Autowired
    private DeptService deptService;

    /**
     * 查询部门数据
     * @return
     */
    //@RequestMapping(value = "/depts" , method = RequestMethod.GET)
    @GetMapping("/depts")
    public Result list() {
        log.info("查询所有部门数据");
        List<Dept> deptList = deptService.list();
        return Result.success(deptList);
    }

    /**
     * 删除部门及对应员工
     * @param id
     * @return
     */
    @DeleteMapping("/depts/{id}")
    public Result delete(@PathVariable Integer id) {
        //日志记录
        log.info("根据id删除部门：{}",id);
        //调用service层功能
        deptService.delete(id);
        //响应
        return Result.success();
    }

    /**
     * 新增部门
     * @param dept
     * @return
     */
    @PostMapping("/depts")
    public Result add(@RequestBody Dept dept){
        //记录日志
        log.info("新增部门：{}",dept);
        //调用service层添加功能
        deptService.add(dept);
        //响应
        return Result.success();
    }

    /**
     * 根据id查询部门
     * @param id
     * @return
     */
    @GetMapping("/depts/{id}")
    public Result listbyId(@PathVariable Integer id){
        log.info("根据id查询部门：{}",id);
        List<Dept> list= deptService.listbyId(id);
        return Result.success(list);
    }

    /**
     * 根据id修改部门
     * @param id
     * @param dept
     * @return
     */
    @PutMapping("/depts/{id}")
    public Result update(@PathVariable Integer id, @RequestBody Dept dept){
        log.info("根据id修改部门：{}",id);
        deptService.update(id,dept);
        return Result.success();
    }
}