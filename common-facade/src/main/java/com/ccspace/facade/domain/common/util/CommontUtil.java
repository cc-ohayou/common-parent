package com.ccspace.facade.domain.common.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CommontUtil {

    /**
     * 当天剩余秒数
     */
    public static Long timeRemaining() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midNight = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0);
        return ChronoUnit.SECONDS.between(now, midNight);
    }
}
