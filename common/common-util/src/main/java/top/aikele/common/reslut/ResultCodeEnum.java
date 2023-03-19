package top.aikele.common.reslut;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {
    SUCCESS(200,"成功"),
    FAIL(201,"失败"),
    LOGIN_MOBLE_ERROR(202,"登录失败");
    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code,String message){
        this.code=code;
        this.message=message;
    }
}
