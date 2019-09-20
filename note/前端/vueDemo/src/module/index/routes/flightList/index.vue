<template>
  <div class="flight-list page-container" @click="full()">
    <myHeader></myHeader>
    <counting :top="13"></counting>
    <div class="list-content">
      <div class="flight" v-for="item in flightList">
        <div class="flight-img">
          <img :src="flightImg" alt=""/>
        </div>
        <div class="flight-type">
          {{item.flightType}}
        </div>
      </div>
    </div>
    <div class="tab-bar">
      <div class="sm-circle sm-circle1" ></div>
      <div class="sm-circle sm-circle2"  ></div>
      <div class="sm-circle sm-circle3" ></div>
      <div class="big-circle big-circle1" v-show="getCurTabIsShow(0)"></div>
      <div class="big-circle big-circle2" v-show="getCurTabIsShow(1)"></div>
      <div class="big-circle big-circle3" v-show="getCurTabIsShow(2)"></div>
      <div class="text text1" @click="link(0)" :class="{cur : getTextStyle(0)}">其他</div>
      <div class="text text2" @click="link(1)" :class="{cur : getTextStyle(1)}">波音</div>
      <div class="text text3" @click="link(2)" :class="{cur : getTextStyle(2)}">空客</div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import counting from '../../components/counting'
  import myHeader from './components/header'
  import { fullScreen, isFullScreen } from '../../../../common/utils/BOM.utils.js'

  let img = require('./img/pic_plane1.png');

  export default {
    components : {
      counting : counting,
      myHeader : myHeader
    },
    methods : {
      full : function(){
       !isFullScreen() && fullScreen();
      },
      getCurTabIsShow : function( index ){
        if( index == this.tabIndex ){
          return true;
        }else{
          return false;
        }
      },
      getTextStyle : function( index ){
        if( index == this.tabIndex ){
          return true;
        }else{
          return false;
        }
      },
      link : function(index){
        this.tabIndex = index;
        this.$router.replace({ path: 'flightList', query: { type: index }});
      }

    },
    data : function(){
      return {
        flightImg : img,
        tabIndex : 0,
        flightList : (function(){
          let len = 12;
          let ret = [];
          for( let i = 0; i < len; i++ ){
            ret.push({
              flightType : Math.floor(Math.random() * 1000)
            });
          }
          return  ret;
        })()
      };
    },
    mounted : function(){
    }
  }
</script>

<style lang="scss">
  @import './flightList.scss';
</style>
