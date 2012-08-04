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

package de.openknowledge.util.filter;

import org.apache.commons.lang.Validate;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Manages the choices method for a {@link FilterChoiceField}.
 *
 * @author Marc Petersen - open knowledge GmbH
 */
public class FilterChoiceFieldMetaData extends FilterFieldMetaData {

  private Method choiceMethod;
  private Object choiceValues;

  /**
   * Allocates a <code>FilterChoiceFieldMetaData</code> object and initializes it.
   * <p/>
   * The {@link FilterFieldType} of this class is always <code>SELECT</code>.
   * <p/>
   * In addition to the {@link FilterFieldMetaData} such an object manages the choices for a {@link FilterField}.
   *
   * @param aTargetMethod The targeted method.
   * @param aChoiceMethod The method which returns a list of possible choices.
   * @param aOrder        The order number.
   * @param aDisplayName  The display name.
   */
  public FilterChoiceFieldMetaData(Method aTargetMethod, Method aChoiceMethod, int aOrder, String aDisplayName) {
    super(FilterFieldType.SELECT, aTargetMethod, aOrder, aDisplayName);

    Validate.isTrue(Collection.class.isAssignableFrom(aChoiceMethod.getReturnType()),
                           "choiceMethod must have a return type which implements Collection. Return type of " + aChoiceMethod.getName()
                                   + " is " + aChoiceMethod.getReturnType().toString());

    // TODO Maybe validate that the return type of the targetMethod must be the same as the Collection type of the choiceMethod.

    choiceMethod = aChoiceMethod;
  }

  /**
   * Uses reflection to call the defined <code>choiceMethod</code> for the given <code>targetObject</code>. The return value is an
   * implementation of {@link java.util.Collection}.
   *
   * @param targetObject The object which contains the <code>choiceMethod</code>.
   * @return The return value - implementation of {@link java.util.Collection} - of the <code>choiceMethod</code>.
   */
  public Object getChoiceValues(Object targetObject) {
    try {
      return choiceMethod.invoke(targetObject);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Method getChoiceMethod() {
    return choiceMethod;
  }

}
