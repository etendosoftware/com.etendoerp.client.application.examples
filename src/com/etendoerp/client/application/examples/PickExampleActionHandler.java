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
 * All portions are Copyright (C) 2011-2017 Openbravo SLU
 * All Rights Reserved.
 * Contributor(s):  ______________________________________.
 ************************************************************************
 */
package com.etendoerp.client.application.examples;

import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.openbravo.client.application.ApplicationConstants;
import org.openbravo.client.application.process.BaseProcessActionHandler;
import org.openbravo.client.application.process.ResponseActionsBuilder.MessageType;

/**
 * @author iperdomo
 * 
 */
public class PickExampleActionHandler extends BaseProcessActionHandler {

  private static final Logger log = Logger.getLogger(PickExampleActionHandler.class);

  @Override
  protected JSONObject doExecute(Map<String, Object> parameters, String content) {
    try {
      JSONObject request = new JSONObject(content);

      log.info(">> parameters: " + parameters);
      // log.info(">> content:" + content);

      // _selection contains the rows that the user selected.
      JSONArray selection = new JSONArray(
          request.getString(ApplicationConstants.SELECTION_PROPERTY));

      log.info(">> selected: " + selection);

      // _allRows contains all the rows available in the grid
      JSONArray allRows = new JSONArray(request.getString(ApplicationConstants.ALL_ROWS_PARAM));

      log.info(">> allRows: " + allRows);

      // A Pick and Execute process can have several buttons (buttonList)
      // You can know which button was clicked getting the value of _buttonValue
      log.info(">> clicked button: " + request.getString(ApplicationConstants.BUTTON_VALUE));

      return getResponseBuilder().retryExecution(MessageType.INFO, "This is a sample message")
          .build();

    } catch (Exception e) {
      log.error("Error processing request: " + e.getMessage(), e);
    }
    return new JSONObject();
  }
}
