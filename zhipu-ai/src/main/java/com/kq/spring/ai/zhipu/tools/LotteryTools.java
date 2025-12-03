package com.kq.spring.ai.zhipu.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

/**
 * 抽奖工具类
 */
@Slf4j
public class LotteryTools {

    private static final int[] LOTTERY_NUMBERS = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    @Tool(description = "帮我抽个奖")
    String getLottery() {

        int index = new Random().nextInt(10);

        String message = "恭喜你中了"+index+"等奖！";

        log.info("getLottery result={}",message);

        return message;
    }




}
