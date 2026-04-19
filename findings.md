# Findings

## 2026-03-10
- 初始化审查记录。
- 商品流程存在多个高风险断点：
  - `SetmealDishMapper.xml` 的 namespace 与接口不匹配，且 SQL 用了错误字段，导致菜品删除前的套餐关联校验不可用。
  - `SetmealMapper.java` 声明了 `countByCategoryId`，但 `SetmealMapper.xml` 未提供对应 SQL，分类删除流程会在运行时抛出 `BindingException`。
  - `DishMapper.xml` 缺少 `list` 映射，`/user/dish/list` 会在运行时抛出 `Invalid bound statement`。
  - `DishFlavorMapper.xml` 的批量插入口味 SQL 存在多余 `}`，新增/编辑菜品会触发 SQL 参数解析异常。
  - `DishMapper.xml` 分页查询中使用 `d.categoryId` 而非 `d.category_id`，按分类筛选菜品分页存在失败风险。
- 功能覆盖不完整：
  - 管理端缺少套餐管理控制器（`/admin/setmeal`）及对应 service 能力（新增、分页、启停、删除、详情、修改）。
  - 管理端菜品缺少启售/停售接口（`/admin/dish/status/{status}`）。
  - C 端缺少购物车与下单控制器与服务链路。
