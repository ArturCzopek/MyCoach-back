package pl.arturczopek.mycoach.service;

import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

/**
 * @Author Artur Czopek
 * @Date 10-10-2016
 */


/**
 * ATTENTION!!
 * If you'll send json with date, remember. Add one month! For example, if you want to add date with january date,
 * then send 2 month number. This is associated with wrong serialization
 */

@Service
public class DateService {

    private static final int FIRST_DAY = 1;

    public Date getCurrentDate() {
        return Date.valueOf(LocalDate.now());
    }

    public LocalDate buildFirstMonthDay(int year, int month) {
        return LocalDate.of(year, month, FIRST_DAY);
    }

    public LocalDate getLastDayOfTheMonth(LocalDate date) {
        return LocalDate.of(date.getYear(), date.getMonth(), date.lengthOfMonth());
    }
}

