<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-calendar"></i> 餐品管理</el-breadcrumb-item>
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 餐品列表
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-button
                    type="primary"
                    icon="el-icon-lx-add"
                    class="handle-del mr10"
                    @click="goodsOperation('','1',0,'')"
                >新增餐品
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
            <!--名称查找-->
            <div class="handle-box">
                <el-input v-model="query.likeName" placeholder="请输入餐品名称" class="handle-input mr10"
                          @change="searchLikeName"></el-input>
            </div>
            <el-table
                :data="listGoods"
                style="width: 100%;margin-bottom: 20px;"
                row-key="id"
                border
                default-expand-all
            >
                <el-table-column
                    label="名称"
                    prop="goods.name"
                >
                </el-table-column>
                <el-table-column
                    prop="goods.price"
                    label="价格"
                >
                </el-table-column>
                <el-table-column
                    prop="goods.sort_introduce"
                    label="简介"
                >
                </el-table-column>
                <el-table-column
                    prop="aclass.name"
                    label="所属分类"
                >
                </el-table-column>
                <el-table-column
                    prop="tag"
                    label="状态"
                    width="100"
                    :filters="[{ text: '上架', value: 0 }, { text:'下架', value: 1 }]"
                    :filter-method="filterTag"
                    filter-placement="bottom-end">
                    <template slot-scope="scope">
                        <el-tag
                            :type="scope.row.goods.status === 1 ? 'primary' : 'success'"
                            disable-transitions>{{ scope.row.goods.status === 0 ? '上架' : '下架' }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="350" align="center" fixed="right">
                    <!--商店用户操作栏-->
                    <template slot-scope="scope">
                        <el-button
                            size="mini"
                            @click="goodsOperation(scope.$index, scope.row.goods,1,'')">编辑
                        </el-button>
                        <el-button
                            size="mini"
                            @click="goodsOperation(scope.$index, scope.row.goods,5,'')">查看评价
                        </el-button>
                        <el-button
                            size="mini"
                            type="danger"
                            @click="goodsOperation(scope.$index, scope.row.goods,2,'')">删除
                        </el-button>
                        <el-button
                            size="mini"
                            type="success"
                            v-if="scope.row.goods.status===1"
                            @click="goodsOperation(scope.$index, scope.row.goods,3,0)">上架
                        </el-button>
                        <el-button
                            size="mini"
                            type="danger"
                            v-if="scope.row.goods.status===0"
                            @click="goodsOperation(scope.$index, scope.row.goods,4,1)">下架
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination
                    background
                    layout="total,sizes, prev, pager, next, jumper"
                    @size-change="handlePageSizeChange"
                    @current-change="handlePageChange"
                    :current-page="query.page"
                    :page-sizes="[10, 50, 100, 200,500,1000]"
                    :page-size="query.pageSize"
                    :total="pageTotal"
                >
                </el-pagination>
            </div>
        </div>
        <el-dialog title="评论查看" :visible.sync="commentListView" width="80%">
            <div class="block">
                <el-timeline>
                    <el-timeline-item :timestamp="item.time" placement="top" v-for="(item,index) in commentList"
                                      :key="index">
                        <!--回复输入框-->
                        <div v-if="item.replyStatus ===0">
                            <el-input
                                type="textarea"
                                placeholder="请输入内容"
                                style="padding: 10px"
                                v-model="replyComments[index]"
                            >
                            </el-input>
                            <el-button type="primary" plain @click="replyComment(index,item)">提交回复</el-button>
                        </div>
                        <el-divider content-position="center"></el-divider>
                        <el-card>
                            <h4>内容: {{ item.content }}</h4>
                            <p>{{ item.userName }} 评论于 {{ item.time }}</p>
                            <div v-if="item.replyStatus === 1">
                                <el-divider content-position="center">回复内容</el-divider>
                                <h4>内容: {{ item.replyContent }}</h4>
                                <p>回复于 {{ item.replyTime }}</p>
                            </div>
                        </el-card>

                    </el-timeline-item>
                </el-timeline>
            </div>
            <div class="pagination">
                <el-pagination
                    background
                    layout="total, prev, pager, next, jumper"
                    @current-change="handlePageChangeByComment"
                    :current-page="commentQuery.page"
                    :total="commentPageTotal"
                >
                </el-pagination>
            </div>
            <span slot="footer" class="dialog-footer">
        <el-button @click="commentListView = false">关闭</el-button>
      </span>
        </el-dialog>
    </div>
</template>

<style>
.demo-table-expand {
    font-size: 0;
}

.demo-table-expand label {
    width: 90px;
    color: #99a9bf;
}

.demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
}
</style>
<script>
import { list, parentClass } from '../../../utils';
import operateJs from '@/components/util/operateJS';

export default {
    data() {
        return {
            query: {
                page: 1,
                pageSize: 10,
                classId: '',
                listType: 3,
                likeName: ''
            },
            updateGoodsForm: false,
            addGoodsForm: false,
            pageTotal: 0,
            listGoods: [],
            listParentClass: [],
            operationVto: {
                goods: {
                    //主键
                    id: '',
                    //状态
                    status: 0
                },
                operationType: null,
                type: 3
            },
            userType: '',
            commentQuery: {
                page: 1,
                pageSize: 5,
                listType: 5,
                goodsId: ''
            },
            commentListView: false,
            commentOperationVto: {
                evaluation: {},
                operationType: 0,
                type: 5
            },
            //评论列表
            commentList: [],
            commentPageTotal: 0,
            replyComments: []
        };
    },
    created() {
        this.userType = JSON.parse(localStorage.getItem('userInfo')).type;
        this.getListGoods();
        if (this.userType != 2) {
            this.getListPrentClass();
        }
    },
    methods: {
        filterTag(value, row) {
            return row.goods.status === value;
        },
        getListGoods() {
            list(this.query).then(res => {
                let data = res.data;
                this.pageTotal = data.data.total;
                this.listGoods = data.data.records;
            });
        },
        getListPrentClass() {
            parentClass().then(value => {
                this.listParentClass=value.data.data
            });
        },
        //分类查找
        searchByClassId(val) {
            this.$set(this.query, 'classId', val);
            this.getListGoods();
        },
        //名称模糊查询
        searchLikeName(val) {
            this.$set(this.query, 'likeName', val);
            this.getListGoods();
        },
        /**
         * @param index
         * @param row
         * @param opearType 0增1修2删除3上架4下架
         */
        goodsOperation(index, row, opearType, goodsStatus) {
            var that = this;
            if (opearType === 2) {
                // 二次确认删除
                that.$confirm('确定要删除吗？', '提示', {
                    type: 'warning'
                })
                    .then(() => {
                        that.operationVto.goods.id = row.id;
                        that.operationVto.operationType = 2;
                        operateJs.systemOperation(that.operationVto);
                        /**
                         * 等到2秒刷新
                         */
                        setTimeout(function() {
                            that.getListGoods();
                        }, '2000');
                    })
                    .catch(() => {
                    });
            } else if (opearType === 3 || opearType === 4) {
                that.operationVto.goods.id = row.id;
                that.operationVto.operationType = 0;
                that.operationVto.goods.status = goodsStatus;
                operateJs.systemOperation(that.operationVto);
                /**
                 * 等到2秒刷新
                 */
                setTimeout(function() {
                    that.getListGoods();
                }, '2000');
            } else if (opearType === 0 || opearType === 1) {
                //跳转到新增与修改页面，将改列的所有数据传入
                this.$router.push({
                    path: '/goodsOperation',
                    query: {
                        goods: JSON.stringify(row)
                    }
                });
            } else if (opearType === 5) {
                //评价查看
                that.commentQuery.goodsId = row.id;
                that.commentListGet();
                that.commentListView = true;
            }

        },
        commentListGet() {
            var that = this;
            list(that.commentQuery).then(res => {
                let data = res.data;
                that.commentList = data.data.records;
                that.commentPageTotal = data.data.total;
            });
        },
        // //分页跳转
        handlePageChange(val) {
            this.$set(this.query, 'page', val);
            this.getListGoods();
        },
        //每页大小改变
        handlePageSizeChange(val) {
            this.$set(this.query, 'pageSize', val);
            this.getListGoods();
        },
        handlePageChangeByComment(val) {
            this.$set(this.commentQuery, 'page', val);
            this.commentListGet();
        },
        //提交回复
        replyComment(index, data) {
            var that = this;
            that.commentOperationVto.evaluation.id = data.id;
            that.commentOperationVto.evaluation.replyContent = that.replyComments[index];
            operateJs.systemOperation(that.commentOperationVto);
            /**
             * 等到2秒刷新
             */
            setTimeout(function() {
                that.commentListGet();
                that.commentOperationVto = {
                    evaluation: {},
                    operationType: 0,
                    type: 5
                };
            }, '2000');
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
