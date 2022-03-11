package keysuite.docer.query;

import java.util.List;

public interface ISearchResponse<T> {
        List<T> getData();
        Integer getRecordCount();
}
