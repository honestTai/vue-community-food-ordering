<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-calendar"></i> 餐品管理</el-breadcrumb-item>
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 餐品编辑
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <!--表单开始-->
        <div class="container">
            <el-form ref="form" :model="operationVto.goods" label-width="80px">
                <el-form-item label="名称">
                    <el-input v-model="operationVto.goods.name"></el-input>
                </el-form-item>
                <el-form-item label="分类">
                    <el-select v-model="operationVto.goods.classId" placeholder="请选择分类">
                        <el-option v-for="item in childrenListClass" :value="item.id"
                                   :key="item.id" :label="item.name">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="状态">
                    <el-radio-group v-model="operationVto.goods.status">
                        <el-radio :label="0">上架</el-radio>
                        <el-radio :label="1">下架</el-radio>
                    </el-radio-group>
                </el-form-item>
                <!--简介-->
                <el-form-item label="简介">
                    <el-input type="textarea" v-model="operationVto.goods.sort_introduce"></el-input>
                </el-form-item>
                <!--价格-->
                <el-form-item label="价格">
                    <el-input-number v-model="operationVto.goods.price" controls-position="right"  :min="1" :max="1000"></el-input-number>
                </el-form-item>
                <!--库存-->
                <el-form-item label="库存">
                    <el-input-number v-model="operationVto.goods.stock" controls-position="right"  :min="1" :max="1000"></el-input-number>
                </el-form-item>

                <el-form-item label="介绍">
                    <editor-bar :is="loadEdit" v-model="editor.info" :isClear="isClear" @change="change"></editor-bar>
                </el-form-item>
                <!--富文本框开始-->
                <!--照片开始-->
                <el-form-item label="轮播图片-3张">
                    <el-upload
                        action=""
                        ref="upload"
                        :http-request="goodsPhotoUpload"
                        list-type="picture-card"
                        :on-preview="handlePictureCardPreview"
                        :on-remove="handleRemove"
                        :limit="3"
                        :file-list="goodsPhotoShowList">
                        <i class="el-icon-plus"></i>
                    </el-upload>
                    <el-dialog :visible.sync="goodPhotoVisible">
                        <img width="100%" :src="goodsPhotoUrl" alt="">
                    </el-dialog>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="saveAndUpdateGoods">立即创建</el-button>
                </el-form-item>
            </el-form>
            <!--表单结束-->
        </div>
    </div>
</template>

<script>
import { childrenClass, uploadImage } from '@/utils';
import operateJs from '@/components/util/operateJS';
import EditorBar from '@/components/common/EditorBar';

export default {
    components: {
        EditorBar
    },
    data() {
        return {
            operationVto: {
                goods: {},
                operationType: null,
                type: 3
            },
            userType: '',
            childrenListClass: [],
            goodsPhotoUrl: '',
            goodPhotoVisible: false,
            goodsPhotoList: [],
            //false add true update
            goodsStatus: false,
            //回显List
            goodsPhotoShowList: [],
            //富文本框有关
            loadEdit: null,
            isClear: false,
            editor: {
                info: ''
            },
        };
    },
    created() {
        var that = this;
        that.loadEdit = EditorBar;
        let goods = JSON.parse(that.$route.query.goods);
        if (goods == 1) {
            that.goodsStatus = false;
            that.operationVto.goods = {
                name: '',
                classId: '',
                goodsPhotoList: [],
                introduce: '',
                status: 0
            };
        } else if (goods != 1) {
            that.operationVto.goods = goods;
            that.editor.info = goods.introduce;
            //构造出符合要求的图片回显String
            if (that.operationVto.goods.goodsPhotoList != null) {
                that.goodsPhotoList = that.operationVto.goods.goodsPhotoList;

                that.goodsPhotoList.forEach(function(photo, index) {
                        let photoShow = {};
                        photoShow.name = index;
                        photoShow.url = photo;
                        that.goodsPhotoShowList.push(photoShow);
                    }
                );
            }
            that.goodsStatus = true;
        }
        childrenClass().then(res => {
            that.childrenListClass = res.data.data;
        });
    },
    methods: {
        selectRow(val) {
            console.log(val);
            this.selectlistRow = val;
        },
        //富文本框中内容的修改
        change(val) {
            this.editor.info = val;
            this.operationVto.goods.introduce = val;
        },
        //请求，暂没有表单验证
        saveAndUpdateGoods() {
            var that = this;
            if (that.goodsStatus) {
                that.operationVto.operationType = 0;
                that.operationVto.goods.goodsPhotoList = that.goodsPhotoList;
                operateJs.systemOperation(that.operationVto);
            } else {
                that.operationVto.operationType = 1;
                operateJs.systemOperation(that.operationVto);
            }
            //等待2秒，回到list页面，关闭当前页面
            setTimeout(function() {
                //关闭当前窗口,框架问题，无法关闭
                that.$router.push({
                    path: '/goodsList'
                });
            }, '2000');
        },
        handleRemove(file, fileList) {
            //移除元素
            this.goodsPhotoShowList = fileList;
            //需要到后台的图片LIST根据名字移除
            this.goodsPhotoList = this.goodsPhotoList.filter(function(item) {
                return item != file.url;
            });
        },
        handlePictureCardPreview(file) {
            this.goodsPhotoUrl = file.url;
            this.goodPhotoVisible = true;
        },
        //手动上传图片，需要鉴权
        goodsPhotoUpload(file) {
            var that = this;
            let formData = new FormData();
            formData.append('image', file.file);
            uploadImage(formData).then(res => {
                //构造图片LIST
                that.goodsPhotoList.push(res.data.data);
            });
            let list = that.goodsPhotoList;
            list.forEach(function(photo, index) {
                    let photoShow = {};
                    photoShow.name = index;
                    photoShow.url = photo;
                    that.goodsPhotoShowList.push(photoShow);
                }
            );
            if (that.goodsPhotoShowList.length > 3) {
                that.goodsPhotoShowList = that.goodsPhotoShowList.slice(0, 3);
            }
            if (that.goodsPhotoList.length > 3) {
                that.goodsPhotoList = that.goodsPhotoList.slice(0, 3);
            }

        }
    }
};
</script>