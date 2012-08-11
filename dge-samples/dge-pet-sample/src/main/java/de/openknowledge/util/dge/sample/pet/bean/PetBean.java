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

package de.openknowledge.util.dge.sample.pet.bean;

import de.openknowledge.util.dge.filter.web.FilterAssembler;
import de.openknowledge.util.dge.sample.pet.domain.Pet;
import de.openknowledge.util.dge.sample.pet.domain.Species;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateMidnight;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marc Petersen - open knowledge GmbH
 */
@Named
@SessionScoped
public class PetBean implements Serializable {

  private static final Log LOG = LogFactory.getLog(PetBean.class);

  private List<Pet> pets;
  private FilterAssembler filterAssembler;

  protected PetBean() {

  }

  public List<Pet> getPets() {
    return (List<Pet>)getFilterAssembler().filter(new ArrayList(pets));
  }

  public void setPets(List<Pet> aPets) {
    pets = aPets;
  }

  public FilterAssembler<List<Pet>> getFilterAssembler() {
    if(filterAssembler == null) {
      filterAssembler = new FilterAssembler<List<Pet>>(Pet.class);
      filterAssembler.setFilterActive(true);
    }
    return filterAssembler;
  }

  public void applyFilter() {
    // doing nothing yet.
    LOG.info("applying filter");
  }

  @PostConstruct
  public void init() {
    pets = new ArrayList<Pet>();

    Pet p1 = new Pet();
    p1.setName("Funny");
    p1.setSpecies(Species.DOG);
    p1.setDateOfBirth(new DateMidnight(1990, 2, 3).toDate());
    pets.add(p1);

    Pet p2 = new Pet();
    p2.setName("Evi");
    p2.setSpecies(Species.CAT);
    p2.setDateOfBirth(new DateMidnight(1996, 7, 14).toDate());
    pets.add(p2);

    Pet p3 = new Pet();
    p3.setName("Bandit");
    p3.setSpecies(Species.DOG);
    p3.setDateOfBirth(new DateMidnight(2011, 5, 5).toDate());
    pets.add(p3);

    Pet p4 = new Pet();
    p4.setName("Whisky");
    p4.setSpecies(Species.CAT);
    p4.setDateOfBirth(new DateMidnight(2011, 4, 20).toDate());
    pets.add(p4);
  }
}
