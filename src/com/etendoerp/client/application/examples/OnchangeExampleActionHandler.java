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
 * All portions are Copyright (C) 2011 Openbravo SLU 
 * All Rights Reserved. 
 * Contributor(s):  __________
 ************************************************************************
 */
package com.etendoerp.client.application.examples;

import java.util.Map;

import org.codehaus.jettison.json.JSONObject;
import org.openbravo.base.exception.OBException;
import org.openbravo.client.kernel.BaseActionHandler;

/**
 * Is called from a callout/onchange function in the client.
 * 
 * This class is used as an example in howtos in the Openbravo Developers Guide:
 * http://wiki.openbravo.com/wiki/Category:Developers_Guide
 * 
 * @author mtaal
 */
public class OnchangeExampleActionHandler extends BaseActionHandler {

  @Override
  protected JSONObject execute(Map<String, Object> parameters, String data) {
    try {

      // get the data as json
      final JSONObject jsonData = new JSONObject(data);
      final String value = jsonData.getString("value");

      // create the result
      JSONObject json = new JSONObject();
      json.put("upperCased", value.toUpperCase());

      // and return it
      return json;
    } catch (Exception e) {
      throw new OBException(e);
    }
  }
}
