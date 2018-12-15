package com.example.springLearning.pojo;

import com.github.pagehelper.PageInfo;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description TODO
 * @Author 樊立扬
 * @Date 2018/12/15 20:25
 * @Version 1.0
 * 分页类
 **/

@Data
public class DTO {

    private Integer size;  // 总共多少条
    private Integer index = 1;  // 当前页
    private List data = new ArrayList();  // 分页数据
    private Integer count = 10 ;  // 显示几页
    private boolean hasNext = true;
    private boolean hasPrevious = true;
    private Integer pageSum = 0;  // 总共多少页
    private Integer firstIndex = 1;  //
    private Integer lastIndex = this.firstIndex + this.count - 1 ;
    private Integer limit = 10;

    public DTO(Integer total , Integer limit ,Integer current ,List list){

        this.size = total;
        this.data =list;
        this.pageSum = (total-1)/limit+1;
        this.index = current;
        this.limit = limit;
        if(this.size <= this.count){
            this.hasNext = false;
        }

        if(this.lastIndex >= this.pageSum){
            this.lastIndex = this.pageSum;
        }

        if(this.index==1){
            this.hasPrevious = false;
        }
        if(this.index > ( (lastIndex-firstIndex)/2 + firstIndex )){
            this.firstIndex = current - 5;
            this.lastIndex = current + 5;
            // 当大于总页数时
            if(this.lastIndex > this.pageSum ){
                this.lastIndex = this.pageSum;
                this.firstIndex = this.lastIndex-this.count;
                this.hasNext = false;
            }
            if(this.firstIndex <= 1){
                this.firstIndex = 1;
            }
        }
    }



}
