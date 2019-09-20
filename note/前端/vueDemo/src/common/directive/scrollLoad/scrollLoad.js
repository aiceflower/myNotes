function getScrollCallback( cb, el ){
  var timer = null;

  return function(){
    clearTimeout(timer);

    //防止方法在短时间内被多次触发
    timer = window.setTimeout(function(){
      var elScrollTop = el.scrollTop; //元素当前滚动的scrollTop
      var elOffsetHeight = el.offsetHeight; //元素被截断后的高度
      var elScrollHeight = el.scrollHeight; //元素的真实高度

      if( elScrollTop + elOffsetHeight == elScrollHeight || elScrollHeight == elOffsetHeight ){
        cb();
      }
    },200);
  }
}

export default {

  bind : function( el, binding ){
    var cb = binding.value.cb;
    binding.$cb = getScrollCallback(cb,el);
    el.addEventListener('DOMMouseScroll',binding.$cb,false);
    el.addEventListener('mousewheel',binding.$cb,false);
    el.addEventListener('scroll',binding.$cb,false);
  },

  unbind : function( el, binding ){
    el.removeEventListener('DOMMouseScroll',binding.$cb,false);
    el.removeEventListener('mousewheel',binding.$cb,false);
    el.removeEventListener('scroll',binding.$cb,false);
  }


}
