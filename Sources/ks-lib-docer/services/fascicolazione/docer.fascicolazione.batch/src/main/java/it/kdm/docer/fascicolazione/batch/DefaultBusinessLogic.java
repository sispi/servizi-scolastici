package it.kdm.docer.fascicolazione.batch;

import it.kdm.docer.businesslogic.BusinessLogic;
import it.kdm.docer.sdk.exceptions.DocerException;

public class DefaultBusinessLogic extends BusinessLogic {

	public static final DefaultBusinessLogic INSTANCE;

	static {
		try {
			BusinessLogic.setConfigPath(Configuration.getInstance().getProperty("service.ws.configdir"));
			INSTANCE = new DefaultBusinessLogic();
		} catch (DocerException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private DefaultBusinessLogic() throws DocerException {
		super(Configuration.getInstance().getProperty("bl.provider.class"),
				Integer.parseInt(Configuration.getInstance().getProperty("bl.provider.searchMaxRows", "1000")));
	}

	// private static String providerName() {
	// try {
	// Properties p = new Properties();
	// p.loadFromXML(BLFacade.class.getResourceAsStream("/docer_config.xml"));
	// BusinessLogic.setConfigPath(p.getProperty("CONFIG_DIR"));
	// return p.getProperty("provider");
	// } catch (Exception e) {
	// throw new IllegalArgumentException(e);
	// }
	// }

}
