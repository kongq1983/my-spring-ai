package com.kq.spring.ai.zhipu.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 当前时间
 */
public class DateTimeTools {

    @Tool(description = "获取当前时间")
    String getCurrentDateTime() {
        return "北京时间: "+LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }


    @Tool(description = "获取现在是几点")
    String getCurrentHour() {
        return "现在时间: "+ LocalTime.now();
    }

}
