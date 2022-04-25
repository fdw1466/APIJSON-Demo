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

package apijson;

import apijson.common.utils.PropertyUtil;
import apijson.creator.*;
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

/**
 * SpringBootApplication
 * 右键这个类 > Run As > Java Application
 *
 * @author DWER
 */
@SpringBootApplication
@EnableConfigurationProperties
public class Application extends SpringBootServletInitializer
        implements ApplicationContextAware, ApplicationListener<ApplicationStartedEvent> {
    public static final String TAG = "Application";

    /**
     * 全局 ApplicationContext 实例，方便 getBean 拿到 Spring/SpringBoot 注入的类实例
     */
    private static ApplicationContext APPLICATION_CONTEXT;

    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
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
                return new MySQLConfig();
            }

            @Override
            public SQLExecutor createSQLExecutor() {
                return new MySQLExecutor();
            }

            @Override
            public Parser<Long> createParser() {
                return new MyParser();
            }

            @Override
            public Verifier<Long> createVerifier() {
                return new MyVerifier();
            }

            @Override
            public FunctionParser createFunctionParser() {
                return new MyFunctionParser();
            }
        };

        try {
            //初始化
            APIJSONApplication.init(false);
            //全局修改KEY_USER_ID
            JSONObject.KEY_USER_ID = "customer_id";
            //调试模式
            Log.DEBUG = "1".equals(PropertyUtil.getProperty("debug"));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
