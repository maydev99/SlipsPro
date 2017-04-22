package com.droidloft.slipspro;

import android.view.View;
import android.widget.TextView;

/**
 * Created by DroidLoft2 on 4/22/2017.
 */

public class ListItem  {

    private String dateText;
    private String typeText;
    private String descriptionText;
    private String amountText;

    public String getDateText() {
        return dateText;
    }

    public void setDateText(String dateText) {
        this.dateText = dateText;
    }

    public String getTypeText() {
        return typeText;
    }

    public void setTypeText(String typeText) {
        this.typeText = typeText;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public String getAmountText() {
        return amountText;
    }

    public void setAmountText(String amountText) {
        this.amountText = amountText;
    }
}
