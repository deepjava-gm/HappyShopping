package com.changgou.goods.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 商品组合实体类
 * 作者: kinggm Email:731586355@qq.com
 * 时间:  2020-05-31 18:25
 */
public class Goods implements Serializable{
    private Spu spu;
    private List<Sku> skuList;
    public Spu getSpu() {
        return spu;
    }
    public void setSpu(Spu spu) {
        this.spu = spu;
    }
    public List<Sku> getSkuList() {
        return skuList;
    }
    public void setSkuList(List<Sku> skuList) {
        this.skuList = skuList;
    }

}
