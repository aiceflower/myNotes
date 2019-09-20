<template>
  <div class="touch-list" v-touch="{cb:swipe}">
      <div class="prevent-mask"></div>
      <div :class="{'touch-list-li':true,'cur':index == i}" v-for="(item,i) in list" :style="getItemStyle(i)" >
        <div class="cutter">
          <div class="center-content">
            <img :src="item.poster" alt=""/>
          </div>
        </div>
      </div>
  </div>
</template>

<script type="text/ecmascript-6">
  export default {
    props : [
      'fileList',
      'itemW',
      'itemH',
      'contentW',
      'scaleRate',
      'step',
      'rotate'
    ],
    data : function(){
      return {
        index : 0,
        list : []
      };
    },
    created : function(){
      this.list = this.fileList;
    },
    methods : {
      swipe: function( data ){
        if( !this.list.length ){
          return;
        }
        if( data.type == 'click' ){
          let offsetL = this.contentW / 2 - this.itemW / 2;
          let offsetT = this.$el.offsetTop + 4;
          let clientX;
          let clientY;
          if( 'ontouchstart' in document ){
            clientX = data.e.touches[0].clientX;
            clientY = data.e.touches[0].clientY;
          }else{
            clientX = data.e.clientX;
            clientY = data.e.clientY;
          }
          let YLimit = {
            start : offsetT,
            end : offsetT + this.itemH
          };

          let XLimit = {
            start : offsetL,
            end : offsetL + this.itemW
          };

          if( clientX > XLimit.start && clientX < XLimit.end && clientY > YLimit.start && clientY < YLimit.end ){
            this.$emit('click',{
              index : this.index,
              list : this.list,
              item : this.list[ this.index ]
            });
          }

        }else if( data.type == 'swipeLeft' ){
          this.move( this.index + 1 );
        }else if( data.type == 'swipeRight' ){
          this.move( this.index - 1 );
        }
      },
      move : function( index ){
        if( index === undefined  ){
          index = this.index + 1;
        }
        let max = this.list.length - 1;
        let min = 0;
        if( index >= min && index <= max ){
          this.index = index;
        }
      },
      getItemStyle : function( index ){
        let centerItemL = this.contentW * 0.5 - this.itemW * 0.5;// 中心图片的left
        let centerItemR =  this.contentW * 0.5 + this.itemW * 0.5;// 中心图片的right
        let centerIndex = this.list.length;
        let indexOffset = index - this.index;
        let absIndexOffset = Math.abs( indexOffset );
        let scaleRate = Math.pow( this.scaleRate, absIndexOffset ); //图片的缩放比例
        let offsetL = 0;
        let zIndex = centerIndex - absIndexOffset;

        let rotate = 0;

        if( absIndexOffset ){
          rotate = Math.pow(absIndexOffset,0) * this.rotate;
        }

        for( let i = 0; i < absIndexOffset; i++ ){
          offsetL += this.step * Math.pow( this.scaleRate, i );
        }

        if( indexOffset <= 0 ){
          offsetL = offsetL * -1;
        }else{
          rotate = rotate * -1;
        }

        offsetL += centerItemL;

        return {
          '-webkit-transform' : `translate3d(${offsetL}px,0,0) scale(${scaleRate}) rotateY(${rotate}deg)`,
          zIndex : zIndex
        };

      }
    }
  }
</script>

<style lang="scss">

  @import "touchList.scss";
</style>
