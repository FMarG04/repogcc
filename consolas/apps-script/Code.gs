function doGet() {
  return HtmlService.createHtmlOutputFromFile('Index')
    .setTitle('Consolas')
    .addMetaTag('viewport', 'width=device-width, initial-scale=1');
}

function consolas(accion, id, datos) {
  const metodos = new Map([
    ['listar', 'get'],
    ['crear', 'post'],
    ['editar', 'put'],
    ['eliminar', 'delete']
  ]);
  const metodo = metodos.get(accion);
  if (!metodo) throw new Error('Operación inválida.');

  const requiereId = metodo === 'put' || metodo === 'delete';
  if (requiereId && (!Number.isInteger(id) || id < 1 || id > 2147483647)) {
    throw new Error('ID inválido.');
  }

  const propiedades = PropertiesService.getScriptProperties();
  const url = propiedades.getProperty('API_BASE_URL');
  const clave = propiedades.getProperty('CONSOLAS_API_KEY');
  if (!url || !url.startsWith('https://') || !clave) {
    throw new Error('Configura la URL HTTPS y la clave en las propiedades del script.');
  }

  const opciones = {
    method: metodo,
    headers: { 'X-API-Key': clave },
    muteHttpExceptions: true,
    followRedirects: false
  };
  if (metodo === 'post' || metodo === 'put') {
    opciones.contentType = 'application/json';
    opciones.payload = JSON.stringify(datos);
  }

  const ruta = '/api/consolas' + (requiereId ? '/' + id : '');
  const respuesta = UrlFetchApp.fetch(url.replace(/\/+$/, '') + ruta, opciones);
  const codigo = respuesta.getResponseCode();
  if (codigo === 401 || codigo === 403) {
    throw new Error('Revisa la clave de la API y la visibilidad del puerto 8082.');
  }
  if (codigo === 404) throw new Error('La consola o la ruta solicitada no existe.');
  if (codigo === 400) throw new Error('Revisa la marca y el modelo.');
  if (codigo < 200 || codigo >= 300) {
    throw new Error('La API respondió con HTTP ' + codigo + '.');
  }
  return codigo === 204 ? null : JSON.parse(respuesta.getContentText());
}
