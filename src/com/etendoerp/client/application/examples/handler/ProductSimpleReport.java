/*
 *************************************************************************
 * The contents of this file are subject to the Etendo License
 * (the "License"), you may not use this file except in compliance with
 * the License.
 * You may obtain a copy of the License at
 * https://github.com/etendosoftware/etendo_core/blob/main/legal/Etendo_license.txt
 * Software distributed under the License is distributed on an
 * "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing rights
 * and limitations under the License.
 * All portions are Copyright © 2021–2025 FUTIT SERVICES, S.L
 * All Rights Reserved.
 * Contributor(s): Futit Services S.L.
 *************************************************************************
 */
package com.etendoerp.client.application.examples.handler;

import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.openbravo.base.exception.OBException;
import org.openbravo.client.application.ApplicationConstants;
import org.openbravo.client.application.ReportDefinition;
import org.openbravo.client.application.report.BaseReportActionHandler;

/**
 * Report action handler for the {@code Product Simple Report} example.
 * <p>
 * This class extends {@link BaseReportActionHandler} and is used to provide
 * additional parameters to the Jasper report template (JRXML).
 * </p>
 *
 * <p>
 * In this example, the handler extracts the output format selected by the user
 * (HTML, PDF, XLS, etc.) from the request payload and exposes it as a report
 * parameter. This allows the JRXML template to adapt its behavior depending on
 * the output format (for example, enabling hyperlinks only when the report is
 * rendered in HTML).
 * </p>
 *
 * <p>
 * The main datasource logic and filtering are defined directly in the JRXML
 * template. No custom data retrieval logic is implemented in this handler,
 * making it suitable as a simple and educational example.
 * </p>
 */
public class ProductSimpleReport extends BaseReportActionHandler {

  /**
   * Name of the parameter used to store the report output format.
   * <p>
   * Typical values are {@code HTML}, {@code PDF}, {@code XLS}, etc.
   * This parameter is later consumed by the JRXML template.
   * </p>
   */
  private static final String OUTPUT_FORMAT = "OUTPUT_FORMAT";

  /**
   * Adds additional parameters to the report execution context.
   * <p>
   * This method retrieves the button value from the JSON request content,
   * which represents the selected output format, and stores it in the
   * parameters map so it can be accessed from the Jasper report.
   * </p>
   *
   * @param process
   *     the {@link ReportDefinition} associated with this report execution
   * @param jsonContent
   *     the JSON object containing the request data sent from the client,
   *     including the selected report output format
   * @param parameters
   *     the map of parameters that will be passed to the Jasper report
   * @throws OBException
   *     if an error occurs while reading the output format from the request
   */
  @Override
  protected void addAdditionalParameters(ReportDefinition process, JSONObject jsonContent,
      Map<String, Object> parameters) {

    try {
      parameters.put(OUTPUT_FORMAT, jsonContent.getString(ApplicationConstants.BUTTON_VALUE));
    } catch (JSONException e) {
      throw new OBException(e);
    }
  }
}

