package it.kdm.docer.firma.model;

import it.kdm.sign.model.ResultInfo;

/**
 * Created by Łukasz Kwasek on 23/06/15.
 */
public class DocumentResultInfo {

    private String fileName;
    private ResultInfo resultInfo;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ResultInfo getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(ResultInfo resultInfo) {
        this.resultInfo = resultInfo;
    }
}
