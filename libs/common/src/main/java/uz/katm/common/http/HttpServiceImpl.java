package uz.katm.common.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpServiceImpl implements IHttpService {

    private static final Logger LOG = LoggerFactory.getLogger(HttpServiceImpl.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final RestClient restClient;

    public HttpServiceImpl(boolean sslVerify) {
        this.restClient = RestClient.builder()
                .requestFactory(buildRequestFactory(sslVerify))
                .build();
        if (!sslVerify) {
            LOG.warn("SSL certificate verification is DISABLED — do not use in production");
        }
    }

    @Override
    public HttpResponseInfo sendPostRequest(String url, Object body, Map<String, String> headers) throws Exception {
        String json = OBJECT_MAPPER.writeValueAsString(body);
        LOG.info("POST JSON → {}", url);
        return restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> headers.forEach(h::add))
                .body(json)
                .exchange(this::readResponse);
    }

    @Override
    public HttpResponseInfo sendPostFormRequest(String url, Map<String, String> params, Map<String, String> headers) throws Exception {
        String formBody = params.entrySet().stream()
                .map(e -> encode(e.getKey()) + "=" + encode(e.getValue()))
                .collect(Collectors.joining("&"));
        LOG.info("POST FORM → {}", url);
        return restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .headers(h -> headers.forEach(h::add))
                .body(formBody)
                .exchange(this::readResponse);
    }

    @Override
    public HttpResponseInfo sendPostSoapRequest(String url, String xmlBody, Map<String, String> headers) throws Exception {
        LOG.info("POST SOAP → {}", url);
        return restClient.post()
                .uri(url)
                .contentType(MediaType.TEXT_XML)
                .accept(MediaType.ALL)
                .headers(h -> headers.forEach(h::add))
                .body(xmlBody)
                .exchange(this::readResponse);
    }

    @Override
    public HttpResponseInfo sendGetRequest(String url, Map<String, String> headers) throws Exception {
        LOG.info("GET → {}", url);
        return restClient.get()
                .uri(url)
                .headers(h -> headers.forEach(h::add))
                .exchange(this::readResponse);
    }

    private HttpResponseInfo readResponse(HttpRequest request, ClientHttpResponse response) throws IOException {
        HttpResponseInfo info = new HttpResponseInfo();
        info.setStatusCode(response.getStatusCode().value());
        info.setBody(response.getBody().readAllBytes());
        LOG.info("← {} status={}", request.getURI(), info.getStatusCode());
        if (!info.isSuccess()) {
            LOG.warn("Ошибка от внешнего сервиса {} status={}: {}",
                    request.getURI(), info.getStatusCode(), info.getBodyAsString());
        }
        return info;
    }

    private static String encode(String value) {
        return java.net.URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private static JdkClientHttpRequestFactory buildRequestFactory(boolean sslVerify) {
        try {
            SSLContext sslContext;
            if (sslVerify) {
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                tmf.init((KeyStore) null);
                sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, tmf.getTrustManagers(), null);
            } else {
                TrustManager[] trustAll = { new X509TrustManager() {
                    @Override public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                    @Override public void checkClientTrusted(X509Certificate[] c, String a) { }
                    @Override public void checkServerTrusted(X509Certificate[] c, String a) { }
                }};
                sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustAll, new SecureRandom());
            }

            HttpClient httpClient = HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .connectTimeout(Duration.ofSeconds(60))
                    .build();

            JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(httpClient);
            factory.setReadTimeout(Duration.ofSeconds(60));
            return factory;

        } catch (Exception ex) {
            throw new IllegalStateException("Не удалось создать HTTP-клиент", ex);
        }
    }
}
