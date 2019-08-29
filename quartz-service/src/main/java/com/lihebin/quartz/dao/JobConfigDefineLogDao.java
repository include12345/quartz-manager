package com.lihebin.quartz.dao;

import com.lihebin.quartz.model.JobConfigDefineLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by lihebin on 2019/8/28.
 */
@Repository
public interface JobConfigDefineLogDao extends JpaRepository<JobConfigDefineLog, Long>, JpaSpecificationExecutor<JobConfigDefineLog>{

    JobConfigDefineLog findAllByJobId(long jobId);

}
