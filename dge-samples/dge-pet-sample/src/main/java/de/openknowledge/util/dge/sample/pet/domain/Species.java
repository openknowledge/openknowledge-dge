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

package de.openknowledge.util.dge.sample.pet.domain;

/**
 * @author Marc Petersen - open knowledge GmbH
 */
public enum Species {

  CAT("Cat"),
  DOG("Dog"),
  BIRD("Bird"),
  RABBIT("Rabbit");

  private String name;

  Species(String aName) {
    name = aName;
  }

  public String getName() {
    return name;
  }

  public String toString() {
    return name;
  }
}
