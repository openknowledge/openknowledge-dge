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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang.Validate.notNull;

/**
 * The <code>FilterManager</code> provides method/s to process the classes which contain {@link FilterField} and {@link FilterChoiceField}
 * annotations.
 *
 * @author Marc Petersen - open knowledge GmbH
 */
public class FilterManager<T extends Collection> implements Serializable {

  private List<FilterFieldMetaData> filterFieldMetaData;
  private List<Class<?>> choiceManagers = new ArrayList<Class<?>>();
  private Class<?> filterLine;
  private List<FilterExpression> expressions = new ArrayList<FilterExpression>();

  /**
   * Allocates a <code>FilterManager</code> object.
   * <p/>
   * <i>Note:</i> Use <code>FilterManager(Class<?> aFilterLine, Class<?>... theChoiceManagers)</code> if <code>aFilterLine</code> contains
   * {@link FilterChoiceField} annotations.
   *
   * @param aFilterLine The class which contains {@link FilterField} annotations.
   */
  public FilterManager(Class<?> aFilterLine) {
    notNull(aFilterLine);
    filterLine = aFilterLine;
  }

  /**
   * Allocates a <code>FilterManager</code> object and initializes it so that it is able to extract {@link FilterFieldMetaData} from the
   * given <code>aFilterLine</code> class. It expects at least one class - <code>theChoiceManagers</code> - which contains {@link
   * FilterChoice} annotations as sources for {@linkplain FilterChoiceField}s.
   * <p/>
   * <i>Note</i>: Use <code>FilterManager(Class<?> aFilterLine)</code> if the <code>aFilterLine</code> class does not contain {@link
   * FilterChoiceField} annotations
   *
   * @param aFilterLine The class which contains {@link FilterField} annotations.
   * @param theChoiceManagers One or more classes to scan for {@link FilterChoice} annotations.
   */
  public FilterManager(Class<?> aFilterLine, Class<?>... theChoiceManagers) {
    this(aFilterLine);
    choiceManagers = Arrays.asList(theChoiceManagers);
  }

  /**
   * Filters the given {@link java.util.Collection} based on the previously defined {@linkplain FilterExpression}s.
   * <p/>
   * <i>Note</i>: This methods evaluates all {@linkplain FilterExpression}s as connected with AND. This means an object of the Collection
   * will be removed as soon as one of the filters does not match.
   *
   * @param lines The {@link java.util.Collection} of objects to be filtered.
   * @return The filtered {@link java.util.Collection} of objects.
   */
  public T filter(T lines) {
    List<Object> removeList = new ArrayList<Object>();

    for(Object line : lines) {
      for(FilterExpression expression : expressions) {
        if(!expression.matches(line)) {
          removeList.add(line);
          break;
        }
      }
    }

    lines.removeAll(removeList);
    return lines;
  }

  /**
   * Extracts the methods from the <code>choiceManagers</code> which are annotated with {@link FilterChoice} and returns them as a map
   * containing the annotated method as value and the annotation <code>sourceName</code> as key.
   *
   * @return The map of found choice methods.
   */
  protected Map<String, Method> extractChoiceMethods() {
    Map<String, Method> choiceMethods = new HashMap<String, Method>();

    List<Method> methods = new ArrayList<Method>();
    for (Class<?> choiceClazz : choiceManagers) {
      methods.addAll(Arrays.asList(choiceClazz.getMethods()));
    }

    for (Method method : methods) {
      FilterChoice annotation = method.getAnnotation(FilterChoice.class);
      if (annotation != null) {
        choiceMethods.put(annotation.value(), method);
      }
    }

    return choiceMethods;
  }

  /**
   * Assembles a list of {@link FilterFieldMetaData} based on {@link FilterField} and {@link FilterChoiceField} annotated methods.
   * <p/>
   * Procedure of this method is as follows:
   * <ol>
   * <li>This method iterates over all methods within the given <code>filterLine</code> class.</li>
   * <li>A new {@link FilterFieldMetaData} object is instantiated and added to the list if a method is annotated as {@link FilterField}
   * .</li>
   * <li>A new {@link FilterChoiceFieldMetaData} object is instantiated and added to the list if a method is annotated as {@link
   * FilterChoiceField}.</li>
   * <li>Finally the list is sorted and can be retrieved by calling {@link #getFilterFieldMetaData()}</li>
   * </ol>
   * @throws IllegalArgumentException if the return type of the {@link FilterField} method is not supported.
   * @throws IllegalArgumentException if no corresponding {@link FilterChoice} method is found for a {@link FilterChoiceField}
   * method.
   */
  protected void extractMetaData() {
    Map<String, Method> choiceMethods = extractChoiceMethods();
    boolean hasChoices = !choiceMethods.isEmpty();
    Method[] methods = filterLine.getMethods();
    filterFieldMetaData = new ArrayList<FilterFieldMetaData>();

    for (Method method : methods) {
      FilterField annotation = method.getAnnotation(FilterField.class);
      if (annotation != null) {
        // TODO think about using method.getReturnType to replace FilterFieldType.
        if (BigDecimal.class.equals(method.getReturnType())) {
          filterFieldMetaData.add(
                  new FilterFieldMetaData(FilterFieldType.BIGDECIMAL, method, annotation.order(), annotation.displayName()));
        } else if (Date.class.equals(method.getReturnType())) {
          filterFieldMetaData.add(
                  new FilterFieldMetaData(FilterFieldType.DATE, method, annotation.order(), annotation.displayName()));
        } else {
          throw new IllegalArgumentException("Unsupported return type " + method.getReturnType());
        }
      }

      if (hasChoices) {
        FilterChoiceField choiceAnnotation = method.getAnnotation(FilterChoiceField.class);
        if (choiceAnnotation != null) {
          if (choiceMethods.containsKey(choiceAnnotation.sourceName())) {
            filterFieldMetaData.add(new FilterChoiceFieldMetaData(method,
                                                                  choiceMethods.get(choiceAnnotation.sourceName()),
                                                                  choiceAnnotation.order(),
                                                                  choiceAnnotation.displayName()));
          } else {
            throw new IllegalArgumentException("FilterChoice method not found for FilterChoiceField " + method.getName());
          }
        }
      }
    }

    Collections.sort(filterFieldMetaData, new Comparator<FilterFieldMetaData>() {
      @Override
      public int compare(FilterFieldMetaData o1, FilterFieldMetaData o2) {
        return o1.getOrder() > o2.getOrder() ? 1 : -1;

      }
    });
  }

  public List<FilterFieldMetaData> getFilterFieldMetaData() {
    if(filterFieldMetaData == null) {
      extractMetaData();
    }
    return filterFieldMetaData;
  }

  public void addExpression(FilterExpression expression) {
    expressions.add(expression);
  }

  public void removeExpression(FilterExpression expression) {
    expressions.remove(expression);
  }

  public void resetExpressions() {
    expressions = new ArrayList<FilterExpression>();
  }

  public List<FilterExpression> getExpressions() {
    return expressions;
  }
}
