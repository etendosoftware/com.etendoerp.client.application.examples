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
 * All portions are Copyright (C) 2010-2012 Openbravo SLU
 * All Rights Reserved.
 * Contributor(s):  ______________________________________.
 ************************************************************************
 */

isc.defineClass('OBEXAPP_HelloWorldWindow', 'Window').addProperties({
  keepInParentRect: true,
  canDragReposition: true,
  canDragResize: true,
  width: 200,
  height: 200,
  initWidget: function() {
    this.items = [
      isc.Label.create({
        height: 100,
        padding: 10,
        width: 100,
        align: 'center',
        valign: 'center',
        contents: this.windowContent
      })
    ];
    this.Super('initWidget', arguments);
  }
});

isc.defineClass('OBEXAPP_HelloWorldView', isc.Layout).addProperties({
  windowTitle: 'title should be taken from the parameters',
  width: '100%',
  height: '100%',
  align: 'center',
  defaultLayoutAlign: 'center',
  initWidget: function() {
    this.children = [
      isc.OBEXAPP_HelloWorldWindow.create({
        title: this.windowTitle,
        windowContent: this.windowContent
      })
    ];
    this.Super('initWidget', arguments);
  }
});
