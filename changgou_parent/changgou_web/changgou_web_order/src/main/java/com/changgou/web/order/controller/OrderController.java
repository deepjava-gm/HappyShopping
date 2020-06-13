package com.changgou.web.order.controller;

import com.changgou.entity.Result;
import com.changgou.order.feign.CartFeign;
import com.changgou.order.feign.OrderFeign;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.user.feign.AddressFeign;
import com.changgou.user.pojo.Address;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.soap.Addressing;
import java.util.List;
import java.util.Map;

/**
 * 作者: kinggm Email:731586355@qq.com
 * 时间:  2020-06-11 19:16
 */


@Controller
@RequestMapping("/worder")
public class OrderController {

    @Autowired
    private AddressFeign addressFeign;

    @Autowired
    private CartFeign cartFeign;

    /**
     * 跳转页面
     *
     * @return
     */
    @RequestMapping("/ready/order")
    public String readyOrder(Model model) {

//        收件人信息
        List<Address> addressList = addressFeign.list().getData();
        model.addAttribute("address",addressList);

//        购物车信息
        Map map = cartFeign.list();
        List<OrderItem> orderItemList = (List<OrderItem>) map.get("orderItemList");
        Integer totalMoney = (Integer) map.get("totalMoney");
        Integer totalNum = (Integer) map.get("totalNum");

        model.addAttribute("carts",orderItemList);
        model.addAttribute("totalMoney",totalMoney);
        model.addAttribute("totalNum",totalNum);

//        加载默认收件人信息
        for (Address address : addressList) {
//            默认收件人
            if("1".equals(address.getIsDefault())){
                model.addAttribute("deAddr",address);
                break;
            }
        }


        return "order";
    }


    @Autowired
    private OrderFeign orderFeign;

    @PostMapping("/add")
    @ResponseBody
    public Result add(@RequestBody Order order){
        Result result = orderFeign.add(order);
        return result;
    }


    @GetMapping("/toPayPage")
    public String toPayPage(String orderId,Model model){

//        获取订单相关信息
        Order order = (Order) orderFeign.findById(orderId).getData();

        model.addAttribute("orderId",orderId);
        model.addAttribute("payMoney",order.getPayMoney());
        return "pay";
    }

}
