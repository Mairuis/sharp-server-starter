package com.mairuis.sharp.jctools;

import org.jctools.queues.atomic.MpmcAtomicArrayQueue;
import org.junit.jupiter.api.Test;

/**
 * @author Mairuis
 * @since 2021/6/12
 */
public class JCToolsTest {

    @Test
    public void test() {

        MpmcAtomicArrayQueue a = new MpmcAtomicArrayQueue(8);
        a.offer(1);
        a.poll();
    }

}
