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
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author Marc Petersen - open knowledge GmbH
 */
public class GroupingManagerTest {

  List<PersonTracker> personTrackers;

  @Before
  public void init() throws MalformedURLException {
    personTrackers = new ArrayList<PersonTracker>();

    personTrackers.add(new PersonTracker("Marc", "Today", new BigDecimal(1), new URL("http://foo.de/")));
    personTrackers.add(new PersonTracker("Marc", "Tomorrow", new BigDecimal(2), new URL("http://foo.de/")));
    personTrackers.add(new PersonTracker("Jens", "Today", new BigDecimal(3), new URL("http://bar.de/")));
    personTrackers.add(new PersonTracker("Jens", "Yesterday", new BigDecimal(4), new URL("http://foo.de/")));
    personTrackers.add(new PersonTracker("Bartug", "Yesterday", new BigDecimal(5), new URL("http://bar.de/")));
    personTrackers.add(new PersonTracker("Bartug", "Tomorrow", new BigDecimal(6), new URL("http://foo.de/")));
  }

  @Test
  public void groupingMetaDataTest() {
    GroupingManager<PersonTracker> manager = new GroupingManager<PersonTracker>(personTrackers, PersonTracker.class);
    List<GroupingMetaData> metaData = manager.getGroupingMetaData();

    assertThat(metaData.get(0).getOrder(), is(100));
    assertThat(metaData.get(1).getOrder(), is(200));
  }

  @Test
  public void groupByStringTest() {
    GroupingManager<PersonTracker> manager = new GroupingManager<PersonTracker>(personTrackers, PersonTracker.class);
    manager.groupBy("Name");

    assertThat(((AggregationLine<PersonTracker>)manager.getGroupedLines().get(0)).getDisplayName(), is("Bartug"));
    assertThat(((AggregationLine<PersonTracker>)manager.getGroupedLines().get(1)).getDisplayName(), is("Jens"));
    assertThat(((AggregationLine<PersonTracker>)manager.getGroupedLines().get(2)).getDisplayName(), is("Marc"));
  }

  @Test
  public void groupByObjectTest() throws MalformedURLException {
    GroupingManager<PersonTracker> manager = new GroupingManager<PersonTracker>(personTrackers, PersonTracker.class);
    manager.groupBy("ProfileLink");

    assertEquals(((AggregationLine<PersonTracker>)manager.getGroupedLines().get(0)).getGroupingObject(), new URL("http://bar.de/"));
    assertEquals(((AggregationLine<PersonTracker>)manager.getGroupedLines().get(1)).getGroupingObject(), new URL("http://foo.de/"));
  }

  @Test
  public void initGroupTest() throws Exception {
    GroupingManager<PersonTracker> manager = new GroupingManager<PersonTracker>(personTrackers, PersonTracker.class);

    assertThat(((ValueLine<PersonTracker>)manager.getGroupedLines().get(0)).getObject().getName(), is("Marc"));
    assertThat(((ValueLine<PersonTracker>)manager.getGroupedLines().get(4)).getObject().getName(), is("Bartug"));
    assertThat(((ValueLine<PersonTracker>) manager.getGroupedLines().get(5)).getObject().getName(), is("Bartug"));
  }

  @Test
  public void aggregatedValueTest() {
    GroupingManager<PersonTracker> manager = new GroupingManager<PersonTracker>(personTrackers, PersonTracker.class);
    manager.groupBy("Name");

    assertEquals(((AggregationLine<PersonTracker>) manager.getGroupedLines().get(0)).getValue("getAge"), new BigDecimal(11));
    assertEquals(((AggregationLine<PersonTracker>) manager.getGroupedLines().get(1)).getValue("getAge"), new BigDecimal(7));
    assertEquals(((AggregationLine<PersonTracker>) manager.getGroupedLines().get(2)).getValue("getAge"), new BigDecimal(3));
  }

  public class PersonTracker implements Serializable {

    private String name;
    private String lastSeen;
    private URL url;
    private BigDecimal age;

    public PersonTracker(String aName, String aLastSeen) {
      name = aName;
      lastSeen = aLastSeen;
    }

    public PersonTracker(String aName, String aLastSeen, BigDecimal aAge) {
      this(aName, aLastSeen);
      age = aAge;
    }

    public PersonTracker(String aName, String aLastSeen, BigDecimal aAge, URL aUrl) {
      this(aName, aLastSeen, aAge);
      url = aUrl;
    }

    @Group(order = 100, displayName = "Name")
    public String getName() {
      return name;
    }

    @Group(order = 200, displayName = "LastSeen")
    public String getLastSeen() {
      return lastSeen;
    }

    @Group(order = 200, displayName = "ProfileLink")
    public URL getUrl() {
      return url;
    }

    @AggregrationValue
    public BigDecimal getAge() {
      return age;
    }
  }
}
