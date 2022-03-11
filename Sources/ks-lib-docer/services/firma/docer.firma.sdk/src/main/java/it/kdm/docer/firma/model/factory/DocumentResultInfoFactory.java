package it.kdm.docer.firma.model.factory;

import it.kdm.docer.firma.model.DocumentResultInfo;
import it.kdm.sign.model.ResultInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Łukasz Kwasek on 23/06/15.
 */
public class DocumentResultInfoFactory {

    public static DocumentResultInfo create(String fileName, ResultInfo resultInfo) {
        DocumentResultInfo ret = new DocumentResultInfo();

        ret.setFileName(fileName);
        ret.setResultInfo(resultInfo);

        return ret;
    }

    public static List<DocumentResultInfo> list(Map<String, ResultInfo> resultInfoMap) {
        List<DocumentResultInfo> ret = new ArrayList<DocumentResultInfo>();

        for (String d : resultInfoMap.keySet()) {
            ret.add(create(d, resultInfoMap.get(d)));
        }

        return ret;
    }

}
