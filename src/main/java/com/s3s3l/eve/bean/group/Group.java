/** 
 * Project Name:eve-helper 
 * File Name:Group.java 
 * Package Name:com.s3s3l.eve.bean.group 
 * Date:Jun 22, 20174:59:16 PM 
 * Copyright (c) 2017, kehw.zwei@gmail.com All Rights Reserved. 
 * 
*/

package com.s3s3l.eve.bean.group;

import com.s3s3l.eve.bean.base.Globalization;

/**
 * <p>
 * 物品组
 * </p>
 * ClassName:Group <br>
 * Date: Jun 22, 2017 4:59:16 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class Group {

    /**
     * 是否可以锚定
     */
    private Boolean anchorable;
    /**
     * 是否已经锚定
     */
    private Boolean anchored;
    /**
     * 类型ID
     */
    private Integer categoryID;
    /**
     * 是否不能独立安装（一般弹药类型的物品为true）
     */
    private Boolean fittableNonSingleton;
    /**
     * 名称
     */
    private Globalization name;
    /**
     * 是否为公开（玩家可用）物品（一般玩家能使用的为true）
     */
    private Boolean published;
    /**
     * 是否使用基础价格（即NPC价格，例如NPC蓝图）
     */
    private Boolean useBasePrice;

    public Boolean getAnchorable() {
        return anchorable;
    }

    public void setAnchorable(Boolean anchorable) {
        this.anchorable = anchorable;
    }

    public Boolean getAnchored() {
        return anchored;
    }

    public void setAnchored(Boolean anchored) {
        this.anchored = anchored;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public Boolean getFittableNonSingleton() {
        return fittableNonSingleton;
    }

    public void setFittableNonSingleton(Boolean fittableNonSingleton) {
        this.fittableNonSingleton = fittableNonSingleton;
    }

    public Globalization getName() {
        return name;
    }

    public void setName(Globalization name) {
        this.name = name;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public Boolean getUseBasePrice() {
        return useBasePrice;
    }

    public void setUseBasePrice(Boolean useBasePrice) {
        this.useBasePrice = useBasePrice;
    }
}
