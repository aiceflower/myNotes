
import Vue from 'vue'
import vueRouter from 'vue-router'

import '../../common/css/reset.scss'
import './css/index.scss'


import index from './routes/index'


Vue.use(vueRouter)

const routes = [
  {
    path : '/',
    component : index
  }
]

const router = new vueRouter({
  routes
});


Vue.directive('touch',{
  bind( el, binding ){
    let preX;
    let curX;
    let touchStartEvent = function(e){
      curX = preX = e.touches[0].clientX;
    };
    let touchMoveEvent = function(e){
      curX = e.touches[0].clientX;
    };
    let touchEndEvent = function(e){
      let dist = curX - preX;
      if( dist < 10 && dist >= 0 ){
        binding.value.cb && binding.value.cb({
          type : 'click',
          e : e
        });
      }else if( dist < 0 ){
        binding.value.cb && binding.value.cb({
          type : 'swipeLeft'
        });
      }else{
        binding.value.cb && binding.value.cb({
          type : 'swipeRight'
        });
      }
    };

    let mouseStartEvent = function(e){
      curX = preX = e.clientX;
    };

    let mouseEndEvent = function(e){
      curX = e.clientX;
      touchEndEvent(e);
    };

    if( 'ontouchstart' in document ){
      el.addEventListener('touchstart', touchStartEvent,false);
      el.addEventListener('touchmove', touchMoveEvent,false);
      el.addEventListener('touchend', touchEndEvent,false);
      binding.touchStartEvent = touchStartEvent;
      binding.touchMoveEvent = touchMoveEvent;
      binding.touchEndEvent = touchEndEvent;
    }else{
      el.addEventListener('mousedown', mouseStartEvent,false);
      el.addEventListener('mouseup', mouseEndEvent,false);
      binding.mouseStartEvent = mouseStartEvent;
      binding.mouseEndEvent = mouseEndEvent;
    }

  },
  unbind( el, binding ){
    if( 'ontouchstart' in document ){
      el.removeEventListener( 'touchstart', binding.touchstart, false );
      el.removeEventListener( 'touchmove', binding.touchmove, false );
      el.removeEventListener( 'touchend', binding.touchend, false );
    }else{
      el.removeEventListener( 'mousedown', binding.mouseStartEvent, false );
      el.removeEventListener( 'mouseup', binding.mouseEndEvent, false );
    }
  }
});


new Vue({
  router
}).$mount('#adminApp');




