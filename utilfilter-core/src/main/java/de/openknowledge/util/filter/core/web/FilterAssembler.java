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

package de.openknowledge.util.filter.core.web;

import de.openknowledge.util.filter.core.FilterExpression;
import de.openknowledge.util.filter.core.FilterManager;
import de.openknowledge.util.filter.core.FilterOperand;
import de.openknowledge.util.filter.core.FilterChoiceFieldMetaData;
import de.openknowledge.util.filter.core.FilterFieldMetaData;
import de.openknowledge.util.filter.core.FilterFieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang.Validate.notNull;

/**
 * Represents the filter rows for the dynamic filter form. Finally the valid filter rows must be converted into a FilterExpression.
 *
 * @author Marc Petersen - open knowledge GmbH
 */
public class FilterAssembler implements Serializable {

  private List<FilterRow> filterRows;
  private FilterManager filterManager;
  private List<FilterOperand> possibleOperands;
  private boolean filterActive = false;
  private List<Object> filterChoiceObjects;

  /**
   * Allocates a <code>FilterAssembler</code> object and initializes it.
   *
   * @param aFilterManager The FilterManager instance.
   */
  public FilterAssembler(FilterManager aFilterManager) {
    notNull(aFilterManager);

    filterManager = aFilterManager;
    possibleOperands = Arrays.asList(FilterOperand.values());
    resetFilter();
  }

  public FilterAssembler(FilterManager aFilterManager, Object... aFilterChoiceObjects) {
    this(aFilterManager);
    filterChoiceObjects = Arrays.asList(aFilterChoiceObjects);
  }

  public Collection filter(Collection collection) {
    filterManager.resetExpressions();
    for (FilterRow row : filterRows) {
      FilterExpression fe = row.asExpression();
      if (fe != null) {
        filterManager.addExpression(fe);
      }
    }

    return filterManager.filter(collection);
  }

  public FilterFieldMetaData getMetaDataByString(String meta) {
    for (FilterFieldMetaData data : getFilterFieldMetaData()) {
      if (data.getTargetMethod().getName().equals(meta)) {
        return data;
      }
    }
    return null;
  }

  public List<FilterFieldMetaData> getFilterFieldMetaData() {
    return filterManager.getFilterFieldMetaData();
  }

  public List<FilterRow> getFilterRows() {
    return filterRows;
  }

  public List<FilterOperand> getPossibleOperands() {
    return possibleOperands;
  }

  public void removeFilterRow(FilterRow row) {
    filterRows.remove(row);
    if(filterRows.isEmpty()) {
      filterRows.add(new FilterRow());
    }
  }

  public void addNewFilterRow() {
    filterRows.add(new FilterRow());
  }

  /**
   * Removes all <code>FilterRows</code> and adds one empty <code>FilterRow</code>.
   */
  public void resetFilter() {
    filterRows = new ArrayList<FilterRow>();
    filterRows.add(new FilterRow());
    filterManager.resetExpressions();
  }

  /**
   * UI Helper to decide whether to filter the objects or not.
   * @return
   */
  public boolean isFilterActive() {
    return filterActive;
  }

  public void setFilterActive(boolean aFilterActive) {
    filterActive = aFilterActive;
    if(!filterActive) {
      resetFilter();
    }
  }

  public class FilterRow implements Serializable {

    private FilterFieldMetaData metaData;
    private FilterOperand filterOperand;

    private Object selection;
    private Date date;
    private BigDecimal bigDecimal;

    public FilterOperand getFilterOperand() {
      return filterOperand;
    }

    public FilterExpression asExpression() {
      if (isValid()) {
        return new FilterExpression(metaData, filterOperand, getValue());
      }
      return null;
    }

    private Comparable getValue() {
      switch (metaData.getType()) {
        case BIGDECIMAL:
          return bigDecimal;
        case DATE:
          return date;
        case SELECT:
          return (Comparable) selection;
        default:
          throw new IllegalArgumentException("Type not supported: " + metaData.getType());
      }
    }

    /**
     * Checks whether this {@link FilterRow} is a valid and thus fulfills the requirements to be converted into a {@link FilterExpression}.
     *
     * @return Returns <code>true</code> if the object is valid, otherwise <code>false</code>
     */
    public boolean isValid() {
      if (metaData == null) {
        return false;
      }

      if (filterOperand == null) {
        return false;
      }

      if (getValue() == null) {
        return false;
      }

      return true;
    }

    public Collection getChoiceValues() {
      if (metaData instanceof FilterChoiceFieldMetaData) {
        for (Object o : filterChoiceObjects) {
          try {
            return (Collection) ((FilterChoiceFieldMetaData) metaData).getChoiceValues(o);
          } catch (RuntimeException e) {
            // expected.
          }
        }
      }
      return new ArrayList<Object>();
    }

    protected Object getObjectByString(String s) {
      for (Object o : getChoiceValues()) {
        if (o.toString().equals(s)) {
          return o;
        }
      }
      return null;
    }

    public void setFilterOperand(FilterOperand aFilterOperand) {
      filterOperand = aFilterOperand;
    }

    public FilterFieldMetaData getMetaData() {
      return metaData;
    }

    public void setMetaData(FilterFieldMetaData aMetaData) {
      metaData = aMetaData;
      if(aMetaData instanceof FilterChoiceFieldMetaData) {
        filterOperand = FilterOperand.EQ;
      }
    }

    public String getSelection() {
      if(selection != null) {
        return selection.toString();
      }
      return null;
    }

    public void setSelection(String aSelection) {
      selection = getObjectByString(aSelection);
    }

    public BigDecimal getBigDecimal() {
      return bigDecimal;
    }

    public void setBigDecimal(BigDecimal aBigDecimal) {
      bigDecimal = aBigDecimal;
    }

    public String getMetaDataName() {
      if (metaData != null) {
        return metaData.getTargetMethod().getName();
      }
      return null;
    }

    public void setMetaDataName(String aMetaDataName) {
      setMetaData(getMetaDataByString(aMetaDataName));
    }

    public FilterFieldType getFilterFieldType() {
      if (metaData != null) {
        return metaData.getType();
      }
      return null;
    }

    public void setFilterFieldType(FilterFieldType type) {
      // just a dummy. Needed for inputHidden field.
    }

    public Date getDate() {
      return date;
    }

    public void setDate(Date date) {
      this.date = date;
    }
  }

}
