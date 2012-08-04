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

package de.openknowledge.util.filter.core;

import de.openknowledge.util.filter.core.annotation.FilterChoice;
import de.openknowledge.util.filter.core.annotation.FilterChoiceField;
import de.openknowledge.util.filter.core.annotation.FilterField;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Marc Petersen - open knowledge GmbH
 */
public class FilterManagerTest {

  private static final Log LOG = LogFactory.getLog(FilterManagerTest.class);

  private FilteredChoiceTestLine l1;
  private FilteredChoiceTestLine l2;
  private FilteredChoiceTestLine l3;
  private List<FilteredChoiceTestLine> lines;

  @Before
  public void init() {
    lines = new ArrayList<FilteredChoiceTestLine>();

    BigDecimal bd1 = new BigDecimal("1000");
    Calendar c1 = Calendar.getInstance();
    c1.set(2012, 0, 1, 0, 0, 0);
    c1.set(Calendar.MILLISECOND, 0);
    l1 = new FilteredChoiceTestLine(bd1, c1.getTime(), "MPE");
    lines.add(l1);

    BigDecimal bd2 = new BigDecimal("2000");
    Calendar c2 = Calendar.getInstance();
    c2.set(2012, 0, 1, 0, 0, 0);
    c2.set(Calendar.MILLISECOND, 0);
    l2 = new FilteredChoiceTestLine(bd2, c2.getTime(), "JSC");
    lines.add(l2);

    BigDecimal bd3 = new BigDecimal("2000");
    Calendar c3 = Calendar.getInstance();
    c3.set(2013, 0, 1, 0, 0, 0);
    c3.set(Calendar.MILLISECOND, 0);
    l3 = new FilteredChoiceTestLine(bd3, c3.getTime(), "MPE");
    lines.add(l3);

  }

  @Test
  public void createFilterMetaData() throws Exception {
    FilterManager fs = new FilterManager(FilteredTestLine.class);
    List<FilterFieldMetaData> list = fs.getFilterFieldMetaData();
    assertThat(list.size(), is(2));
    assertThat(list.get(0).getOrder(), is(100));
    assertThat(list.get(1).getOrder(), is(200));
  }

  @Test                                                       <>
  public void createFilterChoiceMetaData() throws Exception {
    FilterManager fs = new FilterManager(FilteredChoiceTestLine.class, FilterTestManager.class);
    List<FilterFieldMetaData> list = fs.getFilterFieldMetaData();
    assertThat(list.size(), is(3));
    assertThat(list.get(0).getOrder(), is(100));
    assertThat(list.get(1).getOrder(), is(200));
    assertThat(list.get(2).getOrder(), is(300));
  }

  @Test
  public void filterTest() {
    FilterManager<List> fs = new FilterManager<List>(FilteredChoiceTestLine.class, FilterTestManager.class);

    List<FilterFieldMetaData> metaData = fs.getFilterFieldMetaData();
    fs.addExpression(metaData.get(0).createFilterExpression(FilterOperand.GT, new BigDecimal("1000")));

    List<FilteredChoiceTestLine> filteredLines = fs.filter(lines);
    assertThat(filteredLines.size(), is(2));

    fs.addExpression(metaData.get(2).createFilterExpression(FilterOperand.EQ, "MPE"));
    filteredLines = fs.filter(lines);
    assertThat(filteredLines.size(), is(1));
    assertThat(filteredLines.get(0).getYadda(), is("MPE"));
  }


  public class FilteredTestLine {

    private BigDecimal foo;
    private Date bar;
    private String yadda;

    public FilteredTestLine(BigDecimal aFoo, Date aBar, String aYadda) {
      foo = aFoo;
      bar = aBar;
      yadda = aYadda;
    }

    @FilterField(order = 200, displayName = "Bar")
    public Date getBar() {
      return bar;
    }

    @FilterField(order = 100, displayName = "Foo")
    public BigDecimal getFoo() {
      return foo;
    }
  }

  public class FilteredChoiceTestLine {

    private BigDecimal foo;
    private Date bar;
    private String yadda;

    public FilteredChoiceTestLine(BigDecimal aFoo, Date aBar, String aYadda) {
      foo = aFoo;
      bar = aBar;
      yadda = aYadda;
    }

    @FilterField(order = 200, displayName = "Bar")
    public Date getBar() {
      return bar;
    }

    @FilterField(order = 100, displayName = "Foo")
    public BigDecimal getFoo() {
      return foo;
    }

    @FilterChoiceField(order = 300, displayName = "Yadda", sourceName = "YADDAS")
    public String getYadda() {
      return yadda;
    }
  }

  public class FilterTestManager {

    @FilterChoice("YADDAS")
    public List<String> choiceYadda() {
      return Arrays.asList("MPE", "JSC");
    }
  }
}
