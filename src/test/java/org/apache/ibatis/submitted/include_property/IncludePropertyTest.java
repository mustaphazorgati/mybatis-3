/*
 *    Copyright 2009-2024 the original author or authors.
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
package org.apache.ibatis.submitted.include_property;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class IncludePropertyTest {

  private static SqlSessionFactory sqlSessionFactory;

  @BeforeAll
  static void setUp() throws Exception {
    // create an SqlSessionFactory
    try (Reader reader = Resources
        .getResourceAsReader("org/apache/ibatis/submitted/include_property/mybatis-config.xml")) {
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }

    // populate in-memory database
    BaseDataTest.runScript(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(),
        "org/apache/ibatis/submitted/include_property/CreateDB.sql");
  }

  @Test
  void simpleProperty() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      List<String> results = sqlSession.selectList("org.apache.ibatis.submitted.include_property.Mapper.selectSimpleA");
      assertEquals("col_a value", results.get(0));
      results = sqlSession.selectList("org.apache.ibatis.submitted.include_property.Mapper.selectSimpleB");
      assertEquals("col_b value", results.get(0));
    }
  }

  @Test
  void propertyContext() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      List<Map<String, String>> results = sqlSession
          .selectList("org.apache.ibatis.submitted.include_property.Mapper.selectPropertyContext");
      Map<String, String> map = results.get(0);
      assertEquals(2, map.size());
      assertEquals("col_a value", map.get("COL_A"));
      assertEquals("col_b value", map.get("COL_B"));
    }
  }

  @Test
  void nestedDynamicValue() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      List<String> results = sqlSession
          .selectList("org.apache.ibatis.submitted.include_property.Mapper.selectNestedDynamicValue");
      assertEquals("col_a value", results.get(0));
    }
  }

  @Test
  void emptyString() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      List<String> results = sqlSession
          .selectList("org.apache.ibatis.submitted.include_property.Mapper.selectEmptyProperty");
      assertEquals("a value", results.get(0));
    }
  }

  @Test
  void propertyInRefid() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      List<String> results = sqlSession
          .selectList("org.apache.ibatis.submitted.include_property.Mapper.selectPropertyInRefid");
      assertEquals("col_a value", results.get(0));
    }
  }

  @Test
  void configVar() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      List<String> results = sqlSession
          .selectList("org.apache.ibatis.submitted.include_property.Mapper.selectConfigVar");
      assertEquals("col_c value", results.get(0), "Property defined in the config file should be used.");
    }
  }

  @Test
  void runtimeVar() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      Map<String, String> params = new HashMap<>();
      params.put("suffix", "b");
      List<String> results = sqlSession
          .selectList("org.apache.ibatis.submitted.include_property.Mapper.selectRuntimeVar", params);
      assertEquals("col_b value", results.get(0));
    }
  }

  @Test
  void nestedInclude() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      List<String> results = sqlSession
          .selectList("org.apache.ibatis.submitted.include_property.Mapper.selectNestedInclude");
      assertEquals("a value", results.get(0));
    }
  }

  @Test
  void parametersInAttribute() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      List<Map<String, String>> results = sqlSession
          .selectList("org.apache.ibatis.submitted.include_property.Mapper.selectPropertyInAttribute");
      Map<String, String> map = results.get(0);
      assertEquals(2, map.size());
      assertEquals("col_a value", map.get("COL_1"));
      assertEquals("col_b value", map.get("COL_2"));
    }
  }
}
