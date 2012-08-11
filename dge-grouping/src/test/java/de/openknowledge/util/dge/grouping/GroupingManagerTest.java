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

package de.openknowledge.util.dge.grouping;

import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Marc Petersen - open knowledge GmbH
 */
public class GroupingManagerTest {

  List<Foo> foos;

  @Before
  public void init() {
    foos = new ArrayList<Foo>();

    foos.add(new Foo("Marc", "Today"));
    foos.add(new Foo("Marc", "Tomorrow"));
    foos.add(new Foo("Jens", "Today"));
    foos.add(new Foo("Jens", "Yesterday"));
    foos.add(new Foo("Bartug", "Yesterday"));
    foos.add(new Foo("Bartug", "Tomorrow"));
  }

  @Test
  public void groupingMetaDataTest() {
    GroupingManager<Foo> manager = new GroupingManager<Foo>(foos, Foo.class);
    List<GroupingMetaData> metaData = manager.getGroupingMetaData();

    assertThat(metaData.get(0).getOrder(), is(100));
    assertThat(metaData.get(1).getOrder(), is(200));
  }

  @Test
  public void groupByTest() {
    GroupingManager<Foo> manager = new GroupingManager<Foo>(foos, Foo.class);
    manager.groupBy("Bar");

    assertThat(((AggregationLine<Foo>)manager.getGroupedLines().get(0)).getDisplayName(), is("Bartug"));
    assertThat(((AggregationLine<Foo>)manager.getGroupedLines().get(1)).getDisplayName(), is("Jens"));
    assertThat(((AggregationLine<Foo>)manager.getGroupedLines().get(2)).getDisplayName(), is("Marc"));
  }

  @Test
  public void initGroupTest() throws Exception {
    GroupingManager<Foo> manager = new GroupingManager<Foo>(foos, Foo.class);

    assertThat(((ValueLine<Foo>)manager.getGroupedLines().get(0)).getObject().getBar(), is("Marc"));
    assertThat(((ValueLine<Foo>)manager.getGroupedLines().get(4)).getObject().getBar(), is("Bartug"));
    assertThat(((ValueLine<Foo>) manager.getGroupedLines().get(5)).getObject().getBar(), is("Bartug"));
  }

  public class Foo implements Serializable {

    private String bar;
    private String yadda;

    public Foo(String aBar, String aYadda) {
      bar = aBar;
      yadda = aYadda;
    }

    @Group(order = 100, displayName = "Bar")
    public String getBar() {
      return bar;
    }

    @Group(order = 200, displayName = "Yadda")
    public String getYadda() {
      return yadda;
    }
  }
}
