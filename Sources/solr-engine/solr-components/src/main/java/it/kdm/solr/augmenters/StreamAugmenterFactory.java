/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package it.kdm.solr.augmenters;

import com.google.common.base.Strings;
import it.kdm.solr.common.DocUtils;
import it.kdm.solr.core.ProviderProxy;
import it.kdm.solr.core.Schema;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.transform.DocTransformer;
import org.apache.solr.response.transform.TransformerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;


/**
 *
 * @since solr 4.0
 */
public class StreamAugmenterFactory extends TransformerFactory
{
	protected NamedList args; 

	@Override
	public void init(NamedList args) {
		this.args = args;
   }
   
	
  
  
  @Override
  public DocTransformer create(String field, SolrParams params, SolrQueryRequest req) {
    return new StreamAugmenter( field, params, req, args );
  }
}

class StreamAugmenter extends DocTransformer
{
	private static Logger log = LoggerFactory.getLogger(StreamAugmenter.class);
	
	private final String display;
	private final String part;
	
	private SolrParams params;
	private SolrQueryRequest req;

  public StreamAugmenter( String display , SolrParams params, SolrQueryRequest req, NamedList args )
  {
	this.display = display;
	this.part = params.get( "part" , (String) args.get( "part" ) );
	this.req = req;
	this.params = params;
  }

  @Override
  public String getName()
  {
    return display;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void transform(SolrDocument doc, int docid, float score) {
	 
	try
	{
		/*Schema schema = Schema.get(req);

		String id = (String) DocUtils.convertIndexableField(req, doc, Schema.Fields.ID);
		String name = (String) DocUtils.convertIndexableField(req, doc, Schema.Fields.NAME);
		java.util.Date cmo = (java.util.Date) DocUtils.convertIndexableField(req, doc, Schema.Fields.CONTENT_MODIFIED_ON);
		*/

		String id = (String) DocUtils.convertIndexableField(req, doc, Schema.Fields.ID);

		ProviderProxy provider = new ProviderProxy(doc,req);

		ContentStream stream = provider.downloadLastVersion();

		File f = null;
		if (stream instanceof ContentStreamBase.FileStream){
			ContentStreamBase.FileStream fs = (ContentStreamBase.FileStream) stream;

			Field fld = fs.getClass().getDeclaredField("file"); //NoSuchFieldException
			fld.setAccessible(true);
			f = (File) fld.get(fs);

			//String path = Paths.get(f.toURI()).toRealPath().toString();
			//doc.setField( display, path );
		}

		if (f!=null){
			Object value=null;
			//if ("relative".equals(part))
			//	value = f.getPath();
			if (Strings.isNullOrEmpty(part))
				value = f.getPath();
			else if ("lastModified".equals(part))
				value = f.lastModified();
			else if ("lastAccessed".equals(part))
				value = Files.readAttributes(Paths.get(f.toURI()), BasicFileAttributes.class).lastAccessTime().toMillis();
			else if ("creationTime".equals(part))
				value = Files.readAttributes(Paths.get(f.toURI()), BasicFileAttributes.class).creationTime().toMillis();
			else if ("size".equals(part))
				value = f.length();
			else{
				String abs = Paths.get(f.toURI()).toRealPath().toString();
				String prefix = "/"  +id.split("!")[0].replace(".","/");
				int idx = abs.indexOf(prefix);

				if ("absolute".equals(part))
					value = abs;
				else if ("relative".equals(part))
					value = abs.substring(idx);
				else if ("store".equals(part))
					value = abs.substring(0,idx);
			}
			doc.setField( display, value );
		}

		//java.io.File f = ProviderProxy.getCacheFile(schema, id , cmo );
		//if (f!=null && f.exists() )

	}
	catch(Exception e)
	{
		log.error("transform error",e);
	}
	
  }
}


