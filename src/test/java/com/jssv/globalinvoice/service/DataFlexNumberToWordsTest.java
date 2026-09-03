package com.jssv.globalinvoice.service;

import com.sun.net.httpserver.HttpServer;
import com.jssv.globalinvoice.service.integration.DataFlexNumberToWords;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class DataFlexNumberToWordsTest {

    @Test
    void shouldParseResponseFromSoapService() throws Exception {
        DataFlexNumberToWords service = new DataFlexNumberToWords();

        String xml = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                + "<soap:Body><m:NumberToWordsResponse xmlns:m=\"http://www.dataaccess.com/webservicesserver/\">"
                + "<m:NumberToWordsResult>one hundred twenty three</m:NumberToWordsResult>"
                + "</m:NumberToWordsResponse></soap:Body></soap:Envelope>";

        Method extractResult = DataFlexNumberToWords.class.getDeclaredMethod("extractResult", java.io.InputStream.class);
        extractResult.setAccessible(true);

        String result = (String) extractResult.invoke(service, new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        assertEquals("one hundred twenty three", result);
    }

    @Test
    void convert_shouldReturnWordsFromSoapServer() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/", exchange -> {
            String body = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                    + "<soap:Body><m:NumberToWordsResponse xmlns:m=\"http://www.dataaccess.com/webservicesserver/\">"
                    + "<m:NumberToWordsResult>one hundred twenty three</m:NumberToWordsResult>"
                    + "</m:NumberToWordsResponse></soap:Body></soap:Envelope>";
            exchange.getResponseHeaders().add("Content-Type", "text/xml; charset=utf-8");
            exchange.sendResponseHeaders(200, body.getBytes(StandardCharsets.UTF_8).length);
            exchange.getResponseBody().write(body.getBytes(StandardCharsets.UTF_8));
            exchange.close();
        });
        server.start();

        try {
            DataFlexNumberToWords service = new DataFlexNumberToWords();
            Field endpointField = DataFlexNumberToWords.class.getDeclaredField("endpoint");
            endpointField.setAccessible(true);
            endpointField.set(service, "http://localhost:" + server.getAddress().getPort() + "/");

            assertEquals("one hundred twenty three", service.convert(new BigDecimal("123")));
        } finally {
            server.stop(0);
        }
    }

    @Test
    void convert_shouldRejectNullAndNegativeValues() {
        DataFlexNumberToWords service = new DataFlexNumberToWords();

        assertThrows(IllegalArgumentException.class, () -> service.convert(null));
        assertThrows(IllegalArgumentException.class, () -> service.convert(new BigDecimal("-1")));
    }

    @Test
    void convert_shouldThrowWhenServiceReturnsErrorStatus() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/", exchange -> {
            exchange.sendResponseHeaders(500, -1);
            exchange.close();
        });
        server.start();

        try {
            DataFlexNumberToWords service = new DataFlexNumberToWords();
            Field endpointField = DataFlexNumberToWords.class.getDeclaredField("endpoint");
            endpointField.setAccessible(true);
            endpointField.set(service, "http://localhost:" + server.getAddress().getPort() + "/");

            IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.convert(new BigDecimal("123")));
            assertTrue(ex.getMessage().contains("DataFlex"));
        } finally {
            server.stop(0);
        }
    }
}
