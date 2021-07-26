/**
 * Created by 1 on 2017/11/20.
 */
import axios from 'axios'
//Qs在安装axios的时候已经安装完毕，无须独立安装
import Qs from 'qs'

import * as config from '../../config/config.js'

export function getApi( data ){
  let proxyRoot = 'http://localhost:8080/chaji/';
  if( config.apiPathType == 1 ){
    return [
      proxyRoot,
      data.path
    ].join('');
  }
}

//创建基本配置
let http = axios.create({
  transformRequest: [function (data) {
    if( data instanceof FormData ){
      return data;
    }
    return Qs.stringify(data);
  }],
  headers : {
    'Content-Type' : 'application/x-www-form-urlencoded'
  }
});

export function post( data ){
  data.method = 'POST';
  return http( data );
}


