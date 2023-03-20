# java-sdk2-sqs
- 修改示例代码的FIFO队列的URL，分别位于[`Receive.java`](sqs/src/main/java/com/grhuang/Receive.java#L15)和[`Send.java`](sqs/src/main/java/com/grhuang/Send.java#L15)
- 进入子目录sqs，编译并打包：`../mvnw clean package`
- 运行Send类：`java -cp target/sqs-1.0-SNAPSHOT-jar-with-dependencies.jar com.grhuang.Send`
- 开启另外一个CMD窗口运行：`java -cp target/sqs-1.0-SNAPSHOT-jar-with-dependencies.jar com.grhuang.Receive`
- 等待两个程序运行完成