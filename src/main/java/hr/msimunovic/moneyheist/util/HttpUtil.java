package hr.msimunovic.moneyheist.util;

import org.springframework.http.HttpHeaders;

public class HttpUtil {

    private HttpUtil() {
    }

    public static HttpHeaders generateHttpHeaders(String headerName, String headerValue) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(headerName, headerValue);

        return httpHeaders;
    }

}
