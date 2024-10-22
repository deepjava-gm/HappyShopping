package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.PageResult;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.ESManagerMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.ESManagerService;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ESManagerServiceImpl implements ESManagerService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private ESManagerMapper esManagerMapper;

    //创建索引库结构
    @Override
    public void createMappingAndIndex() {
        //创建索引
        elasticsearchTemplate.createIndex(SkuInfo.class);
        //创建映射
        elasticsearchTemplate.putMapping(SkuInfo.class);
    }

    //导入全部sku集合进入到索引库  （All全部导入）
    @Override
    public void importAll() {
        //查询sku集合
        List<Sku> skuList = skuFeign.findSkuListBySpuId("all");
        if (skuList == null || skuList.size()<=0){
            throw new RuntimeException("当前没有数据被查询到,无法导入索引库");
        }

        //skulist转换为json
        String jsonSkuList = JSON.toJSONString(skuList);
        //将json转换为skuinfo
        List<SkuInfo> skuInfoList = JSON.parseArray(jsonSkuList, SkuInfo.class);

        for (SkuInfo skuInfo : skuInfoList) {
            //将规格信息转换为map
            Map specMap = JSON.parseObject(skuInfo.getSpec(), Map.class);
            skuInfo.setSpecMap(specMap);
        }

        //导入索引库
        esManagerMapper.saveAll(skuInfoList);
    }

    //根据spuid查询skuList,添加到索引库
    @Override
    public void importDataBySpuId(String spuId) {
        List<Sku> skuList = skuFeign.findSkuListBySpuId(spuId);
        if (skuList == null || skuList.size()<=0){
            throw new RuntimeException("当前没有数据被查询到,无法导入索引库");
        }
        //将集合转换为json
        String jsonSkuList = JSON.toJSONString(skuList);
        List<SkuInfo> skuInfoList = JSON.parseArray(jsonSkuList, SkuInfo.class);

        for (SkuInfo skuInfo : skuInfoList) {
            //将规格信息进行转换
            Map specMap = JSON.parseObject(skuInfo.getSpec(), Map.class);
            skuInfo.setSpecMap(specMap);
        }

        //添加索引库
        esManagerMapper.saveAll(skuInfoList);
    }

    @Override
    public void delDataBySpuId(String spuId) {
       /* List<Sku> skuList = skuFeign.findSkuListBySpuId(spuId);
        if (skuList == null || skuList.size()<=0){
            throw new RuntimeException("当前没有数据被查询到,无法导入索引库");
        }
        for (Sku sku : skuList) {
            esManagerMapper.deleteById(Long.parseLong(sku.getId()));
        }*/

//       直接用简单的方法删除
       this.delDataBySpuId1(spuId);
    }


//    直接删除 映射集合
public void delDataBySpuId1(String spuId) {

    Iterable<SkuInfo> iterable = esManagerMapper.search(QueryBuilders.termQuery("spuId", spuId));
    if (iterable == null) {
        throw new RuntimeException("该Spu没有对应下架商品，删除错误");
    }
    esManagerMapper.deleteAll(iterable);

}




// 分页导入  一页1W条
    /**
     * 分页导入 ES
     */
    @Override
    public void importData() throws Exception {
        // 远程获取sku数据列表
        PageResult<Sku> pageResult = skuFeign.findSkuPageBySpuId("all", 1);

        if (pageResult == null || pageResult.getTotal() == 0 || pageResult.getPages() == 0) {
            throw new Exception("未查询到数据，无法导入！");
        }
        // 获取总页数
        Integer pages = pageResult.getPages();
        for (int i = 1; i <= pages; i++) {
            List<Sku> skuList = skuFeign.findSkuPageBySpuId("all", i).getRows();

            //skulist转换为json   list<Sku> --》 JSON  --》 List<skuInfo>
            String jsonSkuList = JSON.toJSONString(skuList);
            //将json转换为skuinfo
            List<SkuInfo> skuInfoList = JSON.parseArray(jsonSkuList, SkuInfo.class);

            for (SkuInfo skuInfo : skuInfoList) {
                //将规格信息转换为map  {'颜色': '红色', '版本': '8GB+128GB'}  =》map
                Map specMap = JSON.parseObject(skuInfo.getSpec(), Map.class);
                skuInfo.setSpecMap(specMap);
            }

            //导入索引库
            esManagerMapper.saveAll(skuInfoList);
            System.out.println("======================导入 第"+i+"页 SKU数据列表成功");
        }
    }


}
