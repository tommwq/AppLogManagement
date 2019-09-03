package com.github.tommwq.applogmanagement;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
                                          
@Component
public class InjectedBean {

        // @Bean
        // public HttpMessageConverters httpMessageConverter() {
        //         //定义一个转换消息的对象
        //         FastJsonHttpMessageConverter4 fastConverter = new FastJsonHttpMessageConverter4();
        //         //添加fastjson的配置信息 比如 ：是否要格式化返回的json数据
        //         FastJsonConfig fastJsonConfig = new FastJsonConfig();
        //         fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        //         //在转换器中添加配置信息
        //         fastConverter.setFastJsonConfig(fastJsonConfig);
        //         HttpMessageConverter<?> converter = fastConverter;
        //         return new HttpMessageConverters(converter);
        // }
}
