package com.grhuang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchRequestEntry;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import software.amazon.awssdk.services.sqs.model.SqsException;


public class Handler {
    private SqsAsyncClient sqsAsyncClient;
    private SqsClient sqsClient;
    private String queueUrl;

    public Handler(String queueUrl) {
        this.queueUrl = queueUrl;
    }

    public void sendMsgRequest(String message) {
        try {
            this.sqsAsyncClient = DependencyFactory.sqsAsyncClient();
            SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                    .queueUrl(this.queueUrl)
                    .messageBody(message)
                    .messageGroupId("Group1")
                    .build();
            CompletableFuture<SendMessageResponse> future = this.sqsAsyncClient.sendMessage(sendMessageRequest);
            future.thenAccept((response) -> {
                    System.out.println("Message sent: " + response.messageId() + "--" + message);
                });
            future.join();
        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
        }
    }

    public void recvMsgRequest(String recvThread) {
        try {
            this.sqsClient = DependencyFactory.sqsClient();
            ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                .queueUrl(this.queueUrl)//.waitTimeSeconds(10)
                .maxNumberOfMessages(10)
                .attributeNamesWithStrings("All")
                .build();

            while (true) {
                ReceiveMessageResponse messageResponse = sqsClient.receiveMessage(receiveRequest);
                List<Message> messages = messageResponse.messages();
                if (!messageResponse.hasMessages()) {
                    break; // 没有更多未读消息，退出循环
                }
                Collection<DeleteMessageBatchRequestEntry> entries = new ArrayList<>();
                for (Message m : messages) {
                    // 处理消息
                    System.out.println(recvThread + " Message received: " + m.messageId() + "---" + m.body() + "---" + m.attributes());
                    // sqsClient.deleteMessage(DeleteMessageRequest.builder()
                    //             .queueUrl(this.queueUrl)
                    //             .receiptHandle(m.receiptHandle())
                    //             .build());
                    DeleteMessageBatchRequestEntry entry = DeleteMessageBatchRequestEntry.builder()
                                                            .receiptHandle(m.receiptHandle())
                                                            .id(m.messageId())
                                                            .build();
                    
                    entries.add(entry);
                }
                
                sqsClient.deleteMessageBatch(DeleteMessageBatchRequest.builder()
                                .queueUrl(this.queueUrl)
                                .entries(entries)
                                .build());
            }
        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
        }
    }
}
