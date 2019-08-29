package com.lihebin.quartz.param;

import java.util.Map;

/**
 * Created by lihebin on 2019/8/28.
 */
public class JobDetailResult {


    private Long id;

    private String jobName;


    private String topicType;

    /**
     * cron 表达式
     */
    private String cronExpression;

    private Map jobDataMap;

    private String label;

    private int status;

    private String ctime;

    private String mtime;

    private String operatorCreate;

    private String operatorUpdate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public String getOperatorCreate() {
        return operatorCreate;
    }

    public void setOperatorCreate(String operatorCreate) {
        this.operatorCreate = operatorCreate;
    }

    public String getOperatorUpdate() {
        return operatorUpdate;
    }

    public void setOperatorUpdate(String operatorUpdate) {
        this.operatorUpdate = operatorUpdate;
    }
}
