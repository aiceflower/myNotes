<template>
   <div class="com-header" :style="style" >
     {{text}}
   </div>
</template>

<script type="text/ecmascript-6">
  export default {
    props : {
      text : {
        type : String,
        default : ''
      },
      height : {
        type : Number,
        default : 100
      },
      fontSize : {
        type : Number,
        default : 50
      }
    },
    computed : {
      style : function(){
        return {
          height : this.height + 'px',
          lineHeight : this.height + 'px',
          fontSize : this.fontSize + 'px'
        };
      }
    },
    methods : {
    },
    mounted : function(){

    },
    destroyed : function(){

    }
  }
</script>

<style rel="stylesheet/scss" lang="scss"  >
  @import "./header.scss";
</style>
