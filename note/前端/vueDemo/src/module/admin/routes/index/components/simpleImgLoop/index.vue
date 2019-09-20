<template>
  <div class="simple-img-loop" @click="myClick">
    <div class="loop-cutter">
      <ul class="loop-ul" :style="ulStyle">
        <li class="loop-li" v-for="item in imgList">
          <div class="img-center-content">
            <img :src="item.poster" alt=""/>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>myClick

<script type="text/ecmascript-6">
  import proxy from './proxy.js'

  let img = require('./pic_1.png');
  let timer = null;
  export default {
    props : ['movieList','proxyName','click'],
    created : function(){
      this.list = this.movieList;
      proxy.setComponent( this, this.proxyName );
    },
    data : function(){
      return {
        index : 0,
        aniTime : 500,
        aniSpace : 3000, //动画间隔
        isAni : true,
        list : []
      };
    },
    computed : {
      imgList : function(){
        if( this.list && this.list.length  ){
          return this.list.concat( this.list[ this.list.length - 1] );
        }
        return [];
      },
      ulStyle : function(){
        var ret = {
          width : this.imgList.length * 520 + 'px',
          '-webkit-transform' : 'translate3d('+ this.index * -520 +'px,0,0)'
        };

        if( this.isAni ){
          ret['-webkit-transition'] = 'all ' + this.aniTime / 1000 + 's';
        }else{
          ret['-webkit-transition'] = 'all 0s';
        }
        return ret;
      }
    },
    methods : {
      ani : function(){
        if( this.index + 1 == this.list.length ){ //现在已经是最后一张
          this.aniTo( this.index + 1 );
          window.setTimeout(()=>{
            this.moveTo(0);
          },this.aniTime);
        }else{
          this.aniTo( this.index + 1 );
        }
      },
      aniTo : function( index ){
        this.index = index;
        this.isAni = true;
      },
      moveTo : function( index ){
        this.index = index;
        this.isAni = false;
      },
      myClick : function(){
        this.$emit('click',{
          index : proxy.getCurIndex(this.proxyName),
          item : proxy.getCurItem(this.proxyName)
        });
      }
    },
    mounted : function(){
      if( this.list.length <= 1 ){
        return;
      }
      timer = window.setInterval(()=>{
        this.ani();
      },this.aniTime + this.aniSpace)
    },
    destroyed : function(){
      proxy.setComponent( null, this.proxyName );
      window.clearInterval( timer );
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss"  >
  @import "./simpleImgLoop.scss";
</style>
