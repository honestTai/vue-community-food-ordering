<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-calendar"></i> 分类管理</el-breadcrumb-item>
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 分类列表
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-button
                    type="primary"
                    icon="el-icon-lx-add"
                    class="handle-del mr10"
                    @click="addClass"
                >新增分类
                </el-button>
            </div>
            <div class="handle-box">
                <el-select v-model="query.classId" placeholder="分类查找" class="handle-select mr10"
                           @change="searchByClassId"
                           clearable
                           filterable>
                    <el-option key="" label="全部分类" value=""></el-option>
                    <el-option v-for="item in listParentClass" :value="item.id"
                               :key="item.id" :label="item.name">
                    </el-option>
                </el-select>
            </div>
            <el-table
                :data="listClass"
                style="width: 100%;margin-bottom: 20px;"
                row-key="id"
                border
                default-expand-all
                :tree-props="{children: 'children'}">
                <el-table-column
                    prop="id"
                    label="序号"
                    width="180">
                </el-table-column>
                <el-table-column
                    prop="name"
                    label="名称"
                >
                </el-table-column>
                <el-table-column label="操作" width="180" align="center" fixed="right">
                    <template slot-scope="scope">
                        <el-button
                            type="text"
                            icon="el-icon-edit"
                            class="green"
                            @click="updateClass(scope.$index,scope.row)"
                        >编辑
                        </el-button>
                        <el-button
                            type="text"
                            icon="el-icon-delete"
                            class="red"
                            @click="handleDelete(scope.$index, scope.row.id)"
                            v-if="scope.row.parentId!=0 || scope.row.children ===null "
                        >删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
<!--            <div class="pagination">-->
<!--                <el-pagination-->
<!--                    background-->
<!--                    layout="total,sizes, prev, pager, next, jumper"-->
<!--                    @size-change="handlePageSizeChange"-->
<!--                    @current-change="handlePageChange"-->
<!--                    :current-page="query.page"-->
<!--                    :page-sizes="[10, 50, 100, 200,500,1000]"-->
<!--                    :page-size="query.pageSize"-->
<!--                    :total="pageTotal"-->
<!--                >-->
<!--                </el-pagination>-->
<!--            </div>-->
        </div>
        <el-dialog title="修改分类" :visible.sync="updateClassForm" width="50%">
            <el-form ref="form" :model="operationVto" label-width="70px">
                <el-form-item label="名称">
                    <el-input v-model="operationVto.aclass.name" @keyup.enter.native="saveUpdate(0)"></el-input>
                </el-form-item>
                <!--图片-->
                <el-form-item label="分类图片" v-if="uploadShow==true">
                    <el-upload
                        action=""
                        ref="upload"
                        :http-request="goodsPhotoUpload"
                        list-type="picture-card"
                        :on-preview="handlePictureCardPreview"
                        :on-remove="handleRemove"
                        :limit="1"
                        :file-list="goodsPhotoShowList">
                        <i class="el-icon-plus"></i>
                    </el-upload>
                    <el-dialog :visible.sync="goodPhotoVisible">
                        <img width="100%" :src="goodsPhotoUrl" alt="">
                    </el-dialog>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
        <el-button @click="updateClassForm = false">关闭</el-button>
        <el-button type="primary" @click="saveUpdate(0)">修改</el-button>
      </span>
        </el-dialog>
        <el-dialog title="新增分类" :visible.sync="addClassForm" width="50%">
            <el-form ref="form" :model="operationVto" label-width="70px">
                <el-form-item label="上级名称">
                    <el-select v-model="operationVto.aclass.parentId" placeholder="请选择" clearable
                               @change="selectParentId">
                        <el-option value="0" label="请选择">
                        </el-option>
                        <el-option v-for="item in this.listParentClass"
                                   :value="item.id"
                                   :key="item.id"
                                   :label="item.name">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="名称">
                    <el-input v-model="operationVto.aclass.name" @keyup.enter.native="saveUpdate(1)"></el-input>
                </el-form-item>
                <!--图片-->
                <el-form-item label="分类图片" v-if="uploadShow==true">
                    <el-upload
                        action=""
                        ref="upload"
                        :http-request="goodsPhotoUpload"
                        list-type="picture-card"
                        :on-preview="handlePictureCardPreview"
                        :on-remove="handleRemove"
                        :limit="1"
                        :file-list="goodsPhotoShowList">
                        <i class="el-icon-plus"></i>
                    </el-upload>
                    <el-dialog :visible.sync="goodPhotoVisible">
                        <img width="100%" :src="goodsPhotoUrl" alt="">
                    </el-dialog>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
        <el-button @click="addClassForm = false">关闭</el-button>
        <el-button type="primary" @click="saveUpdate(1)">新增</el-button>
      </span>
        </el-dialog>
    </div>
</template>


<script>
import { list, parentClass, uploadImage } from '../../../utils';
import operateJs from '@/components/util/operateJS';

export default {
    data() {
        return {
            query: {
                page: 1,
                pageSize: 100,
                classId: '',
                listType: 2,
                likeName: ''
            },
            updateClassForm: false,
            addClassForm: false,
            pageTotal: 0,
            listClass: [],
            listParentClass: [],
            operationVto: {
                aclass: {
                    id: '',
                    name: '',
                    parentId: '0',
                    picUrl: ''
                },
                operationType: null,
                type: 2
            },
            //是否显示上传图片标识
            uploadShow: false,
            goodsPhotoShowList: [],
            goodPhotoVisible: false,
            goodsPhotoUrl: ''
        };
    },
    created() {
        this.getListClass();
        this.getListPrentClass();
    },
    methods: {
        selectParentId(val) {
            console.log(val)
            if (val == 0) {
                this.uploadShow = false;
            } else {
                this.uploadShow = true;
            }
        },
        getListClass() {
            list(this.query).then(res => {
                let data = res.data;
                // this.pageTotal = data.data.total;
                this.listClass = data.data.records;
            });
        },
        getListPrentClass() {
            parentClass().then(value => {
                this.listParentClass=value.data.data
            });
        },
        //分类查找
        searchByClassId(val) {
            console.log(val);
            this.$set(this.query, 'classId', val);
            this.getListClass();
        },
        //删除操作
        handleDelete(index, row) {
            var that = this;
            // 二次确认删除
            that.$confirm('确定要删除吗？', '提示', {
                type: 'warning'
            })
                .then(() => {
                    that.operationVto.aclass.id = row;
                    that.operationVto.operationType = 2;
                    operateJs.systemOperation(that.operationVto);
                    /**
                     * 等到2秒刷新
                     */
                    setTimeout(function() {
                        that.getListClass();
                        this.getListPrentClass();
                    }, '2000');

                })
                .catch(() => {
                });
        },
        // //分页跳转
        handlePageChange(val) {
            this.$set(this.query, 'page', val);
            this.getListClass();
        },
        //每页大小改变
        handlePageSizeChange(val) {
            this.$set(this.query, 'pageSize', val);
            this.getListClass();
        },
        // //增加分类
        addClass() {
            this.addClassForm = true;
        },
        //修改分类
        updateClass(index, row) {
            let photoShow = {};
            this.goodsPhotoShowList = [];
            //判断parentId是否等于0
            if (row.parentId != 0) {
                this.uploadShow = true;
                if (row.picUrl != null) {
                    photoShow.name = row.picUrl;
                    photoShow.url = row.picUrl;
                    this.goodsPhotoShowList.push(photoShow);
                }
            }
            this.updateClassForm = true;
            this.operationVto.aclass = row;

        },
        //保存修改/新增
        saveUpdate(val) {
            this.operationVto.operationType = val;
            console.log(this.operationVto);
            operateJs.systemOperation(this.operationVto);
            this.updateClassForm = false;
            this.addClassForm = false;
            this.goodsPhotoUrl=""
            if (val === 1) {
                this.getListClass();
            }
            //操作完毕清空值
            this.operationVto = {
                aclass: {
                    id: '',
                    name: '',
                    parentId: '',
                    picUrl: null
                },
                operationType: null,
                type: 2
            };

            /**
             * 等到2秒刷新
             */
            setTimeout(function() {
                list({
                    page: 1,
                    pageSize: 10,
                    classId: '',
                    listType: 2,
                    likeName: ''
                }).then(res => {
                    let data = res.data;
                    this.pageTotal = data.data.total;
                    this.listClass = data.data.records;
                });
                this.getListPrentClass();

            }, '2000');

        },
        //手动上传图片，需要鉴权
        goodsPhotoUpload(file) {
            var that = this;
            let formData = new FormData();
            formData.append('image', file.file);
            uploadImage(formData).then(res => {
                //构造图片LIST
                that.operationVto.aclass.picUrl = res.data.data;
                let photoShow = {};
                photoShow.name = res.data.data;
                photoShow.url = res.data.data;
                that.goodsPhotoShowList.push(photoShow);
            });


            if (that.goodsPhotoShowList.length > 1) {
                that.goodsPhotoShowList = that.goodsPhotoShowList.slice(0, 1);
            }

        },
        handleRemove(file, fileList) {
            //移除元素
            this.goodsPhotoShowList = fileList;
            //需要到后台的图片LIST根据名字移除
            this.operationVto.aclass.picUrl = null;
        },
        handlePictureCardPreview(file) {
            this.goodsPhotoUrl = file.url;
            this.goodPhotoVisible = true;
        }
    }
};
</script>

<style scoped>
.handle-box {
    margin-bottom: 20px;
}

.handle-select {
    width: 120px;
}

.handle-input {
    width: 300px;
    display: inline-block;
}

.table {
    width: 100%;
    font-size: 14px;
}

.red {
    color: #ff0000;
}

.mr10 {
    margin-right: 10px;
}

</style>
