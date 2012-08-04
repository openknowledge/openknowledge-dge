/*
 * Copyright open knowledge GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.openknowledge.util.filter;

import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;

/**
 * @author Marc Petersen - open knowledge GmbH
 */
public class FilterChoiceFieldMetaDataTest {

  @Test
  public void testFilterChoice() throws NoSuchMethodException {
    try {
      new FilterChoiceFieldMetaData(Mockito.<Method>any(), FilterChoiceFieldMetaDataTest.class.getMethod("getGoodList"), 1, "Ding");
    } catch (IllegalArgumentException ex) {
      assertTrue("A List should be a valid return type for a choice method", false);
    }

    try {
      new FilterChoiceFieldMetaData(Mockito.<Method>any(), FilterChoiceFieldMetaDataTest.class.getMethod("getBadMethod"), 1, "Dong");
      assertTrue("A String should NOT be a valid return type for a choice method", false);
    } catch (IllegalArgumentException ex) {
      // expected
    }
  }

  public List<String> getGoodList() {
    return new ArrayList<String>();
  }

  public String getBadMethod() {
    return "foo";
  }
}
