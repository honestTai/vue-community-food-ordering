package com.takeOut.food.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeOut.food.api.bean.dto.CommentDto;
import com.takeOut.food.api.bean.dto.GoodsDto;
import com.takeOut.food.api.bean.general.result.Result;
import com.takeOut.food.api.bean.general.result.ResultException;
import com.takeOut.food.api.bean.vto.AppLoginVto;
import com.takeOut.food.api.bean.vto.AppPageVto;
import com.takeOut.food.api.bean.vto.GoodsPageAppVto;
import com.takeOut.food.api.entity.Class;
import com.takeOut.food.api.entity.User;
import com.takeOut.food.util.File;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * App接口，无需登录访问
 */
@RestController
@RequestMapping("/appNoLogin")
@CrossOrigin
public class AppNoLoginController extends BaseController{
    /**
     * App登录
     *
     * @param appLoginVto App的Code
     * @return
     * @throws ResultException 自定义的错误
     */
    @PostMapping("/login")
    public Result login(@RequestBody AppLoginVto appLoginVto) throws ResultException {
        return Result.success(userService.login(appLoginVto));
    }

    /**
     * 餐品
     */
    @PostMapping("/goodsPage")
    public Result goodsPage(@RequestBody GoodsPageAppVto goodsPageAppVto) {
        return Result.success(goodsService.goodsPageByApp(goodsPageAppVto));
    }



    /**
     * 餐品分类接口获取
     */
    @GetMapping("/tree")
    public List<Class> classTree() {
        return classService.classTree();
    }


    /**
     * 餐品详情查看接口
     */
    @GetMapping("/goodsDetail/{goodsId}")
    public GoodsDto goodsDetail(@PathVariable("goodsId") Integer goodsId) {
        return goodsService.goodsDetail(goodsId);
    }

    /**
     * 单个餐品评价查看
     */
    @PostMapping("/goodsCommentPage")
    public Page<CommentDto> goodsCommentPage(@RequestBody AppPageVto appPageVto) {
        return evaluationService.goodsCommentPage(appPageVto);
    }

    /**
     * App注册
     *
     * @param user
     * @return
     * @throws ResultException 自定义的错误
     */
    @PostMapping("/register")
    public void register(@RequestBody User user) throws ResultException {
        userService.registerAppUser(user);
    }

    /**
     * 修改用户基本信息
     */
    @PostMapping("/updateUserInfo")
    public void updateUserInfo(@RequestBody User user) throws ResultException {
        userService.updateUserInfo(user);
    }

    /**
     * 上传头像拦截
     */
    @PostMapping("/uploadMerchantUserHead")
    @ResponseBody
    public Result uploadMerchantUserHead(@RequestParam("head") MultipartFile uploadFile) throws Exception {
        String fileUrls = fileUrl + "head/";
        String filePaths = filePath + "head/";
        return File.upload(uploadFile, filePaths, fileUrls);
    }


}
