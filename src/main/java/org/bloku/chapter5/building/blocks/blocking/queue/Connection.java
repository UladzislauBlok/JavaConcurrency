package org.bloku.chapter5.building.blocks.blocking.queue;

interface Connection extends AutoCloseable {

    String read();

    void close() throws Exception;
}
