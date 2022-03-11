package it.kdm.solr.common;

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

import org.apache.lucene.search.*;

import org.apache.lucene.util.BytesRef;


import java.util.*;

public class QueryUtils {
  //public static final String NAME = "PATH";

    /*protected NamedList<Object> args = null;

    @Override
    public void init( NamedList args )
    {
        this.args = args;
    }*/

  public static enum Filters {


    termsFilter {
        @Override
        public org.apache.lucene.search.Query makeFilter(String fname, BytesRef[] bytesRefs) {
            return new TermInSetQuery(fname, bytesRefs);
        }
        @Override
        public org.apache.lucene.search.Query makeFilter(String fname, List<BytesRef> bytesRefs) {
            return new TermInSetQuery(fname, bytesRefs);
        }
    },
    docValuesTermsFilter {
        @Override
        public org.apache.lucene.search.Query makeFilter(String fname, BytesRef[] byteRefs) {
            return new DocValuesTermsQuery(fname, byteRefs);
        }
        @Override
        public org.apache.lucene.search.Query makeFilter(String fname, List<BytesRef> bytesRefs) {
            return new DocValuesTermsQuery(fname, bytesRefs);
        }
        @Override
        public org.apache.lucene.search.Query makeFilter(String fname, java.lang.String... terms) {
            return new DocValuesTermsQuery(fname, terms);
        }
    };

    public abstract org.apache.lucene.search.Query makeFilter(String fname, BytesRef[] byteRefs);

    public org.apache.lucene.search.Query makeFilter(String fname, List<BytesRef> byteRefs)
	{
		return makeFilter(fname, byteRefs.toArray(new BytesRef[byteRefs.size()]));
	}

    public org.apache.lucene.search.Query makeFilter(String fname, java.lang.String... terms)
    {
        return makeFilter(fname, DocUtils.bytesRefs(terms) );
    }
  }
  

}

/*  ,
    booleanQuery {
      @Override
      Filter makeFilter(String fname, BytesRef[] byteRefs) {
        BooleanQuery bq = new BooleanQuery(true);
        for (BytesRef byteRef : byteRefs) {
          bq.add(new TermQuery(new Term(fname, byteRef)), BooleanClause.Occur.SHOULD);
        }
        return new QueryWrapperFilter(bq);
      }
    },
    automaton {
      @Override
      Filter makeFilter(String fname, BytesRef[] byteRefs) {
        Automaton union = Automata.makeStringUnion(Arrays.asList(byteRefs));
        return new MultiTermQueryWrapperFilter<AutomatonQuery>(new AutomatonQuery(new Term(fname), union)) {
        };
      }
    }*/