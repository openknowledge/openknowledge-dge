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

import de.openknowledge.util.dge.filter.FilterExpression;
import de.openknowledge.util.dge.filter.FilterFieldMetaData;
import de.openknowledge.util.dge.filter.FilterFieldType;
import de.openknowledge.util.dge.filter.FilterOperand;
import de.openknowledge.util.dge.filter.annotation.FilterField;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Marc Petersen - open knowledge GmbH
 */
public class FilterExpressionTest {

  private FilteredTestLine l1;
  private FilteredTestLine l2;
  private FilteredTestLine l3;

  @Before
  public void init() {
    BigDecimal bd1 = new BigDecimal("1000");
    Calendar c1 = Calendar.getInstance();
    c1.set(2012, 0, 1, 0, 0, 0);
    c1.set(Calendar.MILLISECOND, 0);
    l1 = new FilteredTestLine(bd1, c1.getTime(), "MPE");

    BigDecimal bd2 = new BigDecimal("2000");
    Calendar c2 = Calendar.getInstance();
    c2.set(2012, 0, 1, 0, 0, 0);
    c2.set(Calendar.MILLISECOND, 0);
    l2 = new FilteredTestLine(bd2, c2.getTime(), "JSC");

    BigDecimal bd3 = new BigDecimal("2000");
    Calendar c3 = Calendar.getInstance();
    c3.set(2013, 0, 1, 0, 0, 0);
    c3.set(Calendar.MILLISECOND, 0);
    l3 = new FilteredTestLine(bd3, c3.getTime(), "MPE");
  }

  @Test
  public void matchesBigDecimalTest() throws Exception {
    Method methodFoo = FilteredTestLine.class.getMethod("getFoo");
    FilterFieldMetaData metaDataFoo = new FilterFieldMetaData(FilterFieldType.BIGDECIMAL, methodFoo, 100, "Foo");

    FilterExpression<BigDecimal> fe = metaDataFoo.createFilterExpression(FilterOperand.GT, new BigDecimal("1500"));
    assertThat(fe.matches(l1), is(false));
    assertThat(fe.matches(l2), is(true));
    assertThat(fe.matches(l3), is(true));

    fe = metaDataFoo.createFilterExpression(FilterOperand.EQ, new BigDecimal("2000"));
    assertThat(fe.matches(l1), is(false));
    assertThat(fe.matches(l2), is(true));
    assertThat(fe.matches(l3), is(true));

    fe = metaDataFoo.createFilterExpression(FilterOperand.LT, new BigDecimal("2000"));
    assertThat(fe.matches(l1), is(true));
    assertThat(fe.matches(l2), is(false));
    assertThat(fe.matches(l3), is(false));
  }

  @Test
  public void matchesDateTest() throws Exception {
    Method methodBar = FilteredTestLine.class.getMethod("getBar");
    FilterFieldMetaData metaDataBar = new FilterFieldMetaData(FilterFieldType.DATE, methodBar, 200, "Bar");

    Calendar c = Calendar.getInstance();
    c.set(2012, 0, 1, 0, 0, 0);
    c.set(Calendar.MILLISECOND, 0);
    FilterExpression<Date> fe = metaDataBar.createFilterExpression(FilterOperand.GT, c.getTime());
    assertThat(fe.matches(l1), is(false));
    assertThat(fe.matches(l2), is(false));
    assertThat(fe.matches(l3), is(true));

    c.set(2012, 0, 1, 0, 0, 0);
    fe = metaDataBar.createFilterExpression(FilterOperand.EQ, c.getTime());
    assertThat(fe.matches(l1), is(true));
    assertThat(fe.matches(l2), is(true));
    assertThat(fe.matches(l3), is(false));

    c.set(2013, 0, 1, 0, 0, 0);
    fe = metaDataBar.createFilterExpression(FilterOperand.LT, c.getTime());
    assertThat(fe.matches(l1), is(true));
    assertThat(fe.matches(l2), is(true));
    assertThat(fe.matches(l3), is(false));
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
}
