<template>
    <view :class="'text-' + size + ' text-red'">
        <text :class="'cuIcon-like' + (valueClone > index ? 'fill' : '') + ' margin-right'" @tap="redeHander" :data-index="index" v-for="(item, index) in 5" :key="index"></text>
    </view>
</template>

<script>
export default {
    data() {
        return {
            valueClone: 0
        };
    },
    props: {
        value: {
            type: Number,
            default: 0
        },
        size: {
            type: String,
            default: 'xxl'
        }
    },
    methods: {
        redeHander(e) {
            let value = e.currentTarget.dataset.index + 1;
            this.setData({
                valueClone: value
            });
            this.$emit('onChange', {
                detail: value
            });
        }
    },
    created: function () {}
};
</script>
<style></style>
