package com.sjtu.bwphoto.memory.Class;

import org.springframework.web.client.RestTemplate;

/**
 * Created by ly on 7/21/2016.
 */
public class RestUtil {
    private static RestTemplate restTp = new RestTemplate();

    public static RestTemplate getSession() {
        return restTp;
    }

}
