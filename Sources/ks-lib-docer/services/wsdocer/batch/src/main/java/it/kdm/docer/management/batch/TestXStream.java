package it.kdm.docer.management.batch;

import it.kdm.docer.management.model.Batch;
import it.kdm.docer.management.model.Groups;
import it.kdm.docer.management.model.Source;
import it.kdm.docer.management.model.Target;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.MapperWrapper;


/**
 * Created with IntelliJ IDEA.
 * User: Vaio
 * Date: 28/10/15
 * Time: 14.24
 * To change this template use File | Settings | File Templates.
 */
public class TestXStream {

    public static void main(String[] args) {
    	
    	XStream xStream = new XStream() {
			@Override
			protected MapperWrapper wrapMapper(MapperWrapper next) {
				return new MapperWrapper(next) {
					@Override
					public boolean shouldSerializeMember(Class definedIn, String fieldName) {
						if (definedIn == Object.class) {
							return false;
						}
						return super.shouldSerializeMember(definedIn, fieldName);
					}
				};
			}
		};
		Class[] types = new Class [] {Batch.class, Groups.class, Source.class, Target.class};
		xStream.processAnnotations(types);

		String valueFromGetConfig = "<?xml version='1.0' encoding='UTF-8'?><batch><action>change|split|fusion</action><query_fascicolo>query</query_fascicolo><query_titolario>query</query_titolario><query_documento>query</query_documento><source><groups><group>A</group><group>B</group></groups></source><target><groups><group>C</group><group>D</group></groups></target></batch>";

		Batch batch = (Batch) xStream.fromXML( valueFromGetConfig );
		
		System.out.println( batch );
    }

}
