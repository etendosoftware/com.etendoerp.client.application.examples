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
package com.etendoerp.client.application.examples;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;
import org.openbravo.client.application.process.BaseProcessActionHandler;

/**
 * Example process action handler that demonstrates how to use the
 * Upload File reference in a Java-based Process Definition.
 *
 * <p>
 * This example expects a single file parameter of type <b>Upload File</b>.
 * The uploaded file is validated to ensure it has a <code>.txt</code> extension.
 * If valid, the file content is read and displayed back to the user as a
 * success message.
 * </p>
 *
 * <p>
 * Uploaded files are not persisted automatically. The file content is only
 * available during the execution of the process.
 * </p>
 */
public class ExampleUploadFile extends BaseProcessActionHandler {

  private static final String FILE_PARAM_KEY = "Example File";
  private static final String KEY_CONTENT = "content";
  private static final String KEY_FILE_NAME = "fileName";

  /**
   * Executes the process logic.
   *
   * <p>
   * The uploaded file is retrieved from the {@code parameters} map using the
   * DB Column Name of the Upload File parameter. The process performs the
   * following steps:
   * </p>
   * <ul>
   *   <li>Validates that a file was provided.</li>
   *   <li>Validates that the file has a <code>.txt</code> extension.</li>
   *   <li>Reads the file content using UTF-8 encoding.</li>
   *   <li>Displays the file content as a message in the UI.</li>
   * </ul>
   *
   * @param parameters
   *     Map containing the process parameters. For multipart requests, the
   *     uploaded file is included as a map with file metadata and content.
   * @param content
   *     JSON string containing additional request data (not used in this example).
   * @return a {@link JSONObject} containing the response message to be shown in the UI.
   */
  @Override
  protected JSONObject doExecute(Map<String, Object> parameters, String content) {
    @SuppressWarnings("unchecked") Map<String, Object> fileInfo = (Map<String, Object>) parameters.get(FILE_PARAM_KEY);

    if (fileInfo == null) {
      return buildMessage("error", "No file provided", "Please select a .txt file before running the process.");
    }

    String fileName = (String) fileInfo.get(KEY_FILE_NAME);
    if (fileName == null || fileName.isBlank()) {
      return buildMessage("error", "Invalid file", "The uploaded file has no name. Please upload a valid .txt file.");
    }

    if (!isTxtFile(fileName)) {
      return buildMessage("error", "Invalid file type",
          "Only .txt files are allowed. Uploaded: " + escapeHtml(fileName));
    }

    InputStream input = (InputStream) fileInfo.get(KEY_CONTENT);
    if (input == null) {
      return buildMessage("error", "Invalid upload", "File content was not received. Please try again.");
    }

    try {
      byte[] bytes = input.readAllBytes();
      String text = new String(bytes, StandardCharsets.UTF_8).trim();

      if (text.isBlank()) {
        return buildMessage("warning", "Empty file", "The uploaded .txt file is empty.");
      }

      // Show file content in UI (escape to avoid HTML injection)
      return buildMessage("success", "File content", "<pre>" + escapeHtml(text) + "</pre>");

    } catch (Exception e) {
      return buildMessage("error", "Error reading file",
          "Could not read the uploaded file: " + escapeHtml(e.getMessage()));
    }
  }

  /**
   * Checks whether the given file name corresponds to a text file.
   *
   * @param fileName
   *     Name of the uploaded file.
   * @return {@code true} if the file ends with <code>.txt</code>,
   *     {@code false} otherwise.
   */
  private boolean isTxtFile(String fileName) {
    String lower = fileName.toLowerCase();
    return lower.endsWith(".txt");
  }

  /**
   * Builds a standard response message to be displayed in the Etendo UI.
   *
   * <p>
   * Supported severities include: <code>success</code>, <code>warning</code>,
   * <code>error</code> and <code>info</code>.
   * </p>
   *
   * @param severity
   *     Message severity.
   * @param title
   *     Message title.
   * @param text
   *     Message body.
   * @return a {@link JSONObject} representing the UI message.
   */
  private JSONObject buildMessage(String severity, String title, String text) {
    try {
      JSONObject response = new JSONObject();
      JSONObject msg = new JSONObject();
      msg.put("severity", severity);
      msg.put("title", title);
      msg.put("text", text);
      response.put("message", msg);
      return response;
    } catch (Exception e) {
      // Fallback minimal response
      return new JSONObject();
    }
  }

  /**
   * Escapes basic HTML characters to prevent HTML injection when rendering
   * messages in the UI.
   *
   * @param s
   *     Input string.
   * @return escaped string safe to render as HTML.
   */
  private String escapeHtml(String s) {
    if (s == null) {
      return "";
    }
    return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'",
        "&#39;");
  }
}
