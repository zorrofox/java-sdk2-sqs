package com.grhuang;

public class Send {

    public static void main(String... args) {
        for (int i = 0; i < 1000; i++) {
			new Thread(new SendTask()).start();
		}
    }
}

class SendTask implements Runnable {
    @Override
    public void run() {
        String sqsUrl = "https://sqs.ap-southeast-1.amazonaws.com/12345678901/scq-queue.fifo";
        Handler h = new Handler(sqsUrl);
        for (int i = 0; i < 100; i++) {
            h.sendMsgRequest("Body " + i + " Thread " + Thread.currentThread().getId());
        }
    }
}