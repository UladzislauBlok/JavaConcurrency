package org.bloku.chapter5.building.blocks.blocking.queue;

import java.lang.reflect.Proxy;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.SECONDS;

class ConnectionPool {
    private final BlockingQueue<Connection> queue;

    ConnectionPool(int poolSize) {
        this.queue = new ArrayBlockingQueue<>(poolSize);
        initConnectionPool(poolSize);
    }

    private void initConnectionPool(int poolSize) {
        Connection Connection = initConnection();
        Connection proxyConnection = (Connection) Proxy.newProxyInstance(ConnectionPool.class.getClassLoader(), new Class[]{Connection.class},
                (proxy, method, args) -> method.getName().equals("close")
                        ? queue.add((Connection) proxy)
                        : method.invoke(Connection, args));
        IntStream.range(0, poolSize).forEach(i -> queue.add(proxyConnection));
    }

    Connection getConnection() throws InterruptedException {
        return queue.take();
    }

    private Connection initConnection() {
        return new PostgresConnection();
    }

    static class PostgresConnection implements Connection {

        public String read() {
            try {
                SECONDS.sleep(3);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            return "DB_RESPONSE";
        }

        @Override
        public void close() throws Exception {
            throw new UnsupportedOperationException("You didn't catch me ;)");
        }
    }
}

