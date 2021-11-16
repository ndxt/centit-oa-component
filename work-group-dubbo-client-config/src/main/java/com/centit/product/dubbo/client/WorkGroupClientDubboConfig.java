package com.centit.product.dubbo.client;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({"classpath:dubbo-workgroup-client.xml"})
public class WorkGroupClientDubboConfig {
}
