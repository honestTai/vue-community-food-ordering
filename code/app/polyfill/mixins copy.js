/*
 * @Author: zhang peng
 * @Date: 2021-08-03 10:57:51
 * @LastEditTime: 2022-11-12 08:48:13
 * @LastEditors: zhang peng
 * @Description:
 * @FilePath: /miniprogram-to-uniapp2/src/project/template/polyfill/mixins copy.js
 *
 * 如果你想删除本文件，请先确认它使用的范围，感谢合作~
 * 如有疑问，请直接联系: 375890534@qq.com
 */

/**
 * 防抖
 * @param {Object} fn
 * @param {Object} delay
 */
let t = null
const debounce = function (fn, delay) {
    if (t !== null) {
        clearTimeout(t)
    }
    t = setTimeout(() => {
        fn()
    }, delay)
}

/**
 * lodash set
 * @param {*} obj
 * @param {*} path
 * @param {*} value
 * @returns
 */
function lodashSet (obj, path, value) {
    if (Object(obj) !== obj) return obj // When obj is not an object
    // If not yet an array, get the keys from the string-path
    if (!Array.isArray(path)) path = path.toString().match(/[^.[\]]+/g) || []
    path.slice(0, -1).reduce((a, c, i) => // Iterate all of them except the last one
        Object(a[c]) === a[c] // Does the key exist and is its value an object?
            // Yes: then follow that path
            ?
            a[c]
            // No: create the key. Is the next key a potential array-index?
            :
            a[c] = Math.abs(path[i + 1]) >> 0 === +path[i + 1] ? [] // Yes: assign a new array object
                :
                {}, // No: assign a new plain object
        obj)[path[path.length - 1]] = value // Finally assign the value to the last key
    return obj // Return the top-level object to allow chaining
}

/**
 * 组件pageLifetimes处理，需在页面生命周期里调用
 * @param {Object} lifeName
 */
function handlePageLifetime (lifeName) {
    this.$children.map(child => {
        if (typeof child[lifeName] == 'function') child[lifeName]()
        handlePageLifetime(child)
    })
}

export default {
    onLoad () {
        // #ifndef APP || MP-MP-WEIXIN || MP-KUAISHOU
        uni.onWindowResize((res) => {
            handlePageLifetime("onPageResize")
        })
        // #endif
    },
    onShow () {
        handlePageLifetime("onPageShow")
    },
    onHide () {
        handlePageLifetime("onPageHide")
    },
    onResize () {
        // #ifdef APP || MP-MP-WEIXIN || MP-KUAISHOU
        handlePageLifetime("onPageResize")
        // #endif
    },
    methods: {
        /**
         * 转义符换成普通字符
         * @param {*} str
         * @returns
         */
        escape2Html (str) {
            if (!str) return str
            var arrEntities = {
                'lt': '<',
                'gt': '>',
                'nbsp': ' ',
                'amp': '&',
                'quot': '"'
            }
            return str.replace(/&(lt|gt|nbsp|amp|quot);/ig, function (all, t) {
                return arrEntities[t]
            })
        },
        /**
         * 普通字符转换成转义符
         * @param {*} sHtml
         * @returns
         */
        html2Escape (sHtml) {
            if (!sHtml) return sHtml
            return sHtml.replace(/[<>&"]/g, function (c) {
                return {
                    '<': '&lt;',
                    '>': '&gt;',
                    '&': '&amp;',
                    '"': '&quot;'
                }[c]
            })
        },
        /**
         * 老setData polyfill
         * 用于转换后的uniapp的项目能直接使用this.setData()函数
         * @param {*} obj
         * @param {*} callback
         */
        oldSetData: function (obj, callback) {
            let that = this
            const handleData = (tepData, tepKey, afterKey) => {
                var tepData2 = tepData
                tepKey = tepKey.split('.')
                tepKey.forEach(item => {
                    if (tepData[item] === null || tepData[item] === undefined) {
                        let reg = /^[0-9]+$/
                        tepData[item] = reg.test(afterKey) ? [] : {}
                        tepData2 = tepData[item]
                    } else {
                        tepData2 = tepData[item]
                    }
                })
                return tepData2
            }
            const isFn = function (value) {
                return typeof value == 'function' || false
            }
            Object.keys(obj).forEach(function (key) {
                let val = obj[key]
                key = key.replace(/\]/g, '').replace(/\[/g, '.')
                let front, after
                let index_after = key.lastIndexOf('.')
                if (index_after != -1) {
                    after = key.slice(index_after + 1)
                    front = handleData(that, key.slice(0, index_after), after)
                } else {
                    after = key
                    front = that
                }
                if (front.$data && front.$data[after] === undefined) {
                    Object.defineProperty(front, after, {
                        get () {
                            return front.$data[after]
                        },
                        set (newValue) {
                            front.$data[after] = newValue
                            that.hasOwnProperty("$forceUpdate") && that.$forceUpdate()
                        },
                        enumerable: true,
                        configurable: true
                    })
                    front[after] = val
                } else {
                    that.$set(front, after, val)
                }
            })
            // this.$forceUpdate();
            isFn(callback) && this.$nextTick(callback)
        },
        /**
         * 2022-10-31 重写setData
         * 用于转换后的uniapp的项目能直接使用this.setData()函数
         * @param {Object} obj
         * @param {Object} callback
         */
        setData: function (obj, callback) {
            Object.keys(obj).forEach((key) => {
                lodashSet(this, key, obj[key])
            })

            debounce(this.$forceUpdate, 200)
            if (typeof callback == 'function') this.$nextTick(callback)
        },
        /**
         * 解析事件里的动态函数名，这种没有()的函数名，在uniapp不被执行
         * 比如：<view bindtap="{{openId==undefined?'denglu':'hy_to'}}">立即</view>
         * @param {*} exp
         */
        parseEventDynamicCode (e, exp) {
            if (typeof (this[exp]) === 'function') {
                this[exp](e)
            }
        },
        /**
         * 用于处理对props进行赋值的情况
         * //简单处理一下就行了
         *
         * @param {*} target
         * @returns
         */
        deepClone (target) {
            return JSON.parse(JSON.stringify(target))
        },
        /**
         * 用于处理dataset
         * 自定义组件的事件里，是获取不到e.currentTarget.dataset的
         * 因此收集data-参数，手动传进去
         *
         * @param {*} event
         * @param {*} dataSet
         * @returns
         */
        datasetHandle (event, dataSet = {}) {
            if (event && !event.currentTarget) {
                if (dataSet.tagId) {
                    event.currentTarget = {
                        id: dataSet.tagId
                    }
                } else {
                    event.currentTarget = {
                        dataset: dataSet
                    }
                }
            }
        },
        /**
         * 接管getTabBar函数，默认uni-app是没有这个函数的
         * 适用于使用custom-tab-bar自定义导航栏的小程序项目
         * 需注意：
         * 1.custom-tab-bar下面仍是小程序文件
         * 2.pages.json里面需使用条件编译区分好小程序和非小程序的tabBar配置
         */
        getTabBar () {
            return {
                setData (obj) {
                    if (typeof this.$mp?.page?.getTabBar === 'function' &&
                        this.$mp?.page?.getTabBar()) {
                        this.$mp.page.getTabBar().setData(obj)
                    } else {
                        console.log("当前平台不支持getTabBar()，已稍作处理，详细请参见相关文档。")
                    }
                }
            }
        },
        /**
         * 组件间关系
         * @param {*} name
         * @returns
         */
        getRelationNodes (name) {
            return this.$unicom('@' + name)
        }
    }
}
