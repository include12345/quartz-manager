package com.lihebin.quartz.consumer;

import com.alibaba.fastjson.JSON;
import com.lihebin.quartz.dao.JobConfigDefineDao;
import com.lihebin.quartz.dao.JobConfigDefineLogDao;
import com.lihebin.quartz.model.JobConfigDefine;
import com.lihebin.quartz.model.JobConfigDefineLog;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Map;
import java.util.Optional;

/**
 * Created by lihebin on 2019/8/29.
 */
public class QuartzConsumer {
    private final static Logger log = LoggerFactory.getLogger(QuartzConsumer.class);

    @Autowired
    private JobConfigDefineLogDao jobConfigDefineLogDao;

    @Autowired
    private JobConfigDefineDao jobConfigDefineDao;

    @KafkaListener(topics = "${spring.kafka.topics.quartz-manager}", containerFactory = "kafkaListenerContainerFactory")
    public void receive(String message) {
        log.info("kafka message:{}", message);
        Map jobData = JSON.parseObject(message);
        buildJobConfigDefineLog(jobData);
    }

    private void buildJobConfigDefineLog(Map jobData) {
        String jobName = MapUtils.getString(jobData, "jobName");
        int status = MapUtils.getIntValue(jobData, "jobStatus");
        String result = MapUtils.getString(jobData, "jobResult");
        JobConfigDefine jobConfigDefine = jobConfigDefineDao.findAllByJobName(jobName);
        if (jobConfigDefine == null) {
            log.error("buildJobConfigDefineLog: kafka message is error:{}", jobData);
            return;
        }
        addJobConfigDefineLog(jobConfigDefine, status, result);
    }

    private long addJobConfigDefineLog(JobConfigDefine jobConfigDefine, int status, String result) {
        JobConfigDefineLog jobConfigDefineLog = new JobConfigDefineLog();
        jobConfigDefineLog.setJobId(jobConfigDefine.getId());
        jobConfigDefineLog.setTopicType(jobConfigDefine.getTopicType());
        jobConfigDefineLog.setCronExpression(jobConfigDefine.getCronExpression());
        jobConfigDefineLog.setDataMap(jobConfigDefine.getDataMap());
        jobConfigDefineLog.setStatus(status);
        jobConfigDefineLog.setResult(result);
        jobConfigDefineLog = jobConfigDefineLogDao.save(jobConfigDefineLog);
        return jobConfigDefineLog.getId();
    }
}
