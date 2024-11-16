//package com.example.demo;
//
//import com.example.demo.entity.Currencies;
//import com.example.demo.service.CurrencyService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
//import org.springframework.messaging.simp.stomp.StompFrameHandler;
//import org.springframework.messaging.simp.stomp.StompHeaders;
//import org.springframework.messaging.simp.stomp.StompSession;
//import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
//import org.springframework.web.socket.client.standard.StandardWebSocketClient;
//import org.springframework.web.socket.messaging.WebSocketStompClient;
//import org.springframework.web.socket.sockjs.client.SockJsClient;
//import org.springframework.web.socket.sockjs.client.WebSocketTransport;
//
//import java.lang.reflect.Type;
//import java.util.Collections;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.TimeoutException;
//
//import static java.util.concurrent.TimeUnit.SECONDS;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
//public class CurrencyIntegrationTest {
//
//    @Autowired
//    private CurrencyService currencyService;
//
//    private static final String WEBSOCKET_URI = "ws://localhost:8080/gs-guide-websocket";
//    private static final String WEBSOCKET_TOPIC = "/topic/currency";
//
//    @Test
//    public void testCurrencyOperationsWithWebSocket() throws ExecutionException, InterruptedException, TimeoutException {
//        // Set up WebSocket client
//        WebSocketStompClient stompClient = new WebSocketStompClient(
//                new SockJsClient(Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient())))
//        );
//
//        CompletableFuture<Currencies> completableFuture = new CompletableFuture<>();
//
//        // Connect to WebSocket
//        StompSession session = stompClient.connect(WEBSOCKET_URI, new StompSessionHandlerAdapter() {}).get(5, SECONDS);
//
//        // Subscribe to currency updates
//        session.subscribe(WEBSOCKET_TOPIC, new StompFrameHandler() {
//            @Override
//            public Type getPayloadType(StompHeaders headers) {
//                return Currencies.class;
//            }
//
//            @Override
//            public void handleFrame(StompHeaders headers, Object payload) {
//                completableFuture.complete((Currencies) payload);
//            }
//        });
//
//        // Create a test currency
//        Currencies testCurrency = new Currencies("Test Currency", "TST");
//        currencyService.saveCurrency(testCurrency);
//
//        // Wait for WebSocket message
//        Currencies receivedCurrency = completableFuture.get(5, SECONDS);
//
//        // Verify the received currency
//        assertNotNull(receivedCurrency);
//        assertEquals("Test Currency", receivedCurrency.getName());
//        assertEquals("TST", receivedCurrency.getSymbol());
//
//        // Clean up
//        session.disconnect();
//    }
//}