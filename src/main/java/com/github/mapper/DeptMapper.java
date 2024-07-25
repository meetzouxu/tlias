package com.github.mapper;
import com.github.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeptMapper {
    /**
     * 查询全部部门数据
     * @return
     */
    @Select("select * from dept")
    List<Dept> list();

    /**
     * 根据ID删除部门
     * @param id
     */
    @Delete("delete from dept where id = #{id}")
    void deleteById(Integer id);

    /**
     * 新增部门
     * @param dept
     */
    @Insert("insert into dept (name, create_time, update_time) values (#{name},#{createTime},#{updateTime})")
    void insert(Dept dept);

    /**
     * 根据id查询部门
     * @param id
     * @return
     */
    @Select("select * from dept where id = #{id}")
    List<Dept> listbyId(Integer id);

    /**
     * 根据id修改部门
     * @param id
     * @param dept
     */
    @Update("update dept set name = #{dept.name}, update_time = #{dept.updateTime} where id = #{id}")
    // 如果你的mapper方法中有多个参数, 你可以使用注解@Param给你的参数一个别名(供sql语句中使用), 否则的话你的参数将会以"param"为前缀 后面跟上参数的位置作为别名.
    // 例如:在这里如果不使用@Param，结果是 #{param1}, #{param2}；使用#Param后，结果是 #{id}, #{dept}
    void update(@Param("id")Integer id, @Param("dept")Dept dept);
}
