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

package de.openknowledge.util.dge.filter.web;

import de.openknowledge.util.dge.filter.FilterFieldMetaData;
import de.openknowledge.util.dge.filter.FilteredTestLine;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * @author Marc Petersen - open knowledge GmbH
 * @version Revision: $Revision$
 */
public class FilterAssemblerTest {

  FilterAssembler<List> assembler;

  @Before
  public void init() {
    assembler = new FilterAssembler<List>(FilteredTestLine.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetMetaDataByStringNullParam() {
    assembler.getMetaDataByString(null);
  }

  @Test
  public void testGetMetaDataByStringReturnValue() {
    FilterFieldMetaData metaData = assembler.getMetaDataByString("getFoo");
    assertNotNull(metaData);
    assertNull(assembler.getMetaDataByString("getYadda"));
  }

  @Test
  public void testRemoveFilterRow() {
    assembler.resetFilter();
    assertThat(assembler.getFilterRows().size(), is(1));

    assembler.addNewFilterRow();
    assembler.addNewFilterRow();
    assertThat(assembler.getFilterRows().size(), is(3));

    FilterAssembler.FilterRow row = assembler.getFilterRows().get(0);
    assembler.removeFilterRow(row);
    assertThat(assembler.getFilterRows().size(), is(2));

    assembler.removeFilterRow(assembler.getFilterRows().get(0));
    assembler.removeFilterRow(assembler.getFilterRows().get(0));
    assertThat(assembler.getFilterRows().size(), is(1));
  }

}
