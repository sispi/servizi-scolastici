import it.kdm.docer.conservazione.Utils;
import junit.framework.Assert;

import org.junit.Test;


public class UtilsTest {

	@Test
	public void testFindExtension() {
		
		Assert.assertEquals("PDF.P7M", Utils.findExtension("test.PDF.P7M"));
		Assert.assertEquals("PDF.P7M.P7M", Utils.findExtension("test.PDF.P7M.P7M"));
		Assert.assertEquals("PDF.P7M.P7S", Utils.findExtension("test.PDF.P7M.P7S"));
		Assert.assertEquals("PDF.P7S.P7M", Utils.findExtension("test.PDF.P7S.P7M"));
		Assert.assertEquals("P7M", Utils.findExtension("test.P7M"));
		Assert.assertEquals("test.P7M", Utils.findExtension("test.test.P7M"));
		Assert.assertEquals("PDF", Utils.findExtension("test.PDF"));
		Assert.assertEquals("", Utils.findExtension("test"));
		
		Assert.assertEquals("pdf.p7m", Utils.findExtension("test.pdf.p7m"));
		Assert.assertEquals("pdf.p7m.p7m", Utils.findExtension("test.pdf.p7m.p7m"));
		Assert.assertEquals("pdf.p7m.p7s", Utils.findExtension("test.pdf.p7m.p7s"));
		Assert.assertEquals("pdf.p7s.p7m", Utils.findExtension("test.pdf.p7s.p7m"));
		Assert.assertEquals("p7m", Utils.findExtension("test.p7m"));
		Assert.assertEquals("test.p7m", Utils.findExtension("test.test.p7m"));
		Assert.assertEquals("pdf", Utils.findExtension("test.pdf"));
		Assert.assertEquals("", Utils.findExtension("test"));
		
		Assert.assertEquals("JPG", Utils.findExtension("1149496-FF257191.JPG"));
	}
	
	@Test
	public void testDate() {
		String[] dates = new String[] {
				"2011-09-21 00:00:00",
				"2011-09-21",
				"2011-09-21T00:00:00.000+02:00"
		};
		
		for(String date : dates) {
			Assert.assertTrue(Utils.isDateParsable(date));
			Assert.assertEquals("2011-09-21T00:00:00.000+02:00",
					Utils.parseDateTime(date));
			Assert.assertEquals("2011-09-21",
					Utils.parseDate(date));		
		}
	}
}
