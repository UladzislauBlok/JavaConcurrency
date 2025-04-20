package org.bloku.chapter5.building.blocks.blocking.queue;

import org.bloku.support.domain.Processor;
import org.bloku.support.domain.Request;
import org.bloku.support.domain.Response;

class DatabaseProcessor implements Processor<Request, Response> {
    private static final String REQUEST_MESSAGE_PATTERN = "Read request: %s. Thread: %s%n";
    private static final String DB_RESPONSE_MESSAGE_PATTERN = "Read db response: %s. Thread: %s%n";
    private static final String RESPONSE_MESSAGE_PATTERN = "Send response: %s. Thread: %s%n";
    private static final int CONNECTION_POOL_SIZE = 5;
    private final ConnectionPool connectionPool = new ConnectionPool(CONNECTION_POOL_SIZE);

    @Override
    public Response process(Request request) {
        System.out.printf(REQUEST_MESSAGE_PATTERN, request, Thread.currentThread().getName());
        String dbResponse;
        try (Connection connection = connectionPool.getConnection()) {
            dbResponse = connection.read();
            System.out.printf(DB_RESPONSE_MESSAGE_PATTERN, dbResponse, Thread.currentThread().getName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Response response = new Response("Response: %s".formatted(dbResponse));
        System.out.printf(RESPONSE_MESSAGE_PATTERN, response, Thread.currentThread().getName());
        return response;
    }
}
