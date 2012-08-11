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

package de.openknowledge.util.dge.filter;

/**
 * A <code>FilterExpression</code> connects a pre-defined {@link FilterFieldMetaData} with a {@link FilterOperand} and a
 * <code>value</code>.
 *
 * @author Marc Petersen - open knowledge GmbH
 */
// TODO JavaDoc!
public class FilterExpression<T extends Comparable> {

  private FilterFieldMetaData metaData;
  private FilterOperand filterOperand;
  private T value;

  /**
   *
   * @param aMetaData
   * @param aFilterOperand
   * @param aValue
   */
  // TODO JavaDoc!
  public FilterExpression(FilterFieldMetaData aMetaData, FilterOperand aFilterOperand, T aValue) {
    metaData = aMetaData;
    filterOperand = aFilterOperand;
    value = aValue;
  }

  /**
   *
   * @param targetObject
   * @return
   */
  // TODO JavaDoc!
  public boolean matches(Object targetObject) {
    T currentValue = (T)metaData.getValue(targetObject);
    // TODO check classcastexception
    int result = currentValue.compareTo(value);

    if(filterOperand == FilterOperand.GT && result > 0) {
      return true;
    }

    if(filterOperand == FilterOperand.EQ && result == 0) {
      return true;
    }

    if(filterOperand == FilterOperand.LT && result < 0) {
      return true;
    }

    return false;
  }

  public FilterFieldMetaData getMetaData() {
    return metaData;
  }
}
