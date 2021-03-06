/*
 * Copyright 2006 and onwards Sean Owen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.planetj.servlet.filter.compression;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Tests {@link StatsInputStream}.
 *
 * @author Sean Owen
 * @since 1.6
 */
public final class StatsInputStreamTest extends TestCase {

    private ByteArrayInputStream bais;
    private MockStatsCallback callback;
    private InputStream statsIn;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        bais = new ByteArrayInputStream(new byte[100]);
        callback = new MockStatsCallback();
        statsIn = new StatsInputStream(bais, callback);
    }

    public void testStats() throws Exception {
        assertBytesRead(0);
        assertEquals(0, statsIn.read());
        assertBytesRead(1);
        assertEquals(10, statsIn.read(new byte[10]));
        assertBytesRead(11);
        assertEquals(5, statsIn.read(new byte[10], 0, 5));
        assertBytesRead(16);
        statsIn.close();
        assertBytesRead(16);
    }

    private void assertBytesRead(int numBytes) {
        assertEquals(numBytes, callback.totalBytesRead);
        assertEquals(numBytes, 100 - bais.available());
    }

    private static final class MockStatsCallback implements StatsInputStream.StatsCallback {

        private int totalBytesRead;

        public void bytesRead(int numBytes) {
            totalBytesRead += numBytes;
        }
    }
}
