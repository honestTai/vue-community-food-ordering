<template>
    <div class="login-wrap">
        <div class="ms-login">
            <div class="ms-title">社区点餐系统后台</div>
            <el-form :model="param" :rules="rules" ref="login" label-width="0px" class="ms-content">
                <el-form-item prop="number">
                    <el-input v-model="param.number" placeholder="number">
                        <el-button slot="prepend" icon="el-icon-lx-people"></el-button>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input
                        type="password"
                        placeholder="password"
                        v-model="param.password"
                        show-password
                    >
                        <el-button slot="prepend" icon="el-icon-lx-lock"></el-button>
                    </el-input>
                </el-form-item>
                <div class="login-btn">
                    <el-button type="primary" @click="submitForm()">登录</el-button>

                </div>
                <p class="login-tips">Tips : 请输入用户名与密码进行登录。</p>
            </el-form>
        </div>
    </div>
</template>

<script>
import { login, parentClass } from '../../../utils';

export default {
    data: function() {
        return {
            param: {
                number: '',
                password: '',
            },
            checkCode: '',
            rules: {
                number: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
                password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
            }
        };
    },
    methods: {
        //提交
        submitForm() {
            this.$refs.login.validate(valid => {
                if (valid) {
                    login({
                        number: this.param.number,
                        password: this.param.password
                    }).then((res) => {
                        if(res!=undefined){
                            let data=res.data
                            if (data.code == 200) {

                                //将token存入缓存
                                localStorage.setItem('Authorization', data.data.token);
                                //userInfo存入缓存
                                localStorage.setItem('userInfo', JSON.stringify(data.data.user));
                                this.$message.success(data.message);
                                this.$router.push('/');
                            } else {
                                this.$message.error(data.message);
                                return false;
                            }
                        }

                    });
                } else {
                    return false;
                }
            });
        }
    }
};
</script>

<style scoped>
.login-wrap {
    position: relative;
    width: 100%;
    height: 100%;
    background-size: 100%;
}

.ms-title {
    width: 100%;
    line-height: 50px;
    text-align: center;
    font-size: 20px;
    color: black;
    border-bottom: 1px solid #ddd;
}

.ms-login {
    position: absolute;
    left: 50%;
    top: 50%;
    width: 350px;
    margin: -190px 0 0 -175px;
    border-radius: 5px;
    background: rgba(255, 255, 255, 0.3);
    overflow: hidden;
}

.ms-content {
    padding: 30px 30px;
}

.login-btn {
    text-align: center;
}

.login-btn button {
    width: 100%;
    height: 36px;
    margin-bottom: 10px;
}

.login-tips {
    font-size: 12px;
    line-height: 30px;
    color: red;
}
</style>
