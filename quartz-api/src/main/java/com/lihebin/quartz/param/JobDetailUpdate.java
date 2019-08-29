package com.lihebin.quartz.param;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Created by lihebin on 2019/8/28.
 */
public class JobDetailUpdate {


    @NotNull
    private long id;

//    @NotBlank
//    @Length(max = 200, message = "名称长度范围0-200个字符")
//    private String label;

//    @NotBlank
//    @Length(max = 36, message = "名称长度范围0-36个字符")
//    private String topicType;

//    @Pattern(regexp = "(((^([0-9]|[0-5][0-9])(\\\\,|\\\\-|\\\\/){1}([0-9]|[0-5][0-9]) )|^([0-9]|[0-5][0-9]) |^(\\\\* ))((([0-9]|[0-5][0-9])(\\\\,|\\\\-|\\\\/){1}([0-9]|[0-5][0-9]) )|([0-9]|[0-5][0-9]) |(\\\\* ))((([0-9]|[01][0-9]|2[0-3])(\\\\,|\\\\-|\\\\/){1}([0-9]|[01][0-9]|2[0-3]) )|([0-9]|[01][0-9]|2[0-3]) |(\\\\* ))((([0-9]|[0-2][0-9]|3[01])(\\\\,|\\\\-|\\\\/){1}([0-9]|[0-2][0-9]|3[01]) )|(([0-9]|[0-2][0-9]|3[01]) )|(\\\\? )|(\\\\* )|(([1-9]|[0-2][0-9]|3[01])L )|([1-7]W )|(LW )|([1-7]\\\\#[1-4] ))((([1-9]|0[1-9]|1[0-2])(\\\\,|\\\\-|\\\\/){1}([1-9]|0[1-9]|1[0-2]) )|([1-9]|0[1-9]|1[0-2]) |(\\\\* ))(([1-7](\\\\,|\\\\-|\\\\/){1}[1-7])|([1-7])|(\\\\?)|(\\\\*)|(([1-7]L)|([1-7]\\\\#[1-4]))))|(((^([0-9]|[0-5][0-9])(\\\\,|\\\\-|\\\\/){1}([0-9]|[0-5][0-9]) )|^([0-9]|[0-5][0-9]) |^(\\\\* ))((([0-9]|[0-5][0-9])(\\\\,|\\\\-|\\\\/){1}([0-9]|[0-5][0-9]) )|([0-9]|[0-5][0-9]) |(\\\\* ))((([0-9]|[01][0-9]|2[0-3])(\\\\,|\\\\-|\\\\/){1}([0-9]|[01][0-9]|2[0-3]) )|([0-9]|[01][0-9]|2[0-3]) |(\\\\* ))((([0-9]|[0-2][0-9]|3[01])(\\\\,|\\\\-|\\\\/){1}([0-9]|[0-2][0-9]|3[01]) )|(([0-9]|[0-2][0-9]|3[01]) )|(\\\\? )|(\\\\* )|(([1-9]|[0-2][0-9]|3[01])L )|([1-7]W )|(LW )|([1-7]\\\\#[1-4] ))((([1-9]|0[1-9]|1[0-2])(\\\\,|\\\\-|\\\\/){1}([1-9]|0[1-9]|1[0-2]) )|([1-9]|0[1-9]|1[0-2]) |(\\\\* ))(([1-7](\\\\,|\\\\-|\\\\/){1}[1-7] )|([1-7] )|(\\\\? )|(\\\\* )|(([1-7]L )|([1-7]\\\\#[1-4]) ))((19[789][0-9]|20[0-9][0-9])\\\\-(19[789][0-9]|20[0-9][0-9])))",
//            message = "cron格式有误")
    private String cronExpression;

    private Map dataMap;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Map getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map dataMap) {
        this.dataMap = dataMap;
    }
}
