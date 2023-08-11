package com.sdu.exception;

import com.sdu.entity.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局统一异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 运行时异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = RuntimeException.class)
    public R handler(RuntimeException e){
        log.error("运行时异常：----------------------{}",e.getMessage());
        System.out.println("运行时异常");
        return R.error(e.getMessage());
    }

}
