package org.bloku.chapter7.cancellation.shutdown.interruption.policy;

import org.bloku.support.annotation.GuardedBy;
import org.bloku.support.annotation.ThreadSafe;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class ReaderThread {

    /**
     * CancellableTask interface that extends Callable and adds a cancel method and a newTask factory method for constructing a RunnableFuture. CancellingExecutor extends ThreadPoolExecutor, and overrides newTaskFor to let a CancellableTask create its own Future.<br>
     * SocketUsingTask implements CancellableTask and defines Future#cancel to close the socket as well as call super.cancel. If a SocketUsingTask is cancelled through its Future, the socket is closed and the executing thread is interrupted.
     */
    private static class CancellableReaderExecutor {
        private interface CancellableTask<T> extends Callable<T> {
            void cancel();

            RunnableFuture<T> newTask();
        }

        @ThreadSafe
        public static class CancellingExecutor extends ThreadPoolExecutor {

            public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
                super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
            }

            protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
                if (callable instanceof CancellableTask)
                    return ((CancellableTask<T>) callable).newTask();
                else
                    return super.newTaskFor(callable);
            }
        }

        public abstract class SocketUsingTask<T> implements CancellableTask<T> {
            @GuardedBy("this")
            private Socket socket;

            protected synchronized void setSocket(Socket s) {
                socket = s;
            }

            public synchronized void cancel() {
                try {
                    if (socket != null)
                        socket.close();
                } catch (IOException ignored) {
                }
            }

            public RunnableFuture<T> newTask() {
                return new FutureTask<>(this) {
                    public boolean cancel(boolean mayInterruptIfRunning) {
                        try {
                            SocketUsingTask.this.cancel();
                        } finally {
                            return super.cancel(mayInterruptIfRunning);
                        }
                    }
                };
            }
        }
    }

    /**
     * ReaderThread manages a single socket connection, reading synchronously from the socket and passing any data received to processBuffer. To facilitate terminating a user connection or shutting down the server, Reader-Thread overrides interrupt to both deliver a standard interrupt and close the underlying socket; thus interrupting a ReaderThread makes it stop what it is doing whether it is blocked in read or in an interruptible blocking method.
     */
    private static class CancellableReaderThread extends Thread {

        private static final int BUFFER_SIZE = 256;

        private final Socket socket;
        private final InputStream in;

        public CancellableReaderThread(Socket socket) throws IOException {
            this.socket = socket;
            this.in = socket.getInputStream();
        }

        public void interrupt() {
            try {
                socket.close();
            } catch (IOException ignored) {
            } finally {
                super.interrupt();
            }
        }

        public void run() {
            try {
                byte[] buf = new byte[BUFFER_SIZE];
                while (true) {
                    int count = in.read(buf);
                    if (count < 0)
                        break;
                    else if (count > 0)
                        processBuffer(buf, count);
                }
            } catch (IOException e) {
                /* Allow thread to exit */
            }
        }

        private void processBuffer(byte[] buf, int count) { // dummy
        }
    }
}
