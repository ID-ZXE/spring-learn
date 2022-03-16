package com.github.controller.service;

import com.github.controller.exceptions.GlobalException;
import com.github.controller.service.common.BizTemplate;
import com.github.controller.service.common.monitor.Monitors;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author hangs.zhang
 * @date 19-7-24 下午2:57
 * *********************
 * function:
 */
@Service
@SuppressWarnings("all")
public class FoobarBiz {

    protected static Logger logger = LoggerFactory.getLogger(FoobarBiz.class);

    public String bizMethod(final String data) {

        logger.info("bizMethod info log");
        logger.debug("bizMethod debug log");

        return new BizTemplate<String>(Monitors.inner_error) {
            @Override
            @SneakyThrows
            protected String process() throws GlobalException {
                logger.info("bizMethod info log");
                logger.debug("bizMethod debug log");
                Thread.sleep(new Random().nextInt(100));
                return "deal:" + data;
            }

            @Override
            protected String defaultData() throws GlobalException {
                return "default data";
            }

            @Override
            protected void checkParams() throws GlobalException {
                if (StringUtils.isBlank(data)) {
                    throw new GlobalException("param check error, data is null");
                }
            }
        }.execute();
    }

}
