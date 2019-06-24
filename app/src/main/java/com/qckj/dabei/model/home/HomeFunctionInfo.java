package com.qckj.dabei.model.home;

import android.support.annotation.NonNull;

import com.qckj.dabei.util.json.JsonField;

import java.io.Serializable;
import java.util.List;

/**
 * 首页功能点信息
 * <p>
 * Created by yangzhizhong on 2019/3/23.
 */
public class HomeFunctionInfo implements Serializable {

    @JsonField("F_C_ID")
    private String id;

    @JsonField("F_I_NUM")
    private String num;

    @JsonField("bname")
    private String name;

    @JsonField("category")
    private List<Category> categoryList;

    @JsonField("imgurl")
    private String imgUrl;

    private boolean isSelected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @NonNull
    @Override
    public String toString() {
        return "HomeFunctionInfo{" +
                "id='" + id + '\'' +
                ", num='" + num + '\'' +
                ", name='" + name + '\'' +
                ", categoryList=" + categoryList +
                ", imgUrl=" + imgUrl +
                '}';
    }

    public static class Category implements Serializable {

        public Category() {

        }

        @JsonField("TOWCLASSID")
        private String classId;


        @JsonField("TOWCLASSNAME")
        private String className;

        private boolean isSelected;

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public boolean getIsSelected() {
            return isSelected;
        }

        public void setIsSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

        @NonNull
        @Override
        public String toString() {
            return "Category{" +
                    "classId='" + classId + '\'' +
                    ", className='" + className + '\'' +
                    '}';
        }
    }

}
