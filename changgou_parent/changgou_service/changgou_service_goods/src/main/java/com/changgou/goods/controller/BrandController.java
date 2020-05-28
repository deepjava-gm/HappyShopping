package com.changgou.goods.controller;

import com.changgou.common.pojo.PageResult;
import com.changgou.common.pojo.Result;
import com.changgou.common.pojo.StatusCode;
import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import com.github.pagehelper.Page;
import com.netflix.discovery.converters.Auto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jdk.net.SocketFlow;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 作者: kinggm Email:731586355@qq.com
 * 时间:  2020-05-28 19:24
 * Brand
 */
@RestController
@RequestMapping("/brand")
@Api(value = "品牌管理",tags = "品牌管理" )
public class BrandController {

    @Autowired
    private BrandService brandService;


    /**
     * 查询所有
     *
     * @return
     */
    @ApiOperation("查询所有的品牌")
    @GetMapping
    public Result findAll() {

        List<Brand> brandList = brandService.findAll();
        return new Result(true, StatusCode.OK, "查询成功", brandList);

    }


    /**
     * 根据id查询品牌数据
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "主键",paramType = "path",required=true,dataType="int",allowEmptyValue=false)
    })
    public Result findById(@PathVariable Integer id) {
        Brand brand = brandService.findById(id);
        return new Result(true, StatusCode.OK, "查询成功", brand);
    }


    /**
     * 添加
     *
     * @param brand
     * @return
     */
    @PostMapping
    @ApiOperation("添加品牌")
    public Result findById(@RequestBody Brand brand) {
        brandService.add(brand);
        return new Result(true, StatusCode.OK, "添加成功");
    }


    /**
     * 修改品牌数据
     * @param brand
     * @param id
     * @return
     */
    @PutMapping(value = "/{id}")
    @ApiOperation("修改品牌")
    public Result update(@RequestBody Brand brand,@PathVariable Integer id){
        brand.setId(id);
        brandService.update(brand);
        return new Result(true, StatusCode.OK,"修改成功");
    }


    /**
     * 根据id删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    @ApiOperation("根据id删除品牌数据")
    public Result delete(@PathVariable Integer id){
        brandService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }


    /**
     * 多条件搜索品牌数据
     * @param searchMap  品牌名称 品牌首字母
     * @return
     */
    @GetMapping(value = "/search")
    @ApiOperation("多条件搜索品牌数据")
    public Result findList(@RequestParam Map searchMap){
        List<Brand> list = brandService.findList(searchMap);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }


    /**
     * 分页
     * @param page
     * @param size
     * @return
     */
  @GetMapping(value = "/search/{page}/{size}")
  @ApiOperation("分页")
  public Result findPage(@PathVariable("page") int page, @PathVariable("size") int size){
      Page<Brand> pageList = brandService.findPage(page, size);
      PageResult pageResult = new PageResult(pageList.getTotal(), pageList.getResult());
      return new Result(true,StatusCode.OK,"查询成功",pageResult);
  }


    /**
     * 多条件分页查询
     * @param page
     * @param size
     * @return
     */
  @GetMapping(value = "/searchPage/{page}/{size}")
  @ApiOperation("多条件分页查询")
  public Result findPage(@RequestParam Map searchMap ,@PathVariable("page") int page, @PathVariable("size") int size){
      Page<Brand> pageList = brandService.findPage(searchMap,page, size);
      PageResult pageResult = new PageResult(pageList.getTotal(), pageList.getResult());
      return new Result(true,StatusCode.OK,"查询成功",pageResult);
  }



}
