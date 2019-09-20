var modules = require('./modules.js');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var ret = [];
var path = require('path');

modules.forEach( key => {
  var instance =  new HtmlWebpackPlugin({
    filename: path.resolve(__dirname, `../dist/${key}.html`),
    template: `./src/module/${key}/index.html`,
    inject: true,
    minify: {
      removeComments: true,
      collapseWhitespace: true,
      removeAttributeQuotes: true
      // more options:
      // https://github.com/kangax/html-minifier#options-quick-reference
    },
    chunks :  ['manifest', 'vendor', key]
    // necessary to consistently work with multiple chunks via CommonsChunkPlugin
  });
  ret.push( instance );
});

module.exports = ret;
