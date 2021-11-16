package com.centit.product.dubbo.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({"classpath:dubbo-workgroup-server.xml"})
public class WorkGroupServerDubboConfig {

}
