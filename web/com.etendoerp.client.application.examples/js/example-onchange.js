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

// create some globals
// NOTE: not using var before the declaration as this code is executed
// within a function, so it would create a local var instead of a global one
OB.OBEXAPP = {};
OB.OBEXAPP.OnChangeFunctions = {};

OB.OBEXAPP.OnChangeFunctions.Note_Name = function(item, view, form, grid) {
  // set a message
  view.messageBar.setMessage(
    isc.OBMessageBar.TYPE_INFO,
    'Changed!',
    'You changed the name to ' + item.getValue()
  );

  // set the value for the description and make sure that the
  // onchange handlers are called
  form.setItemValue('description', 'Description ' + item.getValue());
};

OB.OBEXAPP.OnChangeFunctions.Note_Description = function(
  item,
  view,
  form,
  grid
) {
  form.setItemValue('searchKey', item.getValue());
};

// set a sorting
OB.OBEXAPP.OnChangeFunctions.Note_Description.sort = 20;

// register the onchange for the description
OB.OnChangeRegistry.register(
  'FF8081813290114F0132901EB0A2001A',
  'description',
  OB.OBEXAPP.OnChangeFunctions.Note_Description,
  'OBEXAPP_Description1'
);

OB.OBEXAPP.OnChangeFunctions.Note_Description2 = function(
  item,
  view,
  form,
  grid
) {
  form.setValue('value', 'Second onchange called after the first one');
};

// set a sort after the other one
OB.OBEXAPP.OnChangeFunctions.Note_Description2.sort = 30;

//OB.OnChangeRegistry.register('FF8081813290114F0132901EB0A2001A', 'description',
//    OB.OBEXAPP.OnChangeFunctions.Note_Description2, 'OBEXAPP_Description2');
OB.OBEXAPP.OnChangeFunctions.Note_Value = function(item, view, form, grid) {
  // the callback called after the server side call returns
  var callback = function(response, data, request) {
    form.setItemValue(item, data.upperCased);
    view.messageBar.setMessage(
      isc.OBMessageBar.TYPE_WARNING,
      'Uppercased!',
      'The value has been uppercased'
    );
  };

  // do a server side call and on return call the callback
  OB.RemoteCallManager.call(
    'com.etendoerp.client.application.examples.OnchangeExampleActionHandler',
    {
      value: item.getValue()
    },
    {},
    callback
  );
};
OB.OBEXAPP.OnChangeFunctions.Note_Value.sort = 20;

OB.OnChangeRegistry.register(
  'FF8081813290114F0132901EB0A2001A',
  'value',
  OB.OBEXAPP.OnChangeFunctions.Note_Value,
  'OBEXAPP_Value'
);
