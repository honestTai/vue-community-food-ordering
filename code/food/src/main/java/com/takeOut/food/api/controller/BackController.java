package com.takeOut.food.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeOut.food.api.bean.dto.StatisticsOrderDto;
import com.takeOut.food.api.bean.general.result.Result;
import com.takeOut.food.api.bean.general.result.ResultException;
import com.takeOut.food.api.bean.general.result.ResultStatus;
import com.takeOut.food.api.bean.vto.*;
import com.takeOut.food.api.entity.Class;
import com.takeOut.food.api.entity.User;
import com.takeOut.food.constant.operationConstants;
import com.takeOut.food.util.File;
import com.takeOut.food.util.UserLocal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.takeOut.food.util.BaseFunction.chinaNameToEnglishName;

/**
 * 后台统一Add,Update,delete,Select接口
 */
@RestController
@RequestMapping("/back")
public class BackController extends BaseController {


    /**
     * 后台用户登录
     * 管理员与商店用户
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginVto loginVto) throws ResultException {
        return Result.success(userService.login(loginVto));
    }

    /**
     * 退出登录
     */
    @GetMapping("/loginOut")
    public void loginOut() {
        SecurityContextHolder.clearContext();
    }

    /**
     * <p>
     * 查询所有列表
     */
    @PostMapping("/list")
    public Page<?> list(@RequestBody ListVto listVto) throws ResultException {
        Integer listType = listVto.getListType();
        return Objects.equals(listType, operationConstants.USER) ? userService.userList(listVto) : (Objects.equals(listType, operationConstants.CLASS) ? classService.classList(listVto) : (Objects.equals(listType, operationConstants.GOODS) ? goodsService.goodsList(listVto) : (Objects.equals(listType, operationConstants.ORDER) ? orderService.orderList(listVto) : evaluationService.evaluaList(listVto))));
    }


    /**
     * 统一数据库增加，修改接口
     */
    @PostMapping("/operation")
    public void operation(@RequestBody OperationVto operationVto) throws ResultException {
        Integer operationType = operationVto.getOperationType();
        Integer type = operationVto.getType();
        if (Objects.equals(type, operationConstants.USER)) {
            userService.operationUser(operationVto.getUser(), operationType);
        } else if (Objects.equals(type, operationConstants.CLASS)) {
            classService.operationClass(operationVto.getAClass(), operationType);
        } else if (Objects.equals(type, operationConstants.GOODS)) {
            goodsService.operationGoods(operationVto.getGoods(), operationType);
        } else if (Objects.equals(type, operationConstants.ORDER)) {
            orderService.operationOrder(operationVto, operationType);
        } else if (Objects.equals(type, operationConstants.COMMENT)) {
            //评论只能修改
            evaluationService.operationEvaluation(operationVto.getEvaluation());
        }
    }

    /**
     * 获取顶级分类接口
     * 不分页
     */
    @GetMapping("/parentClass")
    public List<Class> parentClass() {
        return classService.list(new QueryWrapper<Class>().eq("parent_id", 0));
    }

    /**
     * 获取末级分类
     * 不分页
     */
    @GetMapping("/childrenClass")
    public List<Class> childrenClass() {
        return classService.list(new QueryWrapper<Class>().ne("parent_id", 0));
    }

    /**
     * 上传图片,登录拦截
     */
    @PostMapping("/uploadGoodsPhoto")
    @ResponseBody
    public Result uploadImgAddUser(@RequestParam("image") MultipartFile uploadFile) throws Exception {
        return File.upload(uploadFile, filePath, fileUrl);
    }

    /**
     * 首页统计接口模块
     */
    @PostMapping("/statisticsOrder")
    public List<StatisticsOrderDto> statisticsOrder(@RequestBody StatisticsVto statisticsVto) throws ParseException {
        return orderService.statisticsOrder(statisticsVto);
    }

    /**
     * 订单导出，且鉴权
     * 首先生成导出的Excel到指定文件夹
     * 然后生成下载按钮，并且存入密钥Key进入Redis,由Redis进行鉴权处理
     * 使用easy-poi 进行导出处理
     */
    @PostMapping("/export")
    @ResponseBody
    public void export(@RequestBody ListVto listVto,HttpServletRequest request,HttpServletResponse response) throws IOException, ResultException {
        orderService.export(filePath, listVto,request,response);
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
