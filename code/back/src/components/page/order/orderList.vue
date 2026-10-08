<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-calendar"></i> 订单管理</el-breadcrumb-item>
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 订单列表
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
<!--            <div class="handle-box">-->
<!--                <el-button-->
<!--                    type="primary"-->
<!--                    icon="el-icon-lx-add"-->
<!--                    class="handle-del mr10"-->
<!--                    @click="exportOrderData()"-->
<!--                >导出-->
<!--                </el-button>-->
<!--            </div>-->
            <div class="handle-box">
                <el-select v-model="query.classId" placeholder="分类查找" class="handle-select mr10"
                           @change="searchByClassId"
                           clearable
                           filterable>
                    <el-option key="" label="全部分类" value=""></el-option>
                    <el-option v-for="item in listChildrenClass" :value="item.id"
                               :key="item.id" :label="item.name">
                    </el-option>
                </el-select>
            </div>
            <!--名称查找-->
            <div class="handle-box">
                <el-input v-model="query.likeName" placeholder="请输入餐品名称" class="handle-input mr10"
                          @change="searchLikeName"></el-input>
            </div>
            <div class="handle-box">
                <el-input v-model="query.phone" placeholder="请输入用户手机号" class="handle-input mr10"
                          @change="searchPhone"></el-input>
            </div>
            <div class="handle-box">
                <el-input v-model="query.orderString" placeholder="请输入订单编号" class="handle-input mr10"
                          @change="searchOrderString"></el-input>
            </div>
            <div class="handle-box">
                <el-input v-model="query.userName" placeholder="请输入用户名称" class="handle-input mr10"
                          @change="searchUserName"></el-input>
            </div>
            <el-table
                :data="listOrders"
                style="width: 100%;margin-bottom: 20px;"
                row-key="order.orderString"
                border
            >
                <el-table-column type="expand">
                    <template slot-scope="props">
                        <el-form label-position="left" inline class="demo-table-expand">
                            <el-form-item label="餐品名称">
                                <span>{{ props.row.goods.name }}</span>
                            </el-form-item>
                            <el-form-item label="餐品 ID">
                                <span>{{ props.row.goods.id }}</span>
                            </el-form-item>
                            <el-form-item label="餐品图片">
                                <img :src="props.row.goods.goodsPhotoList[0]">
                            </el-form-item>
                            <el-form-item label="餐品描述">
                                <span>{{ props.row.goods.sort_introduce }}</span>
                            </el-form-item>
                            <el-form-item label="购买人姓名">
                                <span>{{ props.row.user.name }}</span>
                            </el-form-item>
                            <el-form-item label="头像">
                                <el-avatar :src="props.row.user.headimgUrl"></el-avatar>
                            </el-form-item>
                        </el-form>
                    </template>
                </el-table-column>
                <el-table-column
                    label="编号"
                    prop="order.orderString"
                >
                </el-table-column>
                <el-table-column
                    prop="order.paymentPrice"
                    label="价格"
                >
                </el-table-column>
                <el-table-column
                    prop="order.num"
                    label="数量"
                >
                </el-table-column>
                <el-table-column
                    prop="order.time"
                    label="购买时间"
                >
                </el-table-column>
                <el-table-column
                    label="收货地址"
                >
                    <template slot-scope="scope">
                        {{ scope.row.address.name }}-{{ scope.row.address.phone }}-{{ scope.row.address.address }}
                    </template>
                </el-table-column>
                <el-table-column
                    label="订单类型"
                >
                    <template slot-scope="scope">
                        {{ scope.row.order.type == 0 ? '配送' : '到店' }}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="tag"
                    label="状态"
                    width="100">
                    <template slot-scope="scope">
                        <el-tag
                            disable-transitions>
                            {{ scope.row.order.status == 0 ? '待配送' : scope.row.order.status == 1 ? '待消费' : scope.row.order.status == 2 ? '待评价' : scope.row.order.status == 3 ? '已完成' : scope.row.order.status == 4 ? '取消' : '已配送'
                            }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="350" align="center" fixed="right">
                    <template slot-scope="scope">
                        <el-button
                            size="mini"
                            type="danger"
                            v-if="scope.row.order.status == 4 || scope.row.order.status == 3"
                            @click="goodsOperation(scope.$index, scope.row,2,'')">删除
                        </el-button>
                        <el-button
                            size="mini"
                            type="success"
                            v-if="scope.row.order.status == 0"
                            @click="goodsOperation(scope.$index, scope.row,0,0)">配送
                        </el-button>
                        <el-button
                            size="mini"
                            type="danger"
                            v-if="scope.row.order.status == 3"
                            @click="goodsOperation(scope.$index, scope.row,3,3)">查看评价
                        </el-button>
                        <el-button
                            size="mini"
                            type="danger"
                            v-if="scope.row.order.status == 1"
                            @click="goodsOperation(scope.$index, scope.row,0,1)">消费
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
        <!--评论查看时光轴-->
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

        <!--外卖小哥信息-->
        <el-dialog title="外卖小哥信息" :visible.sync="addTakeUserModel" width="50%">
            <el-form ref="form" :model="operationVto.takeUser.name" label-width="70px">
                <el-form-item label="小哥昵称">
                    <el-input v-model="operationVto.takeUser.name"></el-input>
                </el-form-item>
                <el-form-item label="小哥手机号码">
                    <el-input v-model="operationVto.takeUser.phone" max="11"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                    <el-button @click="addTakeUserModel = false">关闭</el-button>
                    <el-button type="primary" @click="submitOperationOrderByTake()">保存</el-button>
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
import { baseUrl, childrenClass, exportOrder, list, parentClass } from '../../../utils';
import operateJs from '@/components/util/operateJS';
import axios from 'axios';

export default {
    data() {
        return {
            query: {
                page: 1,
                pageSize: 10,
                classId: '',
                listType: 4,
                likeName: '',
                phone: '',
                userName: '',
                orderString: ''
            },
            commentQuery: {
                page: 1,
                pageSize: 5,
                listType: 5,
                goodsId: ''
            },
            pageTotal: 0,
            listOrders: [],
            listChildrenClass: [],
            operationVto: {
                order: {},
                operationType: null,
                type: 4,
                takeUser:{

                }
            },
            commentOperationVto: {
                evaluation: {},
                operationType: 0,
                type: 5
            },
            userType: '',
            //评论列表
            commentList: [],
            commentListView: false,
            commentPageTotal: 0,
            replyComments: [],
            addTakeUserModel:false
        };
    },
    created() {
        this.getListOrders();
        this.getListChildrenClass();
        this.userType = JSON.parse(localStorage.getItem('userInfo')).type;
    },
    methods: {
        filterTag(value, row) {
            return row.goods.status === value;
        },
        getListOrders() {
            list(this.query).then(res => {
                let data = res.data;
                this.pageTotal = data.data.total;
                this.listOrders = data.data.records;
            });
        },
        getListChildrenClass() {
            childrenClass().then(res => {
                this.listChildrenClass = res.data.data;
            });
        },
        //分类查找
        searchByClassId(val) {
            this.$set(this.query, 'classId', val);
            this.getListOrders();
        },
        //名称模糊查询
        searchLikeName(val) {
            this.$set(this.query, 'likeName', val);
            this.getListOrders();
        },
        searchPhone(val) {
            this.$set(this.query, 'phone', val);
            this.getListOrders();
        },
        searchOrderString(val) {
            this.$set(this.query, 'orderString', val);
            this.getListOrders();
        },
        searchUserName(val) {
            this.$set(this.query, 'userName', val);
            this.getListOrders();
        },
        /**
         * @param index
         * @param row
         * @param opearType 0修2删除3查看评价
         */
        goodsOperation(index, row, opearType, orderStatus) {
            var that = this;
            if (opearType === 2) {
                // 二次确认删除
                that.$confirm('确定要删除吗？', '提示', {
                    type: 'warning'
                })
                    .then(() => {
                        that.operationVto.order.id = row.order.id;
                        that.operationVto.operationType = 2;
                        operateJs.systemOperation(that.operationVto);
                        /**
                         * 等到2秒刷新
                         */
                        setTimeout(function() {
                            that.getListOrders();
                        }, '2000');
                    })
                    .catch(() => {
                    });
            } else if (opearType === 0) {
                that.operationVto.order.id = row.order.id;
                that.operationVto.operationType = 0;
                that.operationVto.order.status = orderStatus;
                if (orderStatus === 0) {
                    this.addTakeUserModel=true;
                } else {
                    that.operationOrderByZero()
                }
            } else if (opearType === 3) {
                /**
                 * 请求接口获取该餐品的评价列表
                 * 暂时不分页
                 */
                that.commentQuery.goodsId = row.goods.id;
                that.commentListGet();
                that.commentListView = true;
            }

        },
        //评论列表获取
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
            this.getListOrders();
        },
        //每页大小改变
        handlePageSizeChange(val) {
            this.$set(this.query, 'pageSize', val);
            this.getListOrders();
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
        },
        //导出订单信息
        exportOrderData() {
            axios({
                url: 'http://127.0.0.1:9700/back/export',
                method: 'POST',
                responseType: 'blob',
                headers: {
                    token: localStorage.getItem('token')
                },
                data: this.query
            }).then(res => {
                console.log(res);
                // 创建一个新的 URL 对象，并使用 a 标签进行下载
                const url = window.URL.createObjectURL(new Blob([res.data]));
                const link = document.createElement('a');
                link.href = url;
                link.setAttribute('download', this.removeSlashPrefix(path));
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
                window.URL.revokeObjectURL(url); // 清除 blob URL
            }).catch(error => {
                // 处理错误
                console.error('下载失败:', error);
                this.$message.error('下载失败: ' + error.message);
            });
        },
        operationOrderByZero(){
            let that=this
            operateJs.systemOperation(that.operationVto);
            /**
             * 等到2秒刷新
             */
            setTimeout(function() {
                that.getListOrders();
            }, '2000');
        },
        submitOperationOrderByTake(){
            let that=this;
            that.operationOrderByZero()
            that.addTakeUserModel=false;
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
