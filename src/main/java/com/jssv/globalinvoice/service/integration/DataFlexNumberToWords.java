package com.jssv.globalinvoice.service.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@RequiredArgsConstructor
public class DataFlexNumberToWords implements NumberToWordsService {

    private static final String NAMESPACE = "http://www.dataaccess.com/webservicesserver/";
    private static final String RESULT_NODE = "NumberToWordsResult";

    @Value("${dataflex.soap.url}")
    private String endpoint;

    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    @Override
    public String convert(BigDecimal value) {
        validateValue(value);

        long integerValue = value.longValueExact();
        String request = """
                <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ser="%s">
                    <soapenv:Body>
                        <ser:NumberToWords>
                            <ser:ubiNum>%d</ser:ubiNum>
                        </ser:NumberToWords>
                    </soapenv:Body>
                </soapenv:Envelope>
                """.formatted(NAMESPACE, integerValue);

        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Content-Type", "text/xml; charset=utf-8")
                    .header("SOAPAction", "http://www.dataaccess.com/webservicesserver/NumberToWords")
                    .POST(HttpRequest.BodyPublishers.ofString(request))
                    .build();

            HttpResponse<InputStream> response = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IllegalStateException("El servicio SOAP de DataFlex respondió con código HTTP " + response.statusCode());
            }

            return extractResult(response.body());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IllegalStateException("No fue posible procesar la solicitud SOAP hacia DataFlex.", ex);
        }
    }

    private String extractResult(InputStream responseStream) {
        try {
            Node resultNode = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(responseStream)
                    .getDocumentElement();

            XPath xpath = XPathFactory.newInstance().newXPath();
            Node node = (Node) xpath.evaluate("//*[local-name()='" + RESULT_NODE + "']", resultNode, XPathConstants.NODE);

            if (node == null) {
                throw new IllegalStateException("No se encontró " + RESULT_NODE + " en la respuesta de DataFlex");
            }

            return node.getTextContent().trim();
        } catch (Exception exception) {
            throw new IllegalStateException("No fue posible procesar la respuesta de DataFlex", exception);
        }
    }

    private void validateValue(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("El valor a convertir no puede ser nulo");
        }

        if (value.signum() < 0) {
            throw new IllegalArgumentException("El valor a convertir no puede ser negativo");
        }

        try {
            value.longValueExact();
        } catch (ArithmeticException exception) {
            throw new IllegalArgumentException("NumberToWords solamente permite números enteros", exception);
        }

        if (value.compareTo(BigDecimal.valueOf(Long.MAX_VALUE)) > 0) {
            throw new IllegalArgumentException("El valor excede el rango soportado por NumberToWords");
        }
    }
}