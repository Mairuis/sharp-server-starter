package com.mairuis.sharp.ringbuffer;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mairuis
 * @since 2021/6/7
 */
public class RingBufferTests {

    @Test
    public void testRingBuffer() throws InterruptedException {
        // 队列中的元素
        class Element {

            private int value;

            public int get() {
                return value;
            }

            public void set(int value) {
                this.value = value;
            }

        }
        final AtomicInteger tId = new AtomicInteger();
        ThreadFactory threadFactory = r -> new Thread(r, "Worker-" + tId.incrementAndGet());

        BlockingWaitStrategy strategy = new BlockingWaitStrategy();

        int bufferSize = 16;

        Disruptor<Element> disruptor = new Disruptor<>(Element::new, bufferSize, threadFactory, ProducerType.MULTI, strategy);

        WorkHandler<Element>[] handler = new WorkHandler[2];
        Arrays.fill(handler, (WorkHandler<Element>) (element) -> System.out.println("Worker " + Thread.currentThread().getName() + " work on : " + element.get()));
        disruptor.handleEventsWithWorkerPool(handler);

        disruptor.start();

        RingBuffer<Element> ringBuffer = disruptor.getRingBuffer();

        for (int l = 0; true; l++) {
            long sequence = ringBuffer.next();
            try {
                Element event = ringBuffer.get(sequence);
                event.set(l);
            } finally {
                ringBuffer.publish(sequence);
            }
            Thread.sleep(1000);
        }
    }
}
