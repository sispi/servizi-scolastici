package it.kdm.docer.registrazione.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegistriPerpetui {

    private static List<String> registriList = new ArrayList<String>();

    public void setRegistri(String registri) {
        if (registri != null) {
            registriList = Arrays.asList(registri.trim().split(" *[;,] *"));
        }
    }

    public static List<String> getRegistriPerpetui() {
        return registriList;
    }

}