package com.changgou.goods.feign;

        import com.changgou.entity.PageResult;
        import com.changgou.entity.Result;
        import com.changgou.goods.pojo.Sku;
        import org.springframework.cloud.openfeign.FeignClient;
        import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.PathVariable;
        import org.springframework.web.bind.annotation.PostMapping;
        import org.springframework.web.bind.annotation.RequestParam;

        import java.util.List;

@FeignClient(name = "goods")
public interface SkuFeign {

    @GetMapping("/sku/spu/{spuId}")
    public List<Sku> findSkuListBySpuId(@PathVariable("spuId") String spuId);


    /**
     * 插入 数据  分页
     * 1、 先去查询当前数据库sku的总记录数  1000条 pageSize=1000
     * 2、 计算总页数   总记录数/pageSize = totalPage  向上取整
     * 3、 for 循环  遍历页码  page=1  --> totalPages
     * 4、 List集合（每一页的数据）
     * 5、 每一次遍历转换成 skuInfo 数据列表的时候， 就调用一次 导入索引库的方法
     *
     *  保证 ElasticSearch 索引库不会内存溢出的问题
     * @param spuId
     * @return
     */

    @GetMapping("/sku/spu/{spuId}/{page}")
    public PageResult<Sku> findSkuPageBySpuId(@PathVariable("spuId") String spuId, @PathVariable("page") Integer page);


    @GetMapping("/sku/{id}")
    public Result<Sku> findById(@PathVariable("id") String id);


    @PostMapping("/sku/decr/count")
    public Result decrCount(@RequestParam("username") String username);

    }
