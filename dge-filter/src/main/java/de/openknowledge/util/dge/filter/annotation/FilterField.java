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

package de.openknowledge.util.dge.filter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates that the annotated method returns a value which can be filtered.
 *
 * @author Marc Petersen - open knowledge GmbH
 */
@Retention(RUNTIME)
@Target({ElementType.METHOD})
@Inherited
public @interface FilterField {

  /**
   * Defines the order number of this {@link FilterField} within a {@link java.util.Collection} of {@linkplain FilterField}s.
   *
   * @return The order number.
   */
  int order() default Integer.MAX_VALUE;

  /**
   * Defines the displayable name of this {@link FilterField}.
   *
   * @return The displayable name.
   */
  String displayName();

}
