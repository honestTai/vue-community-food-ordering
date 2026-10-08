import Vue from 'vue'
import * as lifecycle from './lifecycle/index'
import * as methods from './methods/index'

export default Vue.extend({
	...lifecycle,
	methods: {
		...methods
	}
})