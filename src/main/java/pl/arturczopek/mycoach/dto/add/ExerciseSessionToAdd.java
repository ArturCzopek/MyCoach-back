package pl.arturczopek.mycoach.dto.add;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 05-11-2016
 */

@Data
public class ExerciseSessionToAdd implements Serializable{

    private static final long serialVersionUID = -6451200851024914180L;

    private long exerciseId;

    private List<SeriesToAdd> series;
}
