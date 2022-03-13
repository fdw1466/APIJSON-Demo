/*Copyright ©2016 TommyLemon(https://github.com/TommyLemon/APIJSON)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package apijson.demo;

import apijson.JSONObject;
import apijson.Log;
import apijson.demo.creator.*;
import apijson.framework.APIJSONApplication;
import apijson.framework.APIJSONCreator;
import apijson.orm.*;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * SpringBootApplication
 * 右键这个类 > Run As > Java Application
 *
 * @author DWER
 */
@Configuration
@SpringBootApplication
@EnableConfigurationProperties
public class DemoApplication extends SpringBootServletInitializer
        implements ApplicationContextAware, ApplicationListener<ApplicationStartedEvent> {
    /**
     * 全局 ApplicationContext 实例，方便 getBean 拿到 Spring/SpringBoot 注入的类实例
     */
    private static ApplicationContext APPLICATION_CONTEXT;

    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APPLICATION_CONTEXT = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        APIJSONApplication.DEFAULT_APIJSON_CREATOR = new APIJSONCreator() {
            @Override
            public SQLConfig createSQLConfig() {
                return new DemoSQLConfig();
            }

            @Override
            public SQLExecutor createSQLExecutor() {
                return new DemoSQLExecutor();
            }

            @Override
            public Parser<Long> createParser() {
                return new DemoParser();
            }

            @Override
            public Verifier<Long> createVerifier() {
                return new DemoVerifier();
            }

            @Override
            public FunctionParser createFunctionParser() {
                return new DemoFunctionParser();
            }
        };

        try {
            //初始化APIJSON
            APIJSONApplication.init(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //关闭调试模式
        Log.DEBUG = false;

        JSONObject.KEY_USER_ID = "user_id";
    }

    /**
     * 支持 APIAuto 中 JavaScript 代码跨域请求
     *
     * @return
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("*")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
}
