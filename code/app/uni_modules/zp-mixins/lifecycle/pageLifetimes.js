/**
 * 组件pageLifetimes处理，需在页面生命周期里调用
 * @param {Object} node
 * @param {Object} lifeName
 */
function handlePageLifetime(node, lifeName) {
	node.$children.map(child => {
		if (typeof child[lifeName] == 'function') child[lifeName]()
		handlePageLifetime(child)
	})
}

export default {
	onLoad() {
		// #ifndef APP || MP-MP-WEIXIN || MP-KUAISHOU
		uni.onWindowResize((res) => {
			handlePageLifetime(this, "onPageResize")
		})
		// #endif
	},
	onShow() {
		handlePageLifetime(this, "onPageShow")
	},
	onHide() {
		handlePageLifetime(this, "onPageHide")
	},
	onResize() {
		// #ifdef APP || MP-MP-WEIXIN || MP-KUAISHOU
		handlePageLifetime(this, "onPageResize")
		// #endif
	}
};
