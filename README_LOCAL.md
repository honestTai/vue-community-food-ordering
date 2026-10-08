# 本地启动说明

## 1. 初始化 MySQL

项目根目录已经整理好初始化脚本和样例数据：

```bash
mysql -uroot -p < takeoutfoods.sql
```

脚本会创建并使用 `takeoutfoods` 数据库，然后重建表并导入样例数据。

默认账号：

- 后台管理员：`admin / 123456`
- 商家账号：`cs / 123456`
- 小程序用户：`guke01 / 123456`

## 2. 启动后端

后端默认连接：

- MySQL：`127.0.0.1:3306`
- 数据库：`takeoutfoods`
- 用户名：`root`
- 密码：`123456`
- 服务端口：`9700`

如果你的 MySQL 密码不是 `123456`，先设置环境变量再启动：

```powershell
$env:MYSQL_USER="root"
$env:MYSQL_PASSWORD="CHANGE_ME_BEFORE_RUNNING"
```

启动：

```powershell
cd code/food
.\mvnw.cmd spring-boot:run
```

后端接口地址：`http://127.0.0.1:9700`

## 3. 启动后台管理端

```powershell
cd code/back
npm run serve
```

管理端默认地址通常是：`http://localhost:8080`

项目已在 npm 脚本里加入 `NODE_OPTIONS=--openssl-legacy-provider`，用于兼容当前较新的 Node.js 和旧版 Webpack。

## 4. 小程序端

`code/app` 是 uni-app 项目，接口已指向 `http://127.0.0.1:9700/`。可以用 HBuilderX 导入运行到浏览器、微信开发者工具或模拟器。
