package com.changgou.user.feign;

import com.changgou.entity.Result;
import com.changgou.user.pojo.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 作者: kinggm Email:731586355@qq.com
 * 时间:  2020-06-11 19:23
 */

@FeignClient(name = "user")
public interface AddressFeign {

    @GetMapping("/address/list")
    public Result<List<Address>> list();
}
