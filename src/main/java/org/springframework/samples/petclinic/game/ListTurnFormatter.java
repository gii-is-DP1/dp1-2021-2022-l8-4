package org.springframework.samples.petclinic.game;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  @author Ricardo Nadal Garcia 
 */

public class ListTurnFormatter {
    
    public static List<Integer> stringToList(String texto){
        String textoTrabajable=texto.replace("[", "").replace("]","").trim();
        List<Integer> listaResultado=Arrays.asList(textoTrabajable.split(",")).stream()
                                                                              .map(valor-> Integer.valueOf(valor.trim()))
                                                                              .collect(Collectors.toList());
        return listaResultado;
    }
}
