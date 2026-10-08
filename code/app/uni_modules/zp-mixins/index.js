import * as lifecycle from './lifecycle/index'
import * as methods from './methods/index'

export default {
	install(Vue, option) {
		Vue.mixin({
			...lifecycle,
			methods: {
				...methods
			}
		})
	}
}
