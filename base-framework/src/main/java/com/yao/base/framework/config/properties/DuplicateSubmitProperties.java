package com.yao.base.framework.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Data
@Component
@ConfigurationProperties(prefix = "duplicate-submit")
public class DuplicateSubmitProperties {

    private boolean enabled = true;

    private String keyPrefix = "dup:submit:";

    private long defaultTtl = 5;

    private TimeUnit defaultTimeUnit = TimeUnit.SECONDS;

    private String defaultMessage = "请勿重复提交";
}
