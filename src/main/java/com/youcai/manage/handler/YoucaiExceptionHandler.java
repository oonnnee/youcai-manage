package com.youcai.manage.handler;

import com.youcai.manage.exception.YoucaiException;
import com.youcai.manage.utils.ResultVOUtils;
import com.youcai.manage.vo.ResultVO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class YoucaiExceptionHandler {

    @ExceptionHandler(YoucaiException.class)
    @ResponseBody
    public ResultVO handleYoucaiException(YoucaiException e){
        return ResultVOUtils.error(e.getCode(), e.getMessage());
    }
}
