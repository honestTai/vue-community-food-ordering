import { list, operate } from '@/utils';
import { Notification, MessageBox } from 'element-ui';

/**
 * 统一请求JS，统一列表获取，操作请求发起JS
 */

export default {

    //修改&新增&删除
    systemOperation(data) {
        operate(data).then(res => {
            if (res != undefined) {

            }
        });
    }
};