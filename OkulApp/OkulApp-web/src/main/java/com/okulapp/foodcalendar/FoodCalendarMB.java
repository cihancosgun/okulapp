/*
 * 
 * 
 * 
 */
package com.okulapp.foodcalendar;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;
import com.okulapp.data.okul.MyDataSBLocal;
import com.okulapp.dispatcher.DispatcherMB;
import com.okulapp.forms.CrudMB;
import com.okulapp.notify.NotificationMB;
import com.okulapp.security.SecurityMB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
@Named(value = "foodCalendarMB")
@SessionScoped
public class FoodCalendarMB implements Serializable {

    @EJB
    MyDataSBLocal myDataSB;

    @Inject
    CrudMB crudMB;

    private @Inject
    SecurityMB securityMB;

    private @Inject
    DispatcherMB dispatcherMB;

    private @Inject
    NotificationMB notificationMB;

    private final SimpleDateFormat sdfDate = new SimpleDateFormat("dd MMMM yyyy EEEEE", Locale.forLanguageTag("tr"));
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.forLanguageTag("tr"));
    private List<Map<String, Object>> branches;
    private ObjectId selectedBranch;
    private List<String> monthList;
    private String selectedMonth;
    private Map<String, Object> currentRec;

    /**
     * Creates a new instance of FoodCalendarMB
     */
    public FoodCalendarMB() {
    }

    public Map<String, Object> getCurrentRec() {
        return currentRec;
    }

    public List<String> getMonthList() {
        if (monthList == null) {
            monthList = new ArrayList();
            String[] months = new DateFormatSymbols(Locale.forLanguageTag("tr")).getMonths();
            for (String month : months) {
                if (!month.isEmpty()) {
                    monthList.add(month);
                }
            }
        }
        return monthList;
    }

    public String getSelectedMonth() {
        if (selectedMonth == null) {
            setSelectedMonth(monthList.get(Calendar.getInstance().getTime().getMonth()));
        }
        return selectedMonth;
    }

    @PostConstruct
    public void init() {
        branches = myDataSB.getAdvancedDataAdapter().getList(myDataSB.getDbName(), "branch", new BasicDBObject(), new BasicDBObject());
        if (branches != null && securityMB.getLoginUser() != null) {
            setSelectedBranch((ObjectId) securityMB.getLoginUser().getOrDefault("branch", branches.get(0).get("_id")));
        }
    }

    public List<Map<String, Object>> getBranches() {
        return branches;
    }

    public void setBranches(List<Map<String, Object>> branches) {
        this.branches = branches;
    }

    public ObjectId getSelectedBranch() {
        return selectedBranch;
    }

    public void setCurrentRec(Map<String, Object> currentRec) {
        this.currentRec = currentRec;
    }

    public void setSelectedBranch(ObjectId selectedBranch) {
        this.selectedBranch = selectedBranch;
    }

    public void setSelectedMonth(String selectedMonth) {
        this.selectedMonth = selectedMonth;
        if (this.selectedMonth != null) {
            Calendar cldr = Calendar.getInstance();
            currentRec = myDataSB.getAdvancedDataAdapter().read(myDataSB.getDbName(), "foodCalendar", QueryBuilder
                    .start("year").is(cldr.get(Calendar.YEAR))
                    .and("month").is(selectedMonth)
                    .and("monthNumber").is(monthList.indexOf(selectedMonth) + 1)
                    .and("branch").is(selectedBranch)
                    .get().toMap());
            if (currentRec == null) {
                currentRec = new HashMap();
                currentRec.put("branch", selectedBranch);
                currentRec.put("year", cldr.get(Calendar.YEAR));
                currentRec.put("month", selectedMonth);
                currentRec.put("monthNumber", monthList.indexOf(selectedMonth) + 1);
                currentRec.put("creator", securityMB.getRemoteUserName());
                currentRec.put("createDate", Calendar.getInstance().getTime());

                List<Map<String, Object>> foods = new ArrayList();
                int lengthOfMonth = YearMonth.of(Calendar.getInstance().getTime().getYear(), monthList.indexOf(selectedMonth)).lengthOfMonth();
                for (int i = 1; i < lengthOfMonth; i++) {
                    Map<String, Object> rec = new HashMap();
                    Date dt = new Date(Calendar.getInstance().getTime().getYear(), monthList.indexOf(selectedMonth), i);
                    cldr.setTime(dt);
                    if (cldr.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cldr.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                        rec.put("date", dt);
                        rec.put("dateStr", sdfDate.format(dt));
                        rec.put("sabah", "");
                        rec.put("ogle", "");
                        rec.put("ikindi", "");
                        rec.put("aksam", "");
                        foods.add(rec);
                    }
                }
                currentRec.put("foods", foods);
            }
        }
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Yemek Bilgisi Değiştirildi", "Eski : " + oldValue + ", Yeni :" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void save() {
        Calendar cldr = Calendar.getInstance();
        if (currentRec.containsKey("_id")) {
            myDataSB.getAdvancedDataAdapter().update(myDataSB.getDbName(), "foodCalendar", currentRec);
        } else {
            myDataSB.getAdvancedDataAdapter().create(myDataSB.getDbName(), "foodCalendar", currentRec);
            currentRec = myDataSB.getAdvancedDataAdapter().read(myDataSB.getDbName(), "foodCalendar", QueryBuilder
                    .start("year").is(cldr.get(Calendar.YEAR))
                    .and("month").is(selectedMonth)
                    .and("monthNumber").is(monthList.indexOf(selectedMonth) + 1)
                    .and("branch").is(selectedBranch)
                    .get().toMap());
        }

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Yemek Takvimi Kaydedildi", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

}
