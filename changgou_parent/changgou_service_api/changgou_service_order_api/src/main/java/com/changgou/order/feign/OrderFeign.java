package com.changgou.order.feign;

import com.changgou.entity.Result;
import com.changgou.order.pojo.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 作者: kinggm Email:731586355@qq.com
 * 时间:  2020-06-11 21:08
 */
@FeignClient(name = "order")
public interface OrderFeign {
    @PostMapping("/order")
    public Result add(@RequestBody Order order);
}
