package com.changgou.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.dao.*;
import com.changgou.goods.pojo.*;
import com.changgou.goods.service.SpuService;
import com.changgou.util.IdWorker;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private IdWorker idWorker;

    @Autowired
    private CategoryBrandMapper categoryBrandMapper;

    /**
     * 查询全部列表
     *
     * @return
     */
    @Override
    public List<Spu> findAll() {
        return spuMapper.selectAll();
    }

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    @Override
    public Spu findById(String id) {
        return spuMapper.selectByPrimaryKey(id);
    }


    /**
     * 增加
     *
     * @param spu
     */
    @Override
    public void add(Spu spu) {
        spuMapper.insert(spu);
    }


    /**
     * 修改
     *
     * @param goods
     */
    @Override
    public void update(Goods goods) {
//        修改spu
        Spu spu = goods.getSpu();
        spuMapper.updateByPrimaryKey(spu);

//        修改sku
//        首先删除原来的sku列表
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId", spu.getId());
        skuMapper.deleteByExample(example);

//        重新添加现有的sku列表
        this.saveSkuList(goods);
    }

    /**
     * 逻辑删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {

        Spu spu = spuMapper.selectByPrimaryKey(id);

//        检查是否下架的商品
        if (!"0".equals(spu.getIsMarketable())) {
            throw new RuntimeException("商品先下架再删除！");
        }
        spu.setIsDelete("1"); //删除
        spu.setStatus("0");   //未审核

        spuMapper.updateByPrimaryKeySelective(spu);
    }


    /**
     * 条件查询
     *
     * @param searchMap
     * @return
     */
    @Override
    public List<Spu> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return spuMapper.selectByExample(example);
    }

    /**
     * 分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Spu> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        return (Page<Spu>) spuMapper.selectAll();
    }

    /**
     * 条件+分页查询
     *
     * @param searchMap 查询条件
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @Override
    public Page<Spu> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = createExample(searchMap);
        return (Page<Spu>) spuMapper.selectByExample(example);
    }


    /**
     * 构建查询对象
     *
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap) {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (searchMap != null) {
            // 主键
            if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                criteria.andEqualTo("id", searchMap.get("id"));
            }
            // 货号
            if (searchMap.get("sn") != null && !"".equals(searchMap.get("sn"))) {
                criteria.andEqualTo("sn", searchMap.get("sn"));
            }
            // SPU名
            if (searchMap.get("name") != null && !"".equals(searchMap.get("name"))) {
                criteria.andLike("name", "%" + searchMap.get("name") + "%");
            }
            // 副标题
            if (searchMap.get("caption") != null && !"".equals(searchMap.get("caption"))) {
                criteria.andLike("caption", "%" + searchMap.get("caption") + "%");
            }
            // 图片
            if (searchMap.get("image") != null && !"".equals(searchMap.get("image"))) {
                criteria.andLike("image", "%" + searchMap.get("image") + "%");
            }
            // 图片列表
            if (searchMap.get("images") != null && !"".equals(searchMap.get("images"))) {
                criteria.andLike("images", "%" + searchMap.get("images") + "%");
            }
            // 售后服务
            if (searchMap.get("saleService") != null && !"".equals(searchMap.get("saleService"))) {
                criteria.andLike("saleService", "%" + searchMap.get("saleService") + "%");
            }
            // 介绍
            if (searchMap.get("introduction") != null && !"".equals(searchMap.get("introduction"))) {
                criteria.andLike("introduction", "%" + searchMap.get("introduction") + "%");
            }
            // 规格列表
            if (searchMap.get("specItems") != null && !"".equals(searchMap.get("specItems"))) {
                criteria.andLike("specItems", "%" + searchMap.get("specItems") + "%");
            }
            // 参数列表
            if (searchMap.get("paraItems") != null && !"".equals(searchMap.get("paraItems"))) {
                criteria.andLike("paraItems", "%" + searchMap.get("paraItems") + "%");
            }
            // 是否上架
            if (searchMap.get("isMarketable") != null && !"".equals(searchMap.get("isMarketable"))) {
                criteria.andEqualTo("isMarketable", searchMap.get("isMarketable"));
            }
            // 是否启用规格
            if (searchMap.get("isEnableSpec") != null && !"".equals(searchMap.get("isEnableSpec"))) {
                criteria.andEqualTo("isEnableSpec", searchMap.get("isEnableSpec"));
            }
            // 是否删除
            if (searchMap.get("isDelete") != null && !"".equals(searchMap.get("isDelete"))) {
                criteria.andEqualTo("isDelete", searchMap.get("isDelete"));
            }
            // 审核状态
            if (searchMap.get("status") != null && !"".equals(searchMap.get("status"))) {
                criteria.andEqualTo("status", searchMap.get("status"));
            }

            // 品牌ID
            if (searchMap.get("brandId") != null) {
                criteria.andEqualTo("brandId", searchMap.get("brandId"));
            }
            // 一级分类
            if (searchMap.get("category1Id") != null) {
                criteria.andEqualTo("category1Id", searchMap.get("category1Id"));
            }
            // 二级分类
            if (searchMap.get("category2Id") != null) {
                criteria.andEqualTo("category2Id", searchMap.get("category2Id"));
            }
            // 三级分类
            if (searchMap.get("category3Id") != null) {
                criteria.andEqualTo("category3Id", searchMap.get("category3Id"));
            }
            // 模板ID
            if (searchMap.get("templateId") != null) {
                criteria.andEqualTo("templateId", searchMap.get("templateId"));
            }
            // 运费模板id
            if (searchMap.get("freightId") != null) {
                criteria.andEqualTo("freightId", searchMap.get("freightId"));
            }
            // 销量
            if (searchMap.get("saleNum") != null) {
                criteria.andEqualTo("saleNum", searchMap.get("saleNum"));
            }
            // 评论数
            if (searchMap.get("commentNum") != null) {
                criteria.andEqualTo("commentNum", searchMap.get("commentNum"));
            }

        }
        return example;
    }


    /**
     * 新增商品
     *
     * @param goods
     */
    @Override
    @Transactional
    public void add(Goods goods) {
        Spu spu = goods.getSpu();
        long spuId = idWorker.nextId();
        spu.setId(String.valueOf(spuId));
        spu.setIsDelete("0");
        spu.setIsMarketable("0");
        spu.setStatus("0");

        spuMapper.insertSelective(spu);

//        保存sku集合数据到数据库
        this.saveSkuList(goods);


    }


    /**
     * 保存sku列表
     *
     * @param goods
     */
    private void saveSkuList(Goods goods) {
//        获取spu对象
        Spu spu = goods.getSpu();

//        当前日期
        Date date = new Date();

//        获取品牌对象
        Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());

//        获取分类对象
        Category category = categoryMapper.selectByPrimaryKey(spu.getCategory3Id());

//        设置品牌和分类的关联关系
        CategoryBrand categoryBrand = new CategoryBrand();
        categoryBrand.setBrandId(spu.getBrandId());
        categoryBrand.setCategoryId(spu.getCategory3Id());

        int count = categoryBrandMapper.selectCount(categoryBrand);

//        判断是否有这个品牌和分类的关系数据
        if (count == 0) {
//            如果没有关系数据则添加品牌和分类关系数据
            categoryBrandMapper.insert(categoryBrand);
        }


//        获取sku集合
        List<Sku> skuList = goods.getSkuList();

        if (skuList != null) {
            for (Sku sku : skuList) {
//                shezhisku主键id
                sku.setId(String.valueOf(idWorker.nextId()));

//                设置sku规格
                if (sku.getSpec() == null || "".equals(sku.getSpec())) {
                    sku.setSpec("{}");
                }

//                设置sku名称(商品名称加规格)
                String name = spu.getName();

//                将规格json字符串转换为Map
                Map<String, String> specMap = JSON.parseObject(sku.getSpec(), Map.class);

                if (specMap != null && specMap.size() > 0) {
                    for (String s : specMap.values()) {
                        name += " " + s;
                    }
                }
                sku.setName(name); //名称
                sku.setSpuId(spu.getId()); //设置spuID
                sku.setCreateTime(date);//创建日期
                sku.setUpdateTime(date);//修改日期

                if (category != null) {
                    sku.setCategoryId(category.getId());//商品分类ID
                    sku.setCategoryName(category.getName());//商品分类名称
                } else {
                    throw new RuntimeException("当前商品分类不存在");
                }

                if (brand != null) {
                    sku.setBrandName(brand.getName());//品牌名称
                } else {
                    throw new RuntimeException("当前商品品牌不存在");
                }

                skuMapper.insertSelective(sku);//插入sku表数据
            }


        }


    }

    /**
     * 根据商品ID查询商品详情
     *
     * @param id
     * @return
     */
    @Override
    public Goods findGoodsById(String id) {
        Goods goods = new Goods();

//        查询spu封装到goods
        Spu spu = spuMapper.selectByPrimaryKey(id);
        goods.setSpu(spu);

//        查询sku封装到goods
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId", id);
        List<Sku> skuList = skuMapper.selectByExample(example);
        goods.setSkuList(skuList);
        return goods;

    }


    /**
     * 审核商品
     *
     * @param id
     */
    @Override
    @Transactional
    public void audit(String id) {
//        查询spu对象
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu == null) {
            throw new RuntimeException("当前商品不存在");
        }

//        判断当前spu是否处于删除状态
        if ("1".equals(spu.getIsDelete())) {
            throw new RuntimeException("当前商品处于删除状态");
        }

//        不处于删除状态，修改审核状态为1，上下架状态为1
        spu.setStatus("1");
        spu.setIsMarketable("1");

//        执行修改状态
        spuMapper.updateByPrimaryKeySelective(spu);

    }

    /**
     * 下架商品
     *
     * @param id
     */
    @Override
    public void pull(String id) {
//        查询spu
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu == null) {
            throw new RuntimeException("当前商品不存在");
        }
        //判断当前商品是否处于删除状态
        if ("1".equals(spu.getIsDelete())) {
            throw new RuntimeException("当前商品处于删除状态");
        }

        //商品处于未删除状态的话,则修改上下架状态为已下架(0)
        spu.setIsMarketable("0");

        spuMapper.updateByPrimaryKeySelective(spu);

    }

    /**
     * 上架商品
     *
     * @param id
     */
    @Override
    public void put(String id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (!"1".equals(spu.getStatus())) {
            throw new RuntimeException("未通过审核的商品不能上架！");
        }
        spu.setIsMarketable("1");//上架状态
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 恢复商品
     * @param id
     */
    @Override
    public void restore(String id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);

//        检查是否是删除的商品
        if(!"1".equals(spu.getIsDelete())){
            throw new RuntimeException("此商品未删除！");
        }

        spu.setIsDelete("0"); //设置未删除
        spu.setStatus("0");    //设置未审核
        spuMapper.updateByPrimaryKeySelective(spu);

    }

    /**
     * 物理删除
     * @param id
     */
    @Override
    public void realDelete(String id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);

//        检查商品是否是删除的商品
        if("1".equals(spu.getIsDelete())){
            throw new RuntimeException("此商品未删除");
        }

        spuMapper.deleteByPrimaryKey(id);
    }


}
