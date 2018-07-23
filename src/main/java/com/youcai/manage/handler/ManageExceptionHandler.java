package com.youcai.manage.handler;

import com.youcai.manage.exception.HintException;
import com.youcai.manage.exception.ManageException;
import com.youcai.manage.utils.ResultVOUtils;
import com.youcai.manage.vo.ResultVO;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
public class ManageExceptionHandler {

    @ExceptionHandler(ManageException.class)
    @ResponseBody
    public ResultVO handleManageException(ManageException e){
        return ResultVOUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(HintException.class)
    @ResponseBody
    public ResultVO handleGuestException(HintException e){
        return ResultVOUtils.success(e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResultVO handleGuestException(BindException e){
        return ResultVOUtils.error(e.getAllErrors().get(0).getDefaultMessage());
    }

}
