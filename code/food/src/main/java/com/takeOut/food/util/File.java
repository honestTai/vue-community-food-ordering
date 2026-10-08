package com.takeOut.food.util;

import com.takeOut.food.api.bean.general.result.Result;
import com.takeOut.food.api.bean.general.result.ResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class File {

    @Autowired
    RedisUtil redisUtil;

    /**
     * 常量注入bean
     */
    private static File file;

    @PostConstruct
    public void init() {
        file = this;
        file.redisUtil = this.redisUtil;
    }

    /**
     * 上传
     *
     * @param uploadFile
     * @param filePath
     * @param fileUrl
     * @return
     * @throws Exception
     */
    public static Result upload(MultipartFile uploadFile, String filePath, String fileUrl) throws Exception {

        String fileName = uploadFile.getOriginalFilename();
        fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + fileName;
        //加个时间戳，尽量避免文件名称重复
        String path = filePath + fileName;
        //创建文件路径
        java.io.File dest = new java.io.File(path);
        java.io.File parent = dest.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new Exception("创建上传目录失败：" + parent.getAbsolutePath());
        }
        try {
            //保存文件
            uploadFile.transferTo(dest);
            //构造Url
            return Result.success(fileUrl != null ? fileUrl + fileName : fileName);
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 下载
     */
    public static void downLoad(HttpServletRequest request, HttpServletResponse response, String fileName, String merchantFile) throws FileNotFoundException, ResultException {
        String path = merchantFile + fileName;
        InputStream inputStream = new FileInputStream(path);
        response.reset();
        response.setContentType("application/x-msdownload");
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        // 循环取出流中的数据
        byte[] b = new byte[100];
        int len;
        try {
            while ((len = inputStream.read(b)) > 0)
                response.getOutputStream().write(b, 0, len);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
