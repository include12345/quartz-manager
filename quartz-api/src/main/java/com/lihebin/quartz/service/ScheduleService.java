package com.lihebin.quartz.service;

import com.lihebin.quartz.param.JobDetailResult;
import com.lihebin.quartz.param.JobDetailAdd;
import com.lihebin.quartz.param.JobDetailResultLog;
import com.lihebin.quartz.param.JobDetailUpdate;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.Optional;

/**
 * Created by lihebin on 2019/8/28.
 */
public interface ScheduleService {

    /**
     * 分页获取任务列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<JobDetailResult> listJobDetailPage(Optional<Date> ctimeStart,
                                            Optional<Date> ctimeEnd,
                                            Optional<String> label,
                                            Optional<String> jobName,
                                            Optional<String> topicType,
                                            int pageNo, int pageSize);

    /**
     * 分页获取任务列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<JobDetailResultLog> listJobDetailLogPage(long jobId,
                                                  Optional<Date> ctimeStart,
                                                  Optional<Date> ctimeEnd,
                                                  int pageNo, int pageSize);

    /**
     * 创建定时任务
     *
     * @param jobDetailAdd
     * @return
     */
    JobDetailResult createScheduleJob(JobDetailAdd jobDetailAdd);

    /**
     * 更新定时任务
     *
     * @param jobDetailUpdate
     * @return
     */
    JobDetailResult updateScheduleJob(JobDetailUpdate jobDetailUpdate);

    /**
     * 获取定时任务信息
     *
     * @param id
     * @return
     */
    JobDetailResult getScheduleJobDetail(long id);


    /**
     * 启动定时任务
     *
     * @param id
     * @return
     */
    boolean startScheduleJob(long id);

    /**
     * 暂停定时任务
     *
     * @param id
     * @return
     */
    boolean stopScheduleJob(long id);

    /**
     * 恢复定时任务
     *
     * @param id
     * @return
     */
    boolean resumeScheduleJob(long id);


    /**
     * 删除定时任务
     * @param id
     * @return
     */
    boolean deleteScheduleJob(long id);
}
