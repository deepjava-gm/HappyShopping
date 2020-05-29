package com.changgou.goods.dao;

import com.changgou.goods.pojo.Spec;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.code.ORDER;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface SpecMapper extends Mapper<Spec> {

    /**
     * 根据商品分类名称查询规格列表
     * @param categoryName
     * @return
     */
    @Select("SELECT name,options FROM tb_spec WHERE template_id IN (SELECT template_id FROM tb_category WHERE NAME = #{categoryName} ORDER BY seq)")
    public List<Map> findSpecListByCategoryName(@Param("categoryName") String categoryName);
}
