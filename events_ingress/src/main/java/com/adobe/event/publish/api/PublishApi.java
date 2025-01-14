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
package com.adobe.event.publish.api;

import com.adobe.event.publish.model.CloudEvent;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import java.util.Optional;

@Headers("Accept: application/json")
public interface PublishApi {

  String DEFAULT_URL = "https://eventsingress.adobe.io";

  /**
   * publish a Cloud Event Payload
   *
   * @param body your CloudEvent Input Model
   */
  @RequestLine("POST")
  @Headers({"Content-Type: application/cloudevents+json"})
  void publishCloudEvent(CloudEvent body);

  /**
   * publish a Raw Event Json Payload
   * @param eventCode the Adobe I/O EventMetadata eventCode associated with the Event
   * @param providerId  the Adobe I/O EventMetadata ProviderId associated with the Event
   * @param rawEvent the Raw Event Json Payload to publish
   */
  @RequestLine("POST")
  @Headers({
      "Content-Type: application/json",
      "x-adobe-event-provider-id: {providerId}",
      "x-adobe-event-code: {eventCode}"
  })
  void publishRawEvent(
      @Param("providerId") String providerId,
      @Param("eventCode") String eventCode,
      String rawEvent);

}
