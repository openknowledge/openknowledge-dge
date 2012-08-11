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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marc Petersen - open knowledge GmbH
 */
public final class AggregationLine<E extends Serializable> extends Line {

  private String displayName;
  private List<ValueLine<E>> valueLines;
  private Map<String, BigDecimal> values;

  public AggregationLine(String aDisplayName) {
    displayName = aDisplayName;
    valueLines = new ArrayList<ValueLine<E>>();
    values = new HashMap<String, BigDecimal>();
  }

  public String getDisplayName() {
    return displayName;
  }

  public List<ValueLine<E>> getValueLines() {
    return valueLines;
  }

  public void addValueLine(ValueLine<E> aValueLine, List<Method> aAggregrationMethods) {
    for (Method m : aAggregrationMethods) {
      try {
        values = putOrAddValue(values, m.getName(), (BigDecimal) m.invoke(aValueLine.getObject()));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    valueLines.add(aValueLine);
  }

  protected Map putOrAddValue(Map<String, BigDecimal> map, String key, BigDecimal addValue) {
    BigDecimal value = map.containsKey(key) ? map.get(key) : BigDecimal.ZERO;
    map.put(key, value.add(addValue));
    return map;
  }

  public BigDecimal getValue(String key) {
    return values.get(key);
  }

  @Override
  public boolean isExpanded() {
    return expanded;
  }

  @Override
  public boolean isAggregated() {
    return true;
  }

}
