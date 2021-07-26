<template>
  <div class="movie-list page-container" @click="full()">
    <myHeader></myHeader>
    <counting></counting>
    <touch-list
      :itemW="600"
      :itemH="850"
      :contentW="1920"
      :scaleRate="0.8"
      :step="400"
      :rotate="40"
      :fileList="fileList"></touch-list>
  </div>
</template>

<script type="text/ecmascript-6">
  import counting from '../../components/counting'
  import myHeader from './components/header'
  import { fullScreen, isFullScreen } from '../../../../common/utils/BOM.utils.js'
  import touchList from '../../components/touchList'

  let img = require('./img/pic_2.png');

  export default {
    components : {
      counting : counting,
      myHeader : myHeader,
      touchList : touchList
    },
    methods : {
      full : function(){
        !isFullScreen() && fullScreen();
      }
    },
    data : function(){
      return {
        fileList : [
          {
            poster : img
          },
          {
            poster : img
          },
          {
            poster : img
          },
          {
            poster : img
          },
          {
            poster : img
          }
        ]
      };
    }
  }
</script>

<style>


  .movie-list{
    background-image: url("./img/bg.jpg");
    background-size:100%;
    background-repeat: no-repeat;
  }
</style>
