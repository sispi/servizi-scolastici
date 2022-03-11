package keysuite.docer.client;

import keysuite.docer.query.ISearchResponse;

import java.util.List;
import java.util.Map;

public class SearchResponse<T> implements ISearchResponse<T> {

    List<T> data;
    Integer recordCount;

    public SearchResponse(Map<String,Object> map){
        this((List) map.get("data"), (Integer) map.get("recordCount"));
    }

    public SearchResponse(List<T> data, Integer recordCount){
        this.data = data;
        this.recordCount = recordCount;
    }

    @Override
    public List<T> getData() {
        return data;
    }

    @Override
    public Integer getRecordCount() {
        return recordCount;
    }

    @Override
    public String toString(){
        return "size:"+(data!=null?data.size():"null")+" count:"+recordCount;
    }
}
