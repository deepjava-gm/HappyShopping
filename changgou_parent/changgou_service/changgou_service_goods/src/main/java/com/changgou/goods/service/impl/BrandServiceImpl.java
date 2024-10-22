package com.changgou.goods.service.impl;

import com.changgou.goods.dao.BrandMapper;
import com.changgou.goods.service.BrandService;
import com.changgou.goods.pojo.Brand;
import com.changgou.util.PinYinUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 查询全部列表
     * @return
     */
    @Override
    public List<Brand> findAll() {
        return brandMapper.selectAll();
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public Brand findById(Integer id){
        return  brandMapper.selectByPrimaryKey(id);
    }


    /**
     * 新增品牌
     *
     * @param brand
     */
    @Override
    public void add(Brand brand) {


        String name = brand.getName();
        if (StringUtils.isBlank(name)) {
            throw new RuntimeException("参数非法");
        }

        // 1 判断 填写 品牌首字母
        String letter = brand.getLetter();
        if (StringUtils.isBlank(letter)) {  // 没传参数
            letter = PinYinUtils.getFirstLetter(name);  //  name ? null
        } else {
            // 转成大小存储到数据库
            letter = letter.toUpperCase();
        }
        brand.setLetter(letter);


        brandMapper.insertSelective(brand);
    }

    /**
     * 修改
     * @param brand
     */
    @Override
    public void update(Brand brand){
        brandMapper.updateByPrimaryKey(brand);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Integer id){
        brandMapper.deleteByPrimaryKey(id);
    }


    /**
     * 条件查询
     * @param searchMap
     * @return
     */
    @Override
    public List<Brand> findList(Map<String, Object> searchMap){
        Example example = createExample(searchMap);
        return brandMapper.selectByExample(example);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Brand> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return (Page<Brand>)brandMapper.selectAll();
    }

    /**
     * 条件+分页查询
     * @param searchMap 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public Page<Brand> findPage(Map<String,Object> searchMap, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        return (Page<Brand>)brandMapper.selectByExample(example);
    }



    /**
     * 构建查询对象
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 品牌名称
            if(searchMap.get("name")!=null && !"".equals(searchMap.get("name"))){
                criteria.andLike("name","%"+searchMap.get("name")+"%");
           	}
            // 品牌图片地址
            if(searchMap.get("image")!=null && !"".equals(searchMap.get("image"))){
                criteria.andLike("image","%"+searchMap.get("image")+"%");
           	}
            // 品牌的首字母
            if(searchMap.get("letter")!=null && !"".equals(searchMap.get("letter"))){
                criteria.andLike("letter","%"+searchMap.get("letter")+"%");
           	}

            // 品牌id
            if(searchMap.get("id")!=null ){
                criteria.andEqualTo("id",searchMap.get("id"));
            }
            // 排序
            if(searchMap.get("seq")!=null ){
                criteria.andEqualTo("seq",searchMap.get("seq"));
            }

        }
        return example;
    }


    /**
     * 根据分类名称查询品牌列表
     * @param categoryName
     * @return
     */
    @Override
    public List<Map> findListByCategoryName(String categoryName) {


        List<Map> brandList = brandMapper.findListByCategoryName(categoryName);

        return brandList;


    }



}
