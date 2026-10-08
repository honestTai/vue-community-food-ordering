<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-calendar"></i> 用户管理</el-breadcrumb-item>
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 用户列表
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>


        <div class="container">
            <div class="handle-box">
                <el-input v-model="query.userName" placeholder="请输入用户姓名" class="handle-input mr10"
                          @change="searchUserName"></el-input>
                <el-input v-model="query.phone" placeholder="请输入用户手机号" class="handle-input mr10"
                          @change="searchPhone" type="text" maxlength="11" show-word-limit></el-input>
            </div>
            <div class="handle-box">
                <el-button
                    type="primary"
                    icon="el-icon-lx-add"
                    class="handle-del mr10"
                    @click="addAdminUser()"
                >添加管理员用户
                </el-button>
            </div>
            <el-tabs v-model="activeName" @tab-click="handleClick" type="border-card">
                <el-tab-pane label="管理员" name="1">
                    <el-table
                        :data="listUser"
                        style="width: 100%;margin-bottom: 20px;"
                        row-key="id"
                        border
                        default-expand-all>
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
                        <el-table-column
                            prop="phone"
                            label="手机号码"
                        >
                        </el-table-column>
                        <el-table-column
                            prop="time"
                            label="注册时间"
                        >
                        </el-table-column>
                        <el-table-column label="操作" width="180" align="center" fixed="right">
                            <template slot-scope="scope">
                                <el-button
                                    type="text"
                                    icon="el-icon-delete"
                                    class="red"
                                    @click="userOperation(scope.$index, scope.row,5)"
                                >删除用户
                                </el-button>
                                <el-button
                                    type="text"
                                    icon="el-icon-delete"
                                    class="red"
                                    @click="userOperation(scope.$index, scope.row,1)"
                                >重置密码
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-tab-pane>
                <el-tab-pane label="社区点餐用户" name="0">
                    <el-table
                        :data="listUser"
                        style="width: 100%;margin-bottom: 20px;"
                        row-key="id"
                        border
                        default-expand-all>
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
                        <el-table-column
                            prop="phone"
                            label="手机号码"
                        >
                        </el-table-column>
                        <el-table-column
                            prop="time"
                            label="注册时间"
                        >
                        </el-table-column>
                        <el-table-column
                            prop="tag"
                            label="状态"
                            width="100">
                            <template slot-scope="scope">
                                <el-tag
                                    :type="scope.row.type === 3 ? 'primary' : 'success'"
                                    disable-transitions>{{ scope.row.type === 3 ? '禁止登录' : '允许登录' }}
                                </el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" width="180" align="center" fixed="right">
                            <template slot-scope="scope">
                                <el-button
                                    type="text"
                                    icon="el-icon-delete"
                                    class="red"
                                    @click="userOperation(scope.$index, scope.row,0)"
                                    v-if="scope.row.type!=3"
                                >禁止登录
                                </el-button>
                                <el-button
                                    type="text"
                                    icon="el-icon-delete"
                                    class="red"
                                    @click="userOperation(scope.$index, scope.row,2)"
                                    v-if="scope.row.type==3"
                                >允许登录
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-tab-pane>
            </el-tabs>
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

            <!--添加用户弹窗，输入用户账号，密码，手机号，账号名称-->
            <el-dialog title="添加管理员用户" :visible.sync="addAdminUserModel" width="50%">
                <el-form ref="form" :model="user" label-width="70px">
                    <el-form-item label="用户昵称">
                        <el-input v-model="user.name"></el-input>
                    </el-form-item>
                    <el-form-item label="登录账号">
                        <el-input v-model="user.number"></el-input>
                    </el-form-item>
                    <el-form-item label="登录密码">
                        <el-input v-model="user.password" type="password" show-password></el-input>
                    </el-form-item>
                    <el-form-item label="手机号码">
                        <el-input v-model="user.phone" type="text" maxlength="11" show-word-limit></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="addAdminUserModel = false">关闭</el-button>
                    <el-button type="primary" @click="submitAddAdminUser">保存</el-button>
                </span>
            </el-dialog>

        </div>
    </div>
</template>


<script>
import { baseUrl, list, parentClass, userRegister } from '../../../utils';
import operateJs from '@/components/util/operateJS';
import axios from 'axios';

export default {
    data() {
        return {
            user: {
                number: '',
                password: '',
                name: '',
                phone: ''
            },
            query: {
                page: 1,
                pageSize: 10,
                classId: '',
                listType: 1,
                userName: '',
                userType: 0,
                phone: ''
            },
            pageTotal: 0,
            listUser: [],
            operationVto: {
                user: {},
                //0禁止登录1重置密码2允许登录3修改信息4添加用户
                operationType: null,
                type: 1
            },
            //默认展示的tag
            activeName: '0',
            userType: '',
            loginUserId: '',
            meStatus: '',
            showAdd: false,
            addAdminUserModel: false
        };
    },
    created() {
        this.getListUser();
        let user = JSON.parse(localStorage.getItem('userInfo'));
        this.userType = user.type;
        this.loginUserId = user.id;
    },
    methods: {
        clear() {
            this.user = {
                number: '',
                password: '',
                name: '',
                phone: ''
            };
        },
        addAdminUser() {
            this.clear();
            this.addAdminUserModel = true;
        },
        userOperation(index, row, type) {
            var that = this;
            that.operationVto.operationType = type;
            that.operationVto.user = row;
            operateJs.systemOperation(this.operationVto);
            /**
             * 等到2秒刷新
             */
            setTimeout(function() {
                that.getListUser();
            }, '2000');
        },
        handleClick(tab, event) {
            let index = tab.index;
            if (index == 0) {
                this.query.userType = 1;
            } else {
                this.query.userType = 0;
            }
            this.getListUser();
        },
        getListUser() {
            list(this.query).then(res => {
                let data = res.data;
                this.pageTotal = data.data.total;
                this.listUser = data.data.records;
            });
        },
        //分类查找
        searchUserName(val) {
            this.$set(this.query, 'userName', val);
            this.getListUser();
        },
        searchPhone(val) {
            this.$set(this.query, 'phone', val);
            this.getListUser();
        },
        // //分页跳转
        handlePageChange(val) {
            this.$set(this.query, 'page', val);
            this.getListUser();
        },
        //每页大小改变
        handlePageSizeChange(val) {
            this.$set(this.query, 'pageSize', val);
            this.getListUser();
        },
        //发送添加用户请求
        submitAddAdminUser(e) {
            let that = this;
            let param = {
                user: {...that.user},
                operationType: 4,
                type: 1
            };
            operateJs.systemOperation(param);
            this.addAdminUserModel = false;
            /**
             * 等到2秒刷新
             */
            setTimeout(function() {
                that.getListUser();
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
