/*
 *    Copyright 2009-2025 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.submitted.auto_type_from_non_ambiguous_constructor;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AutoTypeFromNonAmbiguousConstructorFailingTest {

  @Test
  void testCannotResolveAmbiguousConstructor() throws IOException {
    // Account1 has more than 1 matching constructor, and auto type cannot decide which one to use
    try (Reader reader = Resources.getResourceAsReader(
        "org/apache/ibatis/submitted/auto_type_from_non_ambiguous_constructor/mybatis-config-failing.xml")) {

      Assertions.assertThatThrownBy(() -> new SqlSessionFactoryBuilder().build(reader)).isNotNull()
          .hasCauseInstanceOf(BuilderException.class).hasMessageContaining("Failed to find a constructor");
    }
  }
}
