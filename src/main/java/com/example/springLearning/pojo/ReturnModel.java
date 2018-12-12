package com.example.springLearning.pojo;

/**
 *  Ajax返回数据类
 * @param <T>
 * @author wgb
 */
public class ReturnModel<T> {
    private Integer status = -1;        //状态码: 0 成功, 1 失败
    private String type = "error";       //返回更新结果 , OK 成功, error失败
    private String message;             //提示信息
    private Integer total;              //表格数据 : 总数
    private T data;                     //对象 / 集合

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
