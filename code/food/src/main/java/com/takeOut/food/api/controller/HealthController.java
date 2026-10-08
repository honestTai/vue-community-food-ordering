package com.takeOut.food.api.controller;

import com.takeOut.food.api.bean.general.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 本地部署健康检查，方便直接访问后端根地址确认服务状态。
 */
@RestController
public class HealthController {

    @GetMapping("/")
    public Result<Map<String, String>> health() {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("service", "takeout-food");
        data.put("status", "running");
        return Result.success(data);
    }
}
