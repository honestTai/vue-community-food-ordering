# WSL + Java 8 启动说明

当前验证环境：

- WSL：Debian
- MySQL：Docker 容器 `mysql`，`127.0.0.1:3306`
- 后端：Java 8 容器 `eclipse-temurin:8-jre`
- 后台：WSL Node.js 启动 Vue 管理端

## 1. 导入演示数据

```bash
cd /mnt/g/paper/vue社区点餐小程序
mysql -h127.0.0.1 -uroot -proot < takeoutfoods.sql
```

演示账号：

- 后台管理员：`admin / 123456`
- 商家账号：`cs / 123456`
- 小程序用户：`guke01 / 123456`

## 2. 启动 Java 8 后端

先在 Windows 或 WSL 中确保 jar 已打包：

```bash
cd /mnt/g/paper/vue社区点餐小程序/code/food
./mvnw -DskipTests package
```

如果 WSL Maven 网络不稳定，可以在 Windows PowerShell 中执行：

```powershell
cd G:\paper\vue社区点餐小程序\code\food
.\mvnw.cmd -DskipTests package
```

用 Java 8 容器启动后端：

```bash
cd /mnt/g/paper/vue社区点餐小程序/code/food
docker rm -f takeout-food-java8 2>/dev/null || true
docker run -d --name takeout-food-java8 --network host \
  -v "$PWD:/app" -w /app \
  -e MYSQL_HOST=127.0.0.1 \
  -e MYSQL_PORT=3306 \
  -e MYSQL_DATABASE=takeoutfoods \
  -e MYSQL_USER=root \
  -e MYSQL_PASSWORD=root \
  -e TAKEOUT_FILE_PATH=/app/uploads/foods/ \
  -e TAKEOUT_FILE_URL=http://127.0.0.1:9700/images/ \
  eclipse-temurin:8-jre \
  java -jar target/food-0.0.1-SNAPSHOT.jar
```

验证：

```bash
docker exec takeout-food-java8 java -version
docker logs -f takeout-food-java8
```

## 3. 启动后台管理端

```bash
cd /mnt/g/paper/vue社区点餐小程序/code/back
node node_modules/@vue/cli-service/bin/vue-cli-service.js serve --host 0.0.0.0
```

访问地址：

- 后台管理端：`http://127.0.0.1:8080`
- 后端接口：`http://127.0.0.1:9700`

Windows 运行 `code/app` 时，接口配置已经指向 `http://127.0.0.1:9700/`。
