package top.aikele.common.reslut;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("通用返回")
public class Result<T> {
    @ApiModelProperty("状态码")
    private Integer code;
    @ApiModelProperty("消息")
    private String message;
    @ApiModelProperty("数据")
    private T data;

    private Result(){}

    public static<T> Result<T> ok(){
        return build(null,ResultCodeEnum.SUCCESS);
    }
    public static<T> Result<T> ok(T data){
        return build(data,ResultCodeEnum.SUCCESS);
    }
    public static<T> Result<T> fail() {
        return build(null,ResultCodeEnum.FAIL);
    }
    public static<T> Result<T> fail(T data) {
        return build(data,ResultCodeEnum.FAIL);
    }
    private static<T> Result<T> build(T data,ResultCodeEnum resultCodeEnum){
        Result<T> result = new Result<>();
        if(data!=null)
            result.setData(data);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }
}
