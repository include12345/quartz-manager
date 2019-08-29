package com.lihebin.quartz.dao;

import com.lihebin.quartz.model.JobConfigDefine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by lihebin on 2019/8/28.
 */
@Repository
public interface JobConfigDefineDao extends JpaRepository<JobConfigDefine, Long>, JpaSpecificationExecutor<JobConfigDefine>{

    JobConfigDefine findAllByJobName(String jobName);

    JobConfigDefine findAllByLabel(String label);
}
