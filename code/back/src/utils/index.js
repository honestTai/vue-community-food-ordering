import { post } from './ajax';
import { get } from './ajax';
import { upload } from './ajax';

//后台接口地址
export const baseUrl = 'http://127.0.0.1:9700/back/';


//登录
export const login = data => post(`${baseUrl}login`, data);

//退出登录
export const loginOut = data => get(`${baseUrl}loginOut`);

//统一list查询接口
export const list = data => post(`${baseUrl}list`, data);

//数据操作接口
export const operate = data => post(`${baseUrl}operation`, data);

//获取分类的最上级
export const parentClass = data => get(`${baseUrl}parentClass`);

//获取子级分类
export const childrenClass = data => get(`${baseUrl}childrenClass`);

//统一图片上传
export const uploadImage = data => upload(`${baseUrl}uploadGoodsPhoto`, data);

//注册
export const userRegister = data => post(`${baseUrl}merchantUserRegister`, data);

//文件上传
export const uploadRarFiles = data => upload(`${baseUrl}uploadRarFiles`, data);

//订单信息导出
export const exportOrder = data => post(`${baseUrl}export`, data);

//订单统计
export const statisticsOrder = data => post(`${baseUrl}statisticsOrder`, data);












