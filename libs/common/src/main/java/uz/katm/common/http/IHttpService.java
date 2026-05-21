package uz.katm.common.http;

import java.util.Map;

public interface IHttpService {

    HttpResponseInfo sendPostRequest(String url, Object body, Map<String, String> headers) throws Exception;

    HttpResponseInfo sendPostFormRequest(String url, Map<String, String> params, Map<String, String> headers) throws Exception;

    HttpResponseInfo sendPostSoapRequest(String url, String xmlBody, Map<String, String> headers) throws Exception;

    HttpResponseInfo sendGetRequest(String url, Map<String, String> headers) throws Exception;
}
