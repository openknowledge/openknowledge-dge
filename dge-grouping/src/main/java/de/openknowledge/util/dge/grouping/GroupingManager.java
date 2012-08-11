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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marc Petersen - open knowledge GmbH
 */
public class GroupingManager<E extends Serializable> implements Serializable {

  private List<E> objects;
  private List<Line> groupedLines;
  private Class<E> clazz;
  private String currentGroupingMetaData;
  private transient List<GroupingMetaData> groupingMetaData;
  private transient List<Method> aggregationValueMethods;

  public GroupingManager(List<E> aObjects, Class<E> aClazz) {
    objects = aObjects;
    clazz = aClazz;
    initGroupedLines();
  }

  public List<Line> groupBy(String groupDisplayName) throws IllegalArgumentException {
    for (GroupingMetaData metaData : getGroupingMetaData()) {
      if (metaData.getDisplayName().equals(groupDisplayName)) {
        return groupBy(metaData);
      }
    }
    throw new IllegalArgumentException("GroupingMetaData with displayName '" + groupDisplayName + "' not found.");
  }

  public List<Line> groupBy(GroupingMetaData newGroupingMetaData) {
    Map<String, AggregationLine> map = new HashMap<String, AggregationLine>();

    for (E object : objects) {
      map = putOrAddValueLine(map, newGroupingMetaData.getValue(object), new ValueLine<E>(object));
    }

    groupedLines = new ArrayList<Line>(map.values());

    Collections.sort(groupedLines, new Comparator<Line>() {
      @Override
      public int compare(Line o1, Line o2) {
        return ((AggregationLine<E>) o1).getDisplayName().compareTo(((AggregationLine<E>) o2).getDisplayName());
      }
    });

    currentGroupingMetaData = newGroupingMetaData.getDisplayName();
    return getGroupedLines();
  }

  protected void initGroupingMetaData() {
    Method[] methods = clazz.getMethods();
    aggregationValueMethods = new ArrayList<Method>();
    groupingMetaData = new ArrayList<GroupingMetaData>();

    for (Method method : methods) {
      Group annotation = method.getAnnotation(Group.class);
      if (annotation != null) {
        groupingMetaData.add(new GroupingMetaData(annotation.displayName(), annotation.order(), method));
      }

      AggregrationValue valueAnnotation = method.getAnnotation(AggregrationValue.class);
      if(valueAnnotation != null) {
        aggregationValueMethods.add(method);
      }
    }

    Collections.sort(groupingMetaData, new Comparator<GroupingMetaData>() {
      @Override
      public int compare(GroupingMetaData o1, GroupingMetaData o2) {
        return o1.getOrder() > o2.getOrder() ? 1 : -1;
      }
    });
  }

  protected void initGroupedLines() {
    groupedLines = new ArrayList<Line>();
    for (E object : objects) {
      groupedLines.add(new ValueLine<E>(object));
    }
  }

  protected Map putOrAddValueLine(Map map, Object key, ValueLine<E> currentLine) {
    AggregationLine line = map.containsKey(key) ? (AggregationLine) map.get(key) : new AggregationLine(key.toString());
    line.addValueLine(currentLine, getAggregationValueMethods());
    map.put(key, line);
    return map;
  }

  public List<Line> getGroupedLines() {
    List<Line> lines  = new ArrayList<Line>();
    for (Line line : groupedLines) {
      lines.add(line);
      if (line.isExpanded()) {
        lines.addAll(((AggregationLine)line).getValueLines());
      }
    }

    return lines;
  }

  public List<E> getObjects() {
    return objects;
  }

  public String getCurrentGroupingMetaData() {
    return currentGroupingMetaData;
  }

  public List<GroupingMetaData> getGroupingMetaData() {
    if (groupingMetaData == null) {
      initGroupingMetaData();
    }
    return groupingMetaData;
  }

  public List<Method> getAggregationValueMethods() {
    if (aggregationValueMethods == null) {
      initGroupingMetaData();
    }
    return aggregationValueMethods;
  }

}
