package pl.arturczopek.mycoach.model.preview;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Artur Czopek
 * @Date 10-10-2016
 */

@Data
public class WeightDatesPreview implements Serializable {

    private static final long serialVersionUID = -6574925549576683940L;

    private int year;

    private int month;
}
