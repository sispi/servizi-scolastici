package it.kdm.docer.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by pamput on 2/5/14.
 */
public class StringServiceUtils {

    public static Collection<Long> parseLongQuery(String query){
        List<Long> ret = new ArrayList();

        //Cancelliamo tutti gli spazi
        query = query.replace(" ", "");
        String[] list = query.split(",");

        for(String s : list){
            String[] l = s.split("-");

            if(l.length > 2){
                throw new RuntimeException("Errore nella sintassi della query");
            } else if(l.length == 2){
                long start = Long.parseLong(l[0]);
                long stop = Long.parseLong(l[1]);

                for(int i = 0; i <= stop - start; i++){
                    ret.add(i + start);
                }
            } else {
                ret.add(Long.parseLong(s));
            }
        }

        return ret;
    }
}
