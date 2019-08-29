package com.lihebin.quartz.service;

import com.alibaba.fastjson.JSON;
import com.lihebin.quartz.dao.JobConfigDefineDao;
import com.lihebin.quartz.dao.JobConfigDefineLogDao;
import com.lihebin.quartz.exception.QuartzException;
import com.lihebin.quartz.model.JobConfigDefine;
import com.lihebin.quartz.model.JobConfigDefineLog;
import com.lihebin.quartz.param.JobDetailResult;
import com.lihebin.quartz.param.JobDetailAdd;
import com.lihebin.quartz.param.JobDetailResultLog;
import com.lihebin.quartz.param.JobDetailUpdate;
import com.lihebin.quartz.producer.QuartzProducerBean;
import com.lihebin.quartz.utils.Code;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by lihebin on 2019/8/28.
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private static final int STATUS_PRE = 0;
    private static final int STATUS_ING = 1;
    private static final int STATUS_SUCCESS = 2;
    private static final int STATUS_FAIL = 3;
    private static final int STATUS_STOP = 4;


    @Autowired
    private JobConfigDefineDao jobConfigDefineDao;

    @Autowired
    private JobConfigDefineLogDao jobConfigDefineLogDao;

    @Autowired
    private Scheduler scheduler;

    @Override
    public Page<JobDetailResult> listJobDetailPage(Optional<Date> ctimeStart,
                                                   Optional<Date> ctimeEnd,
                                                   Optional<String> label,
                                                   Optional<String> jobName,
                                                   Optional<String> topicType,
                                                   int pageNo, int pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC,"ctime");
        Page<JobConfigDefine> jobConfigDefinePage = jobConfigDefineDao.findAll((root, criteriaQuery, criteriaBuilder) -> {
            Path<String> namePath = root.get("label");
            Path<String> jobNamePath = root.get("job_name");
            Path<String> topicTypePath = root.get("topic_type");
            Path<Date> timePath = root.get("ctime");
            List<Predicate> predicateList = new ArrayList<>();
            label.ifPresent(s -> predicateList.add(criteriaBuilder.like(namePath, "%" + s + "%")));
            jobName.ifPresent(s -> predicateList.add(criteriaBuilder.equal(jobNamePath, s)));
            topicType.ifPresent(s -> predicateList.add(criteriaBuilder.equal(topicTypePath, s)));
            if (ctimeStart.isPresent() && ctimeEnd.isPresent()) {
                predicateList.add(criteriaBuilder.between(timePath, ctimeStart.get(), ctimeEnd.get()));
            }
            Predicate[] p = new Predicate[predicateList.size()];
            return criteriaBuilder.and(predicateList.toArray(p));
        }, PageRequest.of(pageNo, pageSize, sort));
        return new PageImpl<>(
                jobConfigDefinePage.getContent()
                        .stream()
                        .map(this::buildJobDetailResult)
                        .collect(Collectors.toList()),
                jobConfigDefinePage.previousPageable(), jobConfigDefinePage.getTotalElements());
    }

    @Override
    public Page<JobDetailResultLog> listJobDetailLogPage(long jobId, Optional<Date> ctimeStart, Optional<Date> ctimeEnd, int pageNo, int pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC,"ctime");
        Page<JobConfigDefineLog> jobConfigDefineLogPage = jobConfigDefineLogDao.findAll((root, criteriaQuery, criteriaBuilder) -> {
            Path<String> jobIdPath = root.get("job_id");
            Path<Date> timePath = root.get("ctime");
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(criteriaBuilder.equal(jobIdPath, jobId));
            if (ctimeStart.isPresent() && ctimeEnd.isPresent()) {
                predicateList.add(criteriaBuilder.between(timePath, ctimeStart.get(), ctimeEnd.get()));
            }
            Predicate[] p = new Predicate[predicateList.size()];
            return criteriaBuilder.and(predicateList.toArray(p));
        }, PageRequest.of(pageNo, pageSize, sort));
        return new PageImpl<>(
                jobConfigDefineLogPage.getContent()
                        .stream()
                        .map(this::buildJobDetailLogResult)
                        .collect(Collectors.toList()),
                jobConfigDefineLogPage.previousPageable(), jobConfigDefineLogPage.getTotalElements());
    }

    @Override
    public JobDetailResult createScheduleJob(JobDetailAdd jobDetailAdd) {
        JobConfigDefine jobConfigDefine = jobConfigDefineDao.findAllByLabel(jobDetailAdd.getLabel());
        if (jobConfigDefine != null) {
            throw new QuartzException(Code.CODE_EXIST, "任务名称已存在");
        }
        if (!CronExpression.isValidExpression(jobDetailAdd.getCronExpression())) {
            throw new QuartzException(Code.CODE_PARAM_ERROR, "cron格式有误");
        }
        jobConfigDefine = new JobConfigDefine();
        jobConfigDefine.setLabel(jobDetailAdd.getLabel());
        jobConfigDefine.setCronExpression(jobDetailAdd.getCronExpression());
        jobConfigDefine.setDataMap(JSON.toJSONString(jobDetailAdd.getDataMap()));
        jobConfigDefine.setTopicType(jobDetailAdd.getTopicType());
        jobConfigDefine.setJobName(UUID.randomUUID().toString());
        jobConfigDefine.setStatus(STATUS_PRE);
        jobConfigDefine = jobConfigDefineDao.save(jobConfigDefine);
        JobDetailResult jobDetailResult = new JobDetailResult();
        jobDetailResult.setId(jobConfigDefine.getId());
        return jobDetailResult;
    }

    private JobDetailResultLog buildJobDetailLogResult(JobConfigDefineLog jobConfigDefineLog) {
        JobDetailResultLog jobDetailResultLog = new JobDetailResultLog();
        jobDetailResultLog.setId(jobConfigDefineLog.getId());
        jobDetailResultLog.setJobId(jobConfigDefineLog.getJobId());
        jobDetailResultLog.setTopicType(jobConfigDefineLog.getTopicType());
        jobDetailResultLog.setCronExpression(jobConfigDefineLog.getCronExpression());
        jobDetailResultLog.setJobDataMap(JSON.parseObject(jobConfigDefineLog.getDataMap()));
        jobDetailResultLog.setCtime(String.valueOf(jobConfigDefineLog.getCtime()));
        jobDetailResultLog.setMtime(String.valueOf(jobConfigDefineLog.getMtime()));
        jobDetailResultLog.setStatus(jobConfigDefineLog.getStatus());
        jobDetailResultLog.setResult(jobConfigDefineLog.getResult());
        return jobDetailResultLog;
    }

    private JobDetailResult buildJobDetailResult(JobConfigDefine jobConfigDefine) {
        JobDetailResult jobDetailResult = new JobDetailResult();
        jobDetailResult.setId(jobConfigDefine.getId());
        jobDetailResult.setJobName(jobConfigDefine.getJobName());
        jobDetailResult.setLabel(jobConfigDefine.getLabel());
        jobDetailResult.setTopicType(jobConfigDefine.getTopicType());
        jobDetailResult.setCronExpression(jobConfigDefine.getCronExpression());
        jobDetailResult.setJobDataMap(JSON.parseObject(jobConfigDefine.getDataMap()));
        jobDetailResult.setCtime(String.valueOf(jobConfigDefine.getCtime()));
        jobDetailResult.setMtime(String.valueOf(jobConfigDefine.getMtime()));
        jobDetailResult.setStatus(jobConfigDefine.getStatus());
        jobDetailResult.setOperatorCreate(jobConfigDefine.getOperatorCreate());
        jobDetailResult.setOperatorUpdate(jobConfigDefine.getOperatorUpdate());
        return jobDetailResult;
    }

    @Transactional
    @Override
    public JobDetailResult updateScheduleJob(JobDetailUpdate jobDetailUpdate) {
        JobConfigDefine jobConfigDefine = checkJobConfigDefineExist(jobDetailUpdate.getId());
        if (STATUS_ING == jobConfigDefine.getStatus()) {
            throw new QuartzException(Code.CODE_SYSTEM_ERROR, "任务正在运行");
        }
        if (!CronExpression.isValidExpression(jobDetailUpdate.getCronExpression())) {
            throw new QuartzException(Code.CODE_PARAM_ERROR, "cron格式有误");
        }
        JobKey jobKey = new JobKey(jobConfigDefine.getJobName(), jobConfigDefine.getTopicType());
        TriggerKey trigger = TriggerKey.triggerKey(jobConfigDefine.getJobName(), jobConfigDefine.getTopicType());
        String jobDataMap = JSON.toJSONString(jobDetailUpdate.getDataMap());
        try {
            if (scheduler.checkExists(jobKey) && scheduler.checkExists(trigger)) {
                CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(trigger);
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobDetailUpdate.getCronExpression());
                cronTrigger = cronTrigger.getTriggerBuilder()
                        .withIdentity(trigger)
                        .withSchedule(scheduleBuilder)
                        .build();
                if (!cronTrigger.getJobDataMap().get("jobData").equals(jobDataMap)) {
                    cronTrigger.getJobDataMap().put("jobData", jobDataMap);
                }
                scheduler.rescheduleJob(trigger, cronTrigger);
                jobConfigDefine.setStatus(STATUS_ING);
                jobConfigDefine.setCronExpression(jobDetailUpdate.getCronExpression());
                jobConfigDefine.setDataMap(jobDataMap);
                jobConfigDefineDao.save(jobConfigDefine);
            } else {
                throw new QuartzException(Code.CODE_NOT_EXIST, "任务不存在");
            }
        } catch (SchedulerException e) {
            throw new QuartzException(Code.CODE_SYSTEM_ERROR, "任务修改失败");
        }
        JobDetailResult jobDetailResult = new JobDetailResult();
        jobDetailResult.setId(jobConfigDefine.getId());
        return jobDetailResult;
    }

    @Override
    public JobDetailResult getScheduleJobDetail(long id) {
        return buildJobDetailResult(checkJobConfigDefineExist(id));
    }

    @Transactional
    @Override
    public boolean startScheduleJob(long id) {
        JobConfigDefine jobConfigDefine = checkJobConfigDefineExist(id);
        JobDetail jobDetail = JobBuilder.newJob(QuartzProducerBean.class)
                .withIdentity(jobConfigDefine.getJobName(), jobConfigDefine.getTopicType())
//                .usingJobData("jobData", jobConfigDefine.getDataMap())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobConfigDefine.getJobName(), jobConfigDefine.getTopicType())
                .usingJobData("jobData", jobConfigDefine.getDataMap())
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(jobConfigDefine.getCronExpression()))
                .build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
            jobConfigDefine.setStatus(STATUS_ING);
            jobConfigDefineDao.save(jobConfigDefine);

        } catch (SchedulerException e) {
            throw new QuartzException(Code.CODE_SYSTEM_ERROR, String.format("启动任务失败:%s", e.getMessage()));
        }
        return true;
    }





    @Transactional
    @Override
    public boolean stopScheduleJob(long id) {
        JobConfigDefine jobConfigDefine = checkJobConfigDefineExist(id);
        JobKey jobKey = new JobKey(jobConfigDefine.getJobName(), jobConfigDefine.getTopicType());
        try {
            scheduler.pauseJob(jobKey);
            jobConfigDefine.setStatus(STATUS_STOP);
            jobConfigDefineDao.save(jobConfigDefine);
        } catch (SchedulerException e) {
            throw new QuartzException(Code.CODE_SYSTEM_ERROR, String.format("暂停任务失败:%s", e.getMessage()));
        }
        return true;
    }

    @Transactional
    @Override
    public boolean resumeScheduleJob(long id) {
        JobConfigDefine jobConfigDefine = checkJobConfigDefineExist(id);
        JobKey jobKey = new JobKey(jobConfigDefine.getJobName(), jobConfigDefine.getTopicType());
        try {
            scheduler.resumeJob(jobKey);
            jobConfigDefine.setStatus(STATUS_ING);
            jobConfigDefineDao.save(jobConfigDefine);
        } catch (SchedulerException e) {
            throw new QuartzException(Code.CODE_SYSTEM_ERROR, String.format("恢复任务失败:%s", e.getMessage()));
        }
        return true;
    }

    @Transactional
    @Override
    public boolean deleteScheduleJob(long id) {
        JobConfigDefine jobConfigDefine = checkJobConfigDefineExist(id);
        JobKey jobKey = new JobKey(jobConfigDefine.getJobName(), jobConfigDefine.getTopicType());
        try {
            scheduler.resumeJob(jobKey);
            jobConfigDefineDao.deleteById(id);
        } catch (SchedulerException e) {
            throw new QuartzException(Code.CODE_SYSTEM_ERROR, String.format("删除任务失败:%s", e.getMessage()));
        }
        return true;
    }

    private JobConfigDefine checkJobConfigDefineExist(long id) {
        Optional<JobConfigDefine> jobConfigDefineOptional = jobConfigDefineDao.findById(id);
        if (!jobConfigDefineOptional.isPresent()) {
            throw new QuartzException(Code.CODE_NOT_EXIST, "任务不存在");
        }
        return jobConfigDefineOptional.get();
    }
}
