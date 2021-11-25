package org.springframework.samples.petclinic.dice;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ricardo Nadal Garcia
 */

public class RollValuesFormatter {
    public static List<DiceValues> stringToList(String texto){
        String textoTrabajable=texto.replace("[", "").replace("]","").trim();
        List<DiceValues> listaResultado=Arrays.asList(textoTrabajable.split(",")).stream()
                                                                              .map(valor-> DiceValues.valueOf(valor.trim()))
                                                                              .collect(Collectors.toList());
        return listaResultado;
    }
}
