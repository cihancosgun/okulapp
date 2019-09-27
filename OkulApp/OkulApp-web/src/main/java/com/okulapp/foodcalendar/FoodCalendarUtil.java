/*
 * 
 * 
 * 
 */
package com.okulapp.foodcalendar;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
public class FoodCalendarUtil {

    public static List<String> getMonthList() {
        List<String> monthList = new ArrayList();
        String[] months = new DateFormatSymbols(Locale.forLanguageTag("tr")).getMonths();
        for (String month : months) {
            if (!month.isEmpty()) {
                monthList.add(month);
            }
        }
        return monthList;
    }
}
