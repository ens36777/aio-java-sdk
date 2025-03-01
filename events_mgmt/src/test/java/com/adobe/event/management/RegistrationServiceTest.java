/*
 * Copyright 2017 Adobe. All rights reserved.
 * This file is licensed to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR REPRESENTATIONS
 * OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.adobe.event.management;

import com.adobe.event.management.model.EventsOfInterest;
import com.adobe.event.management.model.RegistrationInputModel;
import com.adobe.ims.JWTAuthInterceptor;
import com.adobe.ims.util.TestUtil;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import feign.RequestInterceptor;
import org.junit.Rule;
import org.junit.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static junit.framework.TestCase.assertNotNull;

public class RegistrationServiceTest {

  private static final String REGISTRATION_ID = "12345";

  @Rule
  public WireMockRule wireMockRule = new WireMockRule(Integer.parseInt(TestUtil.PORT));

  @Test
  public void registrationServiceTest() {

    assertNotNull(getRegistrationService());

//    /** getRegistration */
    stubFor(get(urlEqualTo(String.format("/events/organizations/%s/integrations/%s/registrations/%s", TestUtil.CONSUMER_ORG_ID, TestUtil.CREDENTIAL_ID, REGISTRATION_ID)))
            .withHeader(TestUtil.CONTENT_TYPE_HEADER, equalTo(TestUtil.CONTENT_TYPE_HEADER_VALUE))
            .withHeader(TestUtil.IMS_ORG_ID_HEADER, equalTo(TestUtil.IMS_ORG_ID))
            .willReturn(aResponse()
                    .withStatus(200)
            ));
    getRegistrationService().findById(REGISTRATION_ID);
    verify(getRequestedFor(urlEqualTo(String.format("/events/organizations/%s/integrations/%s/registrations/%s", TestUtil.CONSUMER_ORG_ID, TestUtil.CREDENTIAL_ID, REGISTRATION_ID))));


//    /** postRegistration */
    stubFor(post(urlEqualTo(String.format("/events/organizations/%s/integrations/%s/registrations", TestUtil.CONSUMER_ORG_ID, TestUtil.CREDENTIAL_ID)))
            .withHeader(TestUtil.CONTENT_TYPE_HEADER, equalTo(TestUtil.CONTENT_TYPE_HEADER_VALUE))
            .withHeader(TestUtil.IMS_ORG_ID_HEADER, equalTo(TestUtil.IMS_ORG_ID))
            .willReturn(aResponse()
                    .withStatus(200)
            ));
    getRegistrationService().createRegistration(RegistrationInputModel.builder()
            .description("aio-lib-java registration description")
            .name("aio-lib-java registration name")
            .addEventsOfInterests(EventsOfInterest.builder()
                    .setEventCode("osgi_ping")
                    .setProviderId("11111").build()));
    verify(postRequestedFor(urlEqualTo(String.format("/events/organizations/%s/integrations/%s/registrations", TestUtil.CONSUMER_ORG_ID, TestUtil.CREDENTIAL_ID))));


//    /** deleteRegistration */
    stubFor(delete(urlEqualTo(String.format("/events/organizations/%s/integrations/%s/registrations/%s", TestUtil.CONSUMER_ORG_ID, TestUtil.CREDENTIAL_ID, REGISTRATION_ID)))
            .withHeader(TestUtil.CONTENT_TYPE_HEADER, equalTo(TestUtil.CONTENT_TYPE_HEADER_VALUE))
            .withHeader(TestUtil.IMS_ORG_ID_HEADER, equalTo(TestUtil.IMS_ORG_ID))
            .willReturn(aResponse()
                    .withStatus(200)
            ));
    getRegistrationService().delete(REGISTRATION_ID);
    verify(deleteRequestedFor(urlEqualTo(String.format("/events/organizations/%s/integrations/%s/registrations/%s", TestUtil.CONSUMER_ORG_ID, TestUtil.CREDENTIAL_ID, REGISTRATION_ID))));

  }

  private RegistrationService getRegistrationService(){

    stubFor(post(urlEqualTo("/ims/exchange/jwt"))
            .willReturn(okJson(TestUtil.AUTH_RESPONSE)
                    .withStatus(200)
            ));
    RequestInterceptor authInterceptor = JWTAuthInterceptor.builder()
            .workspace(TestUtil.getWorkspace())
            .build();

    return RegistrationService.builder()
            .authInterceptor(authInterceptor)
            .workspace(TestUtil.getWorkspace())
            .url(TestUtil.API_URL)
            .build();
  }

}
