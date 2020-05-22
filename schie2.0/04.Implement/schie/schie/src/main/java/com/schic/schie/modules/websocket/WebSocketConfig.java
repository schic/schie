/**
 *
 */
package com.schic.schie.modules.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {
    //使用@SpringBootApplication启动类进行启动时需要下面这段代码，但生成war包部署在tomcat中不需要这段
    /*@Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }*/
}
