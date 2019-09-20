<template>
  <div class="ad-list page-container" @click="full()">
    <com-preview :fileUrl="fileUrl" preType="video" :isShow="showPre" @close="closePreview"></com-preview>
    <myHeader></myHeader>
    <counting></counting>
    <touch-list
      @click="selectAd"
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
  import comPreview from '../../components/preview'

  let img = require('./img/pic_2.png');

  export default {
    components : {
      counting : counting,
      myHeader : myHeader,
      touchList : touchList,
      comPreview : comPreview
    },
    methods : {
      full : function(){
        !isFullScreen() && fullScreen();
      },
      selectAd : function( data ){
        if( data.item && data.item.video ){
          this.setPreFileUrl( data.item.video );
          this.showPreview();
        }
      },
      closePreview : function(){
        this.showPre = false;
      },
      showPreview : function(){
        this.showPre = true;
      },
      setPreFileUrl : function( url ){
        this.fileUrl = url;
      }
    },
    data : function(){
      return {
        fileList : [
          {
            poster : img,
            video : 'http://localhost/ppt/MP4/1.mp4'
          },
          {
            poster : img,
            video : 'http://localhost/ppt/MP4/2.mp4'
          },
          {
            poster : img,
            video : 'http://localhost/ppt/MP4/1.mp4'
          },
          {
            poster : img,
            video : 'http://localhost/ppt/MP4/1.mp4'
          },
          {
            poster : img,
            video : 'http://localhost/ppt/MP4/1.mp4'
          }
        ],
      //  fileUrl : 'http://localhost/ppt/img/2.png',
        fileUrl : 'http://localhost/ppt/MP4/1.mp4',
        showPre : false
      };
    }
  }
</script>

<style>


  .ad-list{
    background-image: url("./img/bg.jpg");
    background-size:100%;
    background-repeat: no-repeat;
  }
</style>
