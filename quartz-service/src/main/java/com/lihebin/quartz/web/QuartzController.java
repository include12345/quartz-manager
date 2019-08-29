package com.lihebin.quartz.web;

import com.lihebin.quartz.param.JobDetailAdd;
import com.lihebin.quartz.param.JobDetailUpdate;
import com.lihebin.quartz.param.RequestMap;
import com.lihebin.quartz.service.ScheduleService;
import com.lihebin.quartz.utils.Result;
import com.lihebin.quartz.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

/**
 * Created by lihebin on 2019/8/29.
 */
@RestController
@RequestMapping("/api/quartz")
public class QuartzController {

    @Autowired
    private ScheduleService scheduleService;

    @RequestMapping(value = "/listJobDetailPage", method = RequestMethod.GET)
    public Result listJobDetailPage(@RequestHeader("token") String token,
                                                      @PathVariable(value = "label") String label,
                                                      @PathVariable(value = "jobName") String jobName,
                                                      @PathVariable(value = "topicType") String topicType,
                                                      @RequestParam(value = "ctimeStart", required = false) Date ctimeStart,
                                                      @RequestParam(value = "ctimeEnd", required = false) Date ctimeEnd,
                                                      @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                      @RequestParam(value = "pageSize", defaultValue = "30") int pageSize
    ) {
        return ResultUtil.success(scheduleService.listJobDetailPage(
                Optional.ofNullable(ctimeStart),
                Optional.ofNullable(ctimeEnd),
                Optional.ofNullable(label),
                Optional.ofNullable(jobName),
                Optional.ofNullable(topicType),
                pageNo,
                pageSize));
    }

    @RequestMapping(value = "/jobDetail/{jobId:\\d+}/listLogPage", method = RequestMethod.GET)
    public Result listLogPage(@RequestHeader("token") String token,
                              @PathVariable(value = "jobId") long jobId,
                                    @RequestParam(value = "ctimeStart", required = false) Date ctimeStart,
                                    @RequestParam(value = "ctimeEnd", required = false) Date ctimeEnd,
                                    @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                    @RequestParam(value = "pageSize", defaultValue = "30") int pageSize
    ) {
        return ResultUtil.success(scheduleService.listJobDetailLogPage(
                jobId,
                Optional.ofNullable(ctimeStart),
                Optional.ofNullable(ctimeEnd),
                pageNo,
                pageSize));
    }

    @RequestMapping(value = "/createScheduleJob", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result createScheduleJob(@Valid @RequestHeader("token") String token, @RequestBody JobDetailAdd jobDetailAdd) {
        return ResultUtil.success(scheduleService.createScheduleJob(jobDetailAdd));
    }


    @RequestMapping(value = "/updateScheduleJob", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result updateScheduleJob(@Valid @RequestHeader("token") String token, @RequestBody JobDetailUpdate jobDetailUpdate) {
        return ResultUtil.success(scheduleService.updateScheduleJob(jobDetailUpdate));
    }


    @RequestMapping(value = "/startScheduleJob", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result startScheduleJob(@Valid @RequestHeader("token") String token, @RequestBody RequestMap requestMap) {
        return ResultUtil.success(scheduleService.startScheduleJob(requestMap.getId()));
    }

    @RequestMapping(value = "/stopScheduleJob", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result stopScheduleJob(@Valid @RequestHeader("token") String token, @RequestBody RequestMap requestMap) {
        return ResultUtil.success(scheduleService.stopScheduleJob(requestMap.getId()));
    }

    @RequestMapping(value = "/resumeScheduleJob", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result resumeScheduleJob(@Valid @RequestHeader("token") String token, @RequestBody RequestMap requestMap) {
        return ResultUtil.success(scheduleService.resumeScheduleJob(requestMap.getId()));
    }

    @DeleteMapping(value = "/deleteScheduleJob/{id:\\d+}")
    public Result deleteScheduleJob(@Valid @RequestHeader("token") String token, @PathVariable(value = "id") long id) {
        return ResultUtil.success(scheduleService.deleteScheduleJob(id));
    }
}
