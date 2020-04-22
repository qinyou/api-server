package com.qinyou.apiserver.core.controller;

import cn.hutool.core.date.DateUtil;
import com.qinyou.apiserver.core.base.RequestException;
import com.qinyou.apiserver.core.base.Result;
import com.qinyou.apiserver.core.base.ResultEnum;
import com.qinyou.apiserver.core.utils.WebUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Api(tags = "文件")
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Value("${app.upload.upload-folder}")
    String uploadFileFolder;  // 存盘路径

    @Value("${app.upload.access-path}")
    String uploadAccessPath;  // 访问路径

    @ApiOperation("文件上传, 表单key为 file")
    @PostMapping("/upload")
    @ResponseBody
    public Result<String> upload(HttpServletRequest req, @RequestParam("file") MultipartFile file) {
        String path = null;
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String date = DateUtil.format(new Date(), "yyyy_MM_dd");
            path = date + "/" + fileName;
            String destFileName = uploadFileFolder + path;
            File destFile = new File(destFileName);
            if (!destFile.getParentFile().exists()) {
                if (!destFile.getParentFile().mkdirs()) {
                    throw RequestException.fail(ResultEnum.UPLOAD_FAIL);
                }
            }
            file.transferTo(destFile);
            log.debug("dest file path: {}", destFile.getAbsolutePath());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw RequestException.fail(ResultEnum.UPLOAD_FAIL);
        }
        // /upload-file/**  变为  upload-file/
        return WebUtils.ok(uploadAccessPath.replaceAll("\\**", "").substring(1) + path);
    }
}
