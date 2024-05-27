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
 * Contributor(s):  ______________________________________.
 ************************************************************************
 */

package com.etendoerp.client.application.examples;

import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.openbravo.base.exception.OBException;
import org.openbravo.client.kernel.BaseActionHandler;
import org.openbravo.dal.service.OBDal;
import org.openbravo.model.common.order.Order;

public class ManualProcessActionHandler extends BaseActionHandler {

  @Override
  protected JSONObject execute(Map<String, Object> parameters, String data) {
    try {
      final JSONObject jsonData = new JSONObject(data);
      final JSONArray orderIds = jsonData.getJSONArray("orders");
      final String action = jsonData.getString("action");

      for (int i = 0; i < orderIds.length(); i++) {
        final String orderId = orderIds.getString(i);

        // get the order
        final Order order = OBDal.getInstance().get(Order.class, orderId);

        // and add or subtract 1
        Long originalValue = order.getObexappTotal();
        if (originalValue == null) {
          originalValue = 0L;
        }

        Long finalValue;
        if ("sum".equals(action)) {
          finalValue = originalValue + 1L;
        } else {
          finalValue = originalValue - 1L;
        }

        order.setObexappTotal(finalValue);
      }

      JSONObject result = new JSONObject();
      result.put("updated", orderIds.length());

      return result;
    } catch (Exception e) {
      throw new OBException(e);
    }
  }
}
