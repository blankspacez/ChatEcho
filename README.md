ChatEcho - SpringAI 智能对话 Demo

ChatEcho 是一个基于 SpringAI 框架构建的智能对话演示项目，展示了如何快速集成 AI 能力到 Spring Boot 应用中。

🌟 主要功能
• 智能对话交互：支持自然语言问答

• RAG 增强检索：集成向量数据库实现检索增强生成（Retrieval-Augmented Generation）

  • 支持本地知识库问答

  • 支持PDF/Word文档解析

  • 基于相似度检索的上下文注入

• Function Calling：结构化函数调用能力

  • 动态API调用映射

  • 多工具协同调度

  • 自动参数提取与验证

• 多模态扩展：预留图像/语音处理接口

• 上下文记忆：可配置的对话历史管理

📂 项目结构


ChatEcho/
├── spring-ai-demo/        # 核心AI功能模块
│   ├── src/
│   │   ├── main/java/     # AI服务实现
│   │   └── test/          # 单元测试
│   └── pom.xml            # 依赖配置
│
├── spring-ai-protal/       # 前端交互模块
│   ├── src/
│   │   ├── main/resources # 静态页面
│   │   └── webapp/        # Thymeleaf模板
│   └── build.gradle
│
├── docs/                  # 文档资料
└── LICENSE                # Apache 2.0


🛠️ 技术栈

核心框架:
• Spring Boot 3.2

• Spring AI 1.0

• Vue

AI 集成:
• OpenAI API

• 阿里百炼大模型平台

🚀 快速开始

环境准备

1. JDK 17+
2. Maven 3.9+
3. OpenAI API Key

配置步骤

1. 克隆项目：
   git clone https://github.com/blankspacez/ChatEcho.git
   

2. 配置API密钥：
   # application.yml
   spring:
     ai:
       openai:
         api-key: ${OPENAI_KEY}
   

3. 启动服务：
   cd spring-ai-protal && ./mvnw spring-boot:run
   

接口测试

访问 Swagger 文档：

http://localhost:8080/swagger-ui.html


