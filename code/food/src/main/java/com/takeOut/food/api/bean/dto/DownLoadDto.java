package com.takeOut.food.api.bean.dto;

import lombok.Data;

/**
 * 导出下载的实体
 */
@Data
public class DownLoadDto {

    private String fileName;

    private String key;

    public DownLoadDto(String fileName, String key) {
        this.fileName = fileName;
        this.key = key;
    }
}
