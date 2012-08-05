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

package de.openknowledge.util.filter.example.web.bean;

import de.openknowledge.util.filter.example.web.domain.Pet;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marc Petersen - open knowledge GmbH
 */
@Named
@RequestScoped
public class PetBean {

  private List<Pet> pets;

  protected PetBean() {

  }

  public List<Pet> getPets() {
    return pets;
  }

  public void setPets(List<Pet> aPets) {
    pets = aPets;
  }

  @PostConstruct
  public void init() {
    pets = new ArrayList<Pet>();

    Pet p1 = new Pet();
    p1.setName("Pet One");
    pets.add(p1);

    Pet p2 = new Pet();
    p2.setName("Pet Two");
    pets.add(p2);
  }
}
