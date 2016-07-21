package com.sjtu.bwphoto.memory.Class.Util;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;

/**
 * Created by ly on 7/21/2016.
 */
public class CookieRestTp extends RestTemplate{
    @Override
    protected ClientHttpRequest createRequest(URI url, HttpMethod method) throws IOException {
        ClientHttpRequest request = super.createRequest(url,method);
        request.getHeaders().add("Cookie","aaaaa");
        return request;
    }
}
