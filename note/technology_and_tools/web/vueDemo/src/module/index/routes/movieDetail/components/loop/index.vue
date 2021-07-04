<template>
   <div class="com-loop">
     <div class="btn-l btn" @click="back()"></div>
     <div class="btn-r btn" @click="forward()"></div>
     <div class="cutter">
       <div class="flex-box" :style="getLoopStyle">
         <div
           @click="setCurIndex(index,item)"
           class="item"
           :class="{cur:isCur(index)}"
           v-for="(item,index) in list"
           :style="getItemStyle(item)"></div>
       </div>
     </div>
   </div>
</template>

<script type="text/ecmascript-6">
  let poster1 = require('./img/pic_poster_1.png');
  let poster2 = require('./img/pic_poster_2.png');
  let poster3 = require('./img/pic_poster_3.png');
  let poster4 = require('./img/pic_poster_4.png');
  let poster5 = require('./img/pic_poster_5.png');
  let smItemW = 156;
  let bigItemW = 176;
  let itemSpace = 50;

  export default {
    props : {
      list : {
        type : Array,
        default : function(){
          return []
        }
      },
      curIndex : {
        type : Number,
        default : 0
      }
    },
    methods : {
      getItemStyle : function( item ){
        if( item && item.poster ){
          return {
            backgroundImage : `url(${item.poster})`
          };
        }
      },
      isCur : function( index ){
        return index == this.curIndex;
      },
      setCurIndex : function( index, item ){
        this.curIndex = index;
        this.$emit('click',{
          item : item,
          index : index
        })
      },
      back : function(){
        if( this.leftIndex < 0 ){
          this.leftIndex++;
        }
      },
      forward : function(){
        if( this.listShowLen - this.leftIndex < this.list.length ){
          this.leftIndex--;
        }
      },
      getLoopOffsetL : function(){
        if( this.leftIndex < 0 ){
          if( Math.abs(this.leftIndex + 1) >= this.curIndex ){
           let leftIndex =  Math.abs(this.leftIndex);
            return (( leftIndex - 1 ) * smItemW + bigItemW +  leftIndex * itemSpace) * -1;
          }else{
            return this.leftIndex * (smItemW + itemSpace);
          }
        }else{
          return 0;
        }
      }
    },
    data : function(){
      return {
        listShowLen : 7, //loop显示个数
        leftIndex : 0
      };
    },
    computed : {
      getLoopStyle : function(){
        if(  this.list && this.list.length ){
          let itemNum = this.list.length;
          let w = itemSpace * (itemNum - 1) + (itemNum - 1) * smItemW + bigItemW;
          let offsetL = this.getLoopOffsetL();
          return {
            width : w + 'px',
            transform : `translate3d(${offsetL}px,0,0)`
          }
        }
      }
    }
  }
</script>

<style lang="scss">
  @import "loop.scss";
</style>
