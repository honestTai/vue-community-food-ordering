# 基于Vue的社区点餐小程序的设计与实现

> Uni-app + Spring Boot 的社区点餐小程序。

本项目由 [HRouter](https://hrouter.net) 赞助 —— 面向开发者的 AI 中转站：一个 Key 调用多家大模型，稳定透明，注册即用。

## 文档与资源

- [开发文档](docs/thesis/README.md)：Markdown 图文格式，GitHub 上直接阅读。
- [PlantUML 图源](docs/diagrams/)：共 15 个文件（`.plantuml` / `.puml` / `.wsd`）。
- [数据库初始化说明](database/README.md)

## 源码目录

- `code/`

## 本地运行准备

1. 根据项目中的依赖声明安装对应语言运行时和数据库。
2. 查看下方依赖入口及初始化说明，按实际模块分别安装依赖。
3. 搜索 `CHANGE_ME_BEFORE_RUNNING`，填入自己的本地配置；不要提交真实密码、令牌和第三方服务密钥。
4. 启动相应后端，再启动前端或在小程序开发工具中导入客户端。

以下命令依据现有文件列出，**不代表已完成全项目安装、联调或运行验证**。

### 前端 / Node.js 入口

在 `code/back/` 中执行：

```bash
npm install
npm run dev
```


### Java / Maven 入口

- `code/food/pom.xml`

使用对应模块的 Maven 配置构建；多模块项目需按父子模块依赖顺序构建，并按原配置启动服务。
不要把示例配置中的占位值直接用于生产环境。

## 第三方组件与许可

现有第三方组件的 LICENSE、NOTICE 和作者声明保留原样；本项目不替换原有许可，也不额外声明所有代码适用同一开源许可证。
