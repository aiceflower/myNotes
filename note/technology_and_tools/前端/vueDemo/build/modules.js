var glob = require('glob');

var arr = glob.sync('./src/module/*');
var ret = [];
var pat = /\/(\w+)$/;

arr.forEach( src => {
  let patArr = pat.exec( src );
  ret.push( patArr[1] );
});
console.log('modules:');
console.log(ret);
//返回module文件夹下所有的页面模块名称
module.exports = ret;
