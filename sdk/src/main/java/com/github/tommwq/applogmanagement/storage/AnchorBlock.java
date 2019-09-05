package com.github.tommwq.applogmanagement.storage;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import static com.github.tommwq.applogmanagement.Constant.FIRST_LOG_BLOCK;

public class AnchorBlock {
        private short dataLength;
        private long version;
        private int firstBlockNumber;
        private int lastBlockNumber;
        private int blockCount;
        private long sequence = 0L;

        public AnchorBlock(int aBlockCount) {
                blockCount = aBlockCount;
        }

        public long version() {
                return version;
        }

        public int firstBlockNumber() {
                return firstBlockNumber;
        }

        public int lastBlockNumber() {
                return lastBlockNumber;
        }

        public void moveForward() {
                version++;
                lastBlockNumber++;
                if (lastBlockNumber == blockCount) {
                        lastBlockNumber = FIRST_LOG_BLOCK;
                }

                if (lastBlockNumber == firstBlockNumber) {
                        firstBlockNumber++;
                }
        }

        private void clear() {
                dataLength = 0;
                version = 0;
                firstBlockNumber = 0;
                lastBlockNumber = 0;
        }

        public void read(byte[] block) {
                ByteBuffer buffer = ByteBuffer.wrap(block).order(ByteOrder.LITTLE_ENDIAN);
                buffer.getLong(); // skip adler32
                dataLength = buffer.getShort();
                version = buffer.getLong();
                firstBlockNumber = buffer.getInt();
                lastBlockNumber = buffer.getInt();
                sequence = buffer.getLong();

                // initialize new anchor
                if (firstBlockNumber == 0 && lastBlockNumber == 0) {
                        // 0, 1 for anchor
                        firstBlockNumber = FIRST_LOG_BLOCK;
                        lastBlockNumber = firstBlockNumber + 1;
                }
        }

        public void write(byte[] block) {
                ByteBuffer buffer = ByteBuffer.wrap(block).order(ByteOrder.LITTLE_ENDIAN);
                dataLength = 18;
                buffer.position(8); // skip adler32
                buffer.putShort(dataLength)
                        .putLong(version)
                        .putInt(firstBlockNumber)
                        .putInt(lastBlockNumber)
                        .putLong(sequence);
        }

        public String toString() {
                return String.join("\n", Arrays.asList("version = ", String.valueOf(version),
                                                       "firstBlockNumber = ", String.valueOf(firstBlockNumber),
                                                       "lastBlockNumber = ", String.valueOf(lastBlockNumber)));
        }

        public long sequence() {
                return sequence;
        }

        public void increaseSequence() {
                sequence++;
        }
}
