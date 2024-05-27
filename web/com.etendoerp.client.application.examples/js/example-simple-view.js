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
 * All portions are Copyright (C) 2011-2012 Openbravo SLU
 * All Rights Reserved.
 * Contributor(s):  ______________________________________.
 ************************************************************************
 */

isc.defineClass('OBEXAPP_HelloWorldLabelView', isc.OBBaseView).addProperties({
  labelContent: 'Label content should be passed in as a parameter',

  width: '100%',
  height: '100%',

  align: 'center',
  defaultLayoutAlign: 'center',

  initWidget: function() {
    this.children = [
      isc.Label.create({
        height: 1,
        width: 1,
        overflow: 'visible',
        align: 'center',
        valign: 'center',
        contents: this.labelContent
      })
    ];

    this.Super('initWidget', arguments);
  },

  getBookMarkParams: function() {
    var result = this.Super('getBookMarkParams', arguments);
    result.labelContent = this.labelContent;
    return result;
  }
});
