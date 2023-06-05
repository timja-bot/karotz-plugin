/*
 * The MIT License
 *
 * Copyright (c) 2011, Seiji Sogabe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.karotz;

import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlPage;
import jenkins.model.Jenkins;
import org.jvnet.hudson.test.HudsonTestCase;

/**
 * Config submit Test
 *
 * @author Seiji Sogabe
 */
public class KarotzSystemConfigSubmitTest extends HudsonTestCase {

    private static final String NAME_API_KEY = "_.apiKey";

    private static final String NAME_SECRET_KEY = "_.secretKey";

    private static final String NAME_INSTALL_ID = "_.installId";

    private WebClient webClient;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        webClient = new WebClient();
        webClient.setCssEnabled(false);
        webClient.setThrowExceptionOnFailingStatusCode(false);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testConfigSubmit() throws Exception {
        String apiKey = "187da8aa-7b09-4422-9fed-d609129d094f";
        String secretKey = "187da8ba-7b19-4422-afed-d609d29b004f";
        String installId = "187da8ca-7b19-6422-afe1-d609129b088f";

        HtmlForm form = webClient.goTo("configure").getFormByName("config");
        form.getInputByName(NAME_API_KEY).setValue(apiKey);
        form.getInputByName(NAME_SECRET_KEY).setValue(secretKey);
        form.getInputByName(NAME_INSTALL_ID).setValue(installId);

        submit(form);

        KarotzPublisher.KarotzPublisherDescriptor d = Jenkins.getInstance().getDescriptorByType(KarotzPublisher.KarotzPublisherDescriptor.class);
        assertNotNull(d);

        assertEquals(apiKey, d.getApiKey());
        assertEquals(secretKey, d.getSecretKey());
        assertEquals(installId, d.getInstallId());

        form = webClient.goTo("configure").getFormByName("config");
        assertEquals(apiKey, form.getInputByName("_.apiKey").getValue());
        assertEquals(secretKey, form.getInputByName("_.secretKey").getValue());
        assertEquals(installId, form.getInputByName("_.installId").getValue());
    }

    public void testConfigSubmit_APIKeyIsNull() throws Exception {
        String apiKey = null;
        String secretKey = "187da8ba-7b19-4422-afed-d609d29b004f";
        String installId = "187da8ca-7b19-6422-afe1-d609129b088f";

        HtmlForm form = webClient.goTo("configure").getFormByName("config");
        form.getInputByName(NAME_API_KEY).setTextContent(apiKey);
        form.getInputByName(NAME_SECRET_KEY).setValue(secretKey);
        form.getInputByName(NAME_INSTALL_ID).setValue(installId);

        HtmlPage page = submit(form);
        assertStringContains(page.asText(), "API Key, Secret Key and Install ID are mandatory.");
    }

    public void testConfigSubmit_SecretKeyIsNull() throws Exception {
        String apiKey = "187da8ba-7b19-4422-afed-d609d29b004f";
        String secretKey = null;
        String installId = "187da8ca-7b19-6422-afe1-d609129b088f";

        HtmlForm form = webClient.goTo("configure").getFormByName("config");
        form.getInputByName(NAME_API_KEY).setValue(apiKey);
        form.getInputByName(NAME_SECRET_KEY).setTextContent(secretKey);
        form.getInputByName(NAME_INSTALL_ID).setValue(installId);

        HtmlPage page = submit(form);
        assertStringContains(page.asText(), "API Key, Secret Key and Install ID are mandatory.");
    }

    public void testConfigSubmit_InstallIDIsNull() throws Exception {
        String apiKey = "187da8ba-7b19-4422-afed-d609d29b004f";
        String secretKey = "187da8ca-7b19-6422-afe1-d609129b088f";
        String installId = null;

        HtmlForm form = webClient.goTo("configure").getFormByName("config");
        form.getInputByName(NAME_API_KEY).setValue(apiKey);
        form.getInputByName(NAME_SECRET_KEY).setValue(secretKey);
        form.getInputByName(NAME_INSTALL_ID).setTextContent(installId);

        HtmlPage page = submit(form);
        assertStringContains(page.asText(), "API Key, Secret Key and Install ID are mandatory.");
    }

}
