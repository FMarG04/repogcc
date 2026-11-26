function doGet() {
  return HtmlService.createHtmlOutputFromFile('Index').setTitle('Catálogo de consolas');
}

function listarConsolas() {
  return pedir_('get');
}

function crearConsola(datos) {
  return pedir_('post', '', datos);
}

function editarConsola(id, datos) {
  return pedir_('put', rutaId_(id), datos);
}

function eliminarConsola(id) {
  return pedir_('delete', rutaId_(id));
}

function rutaId_(id) {
  if (!Number.isInteger(id) || id < 1 || id > 2147483647) {
    throw new Error('Identificador de consola inválido.');
  }
  return '/' + id;
}

function pedir_(method, ruta = '', datos) {
  const config = PropertiesService.getScriptProperties();
  const base = config.getProperty('API_BASE_URL');
  const clave = config.getProperty('CONSOLAS_API_KEY');
  if (!base || !base.startsWith('https://') || !clave) {
    throw new Error('Configura API_BASE_URL (HTTPS) y CONSOLAS_API_KEY en el proyecto.');
  }

  const opciones = {
    method,
    headers: { 'X-API-Key': clave },
    muteHttpExceptions: true,
    followRedirects: false
  };
  if (datos !== undefined) {
    opciones.contentType = 'application/json';
    opciones.payload = JSON.stringify(datos);
  }
  const respuesta = UrlFetchApp.fetch(base.replace(/\/+$/, '') + '/api/consolas' + ruta, opciones);
  const estado = respuesta.getResponseCode();
  if (estado === 400) throw new Error('Revisa la marca, el modelo y el precio.');
  if (estado === 401) throw new Error('La API rechazó el acceso. Revisa la clave y el puerto de Codespaces.');
  if (estado === 404) throw new Error('No se encontró la consola o la ruta de la API.');
  if (estado < 200 || estado >= 300) {
    throw new Error('La API respondió con HTTP ' + estado + '. Revisa la URL y el Codespace.');
  }
  return estado === 204 ? null : JSON.parse(respuesta.getContentText());
}
