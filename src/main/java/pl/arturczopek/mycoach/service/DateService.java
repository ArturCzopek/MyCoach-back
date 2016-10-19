package pl.arturczopek.mycoach.service;

import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

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

    public static final int FIRST_MONTH = 1;

    public Date getCurrentDate() {
        return new Date(Calendar.getInstance().getTime().getTime());
    }

    public Date buildFirstMonthDay(int year, int month) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        Date endDate;
        endDate = cal.getTime();

        return endDate;
    }


}
