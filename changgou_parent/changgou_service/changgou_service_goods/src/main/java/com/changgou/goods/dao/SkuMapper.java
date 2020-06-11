package com.changgou.goods.dao;

import com.changgou.goods.pojo.Sku;
import com.changgou.order.pojo.OrderItem;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
@Repository
public interface SkuMapper extends Mapper<Sku> {


    /**
     * 扣减库存增加销量
     * @param orderItem
     * @return
     */
    @Update("update tb_sku set num=num-#{num},sale_num=sale_num+#{num} where id=#{skuId} and num>=#{num}")
        int decrCount(OrderItem orderItem);

        }
