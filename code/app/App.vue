<script>
import api from './utils/api';
import __config from './config/env';
export default {
    data() {
        return {};
    },
    globalData: {
        thirdSession: null,
        appUser: null,
        config: __config,
        api: api,

        updateManager() {
            const updateManager = uni.getUpdateManager();
            updateManager.onUpdateReady(function () {
                uni.showModal({
                    title: '更新提示',
                    content: '新版本已经准备好，是否重启应用？',
                    success(res) {
                        if (res.confirm) {
                            updateManager.applyUpdate();
                        }
                    }
                });
            });
        },

        //初始化，供每个页面调用
        initPage: function () {
            let that = this;
        },

        //获取当前页面带参数的url
        getCurrentPageUrlWithArgs() {
            const pages = getCurrentPages();
            const currentPage = pages[pages.length - 1];
            const url = currentPage.route;
            const options = currentPage.options;
            let urlWithArgs = `/${url}?`;
            for (let key in options) {
                const value = options[key];
                urlWithArgs += `${key}=${value}&`;
            }
            urlWithArgs = urlWithArgs.substring(0, urlWithArgs.length - 1);
            return urlWithArgs;
        }
    },
    onLaunch: function () {
        //检测新版本
        this.globalData.updateManager();
        uni.getSystemInfo({
            success: (e) => {
                this.globalData.StatusBar = e.statusBarHeight;
                let custom = uni.getMenuButtonBoundingClientRect();
                this.globalData.Custom = custom;
                this.globalData.CustomBar = custom.bottom + custom.top - e.statusBarHeight;
            }
        });
    }
};
</script>
<style>
@import './public/colorui/main.css';
@import './public/colorui/icon.css';
@import './public/colorui/animation.css';

.overflow {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}
.overflow-1 {
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 1;
    -webkit-box-orient: vertical;
}
.overflow-2 {
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
}
.display-ib {
    display: inline-block;
}
.display-i {
    display: inline;
}
.margin-top-bar {
    margin-top: 80rpx;
}
.margin-bottom-bar {
    margin-bottom: 80rpx;
}
.vertical-center {
    margin: auto 0rpx;
}
.text-decorat {
    text-decoration: line-through;
}
.mar-top-30 {
    margin-top: -30rpx !important;
}
</style>
