package docer.action;

import docer.exception.ActionRuntimeException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CadesSignedDoc extends DocerAction {

	private final static Logger log = LoggerFactory.getLogger(docer.action.CadesSignedDoc.class);

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {
		throw new UnsupportedOperationException();
	}

}
