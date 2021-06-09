package com.mairuis.sharp.ringbuffer;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadFactory;

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

        ThreadFactory threadFactory = r -> new Thread(r, "simpleThread");

        EventFactory<Element> factory = () -> new Element();

        EventHandler<Element> handler = (element, sequence, endOfBatch) -> System.out.println("Element: " + element.get());

        BlockingWaitStrategy strategy = new BlockingWaitStrategy();

        int bufferSize = 16;

        Disruptor<Element> disruptor = new Disruptor<>(factory, bufferSize, threadFactory, ProducerType.MULTI, strategy);

        disruptor.handleEventsWith(handler);

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
            Thread.sleep(10);
        }
    }
}
