package com.changgou.goods.controller;
import com.changgou.entity.PageResult;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.goods.service.BrandService;
import com.changgou.goods.pojo.Brand;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController
@CrossOrigin
@RequestMapping("/brand")
@Api(value = "品牌管理",tags = "品牌管理" )
public class BrandController {


    @Autowired
    private BrandService brandService;

    /**
     * 查询全部数据
     * @return
     */
    @ApiOperation("查询所有的品牌")
    @GetMapping
    public Result findAll(){
        List<Brand> brandList = brandService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",brandList) ;
    }

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "主键",paramType = "path",required=true,dataType="int",allowEmptyValue=false)
    })
    public Result findById(@PathVariable Integer id){
        Brand brand = brandService.findById(id);
        return new Result(true,StatusCode.OK,"查询成功",brand);
    }


    /***
     * 新增数据
     * @param brand
     * @return
     */
    @PostMapping
    @ApiOperation("添加品牌")
    public Result add(@RequestBody Brand brand){
        brandService.add(brand);
        return new Result(true,StatusCode.OK,"添加成功");
    }


    /***
     * 修改数据
     * @param brand
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    @ApiOperation("修改品牌")
    public Result update(@RequestBody Brand brand,@PathVariable Integer id){
        brand.setId(id);
        brandService.update(brand);
        return new Result(true,StatusCode.OK,"修改成功");
    }


    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    @ApiOperation("根据id删除品牌数据")
    public Result delete(@PathVariable Integer id){
        brandService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/search" )
    @ApiOperation("多条件搜索品牌数据")
    public Result findList(@RequestParam Map searchMap){
        List<Brand> list = brandService.findList(searchMap);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/searchPage/{page}/{size}" )
    @ApiOperation("分页")
    public Result findPage(@RequestParam Map searchMap, @PathVariable  int page, @PathVariable  int size){
        Page<Brand> pageList = brandService.findPage(searchMap, page, size);
        PageResult pageResult=new PageResult(pageList.getTotal(),pageList.getResult());
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }


    /**
     * 根据分类名称查询品牌列表
     * @param categoryName
     * @return
     */
    @GetMapping("/category/{categoryName}")
    @ApiOperation("根据分类名称查询品牌列表")
    public Result<List<Map>>  findBrandListByCategoryName(@PathVariable("categoryName") String categoryName){

        List<Map> brandList = brandService.findListByCategoryName(categoryName);
        return new Result<>(true,StatusCode.OK,"查询成功",brandList);

    }


}
