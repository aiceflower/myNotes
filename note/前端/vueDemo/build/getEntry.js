var modules = require('./modules');


var ret = {};

modules.forEach( key => {
  ret[ key ] = `./src/module/${key}/main.js`;
});

module.exports = ret;
