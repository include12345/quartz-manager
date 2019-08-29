package com.lihebin.quartz.producer;

import com.alibaba.fastjson.JSON;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lihebin on 2019/8/28.
 */

public class QuartzProducerBean extends QuartzJobBean {
    private final static Logger logger = LoggerFactory.getLogger(QuartzProducerBean.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("jobExecutionContext:{}", jobExecutionContext);
        String topic  = jobExecutionContext.getTrigger().getJobKey().getGroup();
        Map dataMap = new HashMap();
        dataMap.put("jobName", jobExecutionContext.getTrigger().getJobKey().getName());
        dataMap.putAll(JSON.parseObject(String.valueOf(jobExecutionContext.getTrigger().getJobDataMap().get("jobData"))));
        ListenableFuture future = kafkaTemplate.send(topic, dataMap);
        future.addCallback(new ListenableFutureCallback() {
            @Override
            public void onFailure(Throwable ex) {
                logger.error("kafkaTemplate error:{},{},{}", topic, dataMap, ex);
            }

            @Override
            public void onSuccess(Object result) {
                logger.info("kafkaTemplate success:{},{},{}", topic, dataMap, result);
            }
        });
    }
}
