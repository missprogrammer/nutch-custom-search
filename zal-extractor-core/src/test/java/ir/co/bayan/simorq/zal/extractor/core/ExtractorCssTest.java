package ir.co.bayan.simorq.zal.extractor.core;

import ir.co.bayan.simorq.zal.extractor.core.ExtractedDoc.LinkData;
import ir.co.bayan.simorq.zal.extractor.model.ExtractorConfig;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class ExtractorCssTest {

	private ExtractEngine extractEngine;
	private InputStream testPageContent;

	@Before
	public void setUpBeforeClass() throws Exception {
		InputStreamReader configReader = new InputStreamReader(
				ExtractorCssTest.class.getResourceAsStream("/extractors-css-test.xml"));
		ExtractorConfig extractorConfig = ExtractorConfig.readConfig(configReader);
		extractEngine = new ExtractEngine(extractorConfig);
		testPageContent = ExtractorCssTest.class.getResourceAsStream("/test.htm");
	}

	@Test
	public void testValues() throws Exception {
		Content content = new Content(new URL("http://some.blog.ir"), testPageContent, "UTF-8", "text/html");
		List<ExtractedDoc> extractedDocs = extractEngine.extract(content);
		Map<String, Object> result = extractedDocs.get(0).getFields();

		assertEquals("t1", result.get("f1"));
		assertEquals("t2", result.get("f2"));
		assertEquals("t3", result.get("f3"));
		assertEquals("http://some.blog.ir", result.get("f4"));
		assertEquals("t1 t2 t3 http://some.blog.ir", result.get("f5"));
	}

	@Test
	public void testSub() throws Exception {
		Content content = new Content(new URL("http://some.blog.ir"), testPageContent, "UTF-8", "text/html");
		List<ExtractedDoc> extractedDocs = extractEngine.extract(content);
		Map<String, Object> result = extractedDocs.get(0).getFields();

		assertEquals("2-t", result.get("f6"));
		assertEquals("2012-12-19", result.get("f6.1"));
	}

	@Test
	public void testSelect() throws Exception {
		Content content = new Content(new URL("http://some.blog.ir"), testPageContent, "UTF-8", "text/html");
		List<ExtractedDoc> extractedDocs = extractEngine.extract(content);
		Map<String, Object> result = extractedDocs.get(0).getFields();

		assertEquals("a b", result.get("f7"));
		assertEquals("a", result.get("f8"));
		assertEquals("2", result.get("f9"));
		assertEquals("b", result.get("f10"));
	}

	@Test
	public void testInheritence() throws Exception {
		Content content = new Content(new URL("http://some.blog.ir2"), testPageContent, "UTF-8", "text/html");
		List<ExtractedDoc> extractedDocs = extractEngine.extract(content);
		Map<String, Object> result = extractedDocs.get(0).getFields();

		assertEquals("t1", result.get("f1"));
		assertEquals("t1", result.get("f11"));
	}

	@Test
	public void testType() throws Exception {
		Content content = new Content(new URL("http://some.blog.ir2"), testPageContent, "UTF-8", "text/html");
		List<ExtractedDoc> extractedDocs = extractEngine.extract(content);
		Map<String, Object> result = extractedDocs.get(0).getFields();

		assertEquals("world!", result.get("f12"));
	}

	@Test
	public void testMultiValues() throws Exception {
		Content content = new Content(new URL("http://some.blog.ir"), testPageContent, "UTF-8", "text/html");
		List<ExtractedDoc> extractedDocs = extractEngine.extract(content);
		Map<String, Object> result = extractedDocs.get(0).getFields();

		Object multi = result.get("multi");
		assertTrue(multi instanceof List);
		assertEquals("v1", ((List<?>) multi).get(0));
		assertEquals("v2", ((List<?>) multi).get(1));

		multi = result.get("no-multi");
		assertEquals("v2", multi);
	}

	@Test
	public void testMultiDoc() throws Exception {
		Content content = new Content(new URL("http://some.blog.ir3"), testPageContent, "UTF-8", "text/html");
		List<ExtractedDoc> extractedDocs = extractEngine.extract(content);
		assertEquals(3, extractedDocs.size());

		ExtractedDoc doc1 = extractedDocs.get(1);
		assertEquals("a", doc1.getFields().get("content"));
		assertEquals("a", doc1.getText());
		assertEquals("http://1", doc1.getUrl());

		ExtractedDoc doc2 = extractedDocs.get(2);
		assertEquals("b", doc2.getFields().get("content"));
		assertEquals("b", doc2.getText());
		assertEquals("http://2", doc2.getUrl());
	}

	@Test
	public void testOutlinks() throws Exception {
		Content content = new Content(new URL("http://some.blog.ir4"), testPageContent, "UTF-8", "text/html");
		List<ExtractedDoc> extractedDocs = extractEngine.extract(content);
		List<LinkData> outlinks = extractedDocs.get(0).getOutlinks();
		assertEquals(2, outlinks.size());
		assertEquals("http://1", outlinks.get(0).getUrl());
		assertEquals("a", outlinks.get(0).getAnchor());
		assertEquals("http://some.blog.ir4/2", outlinks.get(1).getUrl());
	}

	@Test
	public void testCopy() throws Exception {
		Content content = new Content(new URL("http://some.blog.ir"), testPageContent, "UTF-8", "text/html");
		List<ExtractedDoc> extractedDocs = extractEngine.extract(content);

		Map<String, Object> result = extractedDocs.get(0).getFields();

		assertEquals("t1 t2", result.get("content"));
	}

    @Test
    public void testProcess() throws Exception {
        Content content = new Content(new URL("http://some.blog.ir"), testPageContent, "UTF-8", "text/html");
        List<ExtractedDoc> extractedDocs = extractEngine.extract(content);

        Map<String, Object> result = extractedDocs.get(0).getFields();
        assertEquals("hello", result.get("to_lower"));
    }
}
