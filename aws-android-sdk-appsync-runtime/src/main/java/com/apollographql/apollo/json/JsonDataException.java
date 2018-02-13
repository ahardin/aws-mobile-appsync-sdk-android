/**
 * Copyright 2018-2018 Amazon.com,
 * Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Amazon Software License (the "License").
 * You may not use this file except in compliance with the
 * License. A copy of the License is located at
 *
 *     http://aws.amazon.com/asl/
 *
 * or in the "license" file accompanying this file. This file is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, express or implied. See the License
 * for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.apollographql.apollo.json;

/**
 * Thrown when the data in a JSON document doesn't match the data expected by the caller. For
 * example, suppose the application expects a boolean but the JSON document contains a string. When
 * the call to {@link JsonReader#nextBoolean} is made, a {@code JsonDataException} is thrown.
 *
 * <p>Exceptions of this type should be fixed by either changing the application code to accept
 * the unexpected JSON, or by changing the JSON to conform to the application's expectations.
 *
 * <p>This exception may also be triggered if a document's nesting exceeds 31 levels. This depth is
 * sufficient for all practical applications, but shallow enough to avoid uglier failures like
 * {@link StackOverflowError}.
 */
public final class JsonDataException extends RuntimeException {
  public JsonDataException() {
  }

  public JsonDataException(String message) {
    super(message);
  }

  public JsonDataException(Throwable cause) {
    super(cause);
  }

  public JsonDataException(String message, Throwable cause) {
    super(message, cause);
  }
}
