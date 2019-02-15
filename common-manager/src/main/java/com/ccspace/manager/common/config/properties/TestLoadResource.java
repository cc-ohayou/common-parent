package com.ccspace.manager.common.config.properties;

import com.ccspace.facade.domain.common.constants.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Properties;


/**
 * @AUTHOR CF
 * @DATE Created on 2018/5/1/001 11:01.
 */
@Configuration
public class TestLoadResource implements EnvironmentAware {
    private static Logger logger= LoggerFactory.getLogger(TestLoadResource.class);
    public static String globalDir;
    public static Properties propertyJdbc=new Properties();
    public static Properties propertyRedis = new Properties();
    public static final String ENV_DIR= CommonConstants.GLOBAL_RESOURCES_DIR;
    public static final String JDBC_PROPERTY= "/cc_jdbc.properties";
    public static final String REDIS_PROPERTY="/cc_jedis.properties";

    @Bean
    public TestLoadResource getResource(){
        TestLoadResource res=new TestLoadResource();
        return res;
    }


    public static  void loadProperties() {
        if (globalDir == null) {
            globalDir = System.getenv(ENV_DIR);
        }
        logger.info("globalDir="+globalDir);
        FileSystemResource resourceJdbc = new FileSystemResource(globalDir + JDBC_PROPERTY);
        FileSystemResource resourceRedis = new FileSystemResource(globalDir + REDIS_PROPERTY);
        try {
            if (propertyJdbc.isEmpty()) {
                propertyJdbc = PropertiesLoaderUtils.loadProperties(resourceJdbc);
                if (propertyJdbc.isEmpty()) {
                    logger.error("#### TestLoadResource loadProperties error propertyJdbc is empty");
                }
            }
            if (propertyRedis.isEmpty()) {
                propertyRedis = PropertiesLoaderUtils.loadProperties(resourceRedis);
                if (propertyRedis.isEmpty()) {
                    logger.error("#### TestLoadResource loadProperties error propertyRedis is empty");

                }
//                RedisManagerImpl.prop = propertyRedis;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void setEnvironment(Environment environment) {
        globalDir=environment.getProperty(ENV_DIR);
        if(StringUtils.isEmpty(globalDir)){
            logger.error("####error during init globalDir property, probably not set###");
        }
        loadProperties();
    }

    public static void main(String[] args) {
        System.out.println(System.getenv(ENV_DIR));

    }
}
