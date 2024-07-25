package com.github.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页查询包装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageBean {
    private Long total;//总记录数
    private List rows;//数据列表
}
