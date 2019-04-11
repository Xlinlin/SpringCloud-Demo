package com.xiao.springboot.docker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.zipkin.stream.EnableZipkinStreamServer;

//@EnableZipkinServer
@EnableZipkinStreamServer
@SpringBootApplication
public class OmniZipkinServerApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(OmniZipkinServerApplication.class, args);
    }
}
