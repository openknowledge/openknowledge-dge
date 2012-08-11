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
 * Defines the possible types of a {@link de.openknowledge.util.dge.filter.annotation.FilterField}.
 * <p/>
 * Note: {@link #SELECT} should only be used with {@link de.openknowledge.util.dge.filter.annotation.FilterChoiceField}.
 *
 * @author Marc Petersen - open knowledge GmbH
 */
// TODO Can we use method.getGenericReturnType() instead of FilterFieldType?
public enum FilterFieldType {

  /**
   * Represents a any numeric data type.
   */
  BIGDECIMAL,

  /**
   * Represents any date related data type.
   */
  DATE,

  /**
   * Represents a {@link java.util.List} of {@linkplain Object}s.
   */
  SELECT;

//  private List<NamedOperand> possibleOperands;
//
//  private FilterFieldType(NamedOperand... operands) {
//    possibleOperands = Arrays.asList(operands);
//  }

//  public class NamedOperand {
//
//    private FilterOperand operand;
//    private String displayName;
//
//    public NamedOperand(FilterOperand aOperand, String aDisplayName) {
//      operand = aOperand;
//      displayName = aDisplayName;
//    }
//
//    public String getDisplayName() {
//      return displayName;
//    }
//
//    public FilterOperand getOperand() {
//      return operand;
//    }
//  }
}
