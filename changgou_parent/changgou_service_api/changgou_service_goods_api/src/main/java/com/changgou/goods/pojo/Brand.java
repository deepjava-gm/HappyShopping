package com.changgou.goods.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * brand实体类
 * @author 黑马架构师2.5
 *
 */
@Table(name="tb_brand")
@ApiModel("Brand->品牌实体")
public class Brand implements Serializable {

	@Id
	@ApiModelProperty("品牌id")
	private Integer id;//品牌id


	@ApiModelProperty("品牌名称")
	private String name;//品牌名称

	@ApiModelProperty("品牌图片地址")
	private String image;//品牌图片地址

	@ApiModelProperty("品牌的首字母")
	private String letter;//品牌的首字母

	@ApiModelProperty("排序")
	private Integer seq;//排序

	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}

	public String getLetter() {
		return letter;
	}
	public void setLetter(String letter) {
		this.letter = letter;
	}

	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}



}
