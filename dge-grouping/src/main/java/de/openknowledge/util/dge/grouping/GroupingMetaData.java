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

import java.lang.reflect.Method;

/**
 * @author Marc Petersen - open knowledge GmbH
 */
public class GroupingMetaData {

  private int order;
  private transient Method targetMethod;
  private String displayName;

  public GroupingMetaData(String aDisplayName, int aOrder, Method aTargetMethod) {
    displayName = aDisplayName;
    order = aOrder;
    targetMethod = aTargetMethod;
  }

  /**
   * Uses reflection to call the defined <code>targetMethod</code> for the given <code>targetObject</code> and return its return value.
   *
   * @param targetObject The object which contains the <code>targetMethod</code>.
   * @return The return value of the <code>targetMethod</code>.
   */
  public Object getValue(Object targetObject) {
    try {
      return targetMethod.invoke(targetObject);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String getDisplayName() {
    return displayName;
  }

  public int getOrder() {
    return order;
  }

  public Method getTargetMethod() {
    return targetMethod;
  }
}
