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

import de.openknowledge.util.dge.filter.annotation.FilterChoice;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Bartug Bölükemini - open knowledge GmbH
 */
public class PetLineChoices implements Serializable {

  private Set<String> species;

    public PetLineChoices() {
      init();
    }

    public void init() {
      species = new HashSet<String>();
      species.add("Cat");
      species.add("Dog");
      species.add("Bird");
      species.add("Rabbit");
    }

    @FilterChoice("SPECIES")
    public Set<String> getSpecies() {
      return species;
    }

}
