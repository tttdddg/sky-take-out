# 商品流程完整性审查计划

## 目标
- 审查商品流程（分类、菜品、套餐、口味、起售停售、门店端查询、购物车与下单联动）是否存在功能缺失或实现不完整。

## 阶段
- [completed] 定位商品流程相关代码与接口
- [completed] 核对核心业务链路与状态流转
- [completed] 识别缺失点/风险点并给出证据
- [completed] 输出审查结论与建议

## 本轮实现
- 已补齐管理端套餐管理链路（Controller + Service + Mapper + XML）。
- 已补齐菜品启售/停售接口（Controller + Service）。
- 已通过 `mvn -DskipTests compile` 编译验证。

## 备注
- 以代码现状为准，不假设前端已规避后端问题。
