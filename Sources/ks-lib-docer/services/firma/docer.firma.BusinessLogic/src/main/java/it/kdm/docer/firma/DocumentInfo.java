package it.kdm.docer.firma;

/**
 * Created by Łukasz Kwasek on 24/06/15.
 */
public class DocumentInfo {

    private String docNum;
    private String name;
    private String inputFileName;

    public String getDocNum() {
        return docNum;
    }

    public void setDocNum(String docNum) {
        this.docNum = docNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInputFileName() {
        return inputFileName;
    }

    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }
}
