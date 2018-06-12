package com.youcai.manage.handler;

import com.youcai.manage.exception.ManageException;
import com.youcai.manage.utils.ResultVOUtils;
import com.youcai.manage.vo.ResultVO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ManageExceptionHandler {

    @ExceptionHandler(ManageException.class)
    @ResponseBody
    public ResultVO handleManageException(ManageException e){
        return ResultVOUtils.error(e.getCode(), e.getMessage());
    }
}
