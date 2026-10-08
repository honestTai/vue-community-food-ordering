<template>
    <view style="height: 100%">
        <view @tap="onCreate">
            <slot />
        </view>
        <we-canvas id="poster" @success="onCreateSuccess($event, { tagId: 'poster' })" @fail="onCreateFail($event, { tagId: 'poster' })" />
    </view>
</template>

<script>
import weCanvas from '../index/index';
export default {
    components: {
        weCanvas
    },
    data() {
        return {};
    },
    props: {
        config: {
            type: Object,
            default: () => ({})
        },
        preload: {
            // 是否预下载图片资源
            type: Boolean,
            default: false
        },
        hideLoading: {
            // 是否隐藏loading
            type: Boolean,
            default: false
        }
    },
    mounted() {
        // 处理小程序 ready 生命周期
        this.$nextTick(() => this.ready());
    },
    methods: {
        ready() {
            if (this.preload) {
                const poster = this.selectComponent('#poster');
                this.downloadStatus = 'doing';
                poster
                    .downloadResource(this.config)
                    .then(() => {
                        this.downloadStatus = 'success';
                        this.trigger('downloadSuccess');
                    })
                    .catch((e) => {
                        this.downloadStatus = 'fail';
                        this.trigger('downloadFail', e);
                    });
            }
        },

        trigger(event, data) {
            if (this.listener && typeof this.listener[event] === 'function') {
                this.listener[event](data);
            }
        },

        once(event, fun) {
            if (typeof this.listener === 'undefined') {
                this.listener = {};
            }
            this.listener[event] = fun;
        },

        downloadResource(reset) {
            return new Promise((resolve, reject) => {
                if (reset) {
                    this.downloadStatus = null;
                }
                const poster = this.selectComponent('#poster');
                if (this.downloadStatus && this.downloadStatus !== 'fail') {
                    if (this.downloadStatus === 'success') {
                        resolve();
                    } else {
                        this.once('downloadSuccess', () => resolve());
                        this.once('downloadFail', (e) => reject(e));
                    }
                } else {
                    poster
                        .downloadResource(this.config)
                        .then(() => {
                            this.downloadStatus = 'success';
                            resolve();
                        })
                        .catch((e) => reject(e));
                }
            });
        },

        onCreate(reset = false) {
            if (!this.hideLoading) {
                uni.showLoading({
                    mask: true,
                    title: '生成中'
                });
            }
            return this.downloadResource(typeof reset === 'boolean' && reset)
                .then(() => {
                    if (!this.hideLoading) {
                        uni.hideLoading();
                    }
                    const poster = this.selectComponent('#poster');
                    poster.create(this.config);
                })
                .catch((err) => {
                    if (!this.hideLoading) {
                        uni.hideLoading();
                    }
                    uni.showToast({
                        icon: 'none',
                        title: err.errMsg || '生成失败'
                    });
                    console.error(err);
                    this.$emit('fail', {
                        detail: err
                    });
                });
        },

        onCreateSuccess(e) {
            const { detail } = e;
            this.$emit('success', {
                detail: detail
            });
        },

        onCreateFail(err) {
            console.error(err);
            this.$emit('fail', {
                detail: err
            });
        }
    },
    created: function () {}
};
</script>
<style></style>
