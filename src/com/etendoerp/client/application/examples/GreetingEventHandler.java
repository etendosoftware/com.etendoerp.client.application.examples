/*
 *************************************************************************
 * The contents of this file are subject to the Openbravo  Public  License
 * Version  1.1  (the  "License"),  being   the  Mozilla   Public  License
 * Version 1.1  with a permitted attribution clause; you may not  use this
 * file except in compliance with the License. You  may  obtain  a copy of
 * the License at http://www.openbravo.com/legal/license.html 
 * Software distributed under the License  is  distributed  on  an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific  language  governing  rights  and  limitations
 * under the License. 
 * The Original Code is Openbravo ERP. 
 * The Initial Developer of the Original Code is Openbravo SLU 
 * All portions are Copyright (C) 2011-2019 Openbravo SLU 
 * All Rights Reserved. 
 * Contributor(s):  ______________________________________.
 ************************************************************************
 */

package com.etendoerp.client.application.examples;

import java.util.List;

import javax.enterprise.event.Observes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openbravo.base.model.Entity;
import org.openbravo.base.model.ModelProvider;
import org.openbravo.base.model.Property;
import org.openbravo.base.provider.OBProvider;
import org.openbravo.client.kernel.event.EntityDeleteEvent;
import org.openbravo.client.kernel.event.EntityNewEvent;
import org.openbravo.client.kernel.event.EntityPersistenceEventObserver;
import org.openbravo.client.kernel.event.EntityUpdateEvent;
import org.openbravo.dal.service.OBDal;
import org.openbravo.model.ad.system.Language;
import org.openbravo.model.common.businesspartner.Greeting;
import org.openbravo.model.common.businesspartner.GreetingTrl;

/**
 * Listens to events on the {@link Greeting} entity.
 * 
 * This class is used as an example in howtos in the Openbravo Developers Guide:
 * http://wiki.openbravo.com/wiki/Category:Developers_Guide
 * 
 * @author mtaal
 */
class GreetingEventHandler extends EntityPersistenceEventObserver {
  private static Entity[] entities = {
      ModelProvider.getInstance().getEntity(Greeting.ENTITY_NAME) };
  private static final Logger logger = LogManager.getLogger();

  @Override
  protected Entity[] getObservedEntities() {
    return entities;
  }

  public void onUpdate(@Observes EntityUpdateEvent event) {
    if (!isValidEvent(event)) {
      return;
    }
    final Greeting greeting = (Greeting) event.getTargetInstance();
    final String title = greeting.getTitle();
    if (title != null && !title.endsWith(".")) {
      final Entity greetingEntity = ModelProvider.getInstance().getEntity(Greeting.ENTITY_NAME);
      final Property greetingTitleProperty = greetingEntity.getProperty(Greeting.PROPERTY_TITLE);
      // note use setCurrentState and not setters on the Greeting object directly
      event.setCurrentState(greetingTitleProperty, title + ".");
    }
    logger.info("Greeting {} is being updated", event.getTargetInstance().getId());
  }

  public void onSave(@Observes EntityNewEvent event) {
    if (!isValidEvent(event)) {
      return;
    }
    final Entity greetingEntity = ModelProvider.getInstance().getEntity(Greeting.ENTITY_NAME);
    final Greeting greeting = (Greeting) event.getTargetInstance();
    // now also add the dot to the title
    final String title = greeting.getTitle();
    if (title != null && !title.endsWith(".")) {
      final Property greetingTitleProperty = greetingEntity.getProperty(Greeting.PROPERTY_TITLE);
      // note use setCurrentState and not setters on the Greeting object directly
      event.setCurrentState(greetingTitleProperty, title + ".");
    }

    final GreetingTrl greetingTrl = OBProvider.getInstance().get(GreetingTrl.class);
    // set relevant translation properties
    greetingTrl.setGreeting(greeting);
    // 171 is dutch, choose any other language..
    greetingTrl.setLanguage(OBDal.getInstance().get(Language.class, "171"));
    // note we can call getters on the targetInstance, but not setters!
    greetingTrl.setName(greeting.getName());
    greetingTrl.setTitle(greeting.getTitle());
    greetingTrl.setTranslation(false);

    // and add the greetingTrl to the greeting
    // we don't use event.setCurrentState as we get the list and add to it
    // get the trl property for the greeting entity
    final Property greetingTrlProperty = greetingEntity
        .getProperty(Greeting.PROPERTY_GREETINGTRLLIST);
    @SuppressWarnings("unchecked")
    final List<Object> greetingTrls = (List<Object>) event.getCurrentState(greetingTrlProperty);
    greetingTrls.add(greetingTrl);

    // don't need to save the greetingTrl, it is saved as the child of the greeting
    // OBDal.getInstance().save(greetingTrl);

    logger.info("Greeting {} is being created", event.getTargetInstance().getId());
  }

  public void onDelete(@Observes EntityDeleteEvent event) {
    if (!isValidEvent(event)) {
      return;
    }
    logger.info("Greeting {} is being deleted", event.getTargetInstance().getId());
  }
}
