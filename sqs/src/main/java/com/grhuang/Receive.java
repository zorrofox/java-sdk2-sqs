package com.grhuang;

public class Receive {

    public static void main(String... args) {
        for (int i = 0; i < 500; i++) {
			new Thread(new ReceiveTask()).start();
		}
    }
}

class ReceiveTask implements Runnable {
    @Override
    public void run() {
        String sqsUrl = "https://sqs.ap-southeast-1.amazonaws.com/12345678901/scq-queue.fifo";
        Handler h = new Handler(sqsUrl);
        h.recvMsgRequest(Thread.currentThread().getName());
        System.out.print(Thread.currentThread().getName() + " stop!!");
    }
}