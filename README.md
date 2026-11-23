<div align="center">

# 🪁 Pocket Dragon · 口袋龙

**把放出来的龙，重新装回口袋。**

因为服务器的龙无法装在口袋里，于是就有了这个口袋龙插件。

[![License](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](LICENSE)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.16%20~%201.19.2-3C8527.svg)]()
[![PetDragon](https://img.shields.io/badge/%E5%89%8D%E7%BD%AE-PetDragon%201.6.x-8A2BE2.svg)]()
[![Build JDK](https://img.shields.io/badge/%E6%9E%84%E5%BB%BA-JDK%2011%20~%2017-ED8B00.svg)]()

[简介](#简介) · [功能](#功能) · [环境要求](#环境要求) · [安装](#安装) · [使用](#使用) · [权限](#权限) · [构建](#从源码构建) · [常见问题](#常见问题)

</div>

---

## 简介

PocketDragon（口袋龙）是 [PetDragon](https://www.spigotmc.org/resources/82744/) 的一个轻量扩展插件。

PetDragon 让玩家可以召唤、骑乘和指挥自己的末影龙，但它的 `/dragon remove` 只做「移除」：龙被直接删除，召唤时用掉的龙蛋不会回来。无论是想换个地方重新摆龙、暂时把龙收起来，还是单纯手滑放错了位置，都只能再想办法弄一颗蛋。

PocketDragon 补上的正是「回收」这一步：空手执行 `/RecyclePetDragon`，身边 6 格内属于你的宠物龙会被收回，并以一颗 PetDragon 龙蛋的形式回到背包。

| 操作 | 结果 |
| --- | --- |
| PetDragon `/dragon remove` | 龙被删除，**龙蛋不返还** |
| PocketDragon `/RecyclePetDragon` | 龙被收回，**龙蛋返还背包** |

> 一句话：**龙也能被装进口袋，随取随用。**

## 功能

- **回收即返蛋** —— 收回龙的同时返还 PetDragon 龙蛋，不用再为手滑买单
- **就近回收** —— 自动锁定 6 格内距离最近的宠物龙，不需要对准
- **中文别名** —— `/回收龙` 与更短的 `/rpd` 都可以用
- **归属校验** —— 只回收自己的龙，不是你的龙会被明确拒绝
- **空手保护** —— 主手有物品时拒绝执行，避免误触
- **零侵入** —— 只调用 PetDragon 官方 API，不改它的配置、不写 NMS、不覆盖它的命令
- **零配置** —— 装好即用，没有额外的配置文件需要维护

## 环境要求

| 项目 | 要求 |
| --- | --- |
| 服务端 | Spigot / Paper 及其分支（Bukkit API） |
| Minecraft | 1.16 ~ 1.19.2（`api-version: 1.16`；搭配 PetDragon 1.6.1，换用更新的 PetDragon 可相应放宽） |
| 前置插件 | PetDragon 1.6.x（本项目针对 `PetDragon-1.6.1` 编译） |
| 构建 | JDK 11 ~ 17，Gradle 7.4（仓库自带 wrapper，无需本机安装 Gradle） |

> **关于 Java 版本**：插件编译后的 class 文件版本与构建所用的 JDK 一致。用 JDK 17 构建出的 jar 需要服务端运行 Java 17+；如果要装在 Java 8 / 11 的服务端上，请改用对应版本的 JDK 构建。

## 安装

1. 把 [PetDragon](https://www.spigotmc.org/resources/82744/) 放入服务端的 `plugins/` 目录并重启，确认它正常加载。
2. 获取 PocketDragon 的构建产物（可按下方[从源码构建](#从源码构建)自行编译），把 `PocketDragon-*-all.jar` 放入 `plugins/`。
3. 重启服务器。PocketDragon 会通过 `depend: [PetDragon]` 在 PetDragon 之后加载。
4. 按需为玩家分配[权限](#权限)。

启动成功后，可以在控制台执行 `plugins`，列表中应同时出现 `PetDragon` 和绿色的 `PocketDragon`。

## 使用

### 命令

| 命令 | 别名 | 说明 | 权限 |
| --- | --- | --- | --- |
| `/RecyclePetDragon` | `/rpd`、`/回收龙` | 回收身边 6 格内最近的宠物龙，并返还龙蛋 | `petdragon.bypass.remove` |

命令只能由玩家执行；执行时不会读取任何额外参数。

### 执行流程

1. **空手检查** —— 主手必须为空，手持物品时会提示「请空手回收嗷」。
2. **就近找龙** —— 以玩家位置为中心，在各轴 ±6 格（即半径 6 格）的范围内筛选出 PetDragon 生成的龙，取距离最近的一只。
3. **归属校验** —— 龙必须属于你，否则拒绝回收。
4. **收龙返蛋** —— 移除龙实体，并把一颗 PetDragon 龙蛋放入背包。

### 提示消息

| 触发场景 | 提示内容 |
| --- | --- |
| 6 格内没有宠物龙 | `太远了捏~` |
| 没有权限回收他人的龙 | `你无权收回该龙！ 权限节点[petdragon.bypass.remove]` |
| 主手持有物品 | `请空手回收嗷` |
| 龙不属于自己 | `这龙不是你的` |
| 回收成功 | `你收回了龙` |

## 权限

| 权限节点 | 默认 | 说明 |
| --- | --- | --- |
| `petdragon.bypass.remove` | OP | 执行 `/RecyclePetDragon` 回收命令 |

权限沿用了 PetDragon 的「绕过移除限制」节点，因此：

- **默认只有 OP 能使用**，普通玩家需要在权限插件（LuckPerms 等）中显式授予该节点。
- 即使授予了该节点，插件仍会通过 PetDragon API 校验龙的归属，**无法回收别人的龙**，只会收到「这龙不是你的」的提示。

## 注意事项

- **仅限玩家**：命令会把执行者当作玩家处理，请勿从控制台或命令方块中执行。
- **不保存龙的状态**：回收得到的是 PetDragon 的通用召唤蛋，龙的位置、乘客、昵称等信息不会一并保存。
- **龙蛋受 PetDragon 配置约束**：召唤行为仍由 PetDragon 的配置决定（例如 `eggs-abide-by-max-dragons`、`always-use-up-egg`）。
- **没有冷却与次数限制**：回收可以反复执行，建议通过权限节点控制可用人群。
- **回收半径固定为 6 格**，且提示文案写死在代码中，暂不支持配置。

## 从源码构建

```bash
# Linux / macOS（确保 JAVA_HOME 指向 JDK 11 ~ 17）
chmod +x gradlew        # 如果提示权限不足
./gradlew shadowJar

# Windows
gradlew.bat shadowJar
```

构建产物位于 `build/libs/`，取带 `-all` 后缀的那一个：

```text
build/libs/PocketDragon-1.0-SNAPSHOT-all.jar
```

构建说明：

- Gradle wrapper 版本为 **7.4**，官方支持的最高 JDK 为 **17**；使用 JDK 18+ 时，Gradle 解析构建脚本会报 `Unsupported class file major version`。
- 编译依赖 `org.bukkit:bukkit:1.18.1` 与 `com.alkaidmc.alkaid:alkaid-bukkit:1.0.0-SNAPSHOT`，从 `build.gradle` 中配置的镜像源下载；`lib/PetDragon-1.6.1.jar` 作为本地 `compileOnly` 依赖已随仓库提供，无需另行下载。
- 只有 `shade` 打包后的 `-all` 产物包含 Alkaid 命令库，请勿把普通的 `PocketDragon-1.0-SNAPSHOT.jar` 放进服里，否则会因缺少依赖而报错。

## 项目结构

```text
PocketDragon/
├── build.gradle                 # 构建脚本（Gradle 7.4 + Shadow）
├── settings.gradle
├── gradle/wrapper/              # Gradle wrapper
├── lib/
│   └── PetDragon-1.6.1.jar      # PetDragon 前置，编译期依赖
└── src/main/
    ├── java/fun/fifu/pocketdragon/
    │   ├── PocketDragon.java    # 插件入口：命令注册与回收逻辑
    │   └── Main.java            # 构建清单中的 Main-Class，含一个演示用 main 方法
    └── resources/
        └── plugin.yml           # 插件描述：依赖、版本、api-version
```

## 常见问题

**Q：回收时提示「太远了捏~」？**

A：命令只会搜索玩家周围 6 格范围内的龙，走近一点再执行即可。

**Q：回收时提示「请空手回收嗷」？**

A：切换到手部为空（主手没有物品）的状态再执行；副手物品不影响。

**Q：回收时提示「这龙不是你的」？**

A：这只龙属于其他玩家。插件通过 PetDragon 的归属数据校验，只有龙主本人可以回收。

**Q：龙蛋去哪里了？**

A：回收成功后龙蛋会直接进入背包；背包已满时会掉落在脚下。

**Q：为什么不直接把 `/dragon remove` 改成返还龙蛋？**

A：PetDragon 是第三方插件，覆盖它的命令容易在它更新或与其他扩展共存时出问题；新增一条独立命令更稳，也不会影响原有玩家的习惯。

**Q：支持 Folia 吗？**

A：未做适配与测试。PocketDragon 使用的是标准 Bukkit API，但未验证其在 Folia 的区域化调度下是否正常工作。

**Q：1.20 能用吗？**

A：取决于 PetDragon 是否支持对应版本。PocketDragon 自身只依赖 Bukkit 标准 API 与 PetDragon 的公开接口，只要 PetDragon 能在该版本运行，PocketDragon 通常也可以。

## 贡献

欢迎通过 Issue 反馈问题、提出建议，或直接提交 Pull Request。修改代码时请保持现有风格：提示文案使用中文、命令保留别名、尽量不侵入 PetDragon 本体。

## 许可证

本项目基于 [Apache License 2.0](LICENSE) 开源。

Minecraft 是 Mojang Studios 的商标，本项目为第三方作品，与 Mojang Studios 及 PetDragon 的作者均无隶属关系。

## 致谢

- [PetDragon](https://www.spigotmc.org/resources/82744/) —— 由 ericdebouwer (3ricL) 开发，本项目的前置与灵感来源
- [AlkaidMC 仓库](https://repository.alkaidmc.com) —— 提供 `alkaid-bukkit` 命令注册库
- 所有在服务器里养龙、又舍不得把龙删掉的人

---

<div align="center">
Made with ❤️ for 服务器里那些舍不得删掉的龙
</div>
