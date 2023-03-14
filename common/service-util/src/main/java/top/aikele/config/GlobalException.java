package top.aikele.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import top.aikele.common.reslut.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        System.out.println("发生了异常:"+e);
        return Result.fail();
    }
   public static class myException extends Exception{
      private int code;
      private String message;
      public myException(int code, String message){
          this.code=code;
          this.message=message;
      }

       @Override
       public String toString() {
           return "myException{" +
                   "code=" + code +
                   ", message='" + message + '\'' +
                   '}';
       }
   }


}
