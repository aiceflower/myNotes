/**
 * Created by 1 on 2017/11/16.
 */
//使得页面全屏展示
export function fullScreen(){ //该方法只能由用户的操作而触发
  document.documentElement.webkitRequestFullScreen();
}

export function isFullScreen(){
  return document.fullscreen ||
    document.webkitIsFullScreen ||
    document.mozFullScreen ||
    false;
}
