# Findings

- `node_modules` 下大量目录是 Junction，目标指向旧路径 `D:\苍穹外卖前端源码\...`。
- 失效链接数量约 5345，导致 CLI、TypeScript 等可执行文件不存在。
- 当前 Node 版本为 `v22.14.0`，对 Vue CLI 3 老项目存在兼容性风险。
- 根因包含依赖目录损坏 + 依赖版本漂移（vue 2.7 / @types/node 25 与 TS3.6 不兼容）。
