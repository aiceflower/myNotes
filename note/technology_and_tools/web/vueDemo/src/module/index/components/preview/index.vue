<template>
   <div class="com-preview" v-if="isShow">
     <div class="close" @click="close">X</div>
       <div  v-if="preType == 'img'" class="preview-img" :style="img"></div>
       <video controls="" autoplay="" v-else :src="fileUrl" class="preview-video"></video>
   </div>
</template>

<script type="text/ecmascript-6">


  export default {
    data : function(){
      return {

      };
    },
    props : {
      isShow : {
        type : Boolean,
        default : false
      },
      fileUrl : {
        type : String,
        default : ''
      },
      preType : {
        type : String,
        default : 'img'
      }
    },
    computed : {
      img : function(){
        if( this.fileUrl ){
          return {
            'backgroundImage' : `url(${this.fileUrl})`
          };
        }else{
          return {};
        }
      }
    },
    methods : {
      close : function(){
        this.$emit('close');
      }
    },
    mounted : function(){

    },
    destroyed : function(){

    }
  }
</script>

<style rel="stylesheet/scss" lang="scss"  >
  @import "./preview.scss";
</style>
