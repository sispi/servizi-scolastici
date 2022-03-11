package it.kdm.solr.realtime;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.memory.MemoryIndex;
import org.apache.lucene.search.Query;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;

/**
* Created by Paolo_2 on 23/04/17.
*/ /* questa classe memorizza il documento originario ed aggiunge i campi in base allo schema */
public class RealTimeDocument extends MemoryIndex {

    public static final RealTimeDocument EmptyDocument = new RealTimeDocument();

    protected Document document;
    //Integer docid = null;

    private RealTimeDocument()
    {
        super();
        this.document = null;
    }

    public RealTimeDocument( IndexSchema schema, Document document )
    {
        super();
        importDocument(schema,document);
    }

    public Object getFirstValue( String field )
    {
        if (document==null)
            return null;

        IndexableField ind = document.getField(field);

        if (ind==null)
            return null;

        Object val = ind.numericValue();
        if (val==null)
            val = ind.stringValue();
        if (val==null)
            val = ind.binaryValue();

        return val;
    }

    @Override
    public float search(Query query) {
        if (document==null)
            return 0;

        return super.search(query);
    }

    public Boolean isEmpty()
    {
        return (this.document==null);
    }

    private void importDocument( IndexSchema schema, Document doc )
    {
        this.document = doc;
        //String uniqueKeyField = "id";

        for ( IndexableField ind : doc )
        {
            if (RealTimeIndex.log.isTraceEnabled())
                RealTimeIndex.log.trace("importing field '{}' tokenized:{}",ind.name(),ind.fieldType().tokenized());

            if (IndexOptions.NONE.equals(ind.fieldType().indexOptions())
                    && DocValuesType.NONE.equals(ind.fieldType().docValuesType()))
            //if (!ind.fieldType().tokenized())
                continue;

            FieldType ft;

            SchemaField schemaField = schema.getFieldOrNull(ind.name());
            if (schemaField!=null)
                ft = schemaField.getType();
            else
                ft = schema.getDynamicFieldType(ind.name());

            Analyzer analyzer = ft.getIndexAnalyzer() ;

            this.addField( ind , analyzer );
        }

    }

}
