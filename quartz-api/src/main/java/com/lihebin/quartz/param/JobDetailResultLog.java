package com.lihebin.quartz.param;

import java.util.Map;

/**
 * Created by lihebin on 2019/8/28.
 */
public class JobDetailResultLog {


    private Long id;

    private Long jobId;


    private String topicType;

    /**
     * cron 表达式
     */
    private String cronExpression;

    private Map jobDataMap;

    private String result;

    private int status;

    private String ctime;

    private String mtime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopicType() {
        return topicType;
    }

    public void setTopicType(String topicType) {
        this.topicType = topicType;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Map getJobDataMap() {
        return jobDataMap;
    }

    public void setJobDataMap(Map jobDataMap) {
        this.jobDataMap = jobDataMap;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
