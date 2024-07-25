package com.github.mapper;

import com.github.pojo.Emp;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface EmpMapper {
    /*//获取总记录数
    @Select("select count(*) from emp")
    public Long count();

    //获取当前页的结果列表
    @Select("select * from emp limit #{start}, #{pageSize}")
    public List<Emp> list(@Param("start") Integer start, @Param("pageSize") Integer pageSize);*/
    /**
     * 员工信息查询
     */

    /*@Select("select * from emp")
    public List<Emp> list();*/

    /**
     * 获取当前页的结果列表
     * @param name
     * @param gender
     * @param begin
     * @param end
     * @return
     */
    public List<Emp> list(@Param("name") String name, @Param("gender") Short gender, @Param("begin")LocalDate begin, @Param("end") LocalDate end);

    /**
     * 批量删除
     * @param ids
     */
    void delete(@Param("ids") List<Integer> ids);

    /**
     * 新增员工
     * @param emp
     */
    @Insert("insert into emp(username, name, gender, image, job, entrydate, dept_id, create_time, update_time) " +
            "values(#{username},#{name},#{gender},#{image},#{job},#{entrydate},#{deptId},#{createTime},#{updateTime})")
    void insert(Emp emp);

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @Select("select * from emp where id = #{id}")
    Emp getById(Integer id);

    /**
     * 更新员工
     * @param emp
     */
    void update(Emp emp);

    /**
     * 根据用户名和密码查询员工
     * @param emp
     * @return
     */
    @Select("select * from emp where username = #{username} and password = #{password}")
    Emp getByUsernameAndPassWord(Emp emp);

    /**
     * 根据部门id删除员工
     * @param deptId
     */
    @Delete("delete from emp where dept_id = #{deptId}")
    void deleteByDeptId(Integer deptId);
}