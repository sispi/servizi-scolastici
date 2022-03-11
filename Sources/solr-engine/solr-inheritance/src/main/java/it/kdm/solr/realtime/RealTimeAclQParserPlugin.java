package it.kdm.solr.realtime;

//import it.kdm.solr.components.*;
import com.google.common.base.Strings;
import it.kdm.solr.client.CoreClient;
import it.kdm.solr.common.DocUtils;
import it.kdm.solr.common.QueryUtils;
import it.kdm.solr.core.ExpirableRegenerator;
import it.kdm.solr.core.Schema;
import it.kdm.solr.core.Session;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.TermsQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.BytesRefBuilder;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.FieldType;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QParserPlugin;
import org.apache.solr.search.SyntaxError;


import org.apache.lucene.search.*;
import it.kdm.solr.core.Schema.Fields;
import it.kdm.solr.core.Schema.Params;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Finds documents whose specified field has any of the specified values.
 * <br>Parameters:
 * <br><code>f</code>: The field name (mandatory)
 * <br><code>separator</code>: the separator delimiting the values in the query string, defaulting to a comma.
 * If it's a " " then it splits on any consecutive whitespace.
 * <br><code>method</code>: Any of termsFilter (default), booleanQuery, automaton, docValuesTermsFilter.
 * <p>
 * Note that if no values are specified then the query matches no documents.
 */
public class RealTimeAclQParserPlugin extends QParserPlugin {
  public static final String NAME = "acl";
  
  	private transient static Logger log = LoggerFactory.getLogger(RealTimeAclQParserPlugin.class);

    @SuppressWarnings("unchecked")
	private static Query getParentsClause( SolrQueryRequest req, String ticket ) 
	{
		try
		{
            /*

            Grazie al componente Acl "RealTimeAclComponent" in ogni shard si mantiene in cache la lista delle sequence dello shard stesso utilizzate dagli altri shard.

            Il popolamente avviene quando il componente è invocato in modalità "warmup". Questo può avvenire in modalità esplicita su uno specifico shard ( /acls?purpose=warmup )
            oppure quando "serve" durante un quary sicura (con l'utilizzo del plugin Acl).

            In pratica nello shard local (to) nella cache vengono create delle liste (con chiave "terms-<remote shard (from)>-<local shard (to)>"), una per ogni shard della collezione (shard remoti "from").

            Ogni lista è otteuta chiamando (non distribuito) il componente in modalità "processTerm" sullo shard remote (from) ottenendo gli acl parent filtrati in base al target shard (to).

            Viene effettuata ovviamente una chiamata per ogni shard remoto specificando lo shard locale. Il numero dei processamenti è n*n , con n il numero degli shard nella collezione.

            Quindi ogni shard "sa" quali dei suoi record sono utilizzati (citato come valore acl_parent)  dagli altri shard (incluso sè stesso) .


                                    [  purpose=acls  ]  [    purpose=terms      ]

                                                        |---- process terms (A,A)                   purpose=acls è il default del componente, da cui si scatena tutto
                                    |---- warmup A ---->|---- process terms (A,B)
                                    |                   |---- process terms (A,C)
                                    |                   |---- process terms (A,D)
                                    |
                                    |                   |---- process terms (B,A)
                                    |---- warmup B ---->|---- process terms (B,B)                   i processamenti avvengono solo se la lista corrispondente
                                    |                   |---- process terms (B,C)                   non è presente in cache o è scaduta
                                    |                   |---- process terms (B,D)
            -- distributed query -->|
                                    |                   |---- process terms (C,A)
                                    |---- warmup C ---->|---- process terms (C,B)
                                    |                   |---- process terms (C,C)
                                    |                   |---- process terms (C,D)
                                    |
                                    |                   |---- process terms (D,A)
                                    |---- warmup D ---->|---- process terms (D,B)
                                                        |---- process terms (D,C)
                                                        |---- process terms (D,D)





            Si genera così una matrice distribuita (in cui ogni shard mantiene una riga) delle acl sequence utilizzate dagli altri shard

                  -----------------------------
                  |  A   |  B   |  C   |  D   | (from)
            -----------------------------------
        (to)|  A  |l(A,A)|l(A,B)|l(A,C)|l(A,D)|                        l(x,y) è la lista di sequence (long) dello shard "x" utilizzate nello shard "y" ed è
            -----------------------------------                        memorizzata nella cache di "x" per 60 secondi (TERMS_CACHE_DURATION) ed è utilizzato
            |  B  |l(B,A)|l(B,B)|l(B,C)|l(B,D)|                        globalmente in tutte le query "sicure"
            -----------------------------------
            |  C  |l(C,A)|l(C,B)|l(C,D)|l(C,D)|
            -----------------------------------
            |  D  |l(D,A)|l(C,B)|l(D,C)|l(D,D)|
            -----------------------------------

            Quando viene eseguito il plugin Acl (RealTimeAclQParserPlugin {!acl}) sullo shard Q (durante una query ogni shard esegue il proprio pezzo di query)
            si richiede al componente Acl la lista delle sequence relative allo shard locale Q nel ruolo di "from"  (dove la query si svolge) per l'utente corrente U.

            In pratica l'Acl component, lavorando in modalità "processAcls" e distribuita , restituisce le liste relative ai vari shard utili allo shard "querante" .

            Per ogni shard X quindi la lista l(X,Q) viene filtrata in base ai "roles" i U e restuita una nuova lista l'(U,X,Q)

            Le liste vengono unite in un'unica lista di "longs" che diventano, di fatto, il filtro di sicurezza:

                    p(U,Q) = l'(U,A,Q) + l'(U,B,Q) + l'(U,C,Q) + l'(U,D,Q)
                    (lista degli acl_parent di tutta la collezione utilizzati in Q e visibili ad U)


            Un elemento è visibile se l'utente è esplicito nelle acl dell'elemento (con i suoi "roles" r(U) ) o se l'elemento ha per acl parent un "long" della lista p(U,Q).

            Una futura ottimizzazione dovrebbe utilizzare solo i "roles" dell'utente r(U) effettivamente utilizzati nello shard "querante" derivando così r(U,Q)

            Tuttavia in una applicazione normale i roles non sono mai così tanti da creare un problema di performance.

            Il filtro F così ottenuto "AclQuery" è messo in session utente per 60 secondi (ACL_CACHE_DURATION) e viene utilizzato dalla session dell'utente:

                    F(U,Q) = acl_parent:p(U,Q) AND acl_read:r(U,Q)


            Solr eredita da Lucene un limite per cui un filtro non può contenere più di un certo numero di clausole (per default 1024).

            Tale numero può essere elevato con rischio di degrago delle performance.

            Tuttavia splittando un indice si abbassa il numero di clausole coinvolte (per effetto dell'algoritmo
            appena descritto che utilizza solo i valori effettivamente utilizzati nello shard)


            In alternativa può essere utilizzata la scorciatoia di specificare lo "acl_parent" direttamente nella request.

            In questo caso il singolo acl_parent, se visibile all'utente, viene utilizzato invece della lista p(U,Q)

            In una applicazione di navigazione come acl_parent va usato l'acl_parent dell'elemento che si sta navigando per trovare i figli




             */



			long startTime = System.currentTimeMillis();

			String shard = req.getCore().getCoreDescriptor().getCloudDescriptor().getShardId();
			
			CoreClient.Query params = new CoreClient.Query();

            String remoteAddr = req.getParams().get("remoteAddr");

            params.set( Params.SHORTCUT , true );
            params.set( "qt" , "/acls" );
            params.set( "shard.from" , shard );
            params.set( "ticket" , ticket );

            if (remoteAddr!=null)
                params.set( "remoteAddr", remoteAddr );

            log.debug("start getParentsClause shard:{} ticket:{}", shard,ticket);

            log.trace("trace getParentsClause params:{}",params);
			
			NamedList<Object> nl = CoreClient.getInstance().query( params ).getResponse();
			
			nl = (NamedList<Object>) nl.get("values");

            log.trace("trace getParentsClause values:{}",nl);
			
			Collection<Long> acls=null;
		
			for( int i=0; i< nl.size(); i++)
			{
				Collection<Long> lacls = (Collection<Long>) nl.getVal(i);
				
				if (acls==null)
					acls = lacls;
				else
					acls.addAll(lacls);
			}

            log.trace("trace getParentsClause ticket:{} \nacls:{}",ticket,acls);

            if ( acls==null || acls.size()==0)
            {
                log.debug("end getParentsClause EMPTY time {}", System.currentTimeMillis() - startTime );
                return new MatchNoDocsQuery();
            }

			long t1 = System.currentTimeMillis() - startTime ;
			
			List<BytesRef> bytesRefs = new ArrayList<>();

			FieldType ft = req.getSchema().getFieldType( Fields.ACL_SEQUENCES);
			
			for ( Long acl : acls ) {

                BytesRefBuilder term = new BytesRefBuilder();
				
				ft.readableToIndexed( acl.toString() , term);
				
				bytesRefs.add( term.toBytesRef() );
			}
			
			Query parent_filter = QueryUtils.Filters.termsFilter.makeFilter( Fields.ACL_SEQUENCES , bytesRefs );
			
			long elapsedTime = System.currentTimeMillis() - startTime;
			long t2 = elapsedTime - t1 ;
			
			log.debug("end getParentsClause time {}={}+{} \nfilter({}):{}", elapsedTime, t1, t2, bytesRefs.size(), parent_filter);
			
			return new ConstantScoreQuery( parent_filter );
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}  

  private static ExpirableRegenerator.Expirable makeAclQuery(SolrQueryRequest req, Collection<String> roles, String ticket)
  {

    /*

        la query compone due clausole alternative

        1) parent clause: considera tutti gli "acl parent" applicabili allo shard locale

        2) child clause: considera l'acl esplicita sugli elementi

    */

    if (roles==null || roles.size() == 0)
        return new ExpirableRegenerator.CacheEntry( new MatchNoDocsQuery() , RealTimeAclComponent.ACL_CACHE_DURATION );

    BooleanQuery.Builder builder = new BooleanQuery.Builder();

    //BooleanQuery bq = new AclQuery();

    Query parentclause = getParentsClause( req, ticket );



    BytesRef[] refs = DocUtils.bytesRefs( req, Fields.ACL_READ, roles );
    Query child_filters = QueryUtils.Filters.termsFilter.makeFilter( Fields.ACL_READ , refs );

    Query childquery = new ConstantScoreQuery( child_filters );

    Query bq;

    if (parentclause instanceof MatchNoDocsQuery)
    {
          bq = childquery;
    }
    else
    {
          builder.add( new BooleanClause( parentclause , BooleanClause.Occur.SHOULD ) );
          builder.add( new BooleanClause( childquery , BooleanClause.Occur.SHOULD ) );

          bq = builder.build();
    }

    // il filtro ottenuto è cachato per un minuto
    return new ExpirableRegenerator.CacheEntry( bq , RealTimeAclComponent.ACL_CACHE_DURATION );
  }

    private static Query makeParentAclQuery(SolrQueryRequest req, String acl_parent, Collection<String> roles)
    {

        if (acl_parent==null)
            return new MatchNoDocsQuery();

        DocUtils.getDocument( req, acl_parent, Fields.ID ); // andrà in eccezione se non è visibile all'utente

        BooleanQuery.Builder builder = new BooleanQuery.Builder();

        Query parent_filter = new TermsQuery(new Term(Fields.ACL_PARENT,acl_parent));

        BytesRef[] refs = DocUtils.bytesRefs( req, Fields.ACL_READ, roles );

        Query child_filters = QueryUtils.Filters.termsFilter.makeFilter( Fields.ACL_READ , refs );

        builder.add(parent_filter, BooleanClause.Occur.SHOULD);
        builder.add(child_filters, BooleanClause.Occur.SHOULD);

        return builder.build();
    }
	
  @Override
  public QParser createParser( String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {
    
	return new QParser(qstr, localParams, params, req) {

      @Override
      public Query parse() throws SyntaxError {
        
		boolean refresh = params.getBool("acls.refresh",false);
        String acl_field = localParams.get("field");

        Session session = Session.get(req);
		
		if ( (session.getGlobalAcl() & 1) > 0)
			return new org.apache.lucene.search.MatchAllDocsQuery();


        /* se si specifica l'acl_field la query non gestisce l'ereditarietà */
          if ( !Strings.isNullOrEmpty(acl_field) )
          {
              BytesRef[] refs = DocUtils.bytesRefs(req, acl_field, session.getRoles());
              Query f = QueryUtils.Filters.termsFilter.makeFilter( acl_field , refs );
              return new ConstantScoreQuery( f );
          }

        String acl_parent = localParams.get(Fields.ACL_PARENT);

        if (acl_parent!=null)
            return makeParentAclQuery(req,acl_parent,session.getRoles());

        ExpirableRegenerator.Expirable expirable;
		
		if (RealTimeAclComponent.ACL_CACHE_DURATION>0)
			expirable = session.getAclQuery();
		
		if (refresh || expirable==null || expirable.isExpired())
		{
			expirable = makeAclQuery(req, session.getRoles(), session.getTicket() );
			session.setAclQuery(expirable);
		}
		return (Query) expirable.getObject();
      }
    };
  }
}