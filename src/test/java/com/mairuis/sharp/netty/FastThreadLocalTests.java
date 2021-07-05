package com.mairuis.sharp.netty;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.FastThreadLocalThread;
import org.junit.jupiter.api.Test;

/**
 * @author Mairuis
 * @since 2021/6/29
 */
public class FastThreadLocalTests {
    @Test
    public void test() throws InterruptedException {
        final FastThreadLocal<String> fastThreadLocal = new FastThreadLocal<>();

        FastThreadLocalThread thread = new FastThreadLocalThread(() -> {
            fastThreadLocal.set("it's fast, very fast.");
        });
        thread.start();
        thread.join();
    }

}
