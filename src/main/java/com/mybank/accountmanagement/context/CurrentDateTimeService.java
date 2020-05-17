package com.mybank.accountmanagement.context;

import java.time.ZonedDateTime;

public class CurrentDateTimeService implements DateTimeService {
    @Override
    public ZonedDateTime getCurrentDateTime() {
        return ZonedDateTime.now();
    }

}
